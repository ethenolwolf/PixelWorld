package com.mygdx.pixelworld.data;

import com.badlogic.gdx.math.Vector2;

public class MapBound {
    public static Vector2 bound(Vector2 position, float objectWidth, float objectHeight) {
        Vector2 out = new Vector2(position);
        if (out.x < 0) out.x = 0;
        if (out.y < 0) out.y = 0;
        if (out.x + objectWidth > Assets.BACKGROUND.getWidth()) out.x = Assets.BACKGROUND.getWidth() - objectWidth;
        if (out.y + objectHeight > Assets.BACKGROUND.getHeight()) out.y = Assets.BACKGROUND.getHeight() - objectHeight;
        return out;
    }
}
