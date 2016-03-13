package ch.leozulfiu.planetGravity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

import static processing.core.PConstants.CENTER;

/**
 * @author Leo Zulfiu
 * @since 02.03.15
 */
public class Box {
    public Body body;
    private float w;
    private float h;
    private PApplet parent;
    private Box2DProcessing box2d;

    Box(float x, float y, PApplet parent, Box2DProcessing box2d) {
        this.parent = parent;
        this.box2d = box2d;
        w = parent.random(4, 16);
        h = parent.random(4, 16);
        makeBody(new Vec2(x, y), w, h);
    }

    void killBody() {
        box2d.destroyBody(body);
    }

    boolean isOutOfWin() {
        Vec2 pos = box2d.getBodyPixelCoord(body);
        if (pos.y > parent.height + w * h) {
            killBody();
            return true;
        }
        return false;
    }

    void display() {
        Vec2 pos = box2d.getBodyPixelCoord(body);
        float a = body.getAngle();

        parent.rectMode(CENTER);
        parent.pushMatrix();
        parent.translate(pos.x, pos.y);
        parent.rotate(-a);
        parent.fill(175);
        parent.stroke(0);
        parent.rect(0, 0, w, h);
        parent.popMatrix();
    }

    void makeBody(Vec2 center, float w_, float h_) {
        PolygonShape sd = new PolygonShape();
        float box2dW = box2d.scalarPixelsToWorld(w_ / 2);
        float box2dH = box2d.scalarPixelsToWorld(h_ / 2);
        sd.setAsBox(box2dW, box2dH);

        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        fd.density = 1;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(box2d.coordPixelsToWorld(center));

        body = box2d.createBody(bd);
        body.createFixture(fd);

        body.setLinearVelocity(new Vec2(parent.random(-5, 5), parent.random(2, 5)));
        body.setAngularVelocity(parent.random(-5, 5));
    }
}
