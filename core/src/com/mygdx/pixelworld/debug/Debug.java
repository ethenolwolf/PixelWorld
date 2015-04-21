package com.mygdx.pixelworld.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

/**
 * Class for debugging utilities.
 */
public class Debug {

    private static final Properties props = new Properties();
    private static final List<Debuggable> debuggable = new ArrayList<>();
    private static BitmapFont font;

    /**
     * Load values and init font.
     */
    public static void init() {
        try {
            props.load(new FileInputStream("core/config/debug.properties"));
        } catch (IOException e) {
            Logger.log("Debug.init()", "File debug.properties not found.");
        }

        FreeTypeFontGenerator ft = new FreeTypeFontGenerator(Gdx.files.internal("core/assets/various/Ubuntu.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = 16;
        p.color = Color.RED;
        font = ft.generateFont(p);
    }

    /**
     * @param propertyName Name of the property to read from debug.properties
     * @return Boolean value of the property
     */
    public static boolean valueOf(String propertyName) {
        return Boolean.parseBoolean(props.getProperty(propertyName));
    }

    /**
     * Adds a debuggable item ready to watch.
     *
     * @param debuggable Debuggable item
     */
    public static void addDebuggable(Debuggable debuggable){
        Debug.debuggable.add(debuggable);
    }

    /**
     * Print every debuggable with its informations.
     * @param batch SpriteBatch for writing.
     */
    public static void draw(SpriteBatch batch){
        if (!valueOf("SHOW_DEBUG_VALUES")) return;
        float y = Constants.gameHeight - 20 + Game.camera.position.y;
        ListIterator<Debuggable> li = debuggable.listIterator();
        while(li.hasNext()) {
            Debuggable d = li.next();
            if(d == null) {
                li.remove();
                continue;
            }
            write(batch, d.getWatch(), 10 + Game.camera.position.x, y);
            y -= 20;
        }
    }

    /**
     * Write on screen.
     * @param batch SpriteBatch for drawing
     * @param message Message to write
     * @param x X of the message
     * @param y Y of the message
     */
    private static void write(SpriteBatch batch, String message, float x, float y) {
        font.draw(batch, message, x, y);
    }

}
