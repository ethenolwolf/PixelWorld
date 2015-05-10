package com.mygdx.pixelworld.data.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.data.draw.StaticDrawData;
import com.mygdx.pixelworld.data.items.EmptyItem;
import com.mygdx.pixelworld.data.items.Inventory;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.List;

/**
 * Chest for containing dropped items.
 */
public class Chest extends Item {
    private final Vector2 pos;
    private final Inventory inventory = new Inventory(8);

    /**
     * @param items Items to place in the chest
     * @param pos   Position of the chest in world
     */
    public Chest(List<Item> items, Vector2 pos) {
        inventory.fill(items);
        this.pos = pos;
        img = new StaticDrawData("core/assets/background/chest/chest.png", BoundingRect.class);
    }

    /**
     * Empty the items of another chest and place them inside this chest.
     * @param c External chest
     * @return False if items can't sit in the same chest, True otherwise
     */
    public boolean move(Chest c) {
        //Take all items in c and place them here: if it can't be done returns false
        if (c.getInventory().size() + inventory.size() > 8) return false;
        List<Item> oldItems = c.getInventory().getFilledItems();
        for (int i = 0; i < inventory.getItems().length; i++) {
            if (oldItems.size() == 0) return true;
            if (inventory.get(i) instanceof EmptyItem) {
                inventory.set(oldItems.get(0), i);
                oldItems.remove(0);
            }
        }
        return true;
    }

    private Inventory getInventory() {
        return inventory;
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public BoundingShape getBoundingShape() {
        return img.getBoundingShape(pos);
    }

    @Override
    public String toString() {
        return "";
    }
}
