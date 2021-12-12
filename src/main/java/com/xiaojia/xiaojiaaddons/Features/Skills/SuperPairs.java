package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class SuperPairs {
    static ItemStack[] experimentTableSlots = new ItemStack[54];

    @SubscribeEvent()
    public void onTooltip(ItemTooltipEvent event) {
        if (!Checker.enabled) return;
        if (event.toolTip == null) return;
        ItemStack item = event.itemStack;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;
        String chestName = inventory.getName();
        if (Configs.SuperpairsSolver && chestName.contains("Superpairs (")) {
            if (Item.getIdFromItem(item.getItem()) != 95)
                return;
            if (item.getDisplayName().contains("Click any button") ||
                    item.getDisplayName().contains("Click a second button") ||
                    item.getDisplayName().contains("Next button is instantly rewarded") ||
                    item.getDisplayName().contains("Stained Glass")) {
                Slot slot = ((GuiChest) mc.currentScreen).getSlotUnderMouse();
                ItemStack itemStack = experimentTableSlots[slot.getSlotIndex()];
                if (itemStack == null) return;
                String itemName = itemStack.getDisplayName();
                if (event.toolTip.stream().anyMatch(
                        x -> StringUtils.stripControlCodes(x).equals(StringUtils.stripControlCodes(itemName))
                )) return;
                event.toolTip.removeIf(x -> {
                    x = StringUtils.stripControlCodes(x);
                    return x.equals("minecraft:stained_glass") || x.startsWith("NBT: ");
                });
                event.toolTip.add(itemName);
                event.toolTip.add(itemStack.getItem().getRegistryName());
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !Configs.SuperpairsSolver) return;
        String invName = inventory.getName();
        List<Slot> invSlots = ((GuiChest) mc.currentScreen).inventorySlots.inventorySlots;
        if (invName.startsWith("Superpairs ("))
            for (int i = 0; i < 53; i++) {
                ItemStack itemStack = invSlots.get(i).getStack();
                if (itemStack == null) continue;
                String itemName = itemStack.getDisplayName();
                // glass, glass pane
                if (Item.getIdFromItem(itemStack.getItem()) != 95 && Item.getIdFromItem(itemStack.getItem()) != 160 &&
                        !itemName.contains("Instant Find") && !itemName.contains("Gained +")) {
                    if (itemName.contains("Enchanted Book")) itemName = itemStack.getTooltip(getPlayer(), false).get(3);
                    if (itemStack.stackSize > 1) itemName = itemStack.stackSize + " " + itemName;
                    if (experimentTableSlots[i] == null) experimentTableSlots[i] = itemStack.copy().setStackDisplayName(itemName);
                }
            }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled ||inventory == null || !Configs.SuperpairsSolver) return;
        String invName = inventory.getName();
        List<Slot> invSlots = inventory.getSlots();
        if (invName.contains("Superpairs (")) {
            HashMap<String, HashSet<Integer>> matches = new HashMap<>();
            for (int i = 0; i < 53; i++) {
                ItemStack itemStack = experimentTableSlots[i];
                if (itemStack != null) {
                    String itemName = itemStack.getDisplayName();
                    String keyName = itemName + itemStack.getUnlocalizedName();
                    matches.computeIfAbsent(keyName, k -> new HashSet<>());
                    matches.get(keyName).add(i);
                }
            }
            Color[] colors = {
                    new Color(255, 0, 0, 100), new Color(0, 0, 255, 100),
                    new Color(100, 179, 113, 100), new Color(255, 114, 255, 100),
                    new Color(255, 199, 87, 100), new Color(119, 105, 198, 100),
                    new Color(135, 199, 112, 100), new Color(240, 37, 240, 100),
                    new Color(178, 132, 190, 100), new Color(63, 135, 163, 100),
                    new Color(146, 74, 10, 100), new Color(255, 255, 255, 100),
                    new Color(217, 252, 140, 100), new Color(255, 82, 82, 100)
            };
            Iterator<Color> colorIterator = Arrays.stream(colors).iterator();
            matches.forEach((itemName, slotSet) -> {
                if (slotSet.size() < 2) return;
                ArrayList<Slot> slots = new ArrayList<>();
                slotSet.forEach(index -> slots.add(invSlots.get(index)));
                Color color = colorIterator.next();
                slots.forEach(slot -> {
                    GuiUtils.drawOnSlot(invSlots.size(), slot.xDisplayPosition, slot.yDisplayPosition, color.getRGB());
                });
            });
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        experimentTableSlots = new ItemStack[54];
    }
}
