package com.mygdx.pixelworld.data.utilities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;

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


    public void updateFire(Vector2 pos, PlayerStats playerStats, Map map) {
        if (!isFiring) return;
        fireDelay -= Game.deltaTime;
        if (fireDelay <= 0) {
            map.fire(pos, target, playerStats.getGameClass());
            fireDelay += 1 / Algorithms.map(playerStats.get(StatType.DEX), 1, 100, 1, 8);
        }
    }
}
