package com.mygdx.pixelworld.data.items.weapons;

import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Weapon extends Item implements EquipItem {
    private final WeaponStats weaponStats;
    private final boolean isEmpty;

    public Weapon(Class playerClass, int rank) {
        weaponStats = XMLLoader.retrieveWeapon(playerClass, rank);
        if (!weaponStats.getType().isInstance(Enemy.class))
            img = new DrawData(AssetType.WEAPON, weaponStats.getName());
        else img = null;
        isEmpty = false;
    }

    public Weapon() {
        img = new DrawData();
        weaponStats = null;
        isEmpty = true;
    }

    public Weapon(Class playerClass) {
        this(playerClass, 1);
    }

    public WeaponStats getStats() {
        return weaponStats;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public Class getType() {
        return weaponStats.getType();
    }
}
