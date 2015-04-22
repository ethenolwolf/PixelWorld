package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.pixelworld.data.World;

/**
 * BoundingShape of type Circle.
 */
public class BoundingCircle extends BoundingShape {
    private Circle circle;

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
        shapeRenderer.circle(circle.x - World.getCameraOffset().x, circle.y - World.getCameraOffset().y, circle.radius);
    }

    @Override
    public void update(Vector2 pos) {
        circle.x = pos.x;
        circle.y = pos.y;
    }

    @Override
    public Vector2 getPos() {
        return new Vector2(circle.x, circle.y);
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
    public String toString() {
        return String.format("X = %.2f, Y = %.2f, R = %.2f", circle.x, circle.y, circle.radius);
    }
}
