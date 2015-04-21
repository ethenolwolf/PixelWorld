package com.mygdx.pixelworld.data.utilities;

/**
 * Abstract class used to contain utility methods.
 */
abstract class Algorithms {
    /**
     * Maps a value from a range to another range.
     *
     * @param inValue Input value
     * @param inMin   Input minimum
     * @param inMax   Input maximum
     * @param outMin  Output minimum
     * @param outMax  Output maximum
     * @return New mapped value
     */
    public static float map(float inValue, float inMin, float inMax, float outMin, float outMax) {
        return (inValue - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
