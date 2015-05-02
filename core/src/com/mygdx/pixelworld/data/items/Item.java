package com.mygdx.pixelworld.data.items;

import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.draw.DrawData;

/**
 * Generic item.
 */
public abstract class Item implements Disposable {
    protected DrawData img;
    public DrawData getImg() {
        return img;
    }

    @Override
    public void dispose() {
        img.dispose();
    }

    @Override
    public abstract String toString();
}
