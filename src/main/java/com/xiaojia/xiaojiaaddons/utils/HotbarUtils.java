package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.ControlUtils.checkHotbarItem;

public class HotbarUtils {
    public static int terminatorSlot = -1;
    public static int emeraldBladeSlot = -1;
    public static int soulwhipSlot = -1;
    public static int plasmaSlot = -1;
    public static int healingwandSlot = -1;
    public static int gyroSlot = -1;
    public static int gloomlockSlot = -1;
    public static int zombieswordSlot = -1;

    public static boolean checkSoulwhip() {
        return checkHotbarItem(soulwhipSlot, "Soul Whip");
    }

    public static boolean checkEmeraldBlade() {
        return checkHotbarItem(emeraldBladeSlot, "Emerald Blade") || checkHotbarItem(emeraldBladeSlot, "Giant's Sword");
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || inventory.getSize() != 45) return;
        List<ItemStack> items = inventory.getItemStacks().subList(36, 45);
        for (int i = 0; i < 9; i++) {
            ItemStack item = items.get(i);
            if (item == null || !item.hasDisplayName()) continue;
            String name = item.getDisplayName();
            if (name.contains("Terminator")) terminatorSlot = i;
            else if (name.contains("Giant's Sword") || name.contains("Emerald Blade")) emeraldBladeSlot = i;
            else if (name.contains("Soul Whip")) soulwhipSlot = i;
            else if (name.contains("Plasmaflux")) plasmaSlot = i;
            else if (name.contains("Wand of")) healingwandSlot = i;
            else if (name.contains("Gyrokinetic Wand")) gyroSlot = i;
            else if (name.contains("Zombie Sword")) zombieswordSlot = i;
            else if (name.contains("Gloomlock Grimoire")) gloomlockSlot = i;
        }
    }
}
