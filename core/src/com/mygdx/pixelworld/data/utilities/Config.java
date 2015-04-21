package com.mygdx.pixelworld.data.utilities;

import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.armors.ArmorStats;
import com.mygdx.pixelworld.data.items.armors.ArmorType;
import com.mygdx.pixelworld.data.items.weapons.EnemyWeaponStats;
import com.mygdx.pixelworld.data.items.weapons.PlayerWeaponStats;
import com.mygdx.pixelworld.data.items.weapons.WeaponType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading data from .config files in /config folder.
 */
public class Config {

    /**
     * Attempts to load config file.
     *
     * @param fileName Name of config file
     * @return Properties object
     */
    private static Properties loadFrom(String fileName) {
        if (!fileName.endsWith(".properties")) fileName += ".properties";
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(fileName));
        } catch (IOException e) {
            Logger.log("Config.load()", "File " + fileName + " not found.");
        }
        return properties;
    }

    /**
     * Loads experience thresholds needed for the player to level up.
     * @return Array of thresholds.
     */
    public static Integer[] getExperienceThresholds() {
        Properties p = loadFrom("core/config/experienceThresholds.properties");
        String[] ths = p.getProperty("thresholds").split(",");
        Integer[] out = new Integer[ths.length];
        for (int i = 0; i < ths.length; i++) out[i] = Integer.parseInt(ths[i]);
        return out;
    }

    /**
     * Loads stats for an entity.
     *
     * @param isPlayer   Is entity a player
     * @param entityName Name of entity
     * @return Loaded EntityStats.
     */
    public static EntityStats getStats(boolean isPlayer, String entityName) {
        Properties p = loadFrom("core/config/stats/" + (isPlayer ? "characters/" : "enemies/") + entityName.toLowerCase());
        return new EntityStats(
                Float.parseFloat(p.getProperty("health")),
                Float.parseFloat(p.getProperty("mana")),
                Integer.parseInt(p.getProperty("spd")),
                Integer.parseInt(p.getProperty("dex")),
                Integer.parseInt(p.getProperty("wis")),
                Integer.parseInt(p.getProperty("vit")),
                Integer.parseInt(p.getProperty("atk")),
                Integer.parseInt(p.getProperty("def"))
        );
    }

    /**
     * Loads EnemyWeaponStats.
     * @param type Type of enemy
     * @return WeaponStats of the enemy.
     */
    public static EnemyWeaponStats getWeapon(Class<? extends Enemy> type) {
        Properties p = loadFrom("core/config/weapons/enemies/" + type.getSimpleName().toLowerCase());
        return new EnemyWeaponStats(
                Integer.parseInt(p.getProperty("damage")),
                Integer.parseInt(p.getProperty("range")),
                Integer.parseInt(p.getProperty("speed")),
                p.getProperty("name"),
                Float.parseFloat(p.getProperty("rotationSpeed"))
        );
    }

    /**
     * Loads PlayerWeaponStats.
     * @param weaponType Type of weapon
     * @param rank Rank of weapon
     * @return WeaponStats of the player.
     */
    public static PlayerWeaponStats getWeapon(WeaponType weaponType, int rank) {
        Properties p = loadFrom("core/config/weapons/characters/" + weaponType.toString().toLowerCase());
        String prefix = String.valueOf(rank) + ".";
        return new PlayerWeaponStats(
                weaponType,
                p.getProperty(prefix + "name"),
                Integer.parseInt(p.getProperty(prefix + "damage")),
                Integer.parseInt(p.getProperty(prefix + "range")),
                Integer.parseInt(p.getProperty(prefix + "speed")),
                Float.parseFloat(p.getProperty(prefix + "rotationSpeed"))
        );
    }

    /**
     * Loads ArmorStats.
     * @param armorType Type of armor
     * @param rank Rank of armor
     * @return ArmorStats
     */
    public static ArmorStats getArmor(ArmorType armorType, int rank) {
        Properties p = loadFrom("core/config/armors/" + armorType.toString().toLowerCase());
        String prefix = String.valueOf(rank) + ".";
        return new ArmorStats(
                armorType,
                p.getProperty(prefix + "name"),
                Integer.parseInt(p.getProperty(prefix + "defense"))
        );
    }
}
