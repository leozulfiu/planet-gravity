package ch.leozulfiu.planetGravity;

import org.jbox2d.common.Vec2;
import shiffman.box2d.Box2DProcessing;

/**
 * @author Leo Zulfiu
 * @since 11.09.2014
 */
public class Camera {
    public static final int VIEWPORT_SIZE_X = 800;
    public static final int VIEWPORT_SIZE_Y = 600;
    public static final int TILE_SIZE = 32;
    private final Main parent;
    private final Box2DProcessing box2d;
    public int WORLD_SIZE_X;
    public int WORLD_SIZE_Y;
    private float offsetMaxX;
    private float offsetMaxY;
    private float offsetMinX = 0;
    private float offsetMinY = 0;
    private float camX;
    private float camY;
    private int zoom;

    public Camera(Main parent, Box2DProcessing box2d, int width, int height) {
        this.parent = parent;
        this.box2d = box2d;

        WORLD_SIZE_X = width * TILE_SIZE;
        WORLD_SIZE_Y = height * TILE_SIZE;

        offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
        offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
        zoom = 1;

    }

    public void update(Vec2 position) {
        camX = box2d.vectorWorldToPixels(position).x - VIEWPORT_SIZE_X / 2;
        camY = box2d.vectorWorldToPixels(position).y - VIEWPORT_SIZE_Y / 2;

        if (camX > offsetMaxX) {
            camX = offsetMaxX;
        } else if (camX < offsetMinX) {
            camX = offsetMinX;
        }

        if (camY > offsetMaxY) {
            camY = offsetMaxY;
        } else if (camY < offsetMinY) {
            camY = offsetMinY;
        }

        parent.translate(-camX * zoom, -camY * zoom);
        parent.scale(zoom);
    }
}
