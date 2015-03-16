package com.mygdx.pixelworld.data.entities.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.Map;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.utilities.EntityStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Enemy extends Entity {

    protected int ATTACK_RANGE;
    protected int EXPERIENCE;
    protected List<Item> dropItems = new ArrayList<Item>();

    public Enemy(float x, float y, Class type) {
        pos = new Vector2(x, y);
        img = new DrawData(AssetType.ENEMY, type, new Vector2(1, 1), 0);
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

    public void update(Player player, Map map) {
        if (!stats.isAlive()) return;
        if (player.getPos().dst(pos) < ATTACK_RANGE) activeAIUpdate(player, map);
        else passiveAIUpdate(player, map);
    }

    void activeAIUpdate(Player player, Map map) {
    }

    void passiveAIUpdate(Player player, Map map) {
    }

    @Override
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
}
