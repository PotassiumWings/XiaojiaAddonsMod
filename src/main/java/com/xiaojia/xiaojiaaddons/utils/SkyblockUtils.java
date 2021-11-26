package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ArrayList<String> maps = new ArrayList<>(Arrays.asList(
            "Hub", "Spider's Den", "Gunpowder Mines", "Void Sepulture", "Dragon's Nest",
            "The End", "The Mist", "Blazing Fortress", "The Catacombs", "Howling Cave",
            "Your Island", "None"
    ));
    private static String currentMap = "";
    private static String currentServer = "";

    private static String updateCurrentMap() {
        ArrayList<String> lines = ScoreBoard.getLines();
        if (lines.size() < 5) return "";
        String line = lines.get(lines.size() - 4) + lines.get(lines.size() - 5);
        StringBuilder removeSkippingChar = new StringBuilder();
        line = ChatLib.removeFormatting(line);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '\'' || c == ' ')
                removeSkippingChar.append(c);
        }
        line = removeSkippingChar.toString();

        String result = "Others";
        for (String map : maps) {
            if (line.contains(map)) {
                if (!result.equals("Others")) {
                    ChatLib.chat("Undefined Behavior in currentmap()");
                    ChatLib.chat(line);
                }
                result = map;
            }
        }
        return result;
    }

    public static String getCurrentMap() {
        return currentMap;
    }

    public static boolean isInSpiderDen() {
        return currentMap.equals("Spider's Den");
    }

    public static String getCurrentServer() {
        return currentServer;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        currentMap = updateCurrentMap();
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        Pattern pattern = Pattern.compile("^Sending to server (.*)\\.\\.\\..*");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            currentServer = matcher.group(1);
        }
    }
}
