package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.Assets;
import com.mygdx.pixelworld.data.Player;
import com.mygdx.pixelworld.data.enemies.Blocker;

/**
 * Main game class.
 *
 * @author alessandro
 */
public class Game extends ApplicationAdapter {

    public static float deltaTime = 0;
    SpriteBatch batch;
    Map map;
    Player player;

    /**
     * Inits various parts of the program, including batch, map and player.
     *
     * @see com.mygdx.pixelworld.data.Player
     * @see com.mygdx.pixelworld.GUI.Map
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new Map();
        map.addEnemy(Blocker.class, 1250, 150);
        player = new Player();
        Assets.init();
    }

    /**
     * Main loop. Updates player and map, and then draws them.
     */
    @Override
    public void render() {
        //Reads the deltaTime once, for all the moving entities, so that it's the same.
        deltaTime = Gdx.graphics.getDeltaTime();

        player.update();
        map.update(player.getPos());

        batch.begin();
        map.draw(batch);
        player.draw(batch);

        batch.end();
    }
}