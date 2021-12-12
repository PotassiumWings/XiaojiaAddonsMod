package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

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

    public final ArrayList<ItemStack> getItemStacks() {
        ArrayList<ItemStack> res = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            res.add(getItemInSlot(i));
        }
        return res;
    }

    public List<Slot> getSlots() {
        return container.inventorySlots;
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

    public void click(int slot) {
        click(slot, false, "LEFT");
    }

    public void click(int slot, boolean shift, String buttonString) {
        int windowId = getWindowId();
        int button;
        switch (buttonString) {
            case "MIDDLE":
                button = 2;
                break;
            case "RIGHT":
                button = 1;
                break;
            default:
                button = 0;
                break;
        }
        int mode = 0;
        if (button == 1) mode = 3;
        else if (shift) mode = 1;
        mc.playerController.windowClick(
                windowId, slot, button, mode, getPlayer()
        );
        if (XiaojiaAddons.isDebug()) ChatLib.chat(String.format("%d %d %d %d", windowId, slot, button, mode));
    }
}
