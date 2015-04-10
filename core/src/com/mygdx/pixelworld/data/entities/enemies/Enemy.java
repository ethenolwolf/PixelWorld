package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.debug.Debuggable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Enemy extends Entity implements Debuggable, Disposable {

    private final List<Item> dropItems = new ArrayList<Item>();
    int ATTACK_RANGE;
    int EXPERIENCE;

    Enemy(float x, float y) {
        pos = new Vector2(x, y);
        stats = new EntityStats(this.getClass());
        calculateDropItems();
    }

    private void calculateDropItems() {
        //TODO Flavio
        Random rand = new Random();
        int k = rand.nextInt(10);
        if (k > 3) dropItems.add(new Weapon(GameClasses.NINJA, 1));
        if (k > 5) dropItems.add(new Weapon(GameClasses.WIZARD, 1));
        if (k > 8) dropItems.add(new Weapon(GameClasses.WIZARD, 2));

    }

    public void update(Player player, World world) {
        if (!stats.isAlive()) return;
        if (player.getPos().dst(pos) < ATTACK_RANGE) activeAIUpdate(player, world);
        else passiveAIUpdate(player, world);
    }

    void activeAIUpdate(Player player, World world) {
    }

    void passiveAIUpdate(Player player, World world) {
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public boolean isAlive() {
        return stats.isAlive();
    }

    public boolean checkIfInside(Bullet b) {
        return img.getBoundingCircle(pos).intersect(b.getBoundingCircle());
    }

    public BoundingCircle getBoundingCircle() {
        return img.getBoundingCircle(pos);
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
}
