package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.data.classes.Wizard;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.weapons.WeaponStats;

import java.util.HashMap;
import java.util.Map;

/**
 * Class defined to contain all game Constants.
 *
 * @author alessandro
 */
public class Constants {

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=-";
    public static final float MANA_PRICE = 50;
    public static final int MANA_DAMAGE = 100;
    public static final float MANA_ANIMATION_SPEED = 0.1f;
    public static final float ATTACK_RANGE = 400.0f;
    public static final float X_LIMIT_MIN = 0.25f;
    public static final float X_LIMIT_MAX = 0.75f;
    public static final float Y_LIMIT_MIN = 0.15f;
    public static final float Y_LIMIT_MAX = 0.30f;
    public static Map<Class, EntityStats> initStats = new HashMap<Class, EntityStats>();
    public static Map<Class, WeaponStats> enemyStats = new HashMap<Class, WeaponStats>();
    public static float gameWidth = 640;
    public static float gameHeight = 480;
    public static float panelWidth = 160;

    public static void init() {
        enemyStats.put(Blocker.class, new WeaponStats(Blocker.class,
                10, //dmg
                400,//range
                400//spd
        ));

        initStats.put(Wizard.class, new EntityStats(
                300, //health
                100, //mana
                50, //spd
                60, //dex
                15, //wis
                50, //vit
                50, //atk
                10  //def
        ));

        initStats.put(Blocker.class, new EntityStats(
                100, //health
                0, //mana
                50, //spd
                60, //dex
                0, //wis
                0, //vit
                0, //atk
                5  //def
        ));
    }
}
