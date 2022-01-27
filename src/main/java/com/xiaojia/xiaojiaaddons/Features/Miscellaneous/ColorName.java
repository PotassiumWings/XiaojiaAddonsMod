package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class ColorName {
    public static HashMap<String, String> colorMap;
    public static HashMap<String, String> rankMap;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        IChatComponent message = event.message.createCopy();
        event.setCanceled(true);
        ChatLib.addComponent(convert(message), false);
    }

    public static void setColorMap(String rank, String color) {
        Type type = (new TypeToken<HashMap<String, String>>() {
        }).getType();
        rankMap = (new Gson()).fromJson(rank, type);
        colorMap = (new Gson()).fromJson(color, type);
//        for (String str: rankMap.keySet()) {
//            ChatLib.chat(str + ": " + rankMap.get(str) + " " + colorMap.get(str) + str);
//        }
    }

    private static IChatComponent convert(IChatComponent message) {
        List<IChatComponent> siblings = message.getSiblings();
        ChatStyle style = message.getChatStyle();
        if (siblings.isEmpty()) {
            if (!(message instanceof ChatComponentTranslation)) {
                IChatComponent component = new ChatComponentText(addColorName(message.getFormattedText()));
                component.setChatStyle(style);
                return component;
            } else {
                System.out.println(message.getFormattedText());
                ChatLib.chat("what is this ???");
                return message;
            }
        } else {
            int index = message.getFormattedText().indexOf(siblings.get(0).getFormattedText());
            int i;
            for (i = 0; i < siblings.size(); i++) {
                IChatComponent component = siblings.get(i);
                if (component.getChatStyle().getChatClickEvent() == null && component.getChatStyle().getChatHoverEvent() == null) {
                    index += component.getFormattedText().length();
                } else {
                    break;
                }
            }
            IChatComponent component = new ChatComponentText(addColorName(message.getFormattedText().substring(0, index)));
            component.setChatStyle(style);
            for (; i < siblings.size(); i++) {
                IChatComponent component1 = siblings.get(i);
                component.appendSibling(convert(component1));
            }
            return component;
        }
    }

    private static String addColorName(String message) {
//        System.err.println("msg: " + message);
        for (String name : colorMap.keySet()) {
            String color = colorMap.get(name);

            String reg = "(\u00a77|\u00a7.\\[(MVP|VIP)] |\u00a7.\\[(MVP|VIP)(\u00a7.)*\\++(\u00a7.)*] )" + name;
//            System.err.println("reg: " + reg);

            message = message.replaceAll(reg, "\u1105");
            message = message.replaceAll("(?<=\u00a7bXJC > \u00a7r\u00a78)" + name, "\u1105");

            message = message.replace(name, color + name + "&r");

            String dst = colorMap.get(name) + name + "&r";
            if (!rankMap.get(name).equals("")) dst = rankMap.get(name) + " " + dst;
            message = message.replace("\u1105", dst);
        }
        return ChatLib.addColor(message);
    }
}
