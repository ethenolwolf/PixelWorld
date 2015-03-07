package com.mygdx.pixelworld.data.mana;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.Damaging;

public abstract class ManaSigil implements Damaging {

    protected int damage;

    public abstract void update();

    public abstract boolean checkIfInside(Enemy e);

    public abstract void activate(Vector2 abs);

    public abstract void draw(SpriteBatch batch);

    @Override
    public int getDamage() {
        return damage;
    }
}
