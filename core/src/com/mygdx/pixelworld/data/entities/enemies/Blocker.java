package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;
import com.mygdx.pixelworld.data.utilities.*;

import java.util.Random;

public class Blocker extends Enemy {

    private final FireManager fireManager;
    private final WeaponStats weaponStats;
    private Directions currentDirection = Directions.LEFT;
    private float AITimer = 0;

    public Blocker(float x, float y) {
        super(x, y);
        fireManager = new FireManager();
        weaponStats = Config.getWeapon(Blocker.class);
        StaticDrawData tmp = new StaticDrawData(AssetType.BULLET, weaponStats.getName());
        ATTACK_RANGE = 400;
        EXPERIENCE = 3;
    }

    @Override
    protected void activeAIUpdate(Player player, World world) {
        if (!player.getStats().isVisible()) {
            passiveAIUpdate(player, world);
            return;
        }
        currentState = States.WALK;
        img.update();
        if (!fireManager.isFiring()) fireManager.setIsFiring(true);
        Algorithms.moveTowards(pos, player.getPos(), stats.get(StatType.SPD) * Gdx.graphics.getDeltaTime());
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(new Vector2(pos).add(img.getWidth() / 2, img.getHeight() / 2), stats, world, weaponStats);
    }

    @Override
    void passiveAIUpdate(Player player, World world) {
        currentState = States.IDLE;
        if (fireManager.isFiring()) fireManager.setIsFiring(false);

        AITimer -= Gdx.graphics.getDeltaTime();
        if (AITimer <= 0) {
            Random rand = new Random();
            currentDirection = Directions.values()[rand.nextInt(3)];
            AITimer = 2.0f;
        }

        float speed = stats.get(StatType.SPD) * Gdx.graphics.getDeltaTime();

        switch (currentDirection) {
            case UP:
                pos.add(0, speed);
                break;
            case DOWN:
                pos.add(0, -speed);
                break;
            case LEFT:
                pos.add(-speed, 0);
                break;
            case RIGHT:
                pos.add(speed, 0);
                break;
        }
        pos = img.boundMap(pos);

        fireManager.setTarget(player.getPos());
        fireManager.updateFire(pos, stats, world, weaponStats);
        img.update();
    }

    @Override
    public String getWatch() {
        return "";//String.format("Blocker@%x -> X = %.2f\t Y = %.2f", this.hashCode(), pos.x, pos.y);
    }


}
