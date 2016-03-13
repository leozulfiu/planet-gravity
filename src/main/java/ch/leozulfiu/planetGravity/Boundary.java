package ch.leozulfiu.planetGravity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

import static processing.core.PConstants.CENTER;

/**
 * @author Leo Zulfiu
 * @since 02.03.15
 */
public class Boundary {
    private float x;
    private float y;
    private float w;
    private float h;
    private PApplet parent;
    private Box2DProcessing box2d;
    private Body body;

    Boundary(float x_, float y_, float w_, float h_, PApplet parent, Box2DProcessing box2d) {
        this.parent = parent;
        this.box2d = box2d;
        x = x_;
        y = y_;
        w = w_;
        h = h_;

        PolygonShape sd = new PolygonShape();
        float box2dW = box2d.scalarPixelsToWorld(w / 2);
        float box2dH = box2d.scalarPixelsToWorld(h / 2);
        sd.setAsBox(box2dW, box2dH);

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(box2d.coordPixelsToWorld(x, y));
        body = box2d.createBody(bd);

        body.createFixture(sd, 1);
    }

    void display() {
        parent.fill(0);
        parent.stroke(0);
        parent.rectMode(CENTER);
        parent.rect(x, y, w, h);
    }
}
