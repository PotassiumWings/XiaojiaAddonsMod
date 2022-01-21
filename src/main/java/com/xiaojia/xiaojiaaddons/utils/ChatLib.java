package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ChatLib {

    public static String removeFormatting(String text) {
        if (text == null) return null;
        Pattern pattern = Pattern.compile("[\\u00a7&][0-9a-zA-Z]");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("");
    }

    public static void chat(String chatMessage) {
        if (!Configs.ShowXJAMessage) {
            System.err.println("Chat: " + chatMessage);
            return;
        }
        if (chatMessage == null) chatMessage = "null";
        String[] texts = chatMessage.split("\n");
        for (String text : texts) {
            text = "&9[XJA] > &b" + text;
            text = addColor(text);
            System.out.println(text);
            EntityPlayerSP player = getPlayer();
            if (player != null) {
                IChatComponent component = new ChatComponentText(text);
                player.addChatMessage(component);
            }
        }
    }

    // type=0: chat
    // type=1: yikes
    public static void xjchat(int type, String name, String chatMessage) {
        if (chatMessage == null) chatMessage = "null";
        String[] texts = chatMessage.split("\n");
        for (String text : texts) {
//            if (type == 2) text = " ☠ &7" + text;

            text = "&bXJC > &r&8" + name + "&r&f: &r" + text;
            text = addColor(text);
            System.out.println(text);
            EntityPlayerSP player = getPlayer();
            if (player != null) {
                IChatComponent component = new ChatComponentText(text);
                player.addChatMessage(component);
            }
        }
    }

    public static void debug(String message) {
        if (XiaojiaAddons.isDebug()) chat(message);
    }

    public static void say(String message) {
        if (message == null) message = "null";
        EntityPlayerSP player = getPlayer();
        String[] texts = message.split("\n");
        for (String text : texts) {
            if (player != null) {
                player.sendChatMessage(text);
            }
        }
    }

    public static String addColor(String text) {
        if (text == null) return "";
        Pattern pattern = Pattern.compile("((?<!\\\\))&(?![^0-9a-fklmnor]|$)");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("\u00a7");
    }
}
