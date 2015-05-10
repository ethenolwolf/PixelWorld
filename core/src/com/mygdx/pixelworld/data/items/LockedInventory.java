package com.mygdx.pixelworld.data.items;

import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.armors.Armor;
import com.mygdx.pixelworld.data.items.armors.ArmorType;
import com.mygdx.pixelworld.data.items.sigils.ManaSigil;
import com.mygdx.pixelworld.data.items.weapons.Weapon;
import com.mygdx.pixelworld.data.items.weapons.WeaponType;

/**
 * Inventory for player where every slot can be only of a certain kind.
 */
public class LockedInventory extends Inventory {

    public LockedInventory(Player player) {
        super(4);
        set(new Weapon(WeaponType.SWORD, 1), 0);
        set(ManaSigil.getInitial(player), 1);
        set(new Armor(ArmorType.LEATHER, 1), 2);
    }

    public Weapon getWeapon() {
        return (Weapon) get(0);
    }

    public ManaSigil getManaSigil() {
        return (ManaSigil) get(1);
    }

    public Armor getArmor() {
        return (Armor) get(2);
    }

    @Override
    void empty(int slot) {
        switch (slot) {
            case 0:
                set(new Weapon(), 0);
                break;
            case 1:
                set(new ManaSigil(), 1);
                break;
            case 2:
                set(new Armor(), 2);
                break;
            case 3:
                break;
        }
    }

}
