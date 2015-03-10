package com.mygdx.pixelworld.data.items.weapons;

public class WeaponStats {
    private final int damage;
    private final int range;
    private final int speed;
    private final Class type;
    private final String name;
    private final float rotationSpeed;

    //Player constructor
    public WeaponStats(Class playerClass, String name, int damage, int range, int speed, float rotationSpeed) {
        this.type = playerClass;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
        this.rotationSpeed = rotationSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }
}
