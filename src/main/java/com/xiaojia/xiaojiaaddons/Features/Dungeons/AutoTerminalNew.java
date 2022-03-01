package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class AutoTerminalNew {
    private ArrayList<Item> lastInventory = new ArrayList<>();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        lastInventory.clear();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled || !Configs.AutoTerminal) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().startsWith("Click the button on time!")) return;
        ArrayList<Item> thisInventory = new ArrayList<>();
        for (Slot slot : inventory.getSlots()) {
            if (slot.getStack() != null && slot.getSlotIndex() < 45)
                thisInventory.add(slot.getStack().getItem());
        }
        if (!lastInventory.toString().equals(thisInventory.toString())) {
            int purpleIndex = 0;
            for (int i = 1; i <= 5; i++) {
                ItemStack item = inventory.getItemInSlot(i);
                if (item == null) continue;
                int dmg = item.getItemDamage();
                if (dmg == 2 || dmg == 10) purpleIndex = i;
            }
            int toClickRow = 0;
            for (int j = 1; j <= 4; j++) {
                int index = j * 9 + purpleIndex;
                ItemStack item = inventory.getItemInSlot(index);
                if (item == null) continue;
                int dmg = item.getItemDamage();
                if (dmg == 5) {
                    toClickRow = j;
                }
            }
            if (toClickRow != 0) {
                inventory.click(toClickRow * 9 + 7, false, "MIDDLE");
            }
            lastInventory = thisInventory;
        }
    }

}
