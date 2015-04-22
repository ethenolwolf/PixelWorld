package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.HashMap;

/**
 * Class that handles moving sprites loaded from spritesheets.
 */
public class AnimationDrawData extends DrawData {
    private final int sheetCols;
    private final int sheetRows;
    private final Class enumClass;
    private final String baseFilePath;
    private java.util.Map<Integer, Animation> animationMap;
    private int currentAction;
    private float stateTime;

    /**
     * @param baseFilePath Base relative path for loading an image
     * @param enumClass    Class file of enum containing every possible animation
     * @param sheetCols    Column count of spritesheet file
     * @param sheetRows    Rows count of spritesheet file
     * @param boundingType Class file of desired bounding type
     */
    public AnimationDrawData(String baseFilePath, Class enumClass, int sheetCols, int sheetRows, Class<? extends BoundingShape> boundingType) {
        Object[] values = enumClass.getEnumConstants();
        this.sheetCols = sheetCols;
        this.sheetRows = sheetRows;
        this.enumClass = enumClass;
        this.baseFilePath = baseFilePath;
        for (Object value : values)
            Game.assetManager.load(baseFilePath + value.toString().toLowerCase() + ".png", Texture.class);
        setScaleFactor(1);
        setRotationAngle(0);
        this.boundingType = boundingType;
    }

    /**
     * Method called when sprites have finished loading.
     */
    private void initAnimation() {
        Object[] values = enumClass.getEnumConstants();
        animationMap = new HashMap<>();
        for (int i1 = 0; i1 < values.length; i1++) {
            Object value = values[i1];
            Texture walkSheet = Game.assetManager.get(baseFilePath + value.toString().toLowerCase() + ".png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / sheetCols, walkSheet.getHeight() / sheetRows); //Get bidimensional array
            TextureRegion[] walkFrames = new TextureRegion[sheetCols * sheetRows];
            int index = 0;
            for (int i = 0; i < sheetRows; i++) {
                for (int j = 0; j < sheetCols; j++) {
                    walkFrames[index++] = tmp[i][j];
                }
            }
            animationMap.put(i1, new Animation(0.025f, walkFrames));
        }
        stateTime = 0f;
    }

    /**
     * You must call this method for going forward in timeline and move the sprite.
     */
    @Override
    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    /**
     * Sets current animation.
     * @param action New action (by enum index)
     */
    public void setCurrentAction(int action) {
        if(currentAction == action) return;
        this.currentAction = action;
        stateTime = 0;
    }

    /**
     * @return Current texture
     */
    @Override
    protected TextureRegion getTexture() {
        if (animationMap == null) initAnimation();
        return animationMap.get(currentAction).getKeyFrame(stateTime, true);
    }

    /**
     * Draws current texture on screen.
     * @param batch SpriteBatch for drawing
     * @param absolutePosition Absolute position of the sprite
     */
    @Override
    public void draw(SpriteBatch batch, Vector2 absolutePosition) {
        batch.draw(getTexture(), absolutePosition.x, absolutePosition.y, getOriginCenter().x, getOriginCenter().y, getTexture().getRegionWidth(), getTexture().getRegionHeight(), scaleFactor, scaleFactor, 0);
    }
}
