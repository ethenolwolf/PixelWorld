package com.mygdx.pixelworld.data.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.utilities.AssetType;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.List;

public class Chest extends Item {
    private final Vector2 pos;
    private final Inventory inventory = new Inventory(8);

    public Chest(List<Item> items, Vector2 pos) {
        inventory.fill(items);
        this.pos = pos;
        img = new StaticDrawData(AssetType.CHEST);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public boolean checkIfInside(Player player) {
        return BoundingShape.intersect(img.getBoundingShape(pos), player.getBoundingShape());
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public BoundingShape getBoundingShape() {
        return img.getBoundingShape(pos);
    }
}
