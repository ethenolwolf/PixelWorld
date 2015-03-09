package com.mygdx.pixelworld.data.weapons;

public class WeaponStats {
    private final int damage;
    private final int range;
    private final int speed;
    private final Class type;
    private final String name;

    //Enemies stats constructor
    public WeaponStats(Class type, int damage, int range, int speed, String name) {
        this.type = type;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
    }

    //Player constructor
    public WeaponStats(Class playerClass, String name, int damage, int range, int speed) {
        this.type = playerClass;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
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
}
