package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.utilities.Constants;

public class Bullet {
    Vector2 pos;
    Vector2 startPoint;
    Vector2 direction;
    Class type;

    boolean alive = true;
    private DrawData img;

    public Bullet(Vector2 startingPos, Vector2 endingPos, Class type) {
        this.startPoint = new Vector2(startingPos);
        this.direction = new Vector2(endingPos.x - startingPos.x, endingPos.y - startingPos.y).nor();
        this.type = type;
        this.pos = new Vector2(startingPos);
        img = new DrawData(AssetType.BULLET, type, new Vector2(1, 1), direction.angle());
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

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public void die() {
        alive = false;
    }

    public int getDamage() {
        return Constants.BULLET_DAMAGE.get(type);
    }

    public Class getType() {
        return type;
    }

    public BoundingCircle getBoundingCircle() {
        return img.getBoundingCircle(pos);
    }
}
