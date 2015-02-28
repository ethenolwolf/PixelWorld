package com.mygdx.pixelworld.data.Enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.AssetsManagement.AssetType;
import com.mygdx.pixelworld.data.Bullet;
import com.mygdx.pixelworld.data.DrawData;

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

    public Vector2 getPos() {
        return pos;
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
        return img.getBoundingBox(pos).contains(b.getBoundingBox());
    }
}
