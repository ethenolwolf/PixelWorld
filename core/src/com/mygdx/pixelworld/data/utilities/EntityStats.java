package com.mygdx.pixelworld.data.utilities;


import java.util.EnumMap;
import java.util.Map;

import static com.mygdx.pixelworld.data.utilities.Constants.initStats;

public class EntityStats {

    Class type;
    boolean alive = true;
    private Map<StatType, Float> stats = new EnumMap<StatType, Float>(StatType.class);
    private Map<StatType, Float> maxStats;

    public EntityStats(Class type) {
        this(initStats.get(type));
        maxStats = new EnumMap<StatType, Float>(stats);
        this.type = type;
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
    }

    public EntityStats(EntityStats ps) {
        for (StatType st : StatType.values()) stats.put(st, ps.get(st));
    }

    public void setStat(StatType statType, float value) {
        stats.put(statType, value);
    }

    public void addStat(StatType statType, float value) {
        setStat(statType, value + get(statType));
    }

    public float get(StatType statType) {
        return stats.get(statType);
    }

    public float getInit(StatType statType) {
        return initStats.get(type).get(statType);
    }

    public float getMax(StatType statType) {
        return maxStats.get(statType);
    }

    public void setAsInit(StatType statType) {
        setStat(statType, getInit(statType));
    }

    public void getHit(int damage) {
        if (damage > get(StatType.DEF)) addStat(StatType.HEALTH, get(StatType.DEF) - damage);
        if (get(StatType.HEALTH) <= 0) alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}
