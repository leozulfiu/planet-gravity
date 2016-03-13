package ch.leozulfiu.planetGravity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

import static org.jbox2d.dynamics.BodyType.STATIC;
import static processing.core.PApplet.constrain;

/**
 * @author Leo Zulfiu
 * @since 03.03.15
 */
public class Planet {
    public float gravityField;
    public Body body;
    private float radius;
    private int gravity;
    private PApplet parent;
    private Box2DProcessing box2d;

    public Planet(float radius, Vec2 position, int gravity, PApplet parent, Box2DProcessing box2d) {
        this.parent = parent;
        this.box2d = box2d;
        this.radius = radius;
        this.gravity = gravity;
        BodyDef bd = new BodyDef();
        bd.type = STATIC;
        bd.position = box2d.coordPixelsToWorld(position.x, position.y);
        body = box2d.world.createBody(bd);
        CircleShape cs = new CircleShape();
        cs.m_radius = box2d.scalarPixelsToWorld(radius);
        body.createFixture(cs, 1);
        gravityField = radius + 80;
    }

    public Vec2 attract(Body m) {
        Vec2 pos = body.getWorldCenter();
        Vec2 moverPos = m.getWorldCenter();
        Vec2 force = pos.sub(moverPos);
        float distance = force.length();
        distance = constrain(distance, 1, 5);
        force.normalize();
        float strength = (gravity * 1 * m.m_mass) / (distance * distance);
        force.mulLocal(strength);
        return force;
    }

    public void display() {
        Vec2 pos = box2d.getBodyPixelCoord(body);
        float a = body.getAngle();
        parent.pushMatrix();
        parent.translate(pos.x, pos.y);
        parent.rotate(a);
        parent.fill(175);
        parent.stroke(0);
        parent.strokeWeight(2);
        parent.ellipse(0, 0, radius * 2, radius * 2);
        parent.noFill();
        parent.stroke(125);
        parent.strokeWeight(0.5f);
        parent.ellipse(0, 0, gravityField * 2, gravityField * 2);
        parent.popMatrix();
    }
}
