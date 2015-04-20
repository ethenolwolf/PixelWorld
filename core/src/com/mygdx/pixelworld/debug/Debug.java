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
 * Class created to contain all debug options that can be easily switched from here.
 */
public class Debug {

    private static final Properties props = new Properties();
    private static final List<Debuggable> debuggable = new ArrayList<>();
    private static BitmapFont font;

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

    public static boolean valueOf(String propertyName) {
        return Boolean.parseBoolean(props.getProperty(propertyName));
    }

    public static void addDebuggable(Debuggable debuggable){
        Debug.debuggable.add(debuggable);
    }

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

    private static void write(SpriteBatch batch, String message, float x, float y) {
        font.draw(batch, message, x, y);
    }

}
