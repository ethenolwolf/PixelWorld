package com.mygdx.pixelworld.data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Algorithms {
    public static float scale(float valueIn, float baseMin, float baseMax, float limitMin, float limitMax) {
        return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    public static boolean contains(Vector2 pos1, float w1, float h1, Vector2 pos2, float w2, float h2) {
        BoundingBox bb1 = new BoundingBox(new Vector3(pos1.x, pos1.y, 0), new Vector3(w1 + pos1.x, h1 + pos1.y, 1));
        BoundingBox bb2 = new BoundingBox(new Vector3(pos2.x, pos2.y, 0), new Vector3(w2 + pos2.x, h2 + pos2.y, 1));
        return bb1.intersects(bb2);
    }
}
