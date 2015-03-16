package com.mygdx.pixelworld.data.items.weapons;

public abstract class WeaponStats {
    protected int damage;
    protected int range;
    protected int speed;
    protected String name;
    protected float rotationSpeed;

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
        return name;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }
}
