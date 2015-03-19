package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.Map;
import com.mygdx.pixelworld.data.assets.Assets;

public abstract class DrawData {
    protected TextureRegion texture;
    protected Vector2 scaleFactor;
    protected float rotationAngle;

    public DrawData() {
    }

    public float getWidth() {
        return getOriginalWidth() * scaleFactor.x;
    }

    protected float getOriginalWidth() {
        return texture.getRegionWidth();
    }

    public float getHeight() {
        return getOriginalHeight() * scaleFactor.y;
    }

    protected float getOriginalHeight() {
        return texture.getRegionHeight();
    }

    public Vector2 getOriginCenter() {
        return new Vector2(getOriginalWidth() / 2, getOriginalHeight() / 2);
    }

    public Vector2 getEffectivePosition(Vector2 absolutePosition) {
        Vector2 out = new Vector2(absolutePosition);
        return out.add(Map.getOffset());
    }

    public Vector2 getOriginalPosition(Vector2 absolutePosition) {
        return new Vector2(
                absolutePosition.x + (getWidth() - getOriginalWidth()) / 2,
                absolutePosition.y + (getHeight() - getOriginalHeight()) / 2
        );
    }

    void setRotationAngle(float angle) {
        rotationAngle = angle;
    }

    public abstract void draw(SpriteBatch batch, Vector2 absolutePosition);

    public void draw(SpriteBatch batch, Vector2 absolutePosition, float alpha) {
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, alpha);
        draw(batch, absolutePosition);
        batch.setColor(c.r, c.g, c.b, 1.0f);
    }

    protected boolean isVisible(Vector2 absolutePosition) {
        return !(absolutePosition.x + getWidth() < 0 || absolutePosition.x > Map.getWidth() ||
                absolutePosition.y + getHeight() < 0 || absolutePosition.y > Map.getHeight());
    }

    public void drawEffective(SpriteBatch batch, Vector2 effectivePosition) {
        batch.draw(texture, effectivePosition.x, effectivePosition.y, getOriginCenter().x,
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
        if (out.x + getWidth() > Map.getWidth()) out.x = Map.getWidth() - getWidth();
        if (out.y + getHeight() > Map.getHeight()) out.y = Map.getHeight() - getHeight();
        return out;
    }

    public void write(SpriteBatch batch, String name, float x, float y) {
        Assets.write(batch, name, x, y);
    }

    void setScaleFactor(Vector2 scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public void addRotationAngle(float v) {
        rotationAngle += v;
    }

    public abstract void update();
}
