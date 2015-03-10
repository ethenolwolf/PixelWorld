package com.mygdx.pixelworld.data.items;

import com.mygdx.pixelworld.data.draw.DrawData;

public abstract class Item {
    protected DrawData img;
    protected int slotPosition;

    public int getSlotPosition() {
        return slotPosition;
    }

    public void setSlotPosition(int slotPosition) {
        this.slotPosition = slotPosition;
    }

    public DrawData getImg() {
        return img;
    }
}
