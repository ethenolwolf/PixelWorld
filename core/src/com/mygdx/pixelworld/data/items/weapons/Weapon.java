package com.mygdx.pixelworld.data.items.weapons;

import com.mygdx.pixelworld.data.draw.StaticDrawData;
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
     * @param rank        Rank of weapon
     */
    public Weapon(WeaponType type, int rank) {
        weaponStats = Config.getWeapon(type, rank);
        img = new StaticDrawData(AssetType.WEAPON, weaponStats != null ? weaponStats.getName() : null);
        new StaticDrawData(AssetType.BULLET, weaponStats != null ? weaponStats.getName() : null);
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

    public WeaponStats getStats() {
        return weaponStats;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}