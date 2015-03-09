package com.mygdx.pixelworld.data.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Weapon {
    private final WeaponStats weaponStats;
    private final DrawData img;

    public Weapon(Class playerClass, int rank) {
        weaponStats = XMLLoader.retrieveWeapon(playerClass, rank);
        if (!weaponStats.getType().isInstance(Enemy.class))
            img = new DrawData(AssetType.WEAPON, weaponStats.getName());
        else img = null;
    }

    public WeaponStats getStats() {
        return weaponStats;
    }

    public void draw(SpriteBatch batch) {
        if (img == null) return;
        img.drawEffective(batch, new Vector2(Constants.gameWidth + 20, 300));
    }
}
