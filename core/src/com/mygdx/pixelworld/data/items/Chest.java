package com.mygdx.pixelworld.data.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.classes.Player;
import com.mygdx.pixelworld.data.draw.DrawData;

import java.util.List;

public class Chest extends Item {
    private final Vector2 pos;
    private Item[] inv = new Item[8];

    public Chest(List<Item> dropItems, Vector2 pos) {
        for (int i = 0; i < inv.length; i++) {
            if (i < dropItems.size()) inv[i] = dropItems.get(i);
            else inv[i] = new EmptyItem();
        }
        Logger.log("Chest created with " + dropItems.size() + " items.");
        this.pos = pos;
        img = new DrawData(AssetType.CHEST, null, new Vector2(1, 1), 0);
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public boolean isEmpty() {
        for (Item i : inv) {
            if (!(i instanceof EmptyItem)) return false;
        }
        return true;
    }

    public Item[] getInventory() {
        return inv;
    }

    public Item remove(int chestPosition) {
        if (chestPosition >= 8) return null;
        Item out = inv[chestPosition];
        inv[chestPosition] = new EmptyItem();
        return out;
    }

    public Item get(int chestPosition) {
        if (chestPosition >= 8) return null;
        return inv[chestPosition];
    }

    public boolean tryToPlace(Item item, int slot) {
        if (slot >= inv.length) return false;
        if (!(inv[slot] instanceof EmptyItem)) return false;
        inv[slot] = item;
        return true;
    }

    public void swapInventory(int startSlot, int endSlot) {
        if (startSlot >= inv.length || endSlot >= inv.length || startSlot == endSlot) return;
        if (tryToPlace(inv[startSlot], endSlot)) remove(startSlot);
    }

    public boolean checkIfInside(Player player) {
        return img.getBoundingCircle(pos).intersect(player.getImg().getBoundingCircle(player.getPos()));
    }
}
