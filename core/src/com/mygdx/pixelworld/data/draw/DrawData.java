package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;

public class DrawData {
    protected TextureRegion texture;
    protected Vector2 scaleFactor;
    protected float rotationAngle;
    protected Class type;

    public DrawData(AssetType assetType, Class classType, Vector2 scaleFactor, float rotationAngle) {
        this.type = classType;
        setScaleFactor(scaleFactor);
        setRotationAngle(rotationAngle);
        texture = new TextureRegion(Assets.getTexture(assetType, classType));
    }

    public DrawData(AssetType assetType, String name) { //Weapon / Armor
        this.type = null;
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
        texture = new TextureRegion(Assets.getTexture(assetType, name));
    }

    public DrawData(String name, float rotationAngle) {
        this.type = null;
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(rotationAngle);
        texture = new TextureRegion(Assets.getTexture(AssetType.BULLET, name));
    }

    public float getWidth() {
        return texture.getRegionWidth();
    }

    public float getHeight() {
        return texture.getRegionHeight();
    }

    public Vector2 getOriginCenter() {
        return new Vector2(getWidth() / 2, getHeight() / 2);
    }

    public Vector2 getEffectivePosition(Vector2 absolutePosition) {
        Vector2 out = new Vector2(absolutePosition);
        return out.add(Map.getOffset());
    }

    public void setRotationAngle(float angle) {
        rotationAngle = angle;
    }

    public void draw(SpriteBatch batch, Vector2 absolutePosition) {
        //TODO draw only if inScreen
        batch.draw(texture, getEffectivePosition(absolutePosition).x, getEffectivePosition(absolutePosition).y, getOriginCenter().x,
                getOriginCenter().y, getWidth(), getHeight(), scaleFactor.x, scaleFactor.y, rotationAngle);
    }

    public void draw(SpriteBatch batch, Vector2 absolutePosition, float alpha) {
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, alpha);
        draw(batch, absolutePosition);
        batch.setColor(c.r, c.g, c.b, 1.0f);
    }

    public void drawEffective(SpriteBatch batch, Vector2 effectivePosition) {
        batch.draw(texture, effectivePosition.x, effectivePosition.y, getOriginCenter().x,
                getOriginCenter().y, getWidth(), getHeight(), scaleFactor.x, scaleFactor.y, rotationAngle);
    }

    public BoundingCircle getBoundingCircle(Vector2 pos) {
        Vector2 absolutePosition = new Vector2(pos);
        //System.out.println("[getBounding] center = "+absolutePosition.add(getOriginCenter()).toString()+" scaleFactors = "+scaleFactor.toString());
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

    public void setScaleFactor(Vector2 scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
