package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingCircle;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

public class StaticDrawData extends DrawData {

    private String path;

    public StaticDrawData(String path, Class<? extends BoundingShape> boundingType) {
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
        this.path = path;
        this.boundingType = boundingType;
        addToLoader();
    }

    public StaticDrawData() {
        this("core/assets/placeholder.png", BoundingRect.class);
    }

    public StaticDrawData(AssetType assetType, String name) {
        //Weapon // Armor //Sigil //Bullet
        this("core/assets/" + assetType.toString().toLowerCase() + "/" + name + ".png", assetType == AssetType.BULLET ? BoundingCircle.class : BoundingRect.class);
    }

    public StaticDrawData(AssetType assetType) {
        //Chest
        this("core/assets/" + assetType.toString().toLowerCase() + ".png", BoundingRect.class);
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
    public void draw(SpriteBatch batch, Vector2 absolutePosition, float scaleFactor) {
        draw(batch, absolutePosition);
    }

    @Override
    public void draw(SpriteBatch batch, Vector2 absolutePosition) {
        if (!isVisible(absolutePosition)) return;
        batch.draw(getTexture(), getOriginalPosition(absolutePosition).x, getOriginalPosition(absolutePosition).y, getOriginCenter().x,
                getOriginCenter().y, getOriginalWidth(), getOriginalHeight(), scaleFactor.x, scaleFactor.y, rotationAngle);
    }

    @Override
    public void update() {
    }
}
