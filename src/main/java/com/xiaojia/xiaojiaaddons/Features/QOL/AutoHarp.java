package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class AutoHarp {
    private ArrayList<Item> lastInventory = new ArrayList<>();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        lastInventory.clear();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled || !Configs.AutoHarp) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().startsWith("Harp -")) return;

        ArrayList<Item> thisInventory = new ArrayList<>();
        for (Slot slot : inventory.getSlots()) {
            if (slot.getStack() != null && slot.getSlotIndex() < 45)
                thisInventory.add(slot.getStack().getItem());
        }
        if (!lastInventory.toString().equals(thisInventory.toString()))
            for (Slot slot : inventory.getSlots()) {
                if (slot.getStack() != null && slot.getStack().getItem() instanceof ItemBlock &&
                        ((ItemBlock) slot.getStack().getItem()).getBlock() == Blocks.quartz_block) {
                    inventory.click(slot.slotNumber, false, "MIDDLE");
                    break;
                }
            }
        lastInventory = thisInventory;
    }
}
