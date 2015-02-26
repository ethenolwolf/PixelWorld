package com.mygdx.pixelworld.data;

import com.mygdx.pixelworld.data.enemies.Blocker;

import java.util.HashMap;
import java.util.Map;

/**
 * Class defined to contain all game Constants.
 *
 * @author alessandro
 */
public class Constants {

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=-";

    public static final float CHARACTER_WIDTH = (float) 1 / 13;
    public static final float CHARACTER_HEIGHT = (float) 1 / 6;
    public static final float PLAYER_SPEED = 250.0f;
    public static final int PLAYER_DEXTERITY = 60;

    public static final float X_LIMIT_MIN = (float) 0.25;
    public static final float X_LIMIT_MAX = (float) 0.75;
    public static final float Y_LIMIT_MIN = (float) 0.15;
    public static final float Y_LIMIT_MAX = (float) 0.75;

    public static Map<Class, Integer> BULLET_DAMAGE = new HashMap<Class, Integer>();
    public static Map<Class, Float> BULLET_RANGE = new HashMap<Class, Float>();
    public static Map<Class, Float> BULLET_SPEED = new HashMap<Class, Float>();

    public static void init() {
        BULLET_DAMAGE.put(Player.class, 20);
        BULLET_SPEED.put(Player.class, 300.0f);
        BULLET_RANGE.put(Player.class, 400.0f);

        BULLET_DAMAGE.put(Blocker.class, 20);
        BULLET_SPEED.put(Blocker.class, 100.0f);
        BULLET_RANGE.put(Blocker.class, 200.0f);
    }
}
