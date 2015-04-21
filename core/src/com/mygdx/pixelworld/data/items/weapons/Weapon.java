package com.mygdx.pixelworld.data.items.weapons;

import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.Config;

/**
 * Class for weapons.
 */
public class Weapon extends Item implements EquipItem {
    private final PlayerWeaponStats weaponStats;
    private final boolean isEmpty;

    /**
     * @param playerClass Player class
     * @param rank        Rank of weapon
     */
    public Weapon(GameClasses playerClass, int rank) {
        WeaponType weaponType = WeaponType.values()[0];
        for (int i = 0; i < WeaponType.values().length && !isSuitable(playerClass, weaponType); i++)
            weaponType = WeaponType.values()[i];
        weaponStats = Config.getWeapon(weaponType, rank);
        img = new StaticDrawData(AssetType.WEAPON, weaponStats != null ? weaponStats.getName() : null);
        StaticDrawData tmp = new StaticDrawData(AssetType.BULLET, weaponStats != null ? weaponStats.getName() : null);
        isEmpty = false;
    }

    /**
     * Empty weapon
     */
    public Weapon() {
        img = new StaticDrawData();
        weaponStats = null;
        isEmpty = true;
    }

    /**
     * Initial weapon
     * @param playerClass Player class
     */
    public Weapon(GameClasses playerClass) {
        this(playerClass, 1);
    }

    public WeaponStats getStats() {
        return weaponStats;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public boolean isSuitable(GameClasses gameClass) {
        return isSuitable(gameClass, weaponStats.getType());
    }

    private boolean isSuitable(GameClasses gameClass, WeaponType weaponType) {
        switch (weaponType) {
            case STAFF:
                return gameClass == GameClasses.WIZARD ||
                        gameClass == GameClasses.CLERIC;

            case SHURIKEN:
                return gameClass == GameClasses.NINJA;

            default:
                Logger.log("Weapon.isSuitable()", "WeaponType not yet implemented in switch");
                return false;
        }
    }
}