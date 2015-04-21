package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Bounding shape is an abstract class used to handle all types of collisions.
 */
public abstract class BoundingShape {
    /**
     * Checks whenever a bounding shape is intersecting with another one.
     *
     * @param a First shape
     * @param b Second shape
     * @return Are they intersecting
     */
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

    /**
     * Method used to draw shapes using a shape renderer.
     * @param shapeRenderer shape renderer
     */
    public abstract void draw(ShapeRenderer shapeRenderer);

    public abstract void update(Vector2 pos);

    public abstract Vector2 getPos();

    /**
     * Method used to check if the shape is valid
     * @return is valid
     */
    public abstract boolean isValid();
}