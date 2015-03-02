package com.mygdx.pixelworld.data.utilities;

public class Algorithms {
    public static float map(float inValue, float inMin, float inMax, float outMin, float outMax) {
        return (inValue - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

}
