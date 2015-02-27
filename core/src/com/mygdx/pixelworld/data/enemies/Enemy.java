package com.mygdx.pixelworld.data.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.Algorithms;
import com.mygdx.pixelworld.data.Assets;
import com.mygdx.pixelworld.data.Bullet;
import com.mygdx.pixelworld.data.Constants;

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

        float ew = Gdx.graphics.getWidth() * Constants.CHARACTER_WIDTH;
        float eh = Gdx.graphics.getHeight() * Constants.CHARACTER_HEIGHT;
        float ex = pos.x + offset.x;
        float ey = pos.y + offset.y;

        batch.draw(new TextureRegion(Assets.ENEMY_IMG.get(this.getClass())), ex, ey, (ex + ew) / 2, (ey + eh) / 2, ew, eh, 1, 1, 0);
        // batch.draw(Assets.ENEMY_IMG.get(this.getClass()), pos.x + offset.x, pos.y + offset.y);
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
        float ew = Gdx.graphics.getWidth() * Constants.CHARACTER_WIDTH;
        float eh = Gdx.graphics.getHeight() * Constants.CHARACTER_HEIGHT;
        return Algorithms.contains(pos, ew, eh, b.getPos(), b.getWidth(), b.getHeight());
    }
}
