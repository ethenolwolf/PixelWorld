package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.math.Vector2;

public class BoundingCircle {
    private final float radius;
    private final Vector2 center;

    public BoundingCircle(Vector2 center, float radius) {
        this.radius = radius;
        this.center = center;
    }

    public boolean intersect(BoundingCircle boundingCircle2) {
        return isValid() && center.dst(boundingCircle2.getCenter()) <= radius + boundingCircle2.getRadius();
    }

    Vector2 getCenter() {
        return center;
    }

    float getRadius() {
        return radius;
    }

    public boolean isValid() {
        return radius > 0 && center != null;
    }

    @Override
    public String toString() {
        return "C=" + center.toString() + "-R=" + String.valueOf(radius);
    }
}
