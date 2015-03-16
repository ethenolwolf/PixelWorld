package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.enemies.Blocker;
import com.mygdx.pixelworld.data.items.weapons.EnemyWeaponStats;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;

import java.util.*;

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
    public static final Map<GameClasses, EntityStats> playerInitStats = new EnumMap<GameClasses, EntityStats>(GameClasses.class);
    public static final Map<Class, EntityStats> enemyInitStats = new HashMap<Class, EntityStats>();
    public static final Map<Class, WeaponStats> enemyStats = new HashMap<Class, WeaponStats>();
    public static final List<Integer> levelUpValues = new ArrayList<Integer>();
    public static final float gameWidth = 640;
    public static final float gameHeight = 480;
    public static final float panelWidth = 160;

    public static void init() {
        enemyStats.put(Blocker.class, new EnemyWeaponStats(Blocker.class,
                10, //dmg
                400,//range
                400,//spd
                "blocker", //name
                0 //rotationSpeed
        ));

        playerInitStats.put(GameClasses.WIZARD, new EntityStats(
                100, //health
                100, //mana
                50, //spd
                60, //dex
                15, //wis
                3, //vit
                7, //atk
                0  //def
        ));

        playerInitStats.put(GameClasses.NINJA, new EntityStats(
                100, //health
                100, //mana
                50, //spd
                95, //dex
                80, //wis
                3, //vit
                2, //atk
                0  //def
        ));

        enemyInitStats.put(Blocker.class, new EntityStats(
                100, //health
                0, //mana
                50, //spd
                15, //dex
                0, //wis
                0, //vit
                0, //atk
                5  //def
        ));

        levelUpValues.add(50);
        levelUpValues.add(200);
        levelUpValues.add(500);
        levelUpValues.add(1500);
        levelUpValues.add(5000);
    }
}
