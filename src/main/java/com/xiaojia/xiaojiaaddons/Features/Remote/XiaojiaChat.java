package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class XiaojiaChat {

    public static void ping() {
        new Thread(() -> System.out.println("ping: " + RemoteUtils.get("xjaddons_ping"))).start();
    }

    public static void chat(String message, int type) {
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"msg\": \"%s\", \"type\": \"%d\"}",
                getUUID(), getPlayer().getName(), message, type);
        ChatLib.debug("body: " + body + ", type: " + type + ".");
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void chat(String nbt, String displayName, int type) {
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"dis\": \"%s\", \"type\": \"%d\", \"nbt\": \"%s\"}",
                getUUID(), getPlayer().getName(), displayName, type, nbt);
        ChatLib.debug("body: " + body + ", type: " + type + ".");
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void queryOnline() {
        String body = "{\"type\": \"7\"}";
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void addRank(String name, String rank, String color) {
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"person\": \"%s\", \"rank\": \"%s\", \"color\": \"%s\", \"type\": \"%d\"}",
                getUUID(), getPlayer().getName(), name, rank, color, 8);
        ChatLib.chat("body: " + body);
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static String getUUID() {
        return mc.getSession().getProfile().getId().toString().replace("-", "");
    }

    // puzzle fail
    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        String message = event.message.getFormattedText();
        String unformattedMessage = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (unformattedMessage.startsWith("PUZZLE FAIL! " + getPlayer().getName())) {
            chat(message, 1);
        }
        if (unformattedMessage.startsWith(" ☠ You")) {
            chat(message, 2);
        }
    }
}
