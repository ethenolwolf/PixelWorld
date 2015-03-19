package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.Map;

import java.util.HashMap;

public class AnimationDrawData extends DrawData {

    java.util.Map<Integer, Animation> animationMap;
    int currentAction;
    float stateTime;

    public <E extends Enum<E>> AnimationDrawData(String baseFilePath, Class<E> enumClass, int sheetCols, int sheetRows) {
        E[] values = enumClass.getEnumConstants();
        animationMap = new HashMap<Integer, Animation>();
        for (E value : values) {
            Texture walkSheet = new Texture(Gdx.files.internal(baseFilePath + value.toString().toLowerCase() + ".png")); //Load the big image
            TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / sheetCols, walkSheet.getHeight() / sheetRows); //Get bidimensional array
            TextureRegion[] walkFrames = new TextureRegion[sheetCols * sheetRows];
            int index = 0;
            for (int i = 0; i < sheetRows; i++) {
                for (int j = 0; j < sheetCols; j++) {
                    walkFrames[index++] = tmp[i][j];
                }
            }
            animationMap.put(value.ordinal(), new Animation(0.025f, walkFrames));
        }
        stateTime = 0f;
        texture = animationMap.get(0).getKeyFrame(0);
        setScaleFactor(new Vector2(1, 1));
        setRotationAngle(0);
    }

    @Override
    public void update() {
        stateTime += Game.deltaTime;
        texture = animationMap.get(currentAction).getKeyFrame(stateTime, true);
    }

    public void setCurrentAction(int action) {
        this.currentAction = action;
        stateTime = 0;
    }

    @Override
    public void draw(SpriteBatch batch, Vector2 absolutePosition) {
        batch.draw(texture, absolutePosition.x + Map.getOffset().x, absolutePosition.y + Map.getOffset().y);
    }
}
