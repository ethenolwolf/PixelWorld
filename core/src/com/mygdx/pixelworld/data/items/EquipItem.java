package com.mygdx.pixelworld.data.items;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;

/**
 * Item that can be equipped
 */
public interface EquipItem {
    boolean isEmpty();
    boolean isSuitable(GameClasses gameClass);
}
