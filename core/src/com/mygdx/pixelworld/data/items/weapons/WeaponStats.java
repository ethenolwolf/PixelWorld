package com.mygdx.pixelworld.data.items.weapons;

public abstract class WeaponStats {
    int damage;
    int range;
    int speed;
    String name;
    float rotationSpeed;

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }
}
