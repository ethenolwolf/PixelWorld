package com.mygdx.pixelworld.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.utilities.Constants;

/**
 * Desktop launcher for the game : call this main().
 */
class DesktopLauncher {
    public static void main (String[] arg) {
        Logger.log("DesktopLauncher.main()", "Starting game...");
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "PixelWorld";
        cfg.height = (int) Constants.gameHeight;
        cfg.width = (int) (Constants.gameWidth);
        new LwjglApplication(new Game(), cfg);
    }
}
