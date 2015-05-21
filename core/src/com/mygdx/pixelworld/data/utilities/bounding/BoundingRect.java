package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.mygdx.pixelworld.GUI.DrawManager;
import com.mygdx.pixelworld.data.CameraManager;

/**
 * BoundingShape of type Rectangle.
 */
public class BoundingRect extends BoundingShape {
    private final Rectangle rectangle;

    public BoundingRect(Vector2 pos, Vector2 dimensions) {
        rectangle = Pools.get(Rectangle.class).obtain();
        rectangle.x = (int) pos.x;
        rectangle.y = (int) pos.y;
        rectangle.width = (int) dimensions.x;
        rectangle.height = (int) dimensions.y;
    }

    public BoundingRect(Rectangle rectangle) {
        this.rectangle = new Rectangle(rectangle);
    }

    public Rectangle get() {
        return rectangle;
    }

    @Override
    public void draw() {
        Vector2 offset = CameraManager.getCameraOffset();
        if (offset == null) return;
        DrawManager.rect(rectangle.x - offset.x, rectangle.y - offset.y, rectangle.width, rectangle.height);
    }

    @Override
    public void drawOnScreen() {
        Vector2 offset = CameraManager.getCameraOffset();
        if (offset == null) offset = new Vector2();
        DrawManager.rect(rectangle.x - offset.x, rectangle.y - offset.y, rectangle.width, rectangle.height);
    }

    @Override
    public boolean isValid() {
        return rectangle.width > 0 && rectangle.height > 0;
    }

    @Override
    public void free() {
        Pools.get(Rectangle.class).free(rectangle);
    }

    @Override
    public String toString() {
        return String.format("X = %.2f, Y = %.2f, W = %.2f, H = %.2f", rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
