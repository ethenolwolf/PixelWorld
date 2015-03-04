package com.mygdx.pixelworld.data.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.WeaponNames;
import com.mygdx.pixelworld.data.classes.Wizard;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Weapon {
    WeaponStats weaponStats;
    DrawData img;

    public Weapon(WeaponNames name) {
        Weapon w = XMLLoader.retrieveWeapon(name.toString());
        weaponStats = new WeaponStats(w);
        img = new DrawData(name);
    }

    public Weapon() {
        weaponStats = new WeaponStats();
        img = null;
    }

    public Weapon(String type, int damage, int range, int speed) {
        Class classType = null;
        if (type.equals("Wizard")) classType = Wizard.class;

        weaponStats = new WeaponStats(classType, damage, range, speed);
    }

    public WeaponStats getStats() {
        return weaponStats;
    }

    public void draw(SpriteBatch batch) {
        img.drawEffective(batch, new Vector2(50, 50));
    }
}
