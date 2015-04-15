package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;

public class StaticDrawData extends DrawData {

    String path;

    public StaticDrawData() {
        //EmptyItem
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
        path = "core/assets/placeholder.png";
        addToLoader();
    }

    public StaticDrawData(AssetType assetType, String name) {
        //Weapon // Armor //Sigil //Bullet
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
        path = "core/assets/" + assetType.toString().toLowerCase() + "/" + name + ".png";
        addToLoader();
    }

    public StaticDrawData(AssetType assetType) {
        //Chest
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
        path = "core/assets/" + assetType.toString().toLowerCase() + ".png";
        addToLoader();
    }

    private void addToLoader() {
        if (Game.assetManager.isLoaded(path)) return;
        Game.assetManager.load(path, Texture.class);
        //Logger.log("StaticDrawData.addToLoader()", "Queued " + path + "...");
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
