package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
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
     *
     * @param player Player to follow
     * @param world  World
     */
    @Override
    protected void activeAIUpdate(Player player, World world) {
        if (!player.getStats().isVisible()) {
            passiveAIUpdate(player, world);
            return;
        }
        currentState = States.WALK;
        img.update();
        if (!fireManager.isFiring()) fireManager.setIsFiring(true);
        Vector2 diff = new Vector2(player.getPos().x - pos.x, player.getPos().y - pos.y);
        bound(world.getMapObstacles(), diff.nor().scl(stats.get(StatType.SPD) * Gdx.graphics.getDeltaTime()));
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(new Vector2(pos).add(img.getWidth() / 2, img.getHeight() / 2), stats, world, weaponStats);
    }

    /**
     * The blocker will randomly move.
     * @param player Player
     * @param world World
     */
    @Override
    void passiveAIUpdate(Player player, World world) {
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
                bound(world.getMapObstacles(), new Vector2(0, speed));
                break;
            case DOWN:
                bound(world.getMapObstacles(), new Vector2(0, -speed));
                break;
            case LEFT:
                bound(world.getMapObstacles(), new Vector2(-speed, 0));
                break;
            case RIGHT:
                bound(world.getMapObstacles(), new Vector2(speed, 0));
                break;
        }
        pos = img.boundMap(pos);
        fireManager.setTarget(player.getPos());
        fireManager.updateFire(pos, stats, world, weaponStats);
        img.update();
    }
}
