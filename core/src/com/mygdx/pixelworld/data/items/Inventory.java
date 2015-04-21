package com.mygdx.pixelworld.data.items;

import com.badlogic.gdx.utils.Disposable;
import com.mygdx.pixelworld.data.items.armors.Armor;
import com.mygdx.pixelworld.data.items.sigils.ManaSigil;
import com.mygdx.pixelworld.data.items.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory generalizes an Item array, and is capable of doing basic operations with its item and work with other inventories.
 */
public class Inventory implements Disposable {
    private Item[] inv;

    /**
     * @param dimension Dimension of the inventory
     */
    public Inventory(int dimension) {
        if (dimension <= 0) throw new ExceptionInInitializerError();
        inv = new Item[dimension];
        empty();
    }

    /**
     * Swaps items between non-locked inventories
     *
     * @param inv1  First inventory
     * @param slot1 Slot of first inventory
     * @param inv2  Second inventory
     * @param slot2 Slot of second inventory
     */
    public static void swap(Inventory inv1, int slot1, Inventory inv2, int slot2) {
        inv1.set(inv2.replace(inv1.get(slot1), slot2), slot1);
    }

    /**
     * Swaps items between Locked and Free inventory (if possible)
     * @param lockedInv Locked inventory
     * @param lockedSlot Slot of locked inventory
     * @param freeInv Free inventory
     * @param freeSlot Slot of free inventory
     */
    public static void swap(LockedInventory lockedInv, int lockedSlot, Inventory freeInv, int freeSlot) {
        Item invItem = freeInv.get(freeSlot);
        Item eqItem = lockedInv.tryReplace(invItem, lockedSlot);
        if (eqItem == null) return;//Can't equip
        //Equipped! Now swap in inventory
        if (eqItem instanceof EquipItem) if (((EquipItem) eqItem).isEmpty()) freeInv.set(new EmptyItem(), freeSlot);
        else freeInv.set(eqItem, freeSlot);
    }

    /**
     * Tries to equip from free inventory into locked inventory.
     * @param lockedInv Locked inventory
     * @param freeInv Free inventory
     * @param freeSlot Slot of free inventory
     */
    public static void swap(LockedInventory lockedInv, Inventory freeInv, int freeSlot) {
        int lockedSlot = 3;
        Item invItem = freeInv.get(freeSlot);
        if (invItem instanceof Weapon) lockedSlot = 0;
        if (invItem instanceof ManaSigil) lockedSlot = 1;
        if (invItem instanceof Armor) lockedSlot = 2;
        swap(lockedInv, lockedSlot, freeInv, freeSlot);
    }

    /**
     * Clears inventory.
     */
    private void empty() {
        for (int i = 0; i < inv.length; i++) empty(i);
    }

    /**
     * Clear inventory slot inserting new EmptyItem.
     * @param slot Inventory slot
     */
    void empty(int slot) {
        set(new EmptyItem(), slot);
    }

    Item get(int slot) {
        return inv[slot];
    }

    void set(Item item, int slot) {
        inv[slot] = item;
    }

    private boolean isEmpty(int slot) {
        return inv[slot] instanceof EmptyItem;
    }

    public boolean isEmpty() {
        for (int i = 0; i < inv.length; i++) if (!isEmpty(i)) return false;
        return true;
    }

    /**
     * Puts new item and returns old one.
     * @param newItem New item to insert
     * @param slot Slot to insert it
     * @return Old item
     */
    public Item replace(Item newItem, int slot) {
        Item oldItem = get(slot);
        empty(slot);
        set(newItem, slot);
        return oldItem;
    }

    /**
     * Fills inventory with list of items.
     * @param items Items to fill with
     */
    public void fill(List<Item> items) {
        empty();
        for (int i = 0; i < items.size(); i++) {
            set(items.get(i), i);
        }
    }

    /**
     * swaps 2 items of the same inventory
     * @param startSlot First slot
     * @param endSlot Second slot
     */
    public void swap(int startSlot, int endSlot) {
        set(replace(get(startSlot), endSlot), startSlot);
    }

    public Item[] getItems() {
        return inv;
    }

    @Override
    public void dispose() {
        for (Item i : inv) if (i != null) i.dispose();
    }

    /**
     * @return Count of occupied slots.
     */
    public int size() {
        int count = 0;
        for (Item i : inv) if (!(i instanceof EmptyItem)) count++;
        return count;
    }

    /**
     * @return List of non-empty items of this inventory.
     */
    public List<Item> getFilledItems() {
        List<Item> out = new ArrayList<>();
        for (Item i : inv) if (!(i instanceof EmptyItem)) out.add(i);
        return out;
    }
}
