package com.mygdx.pixelworld.data.items.armors;

import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.Config;

/**
 * Armor item protects the owner and raise its defense.
 */
public class Armor extends Item implements EquipItem {
    private final boolean empty;
    private final ArmorStats armorStats;

    /**
     * Player armor init.
     *
     * @param playerClass Player class
     * @param rank        Rank of the armor
     */
    private Armor(GameClasses playerClass, int rank) {
        ArmorType armorType = ArmorType.values()[0];
        for (int i = 0; i < ArmorType.values().length && !isSuitable(playerClass, armorType); i++)
            armorType = ArmorType.values()[i];
        armorStats = Config.getArmor(armorType, rank);
        assert armorStats != null;
        img = new StaticDrawData(AssetType.ARMOR, armorStats.getName());
        empty = false;
    }

    /**
     * Initializes empty armor
     */
    public Armor() {
        armorStats = new ArmorStats(null, "", 0);
        img = new StaticDrawData();
        empty = true;
    }

    /**
     * Player initial class armor.
     * @param playerClass Player class
     */
    public Armor(GameClasses playerClass) {
        this(playerClass, 1);
    }

    /**
     * Returns whenever gameClass is compatible with armorType.
     *
     * @param gameClass Player class
     * @param armorType Armor type
     * @return Is compatible
     */
    private static boolean isSuitable(GameClasses gameClass, ArmorType armorType) {
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

    public int getDefense() {
        return armorStats.getDefense();
    }

    public boolean isEmpty() {
        return empty;
    }

    /**
     * Returns whenever gameClass is compatible with this armor.
     *
     * @param gameClass Player class
     * @return Is compatible
     */
    @Override
    public boolean isSuitable(GameClasses gameClass) {
        return isSuitable(gameClass, armorStats.getArmorType());
    }
}
