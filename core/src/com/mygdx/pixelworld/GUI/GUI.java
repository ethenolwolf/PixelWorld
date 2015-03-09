package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.utilities.Constants;

public class GUI {
    public static void draw(SpriteBatch batch, Player player) {
        batch.draw(Assets.getTexture(AssetType.PANEL, GUI.class), Constants.gameWidth, 0);
        player.getWeapon().draw(batch);
        player.getArmor().draw(batch);
    }
}
