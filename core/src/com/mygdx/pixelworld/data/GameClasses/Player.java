package com.mygdx.pixelworld.data.GameClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.*;
import com.mygdx.pixelworld.data.AssetsManagement.AssetType;

import static com.mygdx.pixelworld.data.Directions.*;

/**
 * Class for containing main player.
 *
 * @author alessandro
 */
public class Player {

    private String name;
    private Vector2 pos;
    private DrawData img;

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
        this.name = NameExtractor.extract();
        this.pos = new Vector2(580, 0);
        img = new DrawData(AssetType.CHARACTER, this.getClass(), new Vector2(1, 1), 0);
    }

    public void setIsFiring(boolean isFiring) {
        this.isFiring = isFiring;
        if (isFiring) fireDelay = 0;
    }

    public Vector2 getPos() {
        return new Vector2(pos.x, pos.y);
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
            map.fire(pos, target, this.getClass());
            fireDelay += 1 / Algorithms.map(Constants.PLAYER_DEXTERITY, 1, 100, 1, 8);
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
        pos = img.boundMap(pos);
    }

    /**
     * Draws player on the screen.
     * @param batch SpriteBatch used to draw.
     */
    public void draw(SpriteBatch batch) {
        img.draw(batch, pos);
        img.write(batch, name, img.getEffectivePosition(pos).x + 10.0f, img.getEffectivePosition(pos).y + img.getHeight() + 10.0f);
    }

    public boolean checkIfInside(Bullet b) {
        return img.getBoundingBox(pos).contains(b.getBoundingBox());
    }

    public void getHit(Bullet b) {
        if (b.getDamage() > armor) health -= (b.getDamage() - armor);
        if (health <= 0) alive = false;
    }
}
