package com.mygdx.pixelworld.data.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Inventory;
import com.mygdx.pixelworld.data.items.LockedInventory;
import com.mygdx.pixelworld.data.items.armors.Armor;
import com.mygdx.pixelworld.data.items.sigils.ManaSigil;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.utilities.*;

import java.util.List;

import static com.mygdx.pixelworld.data.utilities.Directions.*;

public abstract class Player extends Entity {

    private final String name;
    private final FireManager fireManager;
    private final LockedInventory equipped;
    private final Inventory inventory;
    private int experience = 0;
    private int level = 1;

    Player() {
        this.name = NameExtractor.extract();
        this.pos = new Vector2(280, 0);
        img = new DrawData(AssetType.CHARACTER, this.getClass(), new Vector2(1, 1), 0);
        stats = new EntityStats(this.getClass());
        fireManager = new FireManager();
        equipped = new LockedInventory(this);
        inventory = new Inventory(8);
    }

    public static Player getPlayer(GameClasses name) {
        switch (name) {
            case WIZARD:
                return new Wizard();
            case NINJA:
                return new Ninja();
        }
        return null;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void update(Map map) {
        //Keyboard events
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT);
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT);
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN);
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP);


        if (!equipped.getWeapon().isEmpty()) fireManager.updateFire(pos, stats, map, equipped.getWeapon().getStats());
        regen();
        if (!equipped.getManaSigil().isEmpty()) equipped.getManaSigil().update();
    }

    public void manaTrigger() {
        if (equipped.getManaSigil().isEmpty()) Logger.log("You must equip a sigil first.");
        if (stats.get(StatType.MANA) >= equipped.getManaSigil().getPrice()) {
            stats.addStat(StatType.MANA, -equipped.getManaSigil().getPrice());
            equipped.getManaSigil().activate(new Vector2(pos));
        }
    }

    private void move(Directions dir) {
        float movement = Game.deltaTime * stats.get(StatType.SPD) * 5;
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

    @Override
    public void draw(SpriteBatch batch) {
        img.draw(batch, pos, stats.isVisible() ? 1f : 0.5f);
        equipped.getManaSigil().draw(batch);
        img.write(batch, name, Constants.gameWidth + 10, Constants.gameHeight - 50);
    }

    private void regen() {
        if (stats.get(StatType.HEALTH) < stats.getInit(StatType.HEALTH))
            stats.addStat(StatType.HEALTH, Game.deltaTime * stats.get(StatType.VIT));
        if (stats.get(StatType.HEALTH) > stats.getInit(StatType.HEALTH)) stats.setAsInit(StatType.HEALTH);

        if (stats.get(StatType.MANA) < stats.getInit(StatType.MANA))
            stats.addStat(StatType.MANA, Game.deltaTime * stats.get(StatType.WIS));
        if (stats.get(StatType.MANA) > stats.getInit(StatType.MANA)) stats.setAsInit(StatType.MANA);
    }

    public boolean checkIfInside(Bullet b) {
        return img.getBoundingCircle(pos).intersect(b.getBoundingCircle());
    }

    @Override
    public void getHit(Damaging d) {
        if (equipped.getArmor().getDefense() + stats.get(StatType.DEF) >= d.getDamage()) return;
        super.getHit(d.getDamage() - equipped.getArmor().getDefense() - (int) stats.get(StatType.DEF));
        if (!stats.isAlive()) {
            Logger.log("[Player.getHit()] Player died :(");
            Gdx.app.exit();
        }
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
        for (Integer threshold : Constants.levelUpValues) {
            if (this.experience > threshold) level++;
        }
        if (level != this.level) Logger.log("[Player.addExp()] Level UP! Level=" + level);
        this.level = level;
    }

    public void equip(Inventory inv, int slot) {
        Inventory.swap(equipped, inv, slot);
    }

    public void unequip(int equipSlot, Inventory inv, int inventorySlot) {
        if (equipped.isCompatible(inv, inventorySlot, equipSlot)) {
            Inventory.swap(equipped, equipSlot, inv, inventorySlot);
        }
    }
}
