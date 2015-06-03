package com.mygdx.pixelworld.debug;

import com.badlogic.gdx.Gdx;
import com.mygdx.pixelworld.GUI.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for debugging utilities.
 */
public class Debug {

    private static final Properties props = new Properties();

    /**
     * Load values and init font.
     */
    public static void init() {
        try {
            FileInputStream stream = new FileInputStream(Gdx.files.internal("core/config/debug.properties").file());
            props.load(stream);
            if (props.getProperty("ENABLE_LOGGER") != null) {
                Logger.setVerbose(isTrue("ENABLE_LOGGER"));
            }
        } catch (IOException e) {
            Logger.log("Debug.init()", "File debug.properties not found: ");
            e.printStackTrace();
        }
    }

    /**
     * @param propertyName Name of the property to read from debug.properties
     * @return Boolean value of the property
     */
    public static boolean isTrue(String propertyName) {
        if (props.getProperty(propertyName) == null) {
            Logger.log("Debug.isTrue()", " Error: Could not find property " + propertyName);
            return false;
        } else return Boolean.parseBoolean(props.getProperty(propertyName));
    }


}
