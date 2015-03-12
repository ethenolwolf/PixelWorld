package com.mygdx.pixelworld.data.items;

import com.mygdx.pixelworld.data.draw.DrawData;

public abstract class Item {
    protected DrawData img;
    public DrawData getImg() {
        return img;
    }
}
