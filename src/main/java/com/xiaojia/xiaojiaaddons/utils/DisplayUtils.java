package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.item.ItemStack;

public class DisplayUtils {
    public static String getHPDisplayFromArmorStandName(String name, String kind) {
        int startIndex = name.indexOf(kind) + kind.length() + 3;
        int endIndex = name.indexOf("❤") - 2;
        return name.substring(startIndex, endIndex);
    }

    public static String hpToString(double hp) {
        return hpToString(hp, false);
    }

    public static String hpToString(double hp, boolean toInt) {
        String res = "";
        if (hp >= 1000000) {
            double x = hp / 1000000;
            if (toInt) res = String.format("%.0f", x) + "M";
            else res = String.format("%.2f", x) + "M";
        } else if (hp >= 1000) {
            double x = hp / 1000;
            if (toInt) res = String.format("%.0f", x) + "K";
            else res = String.format("%.2f", x) + "K";
        } else {
            if (toInt) res = String.format("%.0f", hp);
            else res = String.format("%.2f", hp);
        }
        return res;
    }

    public static String getDisplayString(ItemStack itemStack) {
        String stackSizeSuf = " x" + itemStack.stackSize;
        String name = ChatLib.removeFormatting(itemStack.getDisplayName());
        if (name.endsWith(stackSizeSuf))
            name = name.substring(0, name.length() - stackSizeSuf.length());
        return name;
    }
}
