package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Colors;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class FontUtils {
    public static void drawString(String string, int x, int y, boolean shadow) {
        for (String line : string.split("\n")) {
            mc.fontRendererObj.drawString(line, x, y, Colors.WHITE.getRGB(), shadow);
            y += mc.fontRendererObj.FONT_HEIGHT + 1;
        }
    }
}
