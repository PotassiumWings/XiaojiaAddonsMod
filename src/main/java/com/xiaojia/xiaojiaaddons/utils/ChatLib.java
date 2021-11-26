package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatLib {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static String removeFormatting(String text) {
        if (text == null) return null;
        Pattern pattern = Pattern.compile("[\\u00a7&][0-9a-fklmnor]");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("");
    }

    public static void chat(String text) {
        if (text == null) {
            text = "null";
        }
        text = "&9[XiaojiaAddonsMod] > &b" + text;
        text = addColor(text);
        System.out.println(text);
        EntityPlayerSP player = mc.thePlayer;
        if (player != null) {
            IChatComponent component = new ChatComponentText(text);
            player.addChatMessage(component);
        }
    }

    public static String addColor(String text) {
        Pattern pattern = Pattern.compile("((?<!\\\\))&(?![^0-9a-fklmnor]|$)");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("\u00a7");
    }
}
