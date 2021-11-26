package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.item.ItemStack;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NBTUtils {

    public static List<String> getLore(ItemStack itemStack) {
        return itemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
    }
}
