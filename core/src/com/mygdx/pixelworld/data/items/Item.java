package com.mygdx.pixelworld.data.items;

import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.draw.DrawData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generic item.
 */
public abstract class Item implements Disposable {
    protected static final Logger logger = LogManager.getLogger();
    protected DrawData img;

    @Override
    public void dispose() {
        img.dispose();
    }

    @Override
    public abstract String toString();
}
