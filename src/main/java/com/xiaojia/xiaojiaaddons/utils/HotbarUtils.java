package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    public static int aotsSlot = -1;

    // auto foraging
    public static int enchantedBoneMealSlot = -1;
    public static int boneMealSlot = -1;
    public static int treecapitatorSlot = -1;
    public static int rodSlot = -1;
    public static int saplingSlot = -1;

    public static int aotvSlot = -1;
    public static int shortBowSlot = -1;
    public static int dirtWandSlot = -1;

    public static boolean checkSoulwhip() {
        return checkHotbarItem(soulwhipSlot, "Soul Whip");
    }

    public static boolean checkEmeraldBlade() {
        return checkHotbarItem(emeraldBladeSlot, "Emerald Blade") || checkHotbarItem(emeraldBladeSlot, "Giant's Sword");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(TickEndEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || inventory.getSize() != 45) return;
        List<ItemStack> items = inventory.getItemStacks().subList(36, 45);
        boolean terminator = false, emerald = false, aots = false, soulWhip = false, plasma = false,
                healingWand = false, gyro = false, zombieSword = false, gloomlock = false,
                ebm = false, bm = false, treecap = false, sapling = false, rod = false,
                aotv = false, shortBow = false, dirt = false;
        for (int i = 0; i < 9; i++) {
            ItemStack item = items.get(i);
            if (item == null) continue;
            String name = item.hasDisplayName() ? item.getDisplayName() : item.getUnlocalizedName();
            String registryName = item.getItem().getRegistryName();
            if (name == null || name.toLowerCase().contains("air")) continue;
            if (name.contains("Terminator")) {
                terminatorSlot = i;
                terminator = true;
            } else if (name.contains("Giant's Sword") || name.contains("Emerald Blade")) {
                emeraldBladeSlot = i;
                emerald = true;
            } else if (name.contains("Axe of the Shredded")) {
                aotsSlot = i;
                aots = true;
            } else if (name.contains("Soul Whip")) {
                soulwhipSlot = i;
                soulWhip = true;
            } else if (name.contains("Plasmaflux")) {
                plasmaSlot = i;
                plasma = true;
            } else if (name.contains("Wand of")) {
                healingwandSlot = i;
                healingWand = true;
            } else if (name.contains("Gyrokinetic Wand")) {
                gyroSlot = i;
                gyro = true;
            } else if (name.contains("Zombie Sword")) {
                zombieswordSlot = i;
                zombieSword = true;
            } else if (name.contains("Gloomlock Grimoire")) {
                gloomlockSlot = i;
                gloomlock = true;
            }
            // auto foraging
            else if (name.contains("Enchanted Bone Meal")) {
                enchantedBoneMealSlot = i;
                ebm = true;
            } else if (name.contains("Bone Meal")) {
                boneMealSlot = i;
                bm = true;
            } else if (name.contains("Treecapitator")) {
                treecapitatorSlot = i;
                treecap = true;
            } else if (name.contains("Sapling")) {
                saplingSlot = i;
                sapling = true;
            } else if (registryName.toLowerCase().contains("rod")) {
                rodSlot = i;
                rod = true;
            } else if (name.contains("Aspect of the Void")) {
                aotvSlot = i;
                aotv = true;
            } else if (name.contains("Shortbow")) {
                shortBowSlot = i;
                shortBow = true;
            } else if (name.contains("InfiniDirt")) {
                dirtWandSlot = i;
                dirt = true;
            }
        }
        if (!terminator) terminatorSlot = -1;
        if (!emerald) emeraldBladeSlot = -1;
        if (!aots) aotsSlot = -1;
        if (!soulWhip) soulwhipSlot = -1;
        if (!plasma) plasmaSlot = -1;
        if (!healingWand) healingwandSlot = -1;
        if (!gyro) gyroSlot = -1;
        if (!zombieSword) zombieswordSlot = -1;
        if (!gloomlock) gloomlockSlot = -1;
        if (!ebm) enchantedBoneMealSlot = -1;
        if (!bm) boneMealSlot = -1;
        if (!treecap) treecapitatorSlot = -1;
        if (!sapling) saplingSlot = -1;
        if (!rod) rodSlot = -1;
        if (!aotv) aotvSlot = -1;
        if (!shortBow) shortBowSlot = -1;
        if (!dirt) dirtWandSlot = -1;
    }
}
