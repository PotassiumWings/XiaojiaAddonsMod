package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Inventory {
    public final Container container;

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
        try {
            return container.getSlot(slot).getStack();
        } catch (Exception e) {
            return null;
        }
    }

    public final String getName() {
        if (container instanceof ContainerChest) {
            return ((ContainerChest) container).getLowerChestInventory().getName();
        }
        if (container instanceof ContainerMerchant) {
            return ((ContainerMerchant) container).getMerchantInventory().getName();
        }
        return "container";
    }

    public void click(int slot, boolean shift, String buttonString, int incrementWindowId) {
        int windowId = getWindowId() + incrementWindowId;
        int button;
        int mode = 0;
        switch (buttonString) {
            case "MIDDLE":
                button = 2;
                if (shift) mode = 1;
                else mode = 3;
                break;
            case "RIGHT":
                button = 1;
                mode = 3;
                break;
            case "SWAP":
                button = ControlUtils.getHeldItemIndex();
                mode = 2;
                break;
            default:
                button = 0;
                if (shift) mode = 1;
                break;
        }
        mc.playerController.windowClick(
                windowId, slot, button, mode, getPlayer()
        );
        ChatLib.debug(String.format("%d %d %d %d", windowId, slot, button, mode));
    }

    public void click(int slot) {
        click(slot, false, "LEFT");
    }

    public void click(int slot, boolean shift, String buttonString) {
        click(slot, shift, buttonString, 0);
    }
}
