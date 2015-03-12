package com.mygdx.pixelworld.data.sigils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.SigilName;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.Damaging;

public class ManaSigil extends Item implements Damaging {

    protected boolean empty;
    int damage;
    float price;
    SigilName name;

    public ManaSigil() {
        img = new DrawData();
        empty = true;
    }

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

    public void update() {
    }

    public boolean checkIfInside(Enemy e) {
        return false;
    }

    public void activate(Vector2 abs) {
    }

    public void draw(SpriteBatch batch) {
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public float getPrice() {
        return price;
    }

    public SigilName getName() {
        return name;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
