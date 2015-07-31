package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.DrawManager;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.HashMap;

/**
 * Class that handles moving sprites loaded from spreadsheets.
 */
public class AnimationDrawData extends DrawData {

    private final int sheetCols;
    private final int sheetRows;
    private final String baseFilePath;
    private final String[] actions;
    private java.util.Map<String, Animation> animationMap;
    private String currentAction;
    private float stateTime;

    /**
     * @param baseFilePath Base relative path for loading an image
     * @param actions      Actions of the sprite
     * @param boundingType Class file of desired bounding type
     */
    public AnimationDrawData(String baseFilePath, String[] actions, Class<? extends BoundingShape> boundingType) {
        this.sheetCols = 8;
        this.sheetRows = 8;
        this.baseFilePath = baseFilePath;
        this.actions = actions;
        for (String action : actions)
            Game.assetManager.load(baseFilePath + action + ".png", Texture.class);
        setScaleFactor(1);
        setRotationAngle(0);
        this.boundingType = boundingType;
    }

    /**
     * Method called when sprites have finished loading.
     */
    private void initAnimation() {
        animationMap = new HashMap<>();
        for (String action : actions) {
            Texture walkSheet = Game.assetManager.get(baseFilePath + action + ".png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / sheetCols, walkSheet.getHeight() / sheetRows); //Get bidimensional array
            TextureRegion[] walkFrames = new TextureRegion[sheetCols * sheetRows];
            int index = 0;
            for (int row = 0; row < sheetRows; row++) {
                for (int col = 0; col < sheetCols; col++) {
                    walkFrames[index++] = tmp[row][col];
                }
            }
            animationMap.put(action, new Animation(0.025f, walkFrames));
        }
        stateTime = 0f;
        currentAction = actions[0];
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
     * @param action New action
     */
    public void setCurrentAction(String action) {
        if (animationMap == null) initAnimation();
        if (action == null || currentAction.equals(action)) return;
        if (!animationMap.containsKey(action)) {
            logger.error("Action " + action + " not recognized. Maintaining old action instead.");
            return;
        }
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
     * @param absolutePosition Absolute position of the sprite
     */
    @Override
    public void draw(Vector2 absolutePosition) {
        DrawManager.getBatch().draw(getTexture(), absolutePosition.x, absolutePosition.y, getOriginCenter().x, getOriginCenter().y, getTexture().getRegionWidth(), getTexture().getRegionHeight(), scaleFactor, scaleFactor, 0);
    }
}
