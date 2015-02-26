package com.mygdx.pixelworld.data;

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
    public static final float BULLET_RANGE = (float) 400.0f;
    public static final float BULLET_SPEED = 600.0f;
    public static final int BULLET_DAMAGE = 20;
}
