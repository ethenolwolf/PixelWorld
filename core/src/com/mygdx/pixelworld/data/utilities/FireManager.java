package com.mygdx.pixelworld.data.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;

/**
 * Class used to handle weapon fire rate and keep it stable.
 */
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

    /**
     * Updates timers and fire if necessary.
     *
     * @param pos         Position of entity
     * @param entityStats Stats of the shooting entity
     * @param stats       WeaponStats of the weapon used to shoot
     */
    public void updateFire(Vector2 pos, EntityStats entityStats, WeaponStats stats) {
        if (!isFiring) return;
        fireDelay -= Gdx.graphics.getDeltaTime();
        if (fireDelay <= 0) {
            World.fire(pos, new Vector2(target), entityStats, stats);
            fireDelay += 1 / Utils.map(entityStats.get(StatType.DEX), 1, 100, 1, 8);
        }
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setTarget(Vector2 targetPosition) {
        target = new Vector2(targetPosition);
    }
}
