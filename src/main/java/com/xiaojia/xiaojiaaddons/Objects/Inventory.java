package com.xiaojia.xiaojiaaddons.Objects;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final Container container;

    public Inventory(Container container) {
        this.container = container;
    }

    public int getWindowId() {
        return container.windowId;
    }

    public int getSize() {
        return container.inventoryItemStacks.size();
    }

    public final List<ItemStack> getItemStacks() {
        ArrayList<ItemStack> res = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            res.add(getItemInSlot(i));
        }
        return res;
    }

    public final ItemStack getItemInSlot(int slot) {
        if (getSize() <= slot) return null;
        return container.getSlot(slot).getStack();
    }

    public final String getName() {
        if (container instanceof ContainerChest) {
            return ((ContainerChest) container).getLowerChestInventory().getName();
        }
        return "container";
    }
}
