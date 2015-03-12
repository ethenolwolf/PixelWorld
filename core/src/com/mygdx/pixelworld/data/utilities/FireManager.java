package com.mygdx.pixelworld.data.utilities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;

public class FireManager {

    private double fireDelay;
    private boolean isFiring;
    private Vector2 target;

    public FireManager() {
        fireDelay = 0;
        isFiring = false;
        target = new Vector2();
    }

    public void setIsFiring(boolean isFiring) {
        this.isFiring = isFiring;
        if (isFiring) fireDelay = 0;
    }

    public void setTarget(float x, float y) {
        target.x = x;
        target.y = y;
    }


    public void updateFire(Vector2 pos, EntityStats entityStats, Map map, WeaponStats stats, boolean isWeaponEmpty) {
        if (!isFiring) return;
        if (isWeaponEmpty) {
            Logger.log("You must equip a weapon first.");
            return;
        }
        fireDelay -= Game.deltaTime;
        if (fireDelay <= 0) {
            map.fire(pos, target, entityStats, stats);
            fireDelay += 1 / Algorithms.map(entityStats.get(StatType.DEX), 1, 100, 1, 8);
        }
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setTarget(Vector2 targetPosition) {
        target = new Vector2(targetPosition);
    }
}
