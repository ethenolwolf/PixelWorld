package com.mygdx.pixelworld.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.Game;

import java.util.Random;

import static com.mygdx.pixelworld.data.Directions.*;

public class GameCharacter {

    private String name;
    private final CharacterType charType;
    private final float SPEED = 250.0f;
    private Vector2 pos;

    private float lastHeight;

    public GameCharacter() {
        int pick = new Random().nextInt(CharacterType.values().length);
        this.charType = CharacterType.values()[pick];
        this.name = NameExtractor.extract();
        this.pos = new Vector2(0, Costants.CHARACTER_HEIGHT * Gdx.graphics.getHeight());
    }

    public Vector2 getPos() {
        return pos;
    }

    public void update() {

        if (!(Gdx.graphics.getHeight() == lastHeight)) {
            pos.y = Costants.CHARACTER_HEIGHT * Gdx.graphics.getHeight();
        }

        lastHeight = Gdx.graphics.getHeight();

        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT);
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT);
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN);
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP);

    }

    private void move(Directions dir) {

        float movement = Game.deltaTime * SPEED;

        switch (dir) {
            case UP:
                pos.add(0, movement);
                break;
            case DOWN:
                pos.add(0, -movement);
                break;
            case LEFT:
                pos.add(-movement, 0);
                break;
            case RIGHT:
                pos.add(movement, 0);
                break;
        }
    }

    public void unMove() {

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        if (pos.x < Costants.X_LIMIT_MIN * sw) pos.add(Costants.X_LIMIT_MIN * sw - pos.x + 1, 0);
        if (pos.x > Costants.X_LIMIT_MAX * sw) pos.add(Costants.X_LIMIT_MAX * sw - pos.x - 1, 0);
        if (pos.y < Costants.Y_LIMIT_MIN * sh) pos.add(0, Costants.Y_LIMIT_MIN * sh - pos.y + 1);
        if (pos.y > Costants.Y_LIMIT_MAX * sh) pos.add(0, Costants.Y_LIMIT_MAX * sh - pos.y - 1);

    }

    public void draw(SpriteBatch batch) {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        batch.draw(Assets.CHARACTER_IMG.get(charType), pos.x, pos.y, Costants.CHARACTER_WIDTH * sw, Costants.CHARACTER_HEIGHT * sh);
        Assets.font.draw(batch, name, pos.x, pos.y);
    }
}
