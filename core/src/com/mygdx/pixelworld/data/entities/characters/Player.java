package com.mygdx.pixelworld.data.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Inventory;
import com.mygdx.pixelworld.data.items.LockedInventory;
import com.mygdx.pixelworld.data.utilities.*;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;

import java.util.List;

import static com.mygdx.pixelworld.data.utilities.Direction.*;

/**
 * Main player class.
 */
public class Player extends Entity {

    private final FireManager fireManager;
    private final LockedInventory equipped;
    private final Inventory inventory;
    private States currentState;
    private int experience = 0;
    private int level = 1;

    /**
     * Initializes a new player.
     */
    public Player() {
        this.pos = new Vector2();
        String[] actions = new String[States.values().length];
        for (int i = 0; i < actions.length; i++) actions[i] = States.values()[i].name().toLowerCase();
        img = new AnimationDrawData("core/assets/characters/player/", actions, BoundingRect.class);
        stats = new EntityStats(this.getClass());
        fireManager = new FireManager();
        equipped = new LockedInventory(this);
        inventory = new Inventory(8);
    }

    /**
     * Updates moving.
     */
    public void update(World world) {
        //Keyboard events
        currentState = States.IDLE;
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT, world.getMapObstacles());
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT, world.getMapObstacles());
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN, world.getMapObstacles());
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP, world.getMapObstacles());

        if (fireManager.isFiring()) currentState = States.FIRE;

        if (!equipped.getWeapon().isEmpty())
            fireManager.updateFire(pos, stats, world, equipped.getWeapon().getStats());
        regen();
        if (!equipped.getManaSigil().isEmpty()) equipped.getManaSigil().update();
        ((AnimationDrawData) img).setCurrentAction(currentState.name().toLowerCase());
        img.update();
    }

    /**
     * Activates the mana sigil.
     */
    public void manaTrigger() {
        if (equipped.getManaSigil().isEmpty()) Logger.log("Player.manaTrigger()", "You must equip a sigil first.");
        if (stats.get(StatType.MANA) >= equipped.getManaSigil().getPrice()) {
            stats.addStat(StatType.MANA, -equipped.getManaSigil().getPrice());
            equipped.getManaSigil().activate(new Vector2(pos));
        }
    }

    /**
     * Moves the player
     *
     * @param direction          Direction of the movement
     * @param boundingRectangles Obstacles around the map
     */
    private void move(Direction direction, List<BoundingRect> boundingRectangles) {
        currentState = States.WALK;
        float movement = Gdx.graphics.getDeltaTime() * stats.get(StatType.SPD) * 5;
        switch (direction) {
            case UP:
                bound(boundingRectangles, new Vector2(0, movement));
                break;
            case DOWN:
                bound(boundingRectangles, new Vector2(0, -movement));
                break;
            case LEFT:
                bound(boundingRectangles, new Vector2(-movement, 0));
                break;
            case RIGHT:
                bound(boundingRectangles, new Vector2(movement, 0));
                break;
        }
        pos = img.boundMap(pos);
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos, stats.isVisible() ? 1f : 0.5f);
        equipped.getManaSigil().draw(batch);
    }

    /**
     * Regenerates health and mana.
     */
    private void regen() {
        if (stats.get(StatType.HEALTH) < stats.getInit(StatType.HEALTH))
            stats.addStat(StatType.HEALTH, Gdx.graphics.getDeltaTime() * stats.get(StatType.VIT));
        if (stats.get(StatType.HEALTH) > stats.getInit(StatType.HEALTH)) stats.setAsInit(StatType.HEALTH);

        if (stats.get(StatType.MANA) < stats.getInit(StatType.MANA))
            stats.addStat(StatType.MANA, Gdx.graphics.getDeltaTime() * stats.get(StatType.WIS));
        if (stats.get(StatType.MANA) > stats.getInit(StatType.MANA)) stats.setAsInit(StatType.MANA);
    }

    @Override
    public void getHit(Damaging d) {
        if (equipped.getArmor().getDefense() + stats.get(StatType.DEF) >= d.getDamage()) return;
        super.getHit(d.getDamage() - equipped.getArmor().getDefense() - (int) stats.get(StatType.DEF));
        if (!stats.isAlive()) {
            Logger.log("Player.getHit()", "Player died :(");
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        equipped.dispose();
        inventory.dispose();
    }

    public FireManager getFireManager() {
        return fireManager;
    }

    public BoundingShape getBoundingShape() {
        return img.getBoundingShape(pos);
    }

    public void checkMana(List<Enemy> e) {
        for (Enemy enemy : e)
            if (equipped.getManaSigil().checkIfInside(enemy)) enemy.getHit(equipped.getManaSigil());
    }

    /**
     * When defeating an enemy adds experience.
     *
     * @param experience Experience to add
     */
    public void addExperience(int experience) {
        this.experience += experience;
        int level = 1;
        for (Integer threshold : Config.getExperienceThresholds()) {
            if (this.experience > threshold) level++;
        }
        if (level != this.level) Logger.log("Player.addExperience()", "Level UP! Level=" + level);
        this.level = level;
    }

    public String getSaveParameters() {
        String params = "";
        params += stats.toString() + "\n";
        params += experience + "\n";
        params += equipped.toString() + "\n";
        params += inventory.toString();
        return params;
    }

    public void load() {
        String[] save = Utils.readSave();
        if (save == null) return;
        for (String saveLine : save)
            if (saveLine == null || saveLine.equals("")) {
                Logger.log("Player.load()", "Save file corrupted.");
                return;
            }
        stats = new EntityStats(save[1]);
        try {
            experience = Integer.parseInt(save[2]);
        } catch (Exception e) {
            Logger.log("Player.load()", "Exp malformed! Not integer value.");
            return;
        }

        equipped.load(save[3], this);
        inventory.load(save[4], this);
    }

    private enum States {
        IDLE, WALK, FIRE
    }
}
