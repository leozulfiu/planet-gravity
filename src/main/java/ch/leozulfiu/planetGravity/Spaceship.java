package ch.leozulfiu.planetGravity;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

import static processing.core.PConstants.*;

/**
 * @author Leo Zulfiu
 * @since 03.03.15
 */
public class Spaceship {
    private static int w = 14;
    private static int h = 34;
    private static int radius = 8;
    private static int thrust = 85;
    private static int torque = 50;
    public Body body;
    private PApplet parent;
    private Box2DProcessing box2d;

    public Spaceship(int x, int y, PApplet parent, Box2DProcessing box2d) {
        this.parent = parent;
        this.box2d = box2d;
        makeBody(new Vec2(x, y));
    }

    private void makeBody(Vec2 center) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(box2d.coordPixelsToWorld(center));
        body = box2d.createBody(bd);

        CircleShape circle = new CircleShape();
        circle.m_radius = box2d.scalarPixelsToWorld(radius);
        Vec2 offset = new Vec2(0, -h / 2);
        offset = box2d.vectorPixelsToWorld(offset);
        circle.m_p.set(offset.x, offset.y);

        PolygonShape ps = new PolygonShape();
        float box2dW = box2d.scalarPixelsToWorld(w / 2);
        float box2dH = box2d.scalarPixelsToWorld(h / 2);
        ps.setAsBox(box2dW, box2dH);

        body.createFixture(ps, 1.0f);
        body.createFixture(circle, 1.0f);
    }

    public void display() {
        Vec2 pos = box2d.getBodyPixelCoord(body);
        float angle = body.getAngle();

        if (parent.keyCode == LEFT) {
            body.applyTorque(torque);
        }
        if (parent.keyCode == RIGHT) {
            body.applyTorque(-torque);
        }

        parent.rectMode(CENTER);
        parent.pushMatrix();
        parent.translate(pos.x, pos.y);
        parent.rotate(-angle);
        parent.fill(55);
        parent.stroke(0);
        parent.rect(0, 0, w, h);
        parent.ellipse(0, -h / 2, radius * 2, radius * 2);
        parent.popMatrix();
    }

    public void applyTorque() {
        Transform t = body.getTransform();
        double v = body.getAngle() + Math.PI / 2;
        float t_x = (float) (Math.cos(v) * thrust);
        float t_y = (float) (Math.sin(v) * thrust);
        parent.fill(0, 102, 153);
        parent.text("t_x: " + t_x, 30, 50);
        parent.text("t_y: " + t_y, 30, 60);
        float l_x = (float) (t.p.x - h / 2 * Math.cos(v));
        float l_y = (float) (t.p.y - h / 2 * Math.sin(v));
        parent.text("l_x: " + t_x, 30, 70);
        parent.text("l_y: " + t_y, 30, 80);

        body.applyForce(new Vec2(t_x, t_y), new Vec2(l_x, l_y));
        body.setAngularVelocity(body.getAngularVelocity() * 0.925f);
    }
}
