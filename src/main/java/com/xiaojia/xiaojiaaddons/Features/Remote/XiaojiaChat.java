package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class XiaojiaChat {

    public static void ping() {
        new Thread(() -> System.out.println("ping: " + RemoteUtils.get("xjaddons_ping"))).start();
    }

    public static void chat(String message, int type) {
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"msg\": \"%s\", \"type\": \"%d\"}",
                getUUID(), getPlayer().getName(), message, type);
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void chat(String nbt, String displayName, int type) {
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"dis\": \"%s\", \"type\": \"%d\", \"nbt\": \"%s\"}",
                getUUID(), getPlayer().getName(), displayName, type, nbt);
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void queryOnline() {
        String body = "{\"type\": \"7\"}";
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void uploadLoot(String floor, int score, String chestType, List<String> loots) {
        String lootsString = loots.stream().reduce("", (a, b) -> a.equals("") ? b : a + ", " + b);
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"floor\": \"%s\", \"score\": \"%d\", \"chest\": \"%s\", \"loots\": \"%s\", \"type\": \"%d\"}",
                getUUID(), getPlayer().getName(), floor, score, chestType, lootsString, 9);
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static void bugReport(String reason) {
        StringBuilder log = new StringBuilder();
        try {
            File file = new File("logs/fml-client-latest.log");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }
            String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"introduction\": \"%s\", \"type\": \"%d\"}",
                    getUUID(), getPlayer().getName(), reason, 10);
            new Thread(() -> {
                ClientSocket.chat(body);
                ClientSocket.chat(log.toString());
                ChatLib.chat("Bug report has been successfully sent. Thank you for reporting!");
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            ChatLib.chat("Failed to send bug report. Please ensure you have logs/fml-client-latest.log!");
        }
    }

    public static String getUUID() {
        return SessionUtils.getUUID();
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
        if (unformattedMessage.startsWith(" ☠ You ")) {
            chat(message, 2);
        }
    }
}
