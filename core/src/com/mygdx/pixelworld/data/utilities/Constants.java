package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.data.classes.Wizard;
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
    public static final float X_LIMIT_MIN = 0.25f;
    public static final float X_LIMIT_MAX = 0.75f;
    public static final float Y_LIMIT_MIN = 0.15f;
    public static final float Y_LIMIT_MAX = 0.30f;
    public static final float MANA_PRICE = 50;
    public static final float MANA_REGEN = 40.0f;
    public static final int MANA_DAMAGE = 100;
    public static final float MANA_ANIMATION_SPEED = 0.1f;
    public static final float ATTACK_RANGE = 400.0f;

    public static Map<Class, Integer> BULLET_DAMAGE = new HashMap<Class, Integer>();
    public static Map<Class, Float> BULLET_RANGE = new HashMap<Class, Float>();
    public static Map<Class, Float> BULLET_SPEED = new HashMap<Class, Float>();
    public static Map<Class, EntityStats> initStats = new HashMap<Class, EntityStats>();

    public static void init() {
        BULLET_DAMAGE.put(Wizard.class, 20);
        BULLET_SPEED.put(Wizard.class, 500.0f);
        BULLET_RANGE.put(Wizard.class, 400.0f);

        BULLET_DAMAGE.put(Blocker.class, 20);
        BULLET_SPEED.put(Blocker.class, 400.0f);
        BULLET_RANGE.put(Blocker.class, 350.0f);

        initStats.put(Wizard.class, new EntityStats(
                100, //health
                100, //mana
                50, //spd
                60, //dex
                50, //wis
                50, //vit
                50, //atk
                10  //def
        ));

        initStats.put(Blocker.class, new EntityStats(
                100, //health
                0, //mana
                30, //spd
                60, //dex
                0, //wis
                0, //vit
                20, //atk
                5  //def
        ));
    }
}
