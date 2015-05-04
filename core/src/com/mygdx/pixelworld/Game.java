package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.ScreenWriter;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.data.utilities.Direction;
import com.mygdx.pixelworld.debug.Debug;

/**
 * Class containing main game loop and init.
 */
public class Game extends ApplicationAdapter implements InputProcessor {

    public static OrthographicCamera camera;
    public static AssetManager assetManager;
    public static GameStates gameState = GameStates.MENU;
    private SpriteBatch batch;
    private World world;
    private Player player;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        assetManager = new AssetManager();
        GUI.loadImage();
        assetManager.finishLoading();
        batch = new SpriteBatch();
        Debug.init();
        ScreenWriter.init();
        player = new Player();
        world = new World(player);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.gameWidth, Constants.gameHeight);
        camera.update();
        Gdx.input.setInputProcessor(this);
        shapeRenderer = new ShapeRenderer();
        GUI.init(batch, player, world);
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
                    world.load();
                    player.load();
                    gameState = GameStates.GAME;
                    break;
            }
        } else {
            GUI.splashScreen(shapeRenderer, assetManager.getProgress());
        }
    }

    /**
     * Main game loop.
     */
    private void gameLoop() {
        //Logic update
        if (gameState == GameStates.GAME) {
            player.update(world);
            world.update(batch);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            batch.setColor(0.5f, 0.5f, 0.5f, 1);
        }

        //Draw
        world.drawBottom(batch, camera);
        player.draw(batch);
        Debug.draw(batch);
        world.drawTop(batch);
        batch.begin();
        GUI.draw();
        batch.end();
        world.shapeDraw(shapeRenderer, player);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        world.dispose();
        player.dispose();
        shapeRenderer.dispose();
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
                if (keycode == Input.Keys.SPACE) player.manaTrigger();
                if (keycode == Input.Keys.valueOf(Constants.INTERACTION_KEY)) world.interaction();
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
            if (screenX < Constants.gameWidth) { //InScreen click
                player.getFireManager().setIsFiring(true);
                player.getFireManager().setTarget(screenX + World.getCameraOffset().x, Gdx.graphics.getHeight() - screenY + World.getCameraOffset().y);
            } else { //Panel click
                GUI.clickDown(screenX, (int) Constants.gameHeight - screenY);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.getFireManager().setIsFiring(false);
        GUI.clickUp(screenX, (int) Constants.gameHeight - screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        player.getFireManager().setTarget(screenX + World.getCameraOffset().x, Gdx.graphics.getHeight() - screenY + World.getCameraOffset().y);
        GUI.updateMousePosition(screenX, (int) Constants.gameHeight - screenY);
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