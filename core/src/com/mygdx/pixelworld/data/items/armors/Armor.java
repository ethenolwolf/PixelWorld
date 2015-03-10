package com.mygdx.pixelworld.data.items.armors;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Armor extends Item {
    private final ArmorStats armorStats;

    public Armor(Class playerClass, int rank) {
        armorStats = XMLLoader.retrieveArmor(playerClass, rank);
        img = new DrawData(AssetType.ARMOR, armorStats.getName());
    }

    public int getDefense() {
        return armorStats.getDefense();
    }
}
