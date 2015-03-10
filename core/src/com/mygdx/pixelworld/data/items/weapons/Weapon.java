package com.mygdx.pixelworld.data.items.weapons;

import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Weapon extends Item {
    private final WeaponStats weaponStats;

    public Weapon(Class playerClass, int rank) {
        weaponStats = XMLLoader.retrieveWeapon(playerClass, rank);
        if (!weaponStats.getType().isInstance(Enemy.class))
            img = new DrawData(AssetType.WEAPON, weaponStats.getName());
        else img = null;
    }

    public WeaponStats getStats() {
        return weaponStats;
    }
}
