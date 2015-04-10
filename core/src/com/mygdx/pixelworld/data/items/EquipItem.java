package com.mygdx.pixelworld.data.items;
import com.mygdx.pixelworld.data.entities.characters.GameClasses;

public interface EquipItem {
    boolean isEmpty();
    boolean isSuitable(GameClasses gameClass);
}
