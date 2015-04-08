package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.pixelworld.GUI.GUI;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.assets.Assets;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.utilities.Constants;
import com.mygdx.pixelworld.debug.Debug;

/**
 * Main game class.
 *
 * @author alessandro
 */
public class Game extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
    private World world;
    private Player player;
    private ShapeRenderer shapeRenderer;
    public static OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.init();
        Constants.init();
        world = new World();
        world.generateEnemies(15);
        player = new Player(GameClasses.WIZARD);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.gameWidth+Constants.panelWidth, Constants.gameHeight);
        camera.update();

        Gdx.input.setInputProcessor(this);
        shapeRenderer = new ShapeRenderer();
        GUI.init(batch, player, world);
    }

    /**
     * Main loop.
     */
    @Override
    public void render() {
        //Logic update
        player.update(world);
        world.update(player);

        //Clear screen
        Gdx.gl.glClearColor(0.5f, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw
        world.drawBottom(batch, camera);
        player.draw(batch);
        GUI.draw();
        Debug.draw(batch);
        batch.end();
        world.drawTop();
        world.shapeDraw(shapeRenderer, player);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) player.manaTrigger();
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
            if (screenX < Constants.gameWidth) {
                player.getFireManager().setIsFiring(true);
                player.getFireManager().setTarget(screenX + World.getCameraOffset().x, Gdx.graphics.getHeight() - screenY + World.getCameraOffset().y);
            } else {
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

}