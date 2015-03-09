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
    public static final float X_LIMIT_MIN = 0.25f;
    public static final float X_LIMIT_MAX = 0.75f;
    public static final float Y_LIMIT_MIN = 0.15f;
    public static final float Y_LIMIT_MAX = 0.30f;
    public static final Map<Class, EntityStats> initStats = new HashMap<Class, EntityStats>();
    public static final Map<Class, WeaponStats> enemyStats = new HashMap<Class, WeaponStats>();
    public static final float gameWidth = 640;
    public static final float gameHeight = 480;
    public static final float panelWidth = 160;

    public static void init() {
        enemyStats.put(Blocker.class, new WeaponStats(Blocker.class,
                20, //dmg
                400,//range
                400,//spd
                "blocker" //name
        ));

        initStats.put(Wizard.class, new EntityStats(
                100, //health
                100, //mana
                50, //spd
                60, //dex
                15, //wis
                3, //vit
                7, //atk
                0  //def
        ));

        initStats.put(Blocker.class, new EntityStats(
                100, //health
                0, //mana
                50, //spd
                15, //dex
                0, //wis
                0, //vit
                0, //atk
                5  //def
        ));
    }
}
