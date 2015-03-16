package com.mygdx.pixelworld.data.items.weapons;

import com.mygdx.pixelworld.data.entities.characters.GameClasses;

public class PlayerWeaponStats extends WeaponStats {

    private GameClasses type;

    public PlayerWeaponStats(GameClasses playerClass, String name, int damage, int range, int speed, float rotationSpeed) {
        this.type = playerClass;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
        this.rotationSpeed = rotationSpeed;
    }

    public GameClasses getType() {
        return type;
    }
}
