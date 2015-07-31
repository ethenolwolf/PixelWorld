package com.mygdx.pixelworld.debug;

import com.badlogic.gdx.Gdx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for debugging utilities.
 */
public class Debug {

    private static final Properties props = new Properties();
    private static final Logger logger = LogManager.getLogger();

    /**
     * Load values and init font.
     */
    public static void init() {
        try {
            FileInputStream stream = new FileInputStream(Gdx.files.internal("core/config/debug.properties").file());
            props.load(stream);
        } catch (IOException e) {
            logger.error("File debug.properties not found", e);
        }
    }

    /**
     * @param propertyName Name of the property to read from debug.properties
     * @return Boolean value of the property
     */
    public static boolean isTrue(String propertyName) {
        if (props.getProperty(propertyName) == null) {
            logger.warn("Could not find property " + propertyName);
            return false;
        } else return Boolean.parseBoolean(props.getProperty(propertyName));
    }


}
