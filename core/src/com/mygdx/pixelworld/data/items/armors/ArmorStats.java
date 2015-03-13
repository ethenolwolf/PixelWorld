package com.mygdx.pixelworld.data.items.armors;

public class ArmorStats {
    private final String name;
    private final int defense;
    private Class type;

    public ArmorStats(String name, int defense, Class type) {
        this.name = name;
        this.defense = defense;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public int getDefense() {
        return defense;
    }

    public Class getType() {
        return type;
    }
}
