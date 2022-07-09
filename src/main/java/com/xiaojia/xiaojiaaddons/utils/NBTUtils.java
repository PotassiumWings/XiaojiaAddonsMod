package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NBTUtils {
    private static NBTTagCompound getExtraAttributes(ItemStack itemStack) {
        if (itemStack == null) return null;
        return itemStack.getSubCompound("ExtraAttributes", false);
    }

    public static Pair<String, Integer> getFirstEnchantment(ItemStack itemStack) {
        NBTTagCompound extra = getExtraAttributes(itemStack);
        NBTTagCompound enchantments = extra.getCompoundTag("enchantments");
        Set<String> enc = enchantments.getKeySet();
        for (String s : enc) return new Pair<>(s, enchantments.getInteger(s));
        return null;
    }

    public static Pair<String, Integer> getFirstRune(ItemStack itemStack) {
        NBTTagCompound extra = getExtraAttributes(itemStack);
        NBTTagCompound runes = extra.getCompoundTag("runes");
        Set<String> runeNames = runes.getKeySet();
        for (String s : runeNames) return new Pair<>(s, runes.getInteger(s));
        return null;
    }

    public static String getStringFromExtraAttributes(ItemStack itemStack, String property) {
        NBTTagCompound nbtTagCompound = getExtraAttributes(itemStack);
        if (nbtTagCompound == null || !nbtTagCompound.hasKey(property)) return "";
        return nbtTagCompound.getString(property);
    }

    public static int getIntFromExtraAttributes(ItemStack itemStack, String property) {
        NBTTagCompound nbtTagCompound = getExtraAttributes(itemStack);
        if (nbtTagCompound == null || !nbtTagCompound.hasKey(property)) return 0;
        return nbtTagCompound.getInteger(property);
    }

    public static boolean getBooleanFromExtraAttributes(ItemStack itemStack, String property) {
        NBTTagCompound nbtTagCompound = getExtraAttributes(itemStack);
        if (nbtTagCompound == null || !nbtTagCompound.hasKey(property)) return false;
        return nbtTagCompound.getBoolean(property);
    }

    public static NBTTagCompound getCompoundFromExtraAttributes(ItemStack itemStack, String property) {
        NBTTagCompound nbtTagCompound = getExtraAttributes(itemStack);
        if (nbtTagCompound == null || !nbtTagCompound.hasKey(property)) return null;
        return nbtTagCompound.getCompoundTag(property);
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
            bookName = bookName.replaceAll(" ✖", "");
            return getBookNameAndLevelFromString(bookName);
        } catch (Exception e) {
            return res;
        }
    }

    public static ArrayList<String> getBookNameAndLevelFromString(String bookName) {
        ArrayList<String> res = new ArrayList<>();
        try {
            // "Feather", "Falling", "VI"
            ArrayList<String> bookNameSplit = new ArrayList<>(Arrays.asList(bookName.split(" ")));
            // "VI" -> 6
            String levelString = bookNameSplit.get(bookNameSplit.size() - 1);
            String book = bookName.substring(0, bookName.length() - levelString.length() - 1);
            int level = StringUtils.getNumberFromRoman(levelString);
            levelString = level + "";
            res.add(book);
            res.add(levelString);
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    public static boolean isBookUltimate(ItemStack itemStack) {
        try {
            return isBookUltimateFromName(getLore(itemStack).get(1));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBookUltimateFromName(String name) {
        return name.contains("\u00a79\u00a7d\u00a7l");
    }
}
