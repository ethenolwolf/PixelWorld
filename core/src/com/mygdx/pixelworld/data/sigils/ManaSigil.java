package com.mygdx.pixelworld.data.sigils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.SigilName;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.Damaging;

public abstract class ManaSigil implements Damaging {

    int damage;
    float price;

    public static ManaSigil getFromName(SigilName name, Player player) {
        switch (name) {
            case powerShock:
                return new PowerShock();
            case invisibleCloack:
                return new InvisibleCloak(player);
            default:
                return new PowerShock();
        }
    }

    public static ManaSigil getInitial(Player player) {
        if (player.getClass().toString().contains("Wizard")) return new PowerShock();
        if (player.getClass().toString().contains("Ninja")) return new InvisibleCloak(player);
        return null;
    }

    public abstract void update();

    public abstract boolean checkIfInside(Enemy e);

    public abstract void activate(Vector2 abs);

    public abstract void draw(SpriteBatch batch);

    @Override
    public int getDamage() {
        return damage;
    }

    public float getPrice() {
        return price;
    }
}
