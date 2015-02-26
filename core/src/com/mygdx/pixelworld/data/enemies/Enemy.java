package com.mygdx.pixelworld.data.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.Algorithms;
import com.mygdx.pixelworld.data.Assets;
import com.mygdx.pixelworld.data.Bullet;

public class Enemy {

    protected Vector2 pos;
    protected boolean alive;
    protected int health;
    protected int armor;

    public Enemy(float x, float y) {
        pos = new Vector2(x, y);
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

    public void draw(SpriteBatch batch, Vector2 offset) {
        batch.draw(Assets.ENEMY_IMG.get(this.getClass()), pos.x + offset.x, pos.y + offset.y);
    }

    public boolean isAlive() {
        return alive;
    }

    public void getHit(Bullet b) {
        if (b.getDamage() > armor) health -= (b.getDamage() - armor);
        System.out.println("Ouch! Health = " + health);
        if (health <= 0) alive = false;
    }

    public boolean checkIfInside(Bullet b) {
        int ew = Assets.ENEMY_IMG.get(this.getClass()).getWidth();
        int eh = Assets.ENEMY_IMG.get(this.getClass()).getHeight();
        return Algorithms.contains(pos, ew, eh, b.getPos(), b.getWidth(), b.getHeight());
    }
}
