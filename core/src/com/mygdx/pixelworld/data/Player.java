package com.mygdx.pixelworld.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;

import java.util.Random;

import static com.mygdx.pixelworld.data.Directions.*;

/**
 * Class for containing main player.
 *
 * @author alessandro
 */
public class Player {

    private String name;
    private final CharacterType charType;
    private Vector2 pos;

    private float lastHeight;

    /**
     * Picks a random CharacterType and name.
     *
     * @see com.mygdx.pixelworld.data.CharacterType
     * @see com.mygdx.pixelworld.data.NameExtractor
     */
    public Player() {
        int pick = new Random().nextInt(CharacterType.values().length);
        this.charType = CharacterType.values()[pick];
        this.name = NameExtractor.extract();
        this.pos = new Vector2(0, Constants.CHARACTER_HEIGHT * Gdx.graphics.getHeight());
    }

    public Vector2 getPos() {
        return pos;
    }

    /**
     * Manage keyboard events, and moves() if necessary.
     * It also checks for screen resizing.
     */
    public void update() {
        //If screen is resized, move the player
        if (!(Gdx.graphics.getHeight() == lastHeight)) {
            pos.y = Constants.CHARACTER_HEIGHT * Gdx.graphics.getHeight();
        }
        lastHeight = Gdx.graphics.getHeight();

        //Keyboard events
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT);
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT);
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN);
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP);

    }

    /**
     * Applies a movement, determined by deltaTime and PLAYER_SPEED, towards the required direction
     *
     * @param dir Direction of the movement
     */
    private void move(Directions dir) {

        float movement = Game.deltaTime * Constants.PLAYER_SPEED;

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

        pos = MapBound.bound(pos, Gdx.graphics.getWidth() * Constants.CHARACTER_WIDTH, Gdx.graphics.getHeight() * Constants.CHARACTER_HEIGHT);
    }

    /**
     * Draws player on the screen.
     * @param batch SpriteBatch used to draw.
     */
    public void draw(SpriteBatch batch) {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cw = Constants.CHARACTER_WIDTH;
        float ch = Constants.CHARACTER_HEIGHT;

        float ex = pos.x + Map.getOffset().x;
        float ey = pos.y + Map.getOffset().y;

        batch.draw(Assets.CHARACTER_IMG.get(charType), ex, ey, cw * sw, ch * sh);
        Assets.font.draw(batch, name, ex + 10.0f, ey + cw * sw + 10.0f);
        Assets.font.draw(batch, "PX = " + String.valueOf((int) pos.x) + " PY = " + String.valueOf((int) pos.y), 0, 200);
        Assets.font.draw(batch, "EX = " + String.valueOf((int) ex) + " EY = " + String.valueOf((int) ey), 0, 170);
        batch.draw(Assets.BULLETS_IMG.get(charType), ex + 50, ey, cw * sw / 2, ch * sh / 3);
    }

    public CharacterType getType() {
        return charType;
    }
}
