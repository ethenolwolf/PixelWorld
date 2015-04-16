package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundingRect extends BoundingShape {
    Rectangle rectangle;

    public BoundingRect(Vector2 pos, Vector2 dimensions) {
        rectangle = new Rectangle((int) pos.x, (int) pos.y, (int) dimensions.x, (int) dimensions.y);
    }

    public Rectangle get() {
        return rectangle;
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public void update(Vector2 pos) {
        rectangle.x = pos.x;
        rectangle.y = pos.y;
    }

    @Override
    public Vector2 getPos() {
        return new Vector2(rectangle.x, rectangle.y);
    }

    @Override
    public boolean isValid() {
        return rectangle.width > 0 && rectangle.height > 0;
    }
}
