package com.mygdx.pixelworld.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;

public class Bullet {
    Vector2 pos;
    Vector2 startPoint;
    Vector2 direction;
    Class type;

    boolean alive = true;

    public Bullet(Vector2 startingPos, Vector2 endingPos, Class type) {
        this.startPoint = new Vector2(startingPos);
        this.direction = new Vector2(endingPos.x - startingPos.x, endingPos.y - startingPos.y).nor();
        this.type = type;
        this.pos = new Vector2(startingPos);
    }

    public void update() {
        move();
        if (pos.dst(startPoint) > Constants.BULLET_RANGE.get(type)) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void move() {
        float movement = Game.deltaTime * Constants.BULLET_SPEED.get(type);
        pos.add(direction.x * movement, direction.y * movement);
    }

    public void draw(SpriteBatch batch, Vector2 offset) {
        batch.draw(Assets.BULLETS_IMG.get(type), pos.x + offset.x, pos.y + offset.y, 10, 7);
    }

    public void die() {
        alive = false;
    }

    public int getDamage() {
        return Constants.BULLET_DAMAGE.get(type);
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getWidth() {
        return 10;
    }

    public int getHeight() {
        return 7;
    }

    public Class getType() {
        return type;
    }
}
