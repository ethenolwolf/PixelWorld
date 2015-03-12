package com.mygdx.pixelworld.data.items.armors;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Armor extends Item {
    private final boolean empty;
    private ArmorStats armorStats;

    public Armor(Class playerClass, int rank) {
        armorStats = XMLLoader.retrieveArmor(playerClass, rank);
        img = new DrawData(AssetType.ARMOR, armorStats.getName());
        empty = false;
    }

    public Armor(Armor armor) {
        armorStats = new ArmorStats(armor.getName(), armor.getDefense());
        img = new DrawData(AssetType.ARMOR, armorStats.getName());
        empty = false;
    }

    public Armor() {
        armorStats = null;
        img = new DrawData();
        empty = true;
    }

    public int getDefense() {
        return armorStats.getDefense();
    }

    public String getName() {
        return armorStats.getName();
    }

    public boolean isEmpty() {
        return empty;
    }
}
