package com.mygdx.pixelworld.data.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.DrawData;

import java.util.ArrayList;
import java.util.List;

public class Chest extends Item {
    private final Vector2 pos;
    private List<Item> inv = new ArrayList<Item>();

    public Chest(List<Item> inv, Vector2 pos) {
        this.inv = inv;
        this.pos = pos;
        img = new DrawData(AssetType.CHEST, null, new Vector2(1, 1), 0);
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }
}
