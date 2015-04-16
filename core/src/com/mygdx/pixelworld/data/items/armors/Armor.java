package com.mygdx.pixelworld.data.items.armors;

import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.Config;

public class Armor extends Item implements EquipItem {
    private final boolean empty;
    private final ArmorStats armorStats;

    private Armor(GameClasses playerClass, int rank) {
        ArmorType armorType = ArmorType.values()[0];
        for (int i = 0; i < ArmorType.values().length && !isSuitable(playerClass, armorType); i++)
            armorType = ArmorType.values()[i];
        armorStats = Config.getArmor(armorType, rank);
        assert armorStats != null;
        img = new StaticDrawData(AssetType.ARMOR, armorStats.getName());
        empty = false;
    }

    public Armor() {
        armorStats = new ArmorStats(null, "", 0);
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
    public boolean isSuitable(GameClasses gameClass) {
        return isSuitable(gameClass, armorStats.getArmorType());
    }

    private boolean isSuitable(GameClasses gameClass, ArmorType armorType) {
        switch (armorType) {
            case LEATHER:
                return gameClass == GameClasses.WIZARD ||
                        gameClass == GameClasses.CLERIC;

            case HEAVY:
                return gameClass == GameClasses.NINJA;

            default:
                Logger.log("Armor.isSuitable()", "Armor Type not yet implemented.");
                return false;
        }
    }
}
