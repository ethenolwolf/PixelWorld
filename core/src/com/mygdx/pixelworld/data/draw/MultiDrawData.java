package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.Map;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.utilities.Directions;

public class MultiDrawData extends DrawData {
    private final TextureRegion[] textureRegions = new TextureRegion[8];
    private int currentStep = 0;
    private int timerCounter = 0;
    private Directions currentDirection = Directions.UP;

    public MultiDrawData(GameClasses classType) {
        //Player
        setScaleFactor(new Vector2(2, 2));
        setRotationAngle(0);
        String basePath = "core/assets/Characters/" + classType.toString() + "/";
        String ext = ".png";
        for (Directions dir : Directions.values()) {
            int d = dir.ordinal();
            textureRegions[d * 2] = new TextureRegion(new Texture(basePath + dir.toString() + "0" + ext));
            textureRegions[d * 2 + 1] = new TextureRegion(new Texture(basePath + dir.toString() + "1" + ext));
        }
    }

    private void nextTexture() {
        currentStep++;
        if (currentStep > 1) currentStep = 0;
    }

    public void setDirection(Directions dir) {
        currentDirection = dir;
    }

    @Override
    public void draw(SpriteBatch batch, Vector2 absolutePosition) {
        if (absolutePosition.x + getWidth() < 0 || absolutePosition.x > Map.getWidth() ||
                absolutePosition.y + getHeight() < 0 || absolutePosition.y > Map.getHeight()) return;
        TextureRegion texture = textureRegions[currentDirection.ordinal() * 2 + currentStep];
        batch.draw(texture, getEffectivePosition(absolutePosition).x, getEffectivePosition(absolutePosition).y, getOriginCenter().x,
                getOriginCenter().y, getWidth(), getHeight(), scaleFactor.x, scaleFactor.y, rotationAngle);
    }

    public void update() {
        timerCounter++;
        if (timerCounter > 50) {
            timerCounter = 0;
            nextTexture();
        }
    }
}
