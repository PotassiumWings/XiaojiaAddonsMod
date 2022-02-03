package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ChatLib {
    public static boolean toggleOff = false;

    public static void toggle() {
        toggleOff = !toggleOff;
        ChatLib.chat((toggleOff ? "&cDisabled" : "&aEnabled") + "&r&b xj chat!");
    }

    public static String removeFormatting(String text) {
        if (text == null) return null;
        Pattern pattern = Pattern.compile("[\\u00a7&][0-9a-zA-Z]");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("");
    }

    public static void chat(String chatMessage) {
        if (chatMessage == null) chatMessage = "null";
        if (!Configs.ShowXJAMessage) {
            System.err.println("Chat: " + chatMessage);
            return;
        }
        String[] texts = chatMessage.split("\n");
        for (String text : texts) {
            text = "&9[XJA] > &b" + text;
            text = addColor(text);
            System.out.println(text);
            IChatComponent component = new ChatComponentText(text);
            addComponent(component);
        }
    }

    public static void addComponent(IChatComponent component) {
        addComponent(component, true);
    }

    public static void addComponent(IChatComponent component, boolean post) {
        if (getPlayer() == null) return;
        try {
            ClientChatReceivedEvent event = new ClientChatReceivedEvent((byte) 0, component);
            if (post && MinecraftForge.EVENT_BUS.post(event)) return;
            getPlayer().addChatMessage(event.message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // type=0: chat
    // type=1: yikes
    public static void xjchat(int type, String name, String chatMessage) {
        if (!Checker.enabled) return;
        if (toggleOff) return;
        if (chatMessage == null) chatMessage = "null";
        if (type != 0 && type != 1 && type != 2) return;

        String[] texts = chatMessage.split("\n");
        for (String text : texts) {
            text = "&bXJC > &r&8" + name + "&r&f: &r" + text;
            text = addColor(text);
            System.out.println(text);
            IChatComponent component = new ChatComponentText(text);
            addComponent(component);
        }
    }

    public static void showItem(int type, String name, String displayName, String nbt){
        if (!Checker.enabled) return;
        if (toggleOff) return;
        if (type != 3) return;
        // show item
        String text = "&bXJC > &r&8" + name + "&r&f: &r" + "Showed item ";
        text = addColor(text);
        IChatComponent component = new ChatComponentText(text);

        IChatComponent itemComponent = new ChatComponentText(displayName);
        ChatStyle itemStyle = new ChatStyle();
        itemStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(nbt)));
        itemComponent.setChatStyle(itemStyle);

        component.appendSibling(itemComponent);
        addComponent(component);
    }

    public static void playerJoin(String name) {
        if (!Checker.enabled) return;
        if (toggleOff) return;
        String text = "&bXJC > &r&8" + name + " &ejoined.";
        text = addColor(text);
        IChatComponent component = new ChatComponentText(text);
        addComponent(component);
    }

    public static void playerLeft(String name) {
        if (!Checker.enabled) return;
        if (toggleOff) return;
        String text = "&bXJC > &r&8" + name + " &eleft.";
        text = addColor(text);
        IChatComponent component = new ChatComponentText(text);
        addComponent(component);
    }

    public static void showXJCMessage(String text) {
        if (!Checker.enabled) return;
        if (toggleOff) return;
        text = addColor(text);
        IChatComponent component = new ChatComponentText(text);
        addComponent(component);
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

    public static String removeColor(String text) {
        if (text == null) return "";
        Pattern pattern = Pattern.compile("((?<!\\\\))\u00a7(?![^0-9a-fklmnor]|$)");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("&");
    }

    // remove color first!
    public static String getPrefix(String text) {
        if (text == null) return "";
        Pattern pattern = Pattern.compile("^(&[0-9a-fklmnor])*");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) return matcher.group(0);
        return "&r";
    }
}
