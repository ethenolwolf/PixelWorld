package com.mygdx.pixelworld.data.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Weapon {
    WeaponStats weaponStats;
    DrawData img;

    public Weapon(Class playerClass, int rank) {
        weaponStats = XMLLoader.retrieveWeapon(playerClass, rank);
        if (!weaponStats.getType().isInstance(Blocker.class))
            img = new DrawData(weaponStats.getName());
        else img = null;
    }

    public WeaponStats getStats() {
        return weaponStats;
    }

    public void draw(SpriteBatch batch) {
        if (img == null) return;
        img.drawEffective(batch, new Vector2(50, 50));
    }
}
