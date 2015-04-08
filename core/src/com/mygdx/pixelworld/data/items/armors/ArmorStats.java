package com.mygdx.pixelworld.data.items.armors;

import com.mygdx.pixelworld.data.entities.characters.GameClasses;

public class ArmorStats {
    private final String name;
    private final int defense;
    private final GameClasses type;

    public ArmorStats(String name, int defense, GameClasses type) {
        this.name = name;
        this.defense = defense;
        this.type = type;
    }

    public String getName() {
        return (name != null) ? name : "";
    }

    public int getDefense() {
        return defense;
    }

    public GameClasses getGameClass() {
        return type;
    }
}
