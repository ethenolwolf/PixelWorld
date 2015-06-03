package com.mygdx.pixelworld.GUI;

/**
 * Utility class for debugging on console.
 */
public class Logger {

    private static boolean verbose = true;

    public static void log(String methodName, String message) {
        if (verbose) System.out.println("[" + methodName + "] -> " + message);
    }

    public static void setVerbose(boolean verbose) {
        Logger.verbose = verbose;
    }
}
