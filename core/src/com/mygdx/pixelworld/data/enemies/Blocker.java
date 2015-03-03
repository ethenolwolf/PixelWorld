package com.mygdx.pixelworld.data.enemies;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.Algorithms;
import com.mygdx.pixelworld.data.utilities.FireManager;
import com.mygdx.pixelworld.data.utilities.StatType;

import java.util.Random;

public class Blocker extends Enemy {

    FireManager fireManager;

    public Blocker(float x, float y) {
        super(x, y, Blocker.class);
        fireManager = new FireManager();
    }

    @Override
    protected void activeAIUpdate(Vector2 playerPos, Map map) {
        if (!fireManager.isFiring()) fireManager.setIsFiring(true);
        Algorithms.moveTowards(pos, playerPos, stats.get(StatType.SPD) * Game.deltaTime);
        pos = img.boundMap(pos);
        fireManager.setTarget(playerPos);
        fireManager.updateFire(pos, stats, map);
    }

    @Override
    protected void passiveAIUpdate(Vector2 playerPos, Map map) {
        if (fireManager.isFiring()) fireManager.setIsFiring(false);
        Random rand = new Random();
        float x = rand.nextFloat();
        float y = rand.nextFloat();

        if (rand.nextInt(10) >= 5) x = -x;
        if (rand.nextInt(10) >= 5) y = -y;
        pos.add(x * 5, y * 5);
        pos = img.boundMap(pos);
        fireManager.setTarget(playerPos);
        fireManager.updateFire(pos, stats, map);
    }

}
