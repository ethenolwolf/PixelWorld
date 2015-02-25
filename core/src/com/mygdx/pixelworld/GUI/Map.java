package com.mygdx.pixelworld.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.Costants;
import com.mygdx.pixelworld.data.enemies.Blocker;
import com.mygdx.pixelworld.data.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Enemy> enemies = new ArrayList<Enemy>();

    private final float SPEED = 250.0f;
    private Texture background;
    private Vector2 offset = new Vector2();

    public Map(String background) {
        set(background);
    }

    public void set(String path){
        background = new Texture(path);
    }

    public void addEnemy(Class type, float x, float y){
        if(type == Blocker.class) enemies.add(new Blocker(x,y));
    }

    public void update(Vector2 playerPos){

        for(Enemy e : enemies){
            e.update(playerPos);
        }

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

            if(playerPos.x <= sw*Costants.X_LIMIT_MIN) offset.add(Game.deltaTime * SPEED, 0);
            else if(playerPos.x >= sw*Costants.X_LIMIT_MAX) offset.add(-Game.deltaTime * SPEED, 0);
            if(playerPos.y <= sh*Costants.Y_LIMIT_MIN) offset.add(0, Game.deltaTime * SPEED);
            else if(playerPos.y >= sh*Costants.Y_LIMIT_MAX) offset.add(0, -Game.deltaTime * SPEED);

            if(offset.x > 0) offset.x = 0;
            if(offset.y > 0) offset.y = 0;
            if(offset.x < -background.getWidth() + sw) offset.x = -background.getWidth() + sw;
            if(offset.y < -background.getHeight() + sh) offset.y = -background.getHeight() + sh;

    }

    public void draw(SpriteBatch batch) {
        batch.draw(background, offset.x, offset.y);

        for(Enemy e : enemies){
            if(e.getPos().x < Gdx.graphics.getWidth()-offset.x && e.getPos().x >= 0-offset.x &&
               e.getPos().y < Gdx.graphics.getHeight()-offset.y && e.getPos().y-offset.y >= 0) e.draw(batch);
        }
    }
}
