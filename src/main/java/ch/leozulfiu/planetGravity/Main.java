package ch.leozulfiu.planetGravity;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

import java.util.ArrayList;

/**
 * @author Leo Zulfiu
 * @since 26.02.15
 */
public class Main extends PApplet {
    Box2DProcessing box2d;
    ArrayList<Boundary> boundaries;
    ArrayList<Box> boxes;
    Spaceship spaceship;
    ArrayList<Planet> planets;
    Camera camera;
    boolean pressingSpace;

    public static void main(String args[]) {
        PApplet.main(new String[]{"ch.leozulfiu.planetGravity.Main"});
    }

    public void setup() {
        frame.setTitle("Planet Gravity with box2d");
        size(800, 600);
        smooth();
        box2d = new Box2DProcessing(this);
        box2d.createWorld();
        box2d.setGravity(0, 0);
        camera = new Camera(this, box2d, 800, 600);

        boxes = new ArrayList<Box>();
        boundaries = new ArrayList<Boundary>();
        planets = new ArrayList<Planet>();

        boundaries.add(new Boundary(width / 2, 10, width, 10, this, box2d));
        boundaries.add(new Boundary(width / 2, height - 10, width, 10, this, box2d));
        boundaries.add(new Boundary(0, height / 2, 10, height - 20, this, box2d));
        boundaries.add(new Boundary(width, height / 2, 10, height - 20, this, box2d));
        planets.add(new Planet(80, new Vec2(width / 2, height / 2), 300, this, box2d));
        spaceship = new Spaceship(width / 2, height / 2 - 80, this, box2d);
    }

    public void draw() {
        background(255);
        box2d.step();
        camera.update(spaceship.body.getPosition());

        if (mousePressed) {
            Box p = new Box(mouseX, mouseY, this, box2d);
            boxes.add(p);
        }

        if (pressingSpace) {
            spaceship.applyTorque();
        }

        for (Boundary wall : boundaries) {
            wall.display();
        }

        for (Box b : boxes) {
            b.display();
        }
        for (Planet planet : planets) {
            planet.display();
            Vec2 force = planet.attract(spaceship.body);
            double dist = calcDist(planet.body, spaceship.body);

            if (dist < planet.gravityField) {
                spaceship.body.applyForceToCenter(force);
            }
            for (Box box : boxes) {
                force = planet.attract(box.body);
                dist = calcDist(planet.body, box.body);
                if (dist < planet.gravityField) {
                    box.body.applyForceToCenter(force);
                }
            }
        }

        spaceship.display();

        for (int i = boxes.size() - 1; i >= 0; i--) {
            Box b = boxes.get(i);
            if (b.isOutOfWin()) {
                boxes.remove(i);
            }
        }

        textSize(13);
        text("Press space to accelerate", 10, 35);
    }

    public void keyPressed() {
        if (key == ' ') {
            pressingSpace = true;
        }
    }

    public void keyReleased() {
        if (key == ' ') {
            pressingSpace = false;
        }
    }

    private double calcDist(Body body1, Body body2) {
        Vec2 dirVec = box2d.getBodyPixelCoord(body1).sub(box2d.getBodyPixelCoord(body2));
        return dirVec.length();
    }
}
