package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.pixelworld.data.CameraManager;

/**
 * BoundingShape of type Circle.
 */
public class BoundingCircle extends BoundingShape {
    private final Circle circle;

    public BoundingCircle(Vector2 pos, int radius) {
        circle = Pools.get(Circle.class).obtain();
        circle.x = pos.x;
        circle.y = pos.y;
        circle.radius = radius;
    }

    public Circle get() {
        return circle;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        Vector2 offset = CameraManager.getCameraOffset();
        if (offset == null) return;
        shapeRenderer.circle(circle.x - offset.x, circle.y - offset.y, circle.radius);
    }

    @Override
    public boolean isValid() {
        return circle.area() > 0;
    }

    @Override
    public void free() {
        Pools.get(Circle.class).free(circle);
    }

    @Override
    public void drawOnScreen(ShapeRenderer shapeRenderer, Vector2 cameraOffset) {
        shapeRenderer.circle(circle.x - cameraOffset.x, circle.y - cameraOffset.y, circle.radius);
    }

    @Override
    public String toString() {
        return String.format("X = %.2f, Y = %.2f, R = %.2f", circle.x, circle.y, circle.radius);
    }
}
