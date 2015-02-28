package com.mygdx.pixelworld.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.AssetsManagement.AssetType;

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
        img = new DrawData(AssetType.BULLET, type, new Vector2(1, 1), direction.angle() - 90);
        System.out.println("[" + type.toString() + "] Firing from " + startingPos.toString() + " to " + endingPos.toString());
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
        System.out.println("Dying");
        alive = false;
    }

    public int getDamage() {
        return Constants.BULLET_DAMAGE.get(type);
    }

    public Vector2 getPos() {
        return pos;
    }

    public float getWidth() {
        return img.getWidth();
    }

    public float getHeight() {
        return img.getHeight();
    }

    public Class getType() {
        return type;
    }

    public BoundingBox getBoundingBox() {
        return img.getBoundingBox(pos);
    }
}
