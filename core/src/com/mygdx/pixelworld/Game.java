package com.mygdx.pixelworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.data.Assets;
import com.mygdx.pixelworld.data.GameCharacter;
import com.mygdx.pixelworld.data.enemies.Blocker;

public class Game extends ApplicationAdapter {
    public static float deltaTime = 0;
    SpriteBatch batch;
    Map map;
    GameCharacter player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        map = new Map(Assets.BACKGROUND);
        map.addEnemy(Blocker.class, 250, 150);
        player = new GameCharacter();
        Assets.init();
    }

    @Override
    public void render() {
        deltaTime = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update();
        map.update(player.getPos());
        player.unMove();

        batch.begin();
        map.draw(batch);
        player.draw(batch);

        batch.end();
    }
}