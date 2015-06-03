package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.pixelworld.GUI.DrawManager;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.data.CameraManager;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.Direction;
import com.mygdx.pixelworld.debug.Debug;

/**
 * Class containing main game loop and init.
 */
public class Game extends ApplicationAdapter implements InputProcessor {

    public static AssetManager assetManager;
    public static GameStates gameState = GameStates.MENU;

    @Override
    public void create() {
        Debug.init();
        Gdx.input.setInputProcessor(this);
        CameraManager.init();
        assetManager = new AssetManager();
        GUI.loadImage();
        assetManager.finishLoading();
        DrawManager.init();
        GUI.init();
        if (Debug.isTrue("SKIP_MAIN_MENU")) Game.gameState = Game.GameStates.GAME;
        World.init();
    }

    /**
     * If there are assets unloaded load and draw splash screen, otherwise execute game loop.
     */
    @Override
    public void render() {
        GUI.clearScreen();
        if (assetManager.update()) {
            switch (gameState) {
                case MENU:
                    GUI.menuLoop();
                    break;
                case PAUSE:
                case GAME:
                    gameLoop();
                    break;
                case LOAD:
                    World.load();
                    gameState = GameStates.GAME;
                    break;
            }
        } else {
            GUI.splashScreen(assetManager.getProgress());
        }
        DrawManager.end();
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        //Logic update
        if (gameState == GameStates.GAME) {
            World.update();
            DrawManager.setColor(DrawManager.Type.BATCH, 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            DrawManager.setColor(DrawManager.Type.BATCH, 0.5f, 0.5f, 0.5f, 1);
        }

        //Draw
        World.draw();
        GUI.draw();
        World.shapeDraw();
    }

    @Override
    public void dispose() {
        super.dispose();
        World.dispose();
        DrawManager.dispose();
        assetManager.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (gameState) {
            case MENU:
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) GUI.cursorEvent(Direction.DOWN);
                else if (keycode == Input.Keys.UP || keycode == Input.Keys.W) GUI.cursorEvent(Direction.UP);
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
                    GUI.cursorEvent(Direction.RIGHT);
                break;
            case GAME:
                if (keycode == Input.Keys.SPACE) World.manaTrigger();
                if (keycode == Input.Keys.valueOf(Constants.INTERACTION_KEY)) World.interaction();
                if (keycode == Input.Keys.P) gameState = GameStates.PAUSE;
                break;
            case PAUSE:
                if (keycode == Input.Keys.P) gameState = GameStates.GAME;
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            World.playerStartFire(screenX, Gdx.graphics.getHeight() - screenY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        World.playerStopFire();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        World.playerUpdateTarget(screenX, Gdx.graphics.getHeight() - screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public enum GameStates {
        MENU, GAME, LOAD, PAUSE
    }

}