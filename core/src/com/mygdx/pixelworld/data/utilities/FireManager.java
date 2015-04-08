package com.mygdx.pixelworld.data.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
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


    public void updateFire(Vector2 pos, EntityStats entityStats, World world, WeaponStats stats) {
        if (!isFiring) return;
        fireDelay -= Gdx.graphics.getDeltaTime();
        if (fireDelay <= 0) {

            //centering target
            Vector2 dir = new Vector2(pos.x - target.x, pos.y - target.y);
            StaticDrawData dd = new StaticDrawData(stats.getName(), dir.angle());
            Vector2 drawOffset = new Vector2(dd.getWidth() / 2, dd.getHeight() / 2);

            world.fire(pos, new Vector2(target).sub(drawOffset), entityStats, stats);
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
