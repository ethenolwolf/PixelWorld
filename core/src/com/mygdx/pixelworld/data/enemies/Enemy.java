package com.mygdx.pixelworld.data.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.BoundingCircle;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.draw.ManaPower;

public class Enemy {

    protected Vector2 pos;
    protected boolean alive;
    protected int health;
    protected int armor;
    protected DrawData img;

    public Enemy(float x, float y, Class type) {
        pos = new Vector2(x, y);
        img = new DrawData(AssetType.CHARACTER, type, new Vector2(1,1), 0);
        alive = true;
    }

    public void update(Vector2 pp, Map map) {
        if (!alive) return;
        AI(pp, map);
    }

    protected void AI(Vector2 playerPos, Map map) {
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public boolean isAlive() {
        return alive;
    }

    public void getHit(Bullet b) {
        if (b.getDamage() > armor) health -= (b.getDamage() - armor);
        if (health <= 0) alive = false;
    }

    public boolean checkIfInside(Bullet b) {
        return img.getBoundingCircle(pos).intersect(b.getBoundingCircle());
    }

    public BoundingCircle getBoundingCircle() {
        return img.getBoundingCircle(pos);
    }

    public void getHit(ManaPower mp) {
        if (mp.getDamage() > armor) health -= (mp.getDamage() - armor);
        if (health <= 0) alive = false;
    }
}
