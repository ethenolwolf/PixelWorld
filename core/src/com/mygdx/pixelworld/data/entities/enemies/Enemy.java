package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.items.weapons.WeaponType;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class used to generify enemies of the game.
 */
public abstract class Enemy extends Entity {

    private final List<Item> dropItems = new ArrayList<>();
    States currentState = States.IDLE;
    int ATTACK_RANGE;
    int EXPERIENCE;

    Enemy(float x, float y) {
        pos = new Vector2(x, y);
        stats = new EntityStats(this.getClass());
        String[] actions = new String[States.values().length];
        for (int i = 0; i < actions.length; i++) actions[i] = States.values()[i].name().toLowerCase();
        img = new AnimationDrawData("core/assets/enemies/" + this.getClass().getSimpleName().toLowerCase() + "/", actions, BoundingRect.class);
        calculateDropItems();
    }

    /**
     * Calculates items that this enemy will drop dying.
     */
    private void calculateDropItems() {
        Random rand = new Random();
        int k = rand.nextInt(10);
        if (k > 3) dropItems.add(new Weapon(WeaponType.values()[rand.nextInt(WeaponType.values().length)], 1));
        if (k > 5) dropItems.add(new Weapon(WeaponType.values()[rand.nextInt(WeaponType.values().length)], 2));
        if (k > 8) dropItems.add(new Weapon(WeaponType.values()[rand.nextInt(WeaponType.values().length)], 2));

    }

    /**
     * Updates AI
     *
     * @param player Player
     * @param world  World
     */
    public void update(Player player, World world) {
        if (!stats.isAlive()) return;
        if (player.getPos().dst(pos) < ATTACK_RANGE) activeAIUpdate(player, world);
        else passiveAIUpdate(player, world);
        if (!(img instanceof AnimationDrawData)) return;
        ((AnimationDrawData) img).setCurrentAction(currentState.name().toLowerCase());
    }

    /**
     * AI when near player
     */
    abstract void activeAIUpdate(Player player, World world);

    /**
     * AI when far from player
     */
    abstract void passiveAIUpdate(Player player, World world);

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }
    public boolean isAlive() {
        return stats.isAlive();
    }
    public BoundingShape getBoundingShape() {
        return img.getBoundingShape(pos);
    }
    public int getExperience() {
        return EXPERIENCE;
    }
    public List<Item> getDropItems() {
        return dropItems;
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Item i : dropItems) i.dispose();
    }

    protected enum States {
        IDLE, WALK
    }
}
