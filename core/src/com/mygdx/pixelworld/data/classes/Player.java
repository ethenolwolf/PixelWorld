package com.mygdx.pixelworld.data.classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pixelworld.GUI.Logger;
import com.mygdx.pixelworld.GUI.Map;
import com.mygdx.pixelworld.Game;
import com.mygdx.pixelworld.data.Entity;
import com.mygdx.pixelworld.data.armors.Armor;
import com.mygdx.pixelworld.data.assets.AssetType;
import com.mygdx.pixelworld.data.assets.SigilName;
import com.mygdx.pixelworld.data.draw.Bullet;
import com.mygdx.pixelworld.data.draw.DrawData;
import com.mygdx.pixelworld.data.enemies.Enemy;
import com.mygdx.pixelworld.data.sigils.ManaSigil;
import com.mygdx.pixelworld.data.utilities.*;
import com.mygdx.pixelworld.data.weapons.Weapon;

import java.util.List;

import static com.mygdx.pixelworld.data.utilities.Directions.*;

public abstract class Player extends Entity {

    private final String name;
    private final FireManager fireManager;
    private final Weapon weapon;
    private final Armor armor;
    private final ManaSigil manaSigil;

    Player() {
        this.name = NameExtractor.extract();
        this.pos = new Vector2(580, 0);
        img = new DrawData(AssetType.CHARACTER, this.getClass(), new Vector2(1, 1), 0);
        stats = new EntityStats(this.getClass());
        fireManager = new FireManager();
        weapon = new Weapon(this.getClass(), 1);
        armor = new Armor(this.getClass(), 1);
        manaSigil = ManaSigil.getFromName(SigilName.invisibleCloack, this);
    }

    public void update(Map map) {
        //Keyboard events
        if (Gdx.input.isKeyPressed(Keys.A)) move(LEFT);
        else if (Gdx.input.isKeyPressed(Keys.D)) move(RIGHT);
        if (Gdx.input.isKeyPressed(Keys.S)) move(DOWN);
        else if (Gdx.input.isKeyPressed(Keys.W)) move(UP);



        fireManager.updateFire(pos, stats, map, weapon.getStats());
        regen();
        manaSigil.update();
    }

    public void manaTrigger() {
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
        if (weapon != null) weapon.draw(batch);
        if (armor != null) armor.draw(batch);
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
}
