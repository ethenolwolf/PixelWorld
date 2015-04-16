package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public abstract class BoundingShape {

    public static boolean intersect(BoundingShape a, BoundingShape b) {
        if (a instanceof BoundingRect) {
            if (b instanceof BoundingRect)
                return Intersector.overlaps(((BoundingRect) a).get(), ((BoundingRect) b).get());
            else return Intersector.overlaps(((BoundingCircle) b).get(), ((BoundingRect) a).get());
        } else {
            if (b instanceof BoundingRect)
                return Intersector.overlaps(((BoundingCircle) a).get(), ((BoundingRect) b).get());
            else return Intersector.overlaps(((BoundingCircle) b).get(), ((BoundingCircle) a).get());
        }
    }

    public abstract void draw(ShapeRenderer shapeRenderer);

    public abstract void update(Vector2 pos);

    public abstract Vector2 getPos();

    public abstract boolean isValid();
}