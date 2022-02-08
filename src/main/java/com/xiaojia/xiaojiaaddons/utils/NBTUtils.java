package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NBTUtils {
    private static NBTTagCompound getExtraAttributes(ItemStack itemStack) {
        if (itemStack == null) return null;
        return itemStack.getSubCompound("ExtraAttributes", false);
    }

    private static String getStringFromExtraAttributes(ItemStack itemStack, String property) {
        NBTTagCompound nbtTagCompound = getExtraAttributes(itemStack);
        if (nbtTagCompound == null || !nbtTagCompound.hasKey(property)) return "";
        return nbtTagCompound.getString(property);
    }

    public static int getIntFromExtraAttributes(ItemStack itemStack, String property) {
        NBTTagCompound nbtTagCompound = getExtraAttributes(itemStack);
        if (nbtTagCompound == null || !nbtTagCompound.hasKey(property)) return 0;
        return nbtTagCompound.getInteger(property);
    }

    public static List<String> getLore(ItemStack itemStack) {
        if (itemStack == null) return new ArrayList<>();
        return itemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
    }

    public static String getUUID(ItemStack itemStack) {
        return getStringFromExtraAttributes(itemStack, "uuid");
    }

    public static boolean isItemRecombobulated(ItemStack itemStack) {
        return getIntFromExtraAttributes(itemStack, "rarity_upgrades") == 1;
    }

    public static boolean isItemFullQuality(ItemStack itemStack) {
        return getIntFromExtraAttributes(itemStack, "baseStatBoostPercentage") == 50;
    }

    public static boolean isItemStarred(ItemStack itemStack) {
        return getIntFromExtraAttributes(itemStack, "dungeon_item_level") != 0;
    }

    public static String getSkyBlockID(ItemStack itemStack) {
        return getStringFromExtraAttributes(itemStack, "id");
    }

    public static ArrayList<String> getBookNameAndLevel(ItemStack itemStack) {
        ArrayList<String> res = new ArrayList<>();
        try {
            String bookName = ChatLib.removeFormatting(getLore(itemStack).get(1));
            // "Feather", "Falling", "VI"
            ArrayList<String> bookNameSplit = new ArrayList<>(Arrays.asList(bookName.split(" ")));
            // "VI" -> 6
            String levelString = bookNameSplit.get(bookNameSplit.size() - 1);
            String book = bookName.substring(0, bookName.length() - levelString.length() - 1);
            res.add(book);
            res.add(levelString);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
