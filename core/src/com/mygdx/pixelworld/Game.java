package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.AssetsManagement.Assets;
import com.mygdx.pixelworld.data.Constants;
import com.mygdx.pixelworld.data.Enemies.Blocker;
import com.mygdx.pixelworld.data.GameClasses.Player;
import com.mygdx.pixelworld.data.GameClasses.Wizard;

/**
 * Main game class.
 *
 * @author alessandro
 */
public class Game extends ApplicationAdapter implements InputProcessor {

    public static float deltaTime = 0;
    SpriteBatch batch;
    Map map;
    Player player;

    /**
     * Inits various parts of the program, including batch, map and player.
     *
     * @see com.mygdx.pixelworld.data.GameClasses.Player
     * @see com.mygdx.pixelworld.GUI.Map
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.init();
        Constants.init();
        map = new Map();
        //map.addEnemy(Blocker.class, 120, 150);
        //map.addEnemy(Blocker.class, 180, 70);
        map.addEnemy(Blocker.class, 420, 150);
        //map.addEnemy(Blocker.class, 500, 30);
        //map.addEnemy(Blocker.class, 20, 300);
        player = new Wizard();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Main loop. Updates player and map, and then draws them.
     */
    @Override
    public void render() {
        //Reads the deltaTime once, for all the moving entities, so that it's the same.
        deltaTime = Gdx.graphics.getDeltaTime();

        player.update(map);
        map.update(player);

        batch.begin();
        map.draw(batch, player);
        player.draw(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
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
            player.setIsFiring(true);
            player.setTarget(screenX - Map.getOffset().x, Gdx.graphics.getHeight() - screenY - Map.getOffset().y);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.setIsFiring(false);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        player.setTarget(screenX - Map.getOffset().x, Gdx.graphics.getHeight() - screenY - Map.getOffset().y);
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