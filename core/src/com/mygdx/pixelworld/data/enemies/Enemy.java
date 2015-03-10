package com.mygdx.pixelworld.data.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.Entity;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.utilities.EntityStats;

public abstract class Enemy extends Entity {

    protected int ATTACK_RANGE;
    protected int EXPERIENCE;

    public Enemy(float x, float y, Class type) {
        pos = new Vector2(x, y);
        img = new DrawData(AssetType.CHARACTER, type, new Vector2(1, 1), 0);
        stats = new EntityStats(this.getClass());
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
}
