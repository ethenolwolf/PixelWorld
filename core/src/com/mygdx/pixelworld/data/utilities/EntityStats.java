package com.mygdx.pixelworld.data.utilities;


import com.mygdx.pixelworld.data.draw.DrawHitValue;
import com.mygdx.pixelworld.data.entities.Entity;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.enemies.Enemy;

import java.util.EnumMap;
import java.util.Map;

public class EntityStats {

    private final Map<StatType, Float> stats = new EnumMap<StatType, Float>(StatType.class);
    private Class type;
    private GameClasses gameClass;
    private boolean alive = true;
    private Map<StatType, Float> maxStats;
    private boolean visible = true;

    public EntityStats(GameClasses type) {
        this(Config.getStats(true, type.toString()));
        maxStats = new EnumMap<StatType, Float>(stats);
        this.gameClass = type;
        this.type = null;
    }

    public EntityStats(float health, float mana, int speed, int dexterity, int wisdom, int vitality, int attack, int defense) {
        stats.put(StatType.HEALTH, health);
        stats.put(StatType.MANA, mana);
        stats.put(StatType.SPD, (float) speed);
        stats.put(StatType.DEX, (float) dexterity);
        stats.put(StatType.WIS, (float) wisdom);
        stats.put(StatType.VIT, (float) vitality);
        stats.put(StatType.ATK, (float) attack);
        stats.put(StatType.DEF, (float) defense);
        maxStats = new EnumMap<StatType, Float>(stats);
        stats.get(StatType.HEALTH);
        maxStats.get(StatType.HEALTH);
        gameClass = null;
        type = null;
    }

    private EntityStats(EntityStats ps) {
        for (StatType st : StatType.values()) stats.put(st, ps.get(st));
        gameClass = null;
        type = null;
    }

    public EntityStats(Class<? extends Enemy> enemyClass) {
        this(Config.getStats(false, enemyClass.getSimpleName()));
        maxStats = new EnumMap<StatType, Float>(stats);
        this.type = enemyClass;
        this.gameClass = null;
    }

    private void setStat(StatType statType, float value) {
        stats.put(statType, value);
    }

    public void addStat(StatType statType, float value) {
        setStat(statType, value + get(statType));
    }

    public float get(StatType statType) {
        return stats.get(statType);
    }

    public float getInit(StatType statType) {
        if (gameClass == null) return Config.getStats(false, type.getSimpleName()).get(statType);
        else return Config.getStats(true, gameClass.toString()).get(statType);
    }

    public float getMax(StatType statType) {
        return maxStats.get(statType);
    }

    public void setAsInit(StatType statType) {
        setStat(statType, getInit(statType));
    }

    public void getHit(Entity e, int damage) {
        if (damage > get(StatType.DEF)) addStat(StatType.HEALTH, get(StatType.DEF) - damage);
        if (get(StatType.HEALTH) <= 0) alive = false;
        DrawHitValue.add(e, damage);
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setIsVisible(boolean isVisible) {
        this.visible = isVisible;
    }
}
