package com.mygdx.pixelworld.data.enemies;

import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.utilities.Algorithms;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.FireManager;
import com.mygdx.pixelworld.data.utilities.StatType;

import java.util.Random;

public class Blocker extends Enemy {

    private final FireManager fireManager;

    public Blocker(float x, float y) {
        super(x, y, Blocker.class);
        fireManager = new FireManager();
        ATTACK_RANGE = 400;
        EXPERIENCE = 3;
    }

    @Override
    protected void activeAIUpdate(Player player, Map map) {
        if (!player.getStats().isVisible()) {
            passiveAIUpdate(player, map);
            return;
        }
        if (!fireManager.isFiring()) fireManager.setIsFiring(true);
        Algorithms.moveTowards(pos, player.getPos(), stats.get(StatType.SPD) * Game.deltaTime);
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(pos, stats, map, Constants.enemyStats.get(Blocker.class));
    }

    @Override
    void passiveAIUpdate(Player player, Map map) {
        if (fireManager.isFiring()) fireManager.setIsFiring(false);
        Random rand = new Random();
        float x = rand.nextFloat();
        float y = rand.nextFloat();

        if (rand.nextInt(10) >= 5) x = -x;
        if (rand.nextInt(10) >= 5) y = -y;
        pos.add(x * 5, y * 5);
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(pos, stats, map, Constants.enemyStats.get(Blocker.class));
    }

}
