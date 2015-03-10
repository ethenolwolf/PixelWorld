package com.mygdx.pixelworld.data.draw;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.Damaging;
import com.mygdx.pixelworld.data.utilities.EntityStats;
import com.mygdx.pixelworld.data.utilities.StatType;
import com.mygdx.pixelworld.data.weapons.WeaponStats;

public class Bullet implements Damaging {
    private final Vector2 startPoint;
    private final Vector2 direction;
    private final int range;
    private final boolean isPlayer;
    private final int speed;
    private final float rotationSpeed;
    private final int damage;
    private final Vector2 pos;
    private final DrawData img;
    private boolean alive = true;

    public Bullet(Vector2 startingPos, Vector2 endingPos, EntityStats es, WeaponStats ws) {
        //TODO autoSpin if enabled
        this.startPoint = new Vector2(startingPos);
        this.direction = new Vector2(endingPos.x - startingPos.x, endingPos.y - startingPos.y).nor();
        System.out.println(ws.getType().toString());
        isPlayer = ws.getType().toString().contains("classes"); //If it's a player it's in package classes
        this.pos = new Vector2(startingPos);
        img = new DrawData(ws.getName(), direction.angle());
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
        float movement = Game.deltaTime * speed;
        pos.add(direction.x * movement, direction.y * movement);
        img.addRotationAngle(rotationSpeed * Game.deltaTime);
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
}
