package com.mygdx.pixelworld.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Desktop launcher for the game : call this main().
 */
class DesktopLauncher {

    final static Logger logger = LogManager.getLogger();

    public static void main (String[] arg) {
        logger.trace("Starting application");
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "PixelWorld";
        cfg.height = (int) Constants.gameHeight;
        cfg.width = (int) (Constants.gameWidth);
        new LwjglApplication(new Game(), cfg);
    }
}
