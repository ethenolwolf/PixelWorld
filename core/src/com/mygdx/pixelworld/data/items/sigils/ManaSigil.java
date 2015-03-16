package com.mygdx.pixelworld.data.items.sigils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.Damaging;

public class ManaSigil extends Item implements Damaging, EquipItem {

    protected boolean empty;
    protected GameClasses gameClass;
    int damage;
    float price;
    SigilName name;

    public ManaSigil() {
        img = new DrawData();
        empty = true;
    }

    public static ManaSigil getInitial(Player player) {
        if (player.getGameClass() == GameClasses.WIZARD) return new PowerShock(player);
        if (player.getGameClass() == GameClasses.NINJA) return new InvisibleCloak(player);
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

    @Override
    public GameClasses getGameClass() {
        return gameClass;
    }
}
