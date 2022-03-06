package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.GuiContainerEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.List;

public class AutoLeap {
    private final String[] names = new String[10];
    private long lastClick = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().contains("Spirit Leap")) return;
        // TP to mage
        if (Configs.AutoTpToMage && TimeUtils.curTime() - lastClick > 1000 && Dungeon.bloodOpen < Dungeon.runStarted) {
            for (int i = 9; i < 18; i++) {
                ItemStack itemStack = ControlUtils.getItemStackInSlot(i, false);
                if (itemStack == null || Item.getIdFromItem(itemStack.getItem()) == 160) continue;
                String name = ChatLib.removeFormatting(itemStack.getDisplayName());
                if (Dungeon.isPlayerMage(name)) {
                    inventory.click(i);
                    lastClick = TimeUtils.curTime();
                }
            }
        }
        // Show Name
        if (Configs.SpiritLeapName) {
            for (int i = 9; i < 18; i++) {
                ItemStack itemStack = ControlUtils.getItemStackInSlot(i, false);
                if (itemStack == null || Item.getIdFromItem(itemStack.getItem()) == 160) {
                    names[i - 9] = "";
                    continue;
                }
                String displayName = itemStack.getDisplayName();
                String name = ChatLib.removeFormatting(displayName);
                if (name.length() > 5) name = name.substring(0, 5);
                names[i - 9] = displayName.substring(0, displayName.indexOf(name) + name.length());
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled || !SkyblockUtils.isInDungeon() || inventory == null || !Configs.SpiritLeapName) return;
        String invName = inventory.getName();
        if (!invName.contains("Spirit Leap")) return;
        List<Slot> invSlots = inventory.getSlots();
        for (int i = 0; i < 9; i++) {
            if (names[i] == null || names[i].equals("")) continue;
            int slotIndex = i % 2 == 0 ? i : i + 18;  // slot to draw string
            Slot slot = invSlots.get(slotIndex);
            GuiUtils.drawStringOnSlot(names[i], invSlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, new Color(63, 206, 63, 255));
        }
    }

    @SubscribeEvent
    public void onSlotDraw(GuiContainerEvent.DrawSlotEvent.Pre event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled || !SkyblockUtils.isInDungeon() || inventory == null || !Configs.SpiritLeapName) return;
        String invName = inventory.getName();
        if (!invName.contains("Spirit Leap")) return;
        try {
            int index = event.slot.slotNumber;
            if (index >= inventory.getSize() - 36) return;
            if (!(index >= 9 && index < 18 && names[index - 9] != null && !names[index - 9].equals(""))) {
                event.setCanceled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
