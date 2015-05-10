package com.mygdx.pixelworld.data.items.armors;

import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.Config;

/**
 * Armor item protects the owner and raise its defense.
 */
public class Armor extends Item implements EquipItem {
    private final ArmorStats armorStats;
    private final int rank;

    /**
     * Player armor init.
     * @param rank        Rank of the armor
     */
    public Armor(ArmorType type, int rank) {
        this.rank = rank;
        armorStats = Config.getArmor(type, rank);
        assert armorStats != null;
        img = new StaticDrawData(AssetType.ARMOR, armorStats.getName());
    }

    /**
     * Initializes empty armor
     */
    public Armor() {
        this.rank = 0;
        armorStats = new ArmorStats(null, "", 0);
        img = new StaticDrawData();
    }

    public int getDefense() {
        return armorStats.getDefense();
    }

    @Override
    public String toString() {
        return "ARMOR:" + armorStats.getArmorType().name() + ":" + rank;
    }
}
