package com.mygdx.pixelworld.data.items;

import com.mygdx.pixelworld.data.draw.StaticDrawData;

/**
 * Empty item, used to fill inventory.
 */
public class EmptyItem extends Item {
    public EmptyItem() {
        img = new StaticDrawData();
    }
}
