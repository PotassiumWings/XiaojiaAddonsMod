package com.xiaojia.xiaojiaaddons.utils;

import java.awt.Color;

public class ColorUtils {
    public static final String[] colors = new String[]{
            "&a", "&b", "&c", "&d", "&e", "&f",
            "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9"
    };
    public static final Color[] realColors = new Color[]{
            new Color(0x55FF55),
            new Color(0x55FFFF),
            new Color(0xFF5555),
            new Color(0xFF55FF),
            new Color(0xFFFF55),
            new Color(0xFFFFFF),
            new Color(0x000000),
            new Color(0x0000AA),
            new Color(0x00AA00),
            new Color(0x00AAAA),
            new Color(0xAA0000),
            new Color(0xAA00AA),
            new Color(0xFFAA00),
            new Color(0xAAAAAA),
            new Color(0x555555),
            new Color(0x5555FF)
    };

    public static Color getColorFromCode(String code) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(code)) {
                return realColors[i];
            }
        }
        return null;
    }

    public static Color getColorFromLong(long color) {
        return new Color(
                ((int) ((color >> 16) & 0xFF)) / 255.0F, ((int) ((color >> 8) & 0xFF)) / 255.0F,
                ((int) (color & 0xFF)) / 255.0F, (int) (((color >> 24) & 0xFF)) / 255.0F
        );
    }

    public static Color getColorFromString(String str, Color defaultColor) {
        try {
            return getColorFromLong(Long.parseLong(str, 16));
        } catch (Exception e) {
            System.out.println("/" + str + "/");
            return defaultColor;
        }
    }
}
