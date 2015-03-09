
package com.mygdx.pixelworld.data.armors;

public class ArmorStats {
    Class type;
    String name;
    int rank;
    int defense;

    public ArmorStats(Class type, String name, int rank, int defense) {
        this.type = type;
        this.name = name;
        this.rank = rank;
        this.defense = defense;
    }


    public String getName() {
        return name;
    }

    public int getDefense() {
        return defense;
    }
}
