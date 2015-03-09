package com.mygdx.pixelworld.data.armors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.XMLLoader;

public class Armor {
    ArmorStats armorStats;
    DrawData img;

    public Armor(Class playerClass, int rank) {
        armorStats = XMLLoader.retrieveArmor(playerClass, rank);
        img = new DrawData(AssetType.ARMOR, armorStats.getName());
    }

    public void draw(SpriteBatch batch) {
        img.drawEffective(batch, new Vector2(Constants.gameWidth + 50, 300));
    }

    public int getDefense() {
        return armorStats.getDefense();
    }
}
