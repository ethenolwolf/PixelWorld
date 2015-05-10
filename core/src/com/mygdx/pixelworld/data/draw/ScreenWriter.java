package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.CameraManager;
import com.mygdx.pixelworld.data.utilities.Constants;

public class ScreenWriter {

    private static BitmapFont font;

    public static void init() {
        FreeTypeFontGenerator ft = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/various/Ubuntu.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 16;
        font = ft.generateFont(p);
    }

    public static void write(SpriteBatch batch, String message, float x, float y, Color color) {
        font.setColor(Color.CLEAR);
        font.setColor(color);
        Vector2 offset = CameraManager.getCameraOffset();
        if (offset == null) return;
        font.draw(batch, message, x + offset.x, y + offset.y);
    }

    public static void writeOnCenter(SpriteBatch batch, String message, float y, Color color) {
        float x = (Constants.gameWidth - font.getBounds(message).width) / 2;
        write(batch, message, x, y, color);
    }

}
