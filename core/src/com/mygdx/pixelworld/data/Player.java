package com.mygdx.pixelworld.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private final CharacterType charType;
    private String name;
    private Vector2 pos;

    private double fireDelay;
    private int armor = 10;
    private int health = 1000;
    private boolean alive = true;
    private boolean isFiring = false;
    private Vector2 target = new Vector2();
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

    public void setIsFiring(boolean isFiring) {
        this.isFiring = isFiring;
        if (isFiring) fireDelay = 0;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setTarget(float x, float y) {
        target.x = x;
        target.y = y;
    }

    /**
     * Manage keyboard events, and moves() if necessary.
     * It also checks for screen resizing.
     */
    public void update(Map map) {
        if (!alive) System.exit(0);
        //Keyboard events
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT);
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT);
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN);
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP);

        if (isFiring) updateFire(map);
    }

    private void updateFire(Map map) {
        fireDelay -= Game.deltaTime;
        if (fireDelay <= 0) {
            map.fire(pos, target, Player.class);
            fireDelay += 1 / Algorithms.scale(Constants.PLAYER_DEXTERITY, 1, 100, 1, 8);
        }
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
        float pw = Gdx.graphics.getWidth() * Constants.CHARACTER_WIDTH;
        float ph = Gdx.graphics.getHeight() * Constants.CHARACTER_HEIGHT;
        float ex = pos.x + Map.getOffset().x;
        float ey = pos.y + Map.getOffset().y;

        batch.draw(new TextureRegion(Assets.CHARACTER_IMG.get(charType)), ex, ey, (ex + pw) / 2, (ey + ph) / 2, pw, ph, 1, 1, 0);
        Assets.font.draw(batch, name, ex + 10.0f, ey + pw + 10.0f);
    }

    public boolean checkIfInside(Bullet b) {
        float pw = Gdx.graphics.getWidth() * Constants.CHARACTER_WIDTH;
        float ph = Gdx.graphics.getHeight() * Constants.CHARACTER_HEIGHT;

        return Algorithms.contains(pos, pw, ph, b.getPos(), b.getWidth(), b.getHeight());
    }

    public void getHit(Bullet b) {
        if (b.getDamage() > armor) health -= (b.getDamage() - armor);
        //System.out.println("AHIA! Health = " + health);
        if (health <= 0) alive = false;
    }
}
