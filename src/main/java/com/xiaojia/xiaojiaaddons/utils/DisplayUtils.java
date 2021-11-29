package com.xiaojia.xiaojiaaddons.utils;

public class DisplayUtils {
    public static String getHPDisplayFromArmorStandName(String name, String kind) {
        int startIndex = name.indexOf(kind) + kind.length() + 3;
        int endIndex = name.indexOf("❤") - 2;
        return name.substring(startIndex, endIndex);
    }

    public static String hpToString(double hp) {
        String res = "";
        if (hp >= 1000000) {
            double x = hp / 1000000;
            res = String.format("%.2f", x) + "M";
        } else if (hp >= 1000) {
            double x = hp / 1000;
            res = String.format("%.2f", x) + "K";
        } else {
            res = String.format("%.2f", hp);
        }
        return res;
    }
}
