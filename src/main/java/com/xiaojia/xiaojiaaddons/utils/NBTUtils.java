package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class NBTUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static List<String> getLore(ItemStack itemStack) {
        return itemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
    }
}
