package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.Constants;

import java.util.List;

public class GUI {

    private static SpriteBatch batch;
    private static Player player;

    public static void init(SpriteBatch batch, Player player) {
        GUI.batch = batch;
        GUI.player = player;
    }

    public static void draw() {
        batch.draw(Assets.getTexture(AssetType.PANEL, GUI.class), Constants.gameWidth, 0);
        drawEquipped();
        drawInventory();
    }

    private static void drawInventory() {
        List<Item> inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            inv.get(i).getImg().drawEffective(batch, new Vector2(Constants.gameWidth + 20 + i * 30, 100));
        }
    }

    private static void drawEquipped() {
        player.getWeapon().getImg().drawEffective(batch, new Vector2(Constants.gameWidth + 20, 150));
        player.getArmor().getImg().drawEffective(batch, new Vector2(Constants.gameWidth + 60, 150));
    }
}
