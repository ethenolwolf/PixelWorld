
package com.mygdx.pixelworld.data.armors;

public class ArmorStats {
    private final String name;
    private final int defense;

    public ArmorStats(String name, int defense) {
        this.name = name;
        this.defense = defense;
    }


    public String getName() {
        return name;
    }

    public int getDefense() {
        return defense;
    }
}
