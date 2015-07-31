package com.mygdx.pixelworld.data.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Abstract class used to contain utility methods.
 */
@SuppressWarnings("SameParameterValue")
public abstract class Utils {
    private static final String SAVE_PATH = "core/assets/saves/save.save";
    private static final Logger logger = LogManager.getLogger();
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

    public static String[] readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String tmp = new String(encoded, Charset.defaultCharset());
        return tmp.split("\n");
    }

    public static String[] readSave() {
        String[] out;
        try {
            out = readFile(SAVE_PATH);
        } catch (IOException e) {
            logger.error("Save file not found!");
            return null;
        }
        if (out.length != 5) {
            logger.error("Save file not valid : " + out.length + " lines!");
            return null;
        }
        return out;
    }
}
