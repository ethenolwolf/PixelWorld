package com.mygdx.pixelworld.data.utilities;


import java.util.EnumMap;
import java.util.Map;

public class PlayerStats {

    Class type;
    private Map<StatType, Float> stats = new EnumMap<StatType, Float>(StatType.class);

    public PlayerStats(Class type) {
        this(Constants.initStats.get(type));
        this.type = type;
    }

    public PlayerStats(float health, float mana, int speed, int dexterity, int wisdom, int vitality, int attack, int defense) {
        stats.put(StatType.HEALTH, health);
        stats.put(StatType.MANA, mana);
        stats.put(StatType.SPD, (float) speed);
        stats.put(StatType.DEX, (float) dexterity);
        stats.put(StatType.WIS, (float) wisdom);
        stats.put(StatType.VIT, (float) vitality);
        stats.put(StatType.ATK, (float) attack);
        stats.put(StatType.DEF, (float) defense);
    }

    public PlayerStats(PlayerStats ps) {
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

    public float getInit(StatType health) {
        return Constants.initStats.get(type).get(health);
    }

    public void setAsInit(StatType statType) {
        setStat(statType, getInit(statType));
    }
}
