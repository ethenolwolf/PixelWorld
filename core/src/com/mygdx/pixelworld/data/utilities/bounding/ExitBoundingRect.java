package com.mygdx.pixelworld.data.utilities.bounding;

import com.badlogic.gdx.math.Rectangle;

/**
 * Bounding rect indicating exits to other maps.
 */
public class ExitBoundingRect extends BoundingRect {

    private final String nextMap;

    public ExitBoundingRect(Rectangle rectangle, String nextMap) {
        super(rectangle);
        this.nextMap = "core/assets/maps/" + nextMap + ".tmx";
    }

    public String getNextMap() {
        return nextMap;
    }

    @Override
    public String toString() {
        return super.toString() + ", E = " + nextMap;
    }
}
