package com.mygdx.pixelworld.data.items.armors;

import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Armor extends Item implements EquipItem {
    private final boolean empty;
    private ArmorStats armorStats;

    public Armor(GameClasses playerClass, int rank) {
        armorStats = XMLLoader.retrieveArmor(playerClass, rank);
        img = new StaticDrawData(AssetType.ARMOR, armorStats.getName());
        empty = false;
    }

    public Armor() {
        armorStats = new ArmorStats("", 0, null);
        img = new StaticDrawData();
        empty = true;
    }

    public Armor(GameClasses playerClass) {
        this(playerClass, 1);
    }

    public int getDefense() {
        return armorStats.getDefense();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public GameClasses getGameClass() {
        return armorStats.getGameClass();
    }
}
