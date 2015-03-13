package com.mygdx.pixelworld.data.items.armors;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Armor extends Item implements EquipItem {
    private final boolean empty;
    private ArmorStats armorStats;

    public Armor(Class playerClass, int rank) {
        armorStats = XMLLoader.retrieveArmor(playerClass, rank);
        img = new DrawData(AssetType.ARMOR, armorStats.getName());
        empty = false;
    }

    public Armor() {
        armorStats = new ArmorStats("", 0, null);
        img = new DrawData();
        empty = true;
    }

    public Armor(Class playerClass) {
        this(playerClass, 1);
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

    @Override
    public Class getType() {
        return armorStats.getType();
    }
}
