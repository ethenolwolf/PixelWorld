package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;
import com.mygdx.pixelworld.data.utilities.*;

import java.util.Random;

/**
 * Blocker is an enemy capable of shooting and hit the player.
 */
public class Blocker extends Enemy {

    private final FireManager fireManager;
    private final WeaponStats weaponStats;
    private Direction currentDirection = Direction.LEFT;
    private float AITimer = 0;

    public Blocker(float x, float y) {
        super(x, y);
        img.setScaleFactor(0.75f);
        fireManager = new FireManager();
        weaponStats = Config.getWeapon(Blocker.class);
        new StaticDrawData(AssetType.BULLET, weaponStats.getName());
        ATTACK_RANGE = 400;
        EXPERIENCE = 3;
    }

    /**
     * The blocker will follow the player and shoot at him
     */
    @Override
    protected void activeAIUpdate() {
        if (!World.getPlayer().getStats().isVisible()) {
            passiveAIUpdate();
            return;
        }
        currentState = States.WALK;
        img.update();
        if (!fireManager.isFiring()) fireManager.setIsFiring(true);
        Vector2 diff = new Vector2(World.getPlayer().getPos().x - pos.x, World.getPlayer().getPos().y - pos.y);
        bound(World.getMapObstacles(), diff.nor().scl(stats.get(StatType.SPD) * Gdx.graphics.getDeltaTime()));
        pos = img.boundMap(pos);
        fireManager.setTarget(World.getPlayer().getPos());
        fireManager.updateFire(new Vector2(pos).add(img.getWidth() / 2, img.getHeight() / 2), stats, weaponStats);
    }

    /**
     * The blocker will randomly move.
     */
    @Override
    void passiveAIUpdate() {
        currentState = States.IDLE;
        if (fireManager.isFiring()) fireManager.setIsFiring(false);

        AITimer -= Gdx.graphics.getDeltaTime();
        if (AITimer <= 0) {
            Random rand = new Random();
            currentDirection = Direction.values()[rand.nextInt(3)];
            AITimer = 2.0f;
        }

        float speed = stats.get(StatType.SPD) * Gdx.graphics.getDeltaTime();

        switch (currentDirection) {
            case UP:
                bound(World.getMapObstacles(), new Vector2(0, speed));
                break;
            case DOWN:
                bound(World.getMapObstacles(), new Vector2(0, -speed));
                break;
            case LEFT:
                bound(World.getMapObstacles(), new Vector2(-speed, 0));
                break;
            case RIGHT:
                bound(World.getMapObstacles(), new Vector2(speed, 0));
                break;
        }
        pos = img.boundMap(pos);
        fireManager.setTarget(World.getPlayer().getPos());
        fireManager.updateFire(pos, stats, weaponStats);
        img.update();
    }
}
