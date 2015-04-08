package com.mygdx.pixelworld.data.items.weapons;

public class EnemyWeaponStats extends WeaponStats {

    public EnemyWeaponStats(int damage, int range, int speed, String name, float rotationSpeed) {
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
        this.rotationSpeed = rotationSpeed;
    }
}
