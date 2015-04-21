package com.mygdx.pixelworld.GUI;

import com.mygdx.pixelworld.debug.Debug;

/**
 * Utility class for debugging on console.
 */
public class Logger {
    public static void log(String methodName, String message) {
        if (Debug.valueOf("VERBOSE")) System.out.println("[" + methodName + "] -> " + message);
    }
}
