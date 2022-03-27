package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;


import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;

import java.util.ArrayList;

public class DojoUtils {
    public static EnumDojoTask getTask() {
        if (!SkyblockUtils.isInDojo()) return null;

        ArrayList<String> lines = ScoreBoard.getLines();
        for (String line : lines) {
            StringBuilder removeSkippingChar = new StringBuilder();
            line = ChatLib.removeFormatting(line);
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == ':' || c == ' ')
                    removeSkippingChar.append(c);
            }
            line = removeSkippingChar.toString();
            if (line.startsWith("Challenge: ")) {
                return EnumDojoTask.valueOf(line.substring(11).toUpperCase());
            }
        }
        return null;
    }
}
