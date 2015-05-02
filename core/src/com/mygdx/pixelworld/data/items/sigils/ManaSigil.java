package com.mygdx.pixelworld.data.items.sigils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.EquipItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.Damaging;

/**
 * Generic ManaSigil that uses mana to give the player a special ability.
 */
public class ManaSigil extends Item implements Damaging, EquipItem {

    boolean empty;
    int damage;
    float price;
    SigilName name;

    public ManaSigil() {
        img = new StaticDrawData();
        empty = true;
    }

    public static ManaSigil getInitial(Player player) {
        return new HealingCross(player);
    }

    public static ManaSigil getFromName(SigilName name, Player player) {
        switch (name) {
            case powerShock:
                return new PowerShock(player);
            case healingCross:
                return new HealingCross(player);
            case invisibleCloack:
                return new InvisibleCloak(player);
            default:
                return null;
        }
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

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "SIGIL:" + name.name();
    }
}
