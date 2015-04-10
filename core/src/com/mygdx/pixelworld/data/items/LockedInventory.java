package com.mygdx.pixelworld.data.items;

import com.mygdx.pixelworld.data.entities.characters.GameClasses;
import com.mygdx.pixelworld.data.entities.characters.Player;
import com.mygdx.pixelworld.data.items.armors.Armor;
import com.mygdx.pixelworld.data.items.sigils.ManaSigil;
import com.mygdx.pixelworld.data.items.weapons.Weapon;

public class LockedInventory extends Inventory {

    private final GameClasses playerClass;

    public LockedInventory(Player player) {
        super(4);
        playerClass = player.getGameClass();
        set(new Weapon(playerClass), 0);
        set(ManaSigil.getInitial(player), 1);
        set(new Armor(playerClass), 2);
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

    public Item tryReplace(Item item, int slot) {
        if (item instanceof EmptyItem) {
            Item out = get(slot);
            empty(slot);

            switch (slot) {
                case 0:
                    Weapon w = (Weapon) out;
                    if (w.isEmpty()) return new EmptyItem();
                    break;
                case 1:
                    ManaSigil m = (ManaSigil) out;
                    if (m.isEmpty()) return new EmptyItem();
                    break;
                case 2:
                    Armor a = (Armor) out;
                    if (a.isEmpty()) return new EmptyItem();
                    break;
            }

            return out;
        }

        if (!(item instanceof EquipItem)) return null;
        if (!(((EquipItem) item).isSuitable(playerClass))) return null;
        if (item instanceof Weapon) return replace(item, 0);
        if (item instanceof ManaSigil) return replace(item, 1);
        if (item instanceof Armor) return replace(item, 2);
        return null;
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

    public boolean isCompatible(Inventory inventory, int slot, int equipSlot) {
        Item item = inventory.get(slot);
        if (item instanceof EmptyItem) return true;
        switch (equipSlot) {
            case 0:
                if (item instanceof Weapon) return true;
                break;
            case 1:
                if (item instanceof ManaSigil) return true;
                break;
            case 2:
                if (item instanceof Armor) return true;
                break;
            case 3:
                break;
        }
        return false;
    }
}
