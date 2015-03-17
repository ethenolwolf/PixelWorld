package com.mygdx.pixelworld.GUI;

import com.mygdx.pixelworld.DebugOptions;

public class Logger {
    public static void log(String methodName, String message) {
        if (DebugOptions.VERBOSE) System.out.println("[" + methodName + "] -> " + message);
    }
}
