package com.mygdx.pixelworld.data.weapons;

import com.mygdx.pixelworld.data.utilities.Constants;

public class WeaponStats {
    int damage;
    int range;
    int speed;
    Class type;
    String name;
    int rank;

    public WeaponStats(Class type, int damage, int range, int speed, String name) {
        this.type = type;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
    }

    public WeaponStats(Class type) {
        WeaponStats ws = Constants.enemyStats.get(type);
        this.type = ws.getType();
        this.damage = ws.getDamage();
        this.range = ws.getRange();
        this.speed = ws.getSpeed();
    }

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

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public void set(Weapon w) {
        type = w.getStats().getType();
        damage = w.getStats().getDamage();
        speed = w.getStats().getSpeed();
        range = w.getStats().getRange();
    }

    public String getName() {
        return name;
    }
}
