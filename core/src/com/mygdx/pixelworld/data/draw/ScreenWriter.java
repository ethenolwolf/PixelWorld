package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.utilities.Constants;

public class ScreenWriter {

    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static BitmapFont font;

    public static void init() {
        FreeTypeFontGenerator ft = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/various/Ubuntu.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 16;
        font = ft.generateFont(p);
    }

    /**
     * Write on screen.
     *
     * @param batch   SpriteBatch for drawing
     * @param message Message to write + World.getCameraOffset().x
     * @param x       X of the message
     * @param y       Y of the message
     */
    public static void write(SpriteBatch batch, String message, float x, float y) {
        write(batch, message, x, y, DEFAULT_COLOR);
    }

    public static void write(SpriteBatch batch, String message, float x, float y, Color color) {
        font.setColor(Color.CLEAR);
        font.setColor(color);
        font.draw(batch, message, x + World.getCameraOffset().x, y + World.getCameraOffset().y);
    }

    public static void writeOnCenter(SpriteBatch batch, String message, float y, Color color) {
        float x = (Constants.gameWidth - font.getBounds(message).width) / 2;
        write(batch, message, x, y, color);
    }

}
