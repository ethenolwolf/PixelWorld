package com.mygdx.pixelworld.data.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.Assets;

public class Enemy {

    protected Vector2 pos;

    public Enemy(float x, float y){
        pos = new Vector2(x,y);
    }

    public Vector2 getPos() {
        return pos;
    }

    public void update(Vector2 pp) {
        AI(pp);
    }

    protected void AI(Vector2 playerPos){
    }

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.ENEMY_IMG.get(this.getClass()), pos.x, pos.y);
    }
}
