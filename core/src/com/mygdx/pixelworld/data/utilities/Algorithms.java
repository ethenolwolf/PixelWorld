package com.mygdx.pixelworld.data.utilities;

import com.badlogic.gdx.math.Vector2;

public class Algorithms {
    public static float map(float inValue, float inMin, float inMax, float outMin, float outMax) {
        return (inValue - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static void moveTowards(Vector2 pos, Vector2 targetPos, float speed) {
        Vector2 diff = new Vector2(targetPos.x - pos.x, targetPos.y - pos.y);
        pos.add(diff.nor().scl(speed));
    }

}
