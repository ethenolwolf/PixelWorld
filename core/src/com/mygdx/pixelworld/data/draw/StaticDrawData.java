package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.DrawManager;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingCircle;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

/**
 * Draw data class for static images.
 */
public class StaticDrawData extends DrawData {

    private final String path;

    /**
     * @param path         Path of image
     * @param boundingType Type of bounding shape
     */
    public StaticDrawData(String path, Class<? extends BoundingShape> boundingType) {
        setScaleFactor(1);
        setRotationAngle(0);
        this.path = path;
        this.boundingType = boundingType;
        addToLoader();
    }

    /**
     * Empty items
     */
    public StaticDrawData() {
        this("core/assets/gui/placeholder.png", BoundingRect.class);
    }

    /**
     * Generic constructor
     * @param assetType Type of asset
     * @param name Name of asset
     */
    public StaticDrawData(AssetType assetType, String name) {
        //Weapon // Armor //Sigil //Bullet
        this("core/assets/" + assetType.toString().toLowerCase() + "/" + name + ".png", assetType == AssetType.BULLET ? BoundingCircle.class : BoundingRect.class);
    }

    private void addToLoader() {
        if (Game.assetManager.isLoaded(path)) return;
        Game.assetManager.load(path, Texture.class);
    }

    @Override
    protected TextureRegion getTexture() {
        return new TextureRegion(Game.assetManager.get(path, Texture.class));
    }

    @Override
    public void draw(Vector2 absolutePosition, float scaleFactor) {
        draw(absolutePosition);
    }

    @Override
    public void draw(Vector2 absolutePosition) {
        if (!isVisible(absolutePosition)) return;
        DrawManager.getBatch().draw(getTexture(), getOriginalPosition(absolutePosition).x, getOriginalPosition(absolutePosition).y, getOriginCenter().x,
                getOriginCenter().y, getOriginalWidth(), getOriginalHeight(), scaleFactor, scaleFactor, rotationAngle);
    }

    @Override
    public void update() {
    }
}
