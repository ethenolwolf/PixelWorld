package com.mygdx.pixelworld.GUI;

import com.mygdx.pixelworld.debug.Debug;

public class Logger {
    public static void log(String methodName, String message) {
        if (Debug.valueOf("VERBOSE")) System.out.println("[" + methodName + "] -> " + message);
    }
}
