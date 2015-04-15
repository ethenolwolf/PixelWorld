package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;

import java.util.HashMap;

public class AnimationDrawData extends DrawData {

    private java.util.Map<Integer, Animation> animationMap;
    private int currentAction;
    private float stateTime;
    private int sheetCols, sheetRows;
    private Class enumClass;
    private String baseFilePath;

    public AnimationDrawData(String baseFilePath, Class enumClass, int sheetCols, int sheetRows) {
        Object[] values = enumClass.getEnumConstants();
        this.sheetCols = sheetCols;
        this.sheetRows = sheetRows;
        this.enumClass = enumClass;
        this.baseFilePath = baseFilePath;
        for (Object value : values) {
            Game.assetManager.load(baseFilePath + value.toString().toLowerCase() + ".png", Texture.class);
            //Logger.log("AnimationDrawData()", "Queued "+baseFilePath + value.toString().toLowerCase() + ".png ...");
        }
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
    }

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

    @Override
    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public void setCurrentAction(int action) {
        if(currentAction == action) return;
        this.currentAction = action;
        stateTime = 0;
    }

    @Override
    protected TextureRegion getTexture() {
        if (animationMap == null) initAnimation();
        return animationMap.get(currentAction).getKeyFrame(stateTime, true);
    }

    public void draw(SpriteBatch batch, Vector2 absolutePosition, float scaleFactor) {
        batch.draw(getTexture(), absolutePosition.x, absolutePosition.y, getOriginCenter().x, getOriginCenter().y, getTexture().getRegionWidth(), getTexture().getRegionHeight(), scaleFactor, scaleFactor, 0);
    }
}
