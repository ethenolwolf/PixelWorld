package com.mygdx.pixelworld.data.items.weapons;

public class PlayerWeaponStats extends WeaponStats {

    private final WeaponType type;

    public PlayerWeaponStats(WeaponType weaponType, String name, int damage, int range, int speed, float rotationSpeed) {
        this.type = weaponType;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.name = name;
        this.rotationSpeed = rotationSpeed;
    }

    public WeaponType getType() {
        return type;
    }
}
