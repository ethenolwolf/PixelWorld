package com.mygdx.pixelworld.data.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.Entity;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.items.Chest;
import com.mygdx.pixelworld.data.items.EmptyItem;
import com.mygdx.pixelworld.data.items.Item;
import com.mygdx.pixelworld.data.items.ItemType;
import com.mygdx.pixelworld.data.items.armors.Armor;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.sigils.ManaSigil;
import com.mygdx.pixelworld.data.utilities.*;

import java.util.List;

import static com.mygdx.pixelworld.data.utilities.Directions.*;

public abstract class Player extends Entity {

    private final String name;
    private final FireManager fireManager;
    private final Item[] inventory = new Item[8];
    private Armor armor;
    private ManaSigil manaSigil;
    private Weapon weapon;
    private int experience = 0;
    private int level = 1;

    Player() {
        this.name = NameExtractor.extract();
        this.pos = new Vector2(280, 0);
        img = new DrawData(AssetType.CHARACTER, this.getClass(), new Vector2(1, 1), 0);
        stats = new EntityStats(this.getClass());
        fireManager = new FireManager();
        weapon = (Weapon) equipItem(ItemType.WEAPON, 1);
        armor = (Armor) equipItem(ItemType.ARMOR, 1);
        manaSigil = ManaSigil.getInitial(this);
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = new EmptyItem();
        }

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

    public Item[] getInventory() {
        return inventory;
    }

    private Item equipItem(ItemType itemType, int rank) {
        switch (itemType) {
            case WEAPON:
                return new Weapon(this.getClass(), rank);
            case ARMOR:
                return new Armor(this.getClass(), rank);
        }
        return null;
    }

    public void update(Map map) {
        //Keyboard events
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT);
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT);
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN);
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP);


        fireManager.updateFire(pos, stats, map, weapon.getStats(), weapon.isEmpty());
        regen();
        if (!manaSigil.isEmpty()) manaSigil.update();
    }

    public void manaTrigger() {
        if (manaSigil.isEmpty()) Logger.log("You must equip a sigil first.");
        if (stats.get(StatType.MANA) >= manaSigil.getPrice()) {
            stats.addStat(StatType.MANA, -manaSigil.getPrice());
            manaSigil.activate(new Vector2(pos));
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
        manaSigil.draw(batch);
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
        if (armor.getDefense() + stats.get(StatType.DEF) >= d.getDamage()) return;
        super.getHit(d.getDamage() - armor.getDefense() - (int) stats.get(StatType.DEF));
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

    public void checkMana(List<Enemy> e) {
        for (Enemy enemy : e)
            if (manaSigil.checkIfInside(enemy)) enemy.getHit(manaSigil);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
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

    public boolean tryToEquip(Item item) {
        if (item instanceof Weapon && weapon.isEmpty()) {
            weapon = (Weapon) item;
            return true;
        }
        if (item instanceof ManaSigil && manaSigil.isEmpty()) {
            manaSigil = (ManaSigil) item;
            return true;
        }

        if (item instanceof Armor && armor.isEmpty()) {
            armor = (Armor) item;
            return true;
        }
        return false;
    }

    public void tryToEquip(int inventorySlot) {
        //equip from inventory to player
        Logger.log("Trying to equip from inventory. invSlot = " + inventorySlot);
        if (inventory.length <= inventorySlot) return;
        Item item = inventory[inventorySlot];
        if (tryToEquip(item)) inventory[inventorySlot] = new EmptyItem();
    }

    public void tryToEquip(Chest currentChest, int chestSlot, int equipSlot) {
        //Equip from chest to player
        if (inventory.length <= equipSlot) return;
        Logger.log("Trying to equip from chest. equipSlot = " + equipSlot + " and cheSlot = " + chestSlot);
        Item item = currentChest.get(chestSlot);
        if (tryToEquip(item)) currentChest.remove(chestSlot);
    }

    public void placeInInventory(int equipSlot, int inventorySlot) {
        if (equipSlot >= 4 || inventorySlot >= inventory.length) return;
        Logger.log("Trying to place in inventory. equipSlot = " + equipSlot + " and invSlot = " + inventorySlot);
        if (tryToPlace(getEquipped(equipSlot), inventorySlot)) clearEquipped(equipSlot);
    }

    private boolean tryToPlace(Item item, int inventorySlot) {
        if (!isInventoryFree(inventorySlot)) return false;
        Logger.log("Inventory place is free! Placing.");
        inventory[inventorySlot] = item;
        return true;
    }

    private boolean isInventoryFree(int inventorySlot) {
        System.out.println("Checking slot number " + inventorySlot + ". This is of type " + inventory[inventorySlot].toString());
        return inventory[inventorySlot] instanceof EmptyItem;
    }

    private void clearInventory(int inventorySlot) {
        Logger.log("Clearing inventory.");
        inventory[inventorySlot] = new EmptyItem();
    }

    private Item getEquipped(int equipSlot) {
        switch (equipSlot) {
            case 0:
                return weapon;
            case 1:
                return manaSigil;
            case 2:
                return armor;
            default:
                return null;
        }
    }

    private void clearEquipped(int equipSlot) {
        switch (equipSlot) {
            case 0:
                weapon = new Weapon();
                break;
            case 1:
                manaSigil = new ManaSigil();
                break;
            case 2:
                armor = new Armor();
                break;
        }
    }

    public void placeInInventory(Chest chest, int chestSlot, int inventorySlot) {
        if (chest == null) return;
        if (chestSlot >= chest.getInventory().length || inventorySlot >= inventory.length) return;
        if (tryToPlace(chest.get(chestSlot), inventorySlot)) chest.remove(chestSlot);
    }

    public void swapInventory(int startSlot, int endSlot) {
        if (startSlot >= inventory.length || endSlot >= inventory.length || startSlot == endSlot) return;
        if (tryToPlace(inventory[startSlot], endSlot)) clearInventory(startSlot);
    }

    public void placeInvInChest(Chest chest, int inventorySlot, int chestSlot) {
        if (chest == null) return;
        if (chestSlot >= chest.getInventory().length || inventorySlot >= inventory.length) return;
        if (chest.tryToPlace(inventory[inventorySlot], chestSlot)) clearInventory(inventorySlot);
    }

    public void placeInChest(Chest chest, int equipSlot, int chestSlot) {
        if (chest == null) return;
        if (chestSlot >= chest.getInventory().length || equipSlot >= 4) return;
        if (chest.tryToPlace(getEquipped(equipSlot), chestSlot)) clearEquipped(equipSlot);
    }

    public ManaSigil getManaSigil() {
        return manaSigil;
    }
}
