package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.World;

public abstract class DrawData implements Disposable {
    TextureRegion texture;
    Vector2 scaleFactor;
    float rotationAngle;
    DrawData() {
    }

    @Override
    public void dispose() {
        texture.getTexture().dispose();
    }

    public float getWidth() {
        return getOriginalWidth() * scaleFactor.x;
    }

    float getOriginalWidth() {
        return texture.getRegionWidth();
    }

    public float getHeight() {
        return getOriginalHeight() * scaleFactor.y;
    }

    float getOriginalHeight() {
        return texture.getRegionHeight();
    }

    Vector2 getOriginCenter() {
        return new Vector2(getOriginalWidth() / 2, getOriginalHeight() / 2);
    }

    Vector2 getOriginalPosition(Vector2 absolutePosition) {
        return new Vector2(
                absolutePosition.x + (getWidth() - getOriginalWidth()) / 2,
                absolutePosition.y + (getHeight() - getOriginalHeight()) / 2
        );
    }

    void setRotationAngle(float angle) {
        rotationAngle = angle;
    }

    public void draw(SpriteBatch batch, Vector2 absolutePosition) {
        draw(batch, absolutePosition, 1.0f);
    }

    public abstract void draw(SpriteBatch batch, Vector2 absolutePosition, float scaleFactor);

    public void draw(SpriteBatch batch, Vector2 absolutePosition, float scaleFactor, float alpha) {
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, alpha);
        draw(batch, absolutePosition, scaleFactor);
        batch.setColor(c.r, c.g, c.b, 1.0f);
    }

    boolean isVisible(Vector2 absolutePosition) {
        return !(absolutePosition.x + getWidth() < 0 || absolutePosition.x > World.getWidth() ||
                absolutePosition.y + getHeight() < 0 || absolutePosition.y > World.getHeight());
    }

    public void drawOnScreen(SpriteBatch batch, Vector2 screenPosition) {
        batch.draw(texture, screenPosition.x + World.getCameraOffset().x, screenPosition.y + World.getCameraOffset().y, getOriginCenter().x,
                getOriginCenter().y, getWidth(), getHeight(), scaleFactor.x, scaleFactor.y, rotationAngle);
    }

    public BoundingCircle getBoundingCircle(Vector2 pos) {
        Vector2 absolutePosition = getOriginalPosition(pos);
        BoundingCircle out = new BoundingCircle(new Vector2(absolutePosition.x + getOriginCenter().x, absolutePosition.y + getOriginCenter().y), Math.max(getWidth() / 2, getHeight() / 2));
        if (out.isValid()) return out;
        System.out.println("NOT VALID!");
        return null;
    }

    public Vector2 boundMap(Vector2 absolutePosition) {
        Vector2 out = new Vector2(absolutePosition);
        if (out.x < 0) out.x = 0;
        if (out.y < 0) out.y = 0;
        if (out.x + getWidth() > World.getWidth()) out.x = World.getWidth() - getWidth();
        if (out.y + getHeight() > World.getHeight()) out.y = World.getHeight() - getHeight();
        return out;
    }

    void setScaleFactor(Vector2 scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public void addRotationAngle(float v) {
        rotationAngle += v;
    }

    public abstract void update();
}
