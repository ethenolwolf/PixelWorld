package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingCircle;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

/**
 * Class used to generify every sprite drawing process.
 * It takes care of drawing sprites with the correct scale factor and rotation angle, and automatically takes care of
 * map offset and bounding shapes.
 */
public abstract class DrawData implements Disposable {
    float scaleFactor;
    float rotationAngle;
    Class<? extends BoundingShape> boundingType;

    protected abstract TextureRegion getTexture();

    @Override
    public void dispose() {
        getTexture().getTexture().dispose();
    }

    /**
     * @return width of texture with scale factor
     */
    public float getWidth() {
        return getOriginalWidth() * scaleFactor;
    }

    /**
     * @return width of texture (without scale factor)
     */
    float getOriginalWidth() {
        return getTexture().getRegionWidth();
    }

    /**
     * @return height of texture with scale factor
     */
    public float getHeight() {
        return getOriginalHeight() * scaleFactor;
    }

    /**
     * @return height of texture (without scale factor)
     */
    float getOriginalHeight() {
        return getTexture().getRegionHeight();
    }

    /**
     * @return coordinates of the center of texture (without scale factor)
     */
    Vector2 getOriginCenter() {
        return new Vector2(getOriginalWidth() / 2, getOriginalHeight() / 2);
    }

    /**
     * @param absolutePosition position of the scaled sprite
     * @return position of the sprite without scaling
     */
    Vector2 getOriginalPosition(Vector2 absolutePosition) {
        return new Vector2(
                absolutePosition.x + (getWidth() - getOriginalWidth()) / 2,
                absolutePosition.y + (getHeight() - getOriginalHeight()) / 2
        );
    }

    void setRotationAngle(float angle) {
        rotationAngle = angle;
    }
    /**
     * Draws from absolute coordinates to screen.
     *
     * @param batch            SpriteBatch for drawing
     * @param absolutePosition Absolute position of the sprite
     */
    public abstract void draw(SpriteBatch batch, Vector2 absolutePosition);

    public void draw(SpriteBatch batch, Vector2 absolutePosition, float alpha) {
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, alpha);
        draw(batch, absolutePosition);
        batch.setColor(c.r, c.g, c.b, 1.0f);
    }

    /**
     * @param absolutePosition Absolute position of the sprite
     * @return Is the sprite on screen
     */
    boolean isVisible(Vector2 absolutePosition) {
        return !(absolutePosition.x + getWidth() < 0 || absolutePosition.x > World.getWidth() ||
                absolutePosition.y + getHeight() < 0 || absolutePosition.y > World.getHeight());
    }

    /**
     * Draws sprite directly on screen, without taking care of map offset
     * @param batch SpriteBatch for drawing
     * @param screenPosition Screen coordinates
     */
    public void drawOnScreen(SpriteBatch batch, Vector2 screenPosition) {
        batch.draw(getTexture(), screenPosition.x + World.getCameraOffset().x, screenPosition.y + World.getCameraOffset().y, getOriginCenter().x,
                getOriginCenter().y, getWidth(), getHeight(), scaleFactor, scaleFactor, rotationAngle);
    }

    /**
     * @param pos Position of the texture
     * @return Bounding shape
     */
    public BoundingShape getBoundingShape(Vector2 pos) {
        Vector2 position = getOriginalPosition(pos);
        BoundingShape out = new BoundingCircle(new Vector2(), -1);

        if (boundingType == BoundingCircle.class)
            out = new BoundingCircle(position.add(getOriginalWidth() / 2, getOriginalHeight() / 2), (int) Math.max(getWidth(), getHeight()) / 2);
        else if (boundingType == BoundingRect.class)
            out = new BoundingRect(position.add(getOriginalWidth() - getWidth(), getOriginalHeight() - getHeight()), new Vector2(getWidth(), getHeight()));
        if (out.isValid()) return out;
        Logger.log("DrawData.getBoundingShape()", "Error constructing " + boundingType.toString() + ": bounding is invalid.");
        return null;
    }

    /**
     * Bounds this texture to map.
     * @param absolutePosition Absolute position
     * @return new Vector2 bounded inside map.
     */
    public Vector2 boundMap(Vector2 absolutePosition) {
        Vector2 out = new Vector2(absolutePosition);
        if (out.x < 0) out.x = 0;
        if (out.y < 0) out.y = 0;
        if (out.x + getWidth() > World.getWidth()) out.x = World.getWidth() - getWidth();
        if (out.y + getHeight() > World.getHeight()) out.y = World.getHeight() - getHeight();
        return out;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public void addRotationAngle(float v) {
        rotationAngle += v;
    }

    public abstract void update();
}
