package com.mygdx.pixelworld.data.items.armors;

public class ArmorStats {
    private final ArmorType type;
    private final String name;
    private final int defense;

    public ArmorStats(ArmorType type, String name, int defense) {
        this.type = type;
        this.name = name;
        this.defense = defense;
    }

    public String getName() {
        return (name != null) ? name : "";
    }

    public int getDefense() {
        return defense;
    }

    public ArmorType getArmorType() {
        return type;
    }
}
