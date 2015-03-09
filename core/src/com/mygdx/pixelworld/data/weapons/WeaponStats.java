package com.mygdx.pixelworld.data.weapons;

import com.mygdx.pixelworld.data.utilities.Constants;

public class WeaponStats {
    int damage;
    int range;
    int speed;
    Class type;
    String name;
    int rank;

    //Enemies stats constructor
    public WeaponStats(Class type, int damage, int range, int speed, String name) {
        this.type = type;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
    }

    //Enemies constructor
    public WeaponStats(Class type) {
        WeaponStats ws = Constants.enemyStats.get(type);
        this.type = ws.getType();
        this.damage = ws.getDamage();
        this.range = ws.getRange();
        this.speed = ws.getSpeed();
    }

    //Player constructor
    public WeaponStats(Class playerClass, String name, int rank, int damage, int range, int speed) {
        this.type = playerClass;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
        this.rank = rank;
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

    public void setType(Class type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
}
