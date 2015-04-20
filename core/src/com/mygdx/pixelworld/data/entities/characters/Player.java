package com.mygdx.pixelworld.data.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.data.World;
import com.mygdx.pixelworld.data.draw.AnimationDrawData;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Inventory;
import com.mygdx.pixelworld.data.items.LockedInventory;
import com.mygdx.pixelworld.data.items.armors.Armor;
import com.mygdx.pixelworld.data.items.sigils.ManaSigil;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.utilities.*;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingRect;
import com.mygdx.pixelworld.data.utilities.bounding.BoundingShape;
import com.mygdx.pixelworld.debug.Debug;
import com.mygdx.pixelworld.debug.Debuggable;

import java.util.List;

import static com.mygdx.pixelworld.data.utilities.Direction.*;

public class Player extends Entity implements Debuggable{

    private final FireManager fireManager;
    private final LockedInventory equipped;
    private final Inventory inventory;
    private final GameClasses gameClass;
    private int experience = 0;
    private int level = 1;

    public Player(GameClasses gameClass) {
        String name = NameExtractor.extract();
        this.pos = new Vector2(280, 0);
        this.gameClass = gameClass;
        img = new AnimationDrawData("core/assets/characters/" + gameClass.toString().toLowerCase() + "/", States.class, 8, 8, BoundingRect.class);
        stats = new EntityStats(gameClass);
        fireManager = new FireManager();
        equipped = new LockedInventory(this);
        inventory = new Inventory(8);

        //Debug
        Debug.addDebuggable(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void update(World world) {
        //Keyboard events
        States currentState;
        if (Gdx.input.isKeyPressed(Keys.ANY_KEY)) currentState = States.WALK;
        else currentState = States.IDLE;
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT, world.getBoundingRects());
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT, world.getBoundingRects());
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN, world.getBoundingRects());
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP, world.getBoundingRects());

        if (fireManager.isFiring()) currentState = States.FIRE;

        if (!equipped.getWeapon().isEmpty())
            fireManager.updateFire(pos, stats, world, equipped.getWeapon().getStats());
        regen();
        if (!equipped.getManaSigil().isEmpty()) equipped.getManaSigil().update();
        ((AnimationDrawData) img).setCurrentAction(currentState.ordinal());
        img.update();
    }

    public void manaTrigger() {
        if (equipped.getManaSigil().isEmpty()) Logger.log("Player.manaTrigger()", "You must equip a sigil first.");
        if (stats.get(StatType.MANA) >= equipped.getManaSigil().getPrice()) {
            stats.addStat(StatType.MANA, -equipped.getManaSigil().getPrice());
            equipped.getManaSigil().activate(new Vector2(pos));
        }
    }

    private void move(Direction dir, List<BoundingRect> boundingRects) {
        float movement = Gdx.graphics.getDeltaTime() * stats.get(StatType.SPD) * 5;
        switch (dir) {
            case UP:
                bound(boundingRects, new Vector2(0, movement));
                break;
            case DOWN:
                bound(boundingRects, new Vector2(0, -movement));
                break;
            case LEFT:
                bound(boundingRects, new Vector2(-movement, 0));
                break;
            case RIGHT:
                bound(boundingRects, new Vector2(movement, 0));
                break;
        }
        pos = img.boundMap(pos);
    }

    public void draw(SpriteBatch batch) {
        img.draw(batch, pos, 1.0f, stats.isVisible() ? 1f : 0.5f);
        equipped.getManaSigil().draw(batch);
    }

    private void regen() {
        if (stats.get(StatType.HEALTH) < stats.getInit(StatType.HEALTH))
            stats.addStat(StatType.HEALTH, Gdx.graphics.getDeltaTime() * stats.get(StatType.VIT));
        if (stats.get(StatType.HEALTH) > stats.getInit(StatType.HEALTH)) stats.setAsInit(StatType.HEALTH);

        if (stats.get(StatType.MANA) < stats.getInit(StatType.MANA))
            stats.addStat(StatType.MANA, Gdx.graphics.getDeltaTime() * stats.get(StatType.WIS));
        if (stats.get(StatType.MANA) > stats.getInit(StatType.MANA)) stats.setAsInit(StatType.MANA);
    }

    public boolean checkIfInside(Bullet b) {
        return BoundingShape.intersect(b.getBoundingShape(), img.getBoundingShape(pos));
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

    public float getHealthPercentage() {
        return stats.get(StatType.HEALTH) / stats.getMax(StatType.HEALTH);
    }

    public float getManaPercentage() {
        return stats.get(StatType.MANA) / stats.getMax(StatType.MANA);
    }

    public FireManager getFireManager() {
        return fireManager;
    }

    public ManaSigil getManaSigil() {
        return equipped.getManaSigil();
    }

    public BoundingShape getBoundingShape() {
        return img.getBoundingShape(pos);
    }

    public void checkMana(List<Enemy> e) {
        for (Enemy enemy : e)
            if (equipped.getManaSigil().checkIfInside(enemy)) enemy.getHit(equipped.getManaSigil());
    }

    public Weapon getWeapon() {
        return equipped.getWeapon();
    }

    public Armor getArmor() {
        return equipped.getArmor();
    }

    public void addExperience(int experience) {
        this.experience += experience;
        int level = 1;
        for (Integer threshold : Config.getExperienceThresholds()) {
            if (this.experience > threshold) level++;
        }
        if (level != this.level) Logger.log("Player.addExperience()", "Level UP! Level=" + level);
        this.level = level;
    }

    public float getExpPercentage() {
        for (Integer threshold : Config.getExperienceThresholds()) {
            if (experience < threshold) return (float) experience / (float) threshold;
        }
        return 1.0f;
    }

    public void equip(Inventory inv, int slot) {
        Inventory.swap(equipped, inv, slot);
    }

    public void unequip(int equipSlot, Inventory inv, int inventorySlot) {
        if (equipped.isCompatible(inv, inventorySlot, equipSlot)) {
            Inventory.swap(equipped, equipSlot, inv, inventorySlot);
        }
    }

    public LockedInventory getEquipped() {
        return equipped;
    }

    public GameClasses getGameClass() {
        return gameClass;
    }

    @Override
    public String getWatch() {
        return String.format("Player -> X = %f\tY = %f", pos.x, pos.y);
    }

    private enum States {
        IDLE, WALK, FIRE
    }
}
