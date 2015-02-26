package com.mygdx.pixelworld.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;

public class Bullet {
    Vector2 pos;
    Vector2 startPoint;
    Vector2 direction;
    CharacterType type;

    boolean alive = true;

    public Bullet(Vector2 startingPos, Vector2 direction, CharacterType type) {
        this.startPoint = new Vector2(startingPos);
        this.direction = direction.nor();
        this.type = type;
        this.pos = new Vector2(startPoint);
    }

    public void update() {
        move();
        if (pos.dst(startPoint) > Constants.BULLET_RANGE) {
            System.out.println("Dying pos = " + pos.toString() + " st = " + startPoint.toString());
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void move() {
        float movement = Game.deltaTime * Constants.BULLET_SPEED;
        pos.add(direction.x * movement, direction.y * movement);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.BULLETS_IMG.get(type), pos.x, pos.y, 10, 7);
    }
}
