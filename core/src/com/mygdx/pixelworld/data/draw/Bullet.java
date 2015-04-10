package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.items.weapons.PlayerWeaponStats;
import com.mygdx.pixelworld.data.items.weapons.WeaponStats;
import com.mygdx.pixelworld.data.utilities.Damaging;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.StatType;

public class Bullet implements Damaging, Disposable {

    private final boolean isPlayer;
    private final Vector2 startPoint;
    private final Vector2 direction;
    private final Vector2 pos;
    private final StaticDrawData img;
    private final int damage;
    private final int range;
    private final int speed;
    private final float rotationSpeed;
    private boolean alive = true;

    public Bullet(Vector2 startingPos, Vector2 endingPos, EntityStats es, WeaponStats ws) {
        this.startPoint = new Vector2(startingPos);
        this.direction = new Vector2(endingPos.x - startingPos.x, endingPos.y - startingPos.y).nor();
        isPlayer = ws instanceof PlayerWeaponStats;
        this.pos = new Vector2(startingPos);
        img = new StaticDrawData(ws.getName(), direction.angle());
        this.damage = (int) (ws.getDamage() + es.get(StatType.ATK));
        this.range = ws.getRange();
        this.speed = ws.getSpeed();
        this.rotationSpeed = ws.getRotationSpeed();
    }

    public void update() {
        move();
        if (pos.dst(startPoint) > range) {
            alive = false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void move() {
        float movement = Gdx.graphics.getDeltaTime() * speed;
        pos.add(direction.x * movement, direction.y * movement);
        img.addRotationAngle(rotationSpeed * Gdx.graphics.getDeltaTime());
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public void die() {
        alive = false;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public BoundingCircle getBoundingCircle() {
        return img.getBoundingCircle(pos);
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
