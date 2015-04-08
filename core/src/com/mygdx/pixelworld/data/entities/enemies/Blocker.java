package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.utilities.Algorithms;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.FireManager;
import com.mygdx.pixelworld.data.utilities.StatType;

import java.util.Random;

public class Blocker extends Enemy {

    private final FireManager fireManager;

    public Blocker(float x, float y) {
        super(x, y);
        img = new AnimationDrawData("core/assets/Enemies/", States.class, 10, 6);
        fireManager = new FireManager();
        ATTACK_RANGE = 400;
        EXPERIENCE = 3;
    }

    @Override
    protected void activeAIUpdate(Player player, World world) {
        if (!player.getStats().isVisible()) {
            passiveAIUpdate(player, world);
            return;
        }
        img.update();
        if (!fireManager.isFiring()) fireManager.setIsFiring(true);
        Algorithms.moveTowards(pos, player.getPos(), stats.get(StatType.SPD) * Gdx.graphics.getDeltaTime());
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(new Vector2(pos).add(img.getWidth() / 2, img.getHeight() / 2), stats, world, Constants.enemyStats.get(Blocker.class));
    }

    @Override
    void passiveAIUpdate(Player player, World world) {
        if (fireManager.isFiring()) fireManager.setIsFiring(false);
        Random rand = new Random();
        float x = rand.nextFloat();
        float y = rand.nextFloat();

        if (rand.nextInt(10) >= 5) x = -x;
        if (rand.nextInt(10) >= 5) y = -y;
        pos.add(x * 5, y * 5);
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(pos, stats, world, Constants.enemyStats.get(Blocker.class));
        img.update();
    }

    @Override
    public String getWatch() {
        return "";//String.format("Blocker@%x -> X = %.2f\t Y = %.2f", this.hashCode(), pos.x, pos.y);
    }

    private enum States {
        IDLE
    }

}
