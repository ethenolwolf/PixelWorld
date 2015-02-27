package com.mygdx.pixelworld.data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

        float bw = Assets.BULLETS_IMG.get(type).getWidth();
        float bh = Assets.BULLETS_IMG.get(type).getHeight();
        float ex = pos.x + offset.x;
        float ey = pos.y + offset.y;
        batch.draw(new TextureRegion(Assets.BULLETS_IMG.get(type)), ex, ey, bw / 2, bh / 2, bw, bh, 2, 2, direction.angle() - 90);
        //batch.draw(Assets.BULLETS_IMG.get(type), ex, ey, bw, bh);

    }

    public void die() {
        //System.out.println("Dying");
        alive = false;
    }

    public int getDamage() {
        return Constants.BULLET_DAMAGE.get(type);
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getWidth() {
        return Assets.BULLETS_IMG.get(type).getWidth();
    }

    public int getHeight() {
        return Assets.BULLETS_IMG.get(type).getHeight();
    }

    public Class getType() {
        return type;
    }
}
