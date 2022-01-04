package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockUtils {
    private static final ArrayList<String> maps = new ArrayList<>(Arrays.asList(
            "Hub", "Spider's Den", "Gunpowder Mines", "Void Sepulture", "Dragon's Nest",
            "The End", "The Mist", "Blazing Fortress", "The Catacombs", "Howling Cave",
            "Your Island", "None"
    ));
    private static final ArrayList<String> crystalHollowsMaps = new ArrayList<>(Arrays.asList(
            "Fairy Grotto",
            "Goblin Holdout", "Goblin Queen's Den",
            "Jungle", "Jungle Temple",
            "Mines of Divan", "Mithril Deposits",
            "Lost Precursor City", "Precursor Remnants",
            "Magma Fields", "Crystal Nucleus",
            "Khazad-dûm", "Dragon's Lair"
    ));
    private static String currentMap = "";
    private static String currentServer = "";
    private static boolean set = false;
    private static boolean isInCrystalHollows = false;
    private static String dungeon = "F6";

    private static String updateCurrentMap() {
        ArrayList<String> lines = ScoreBoard.getLines();
        if (lines.size() < 5) return "";
        String line = lines.get(lines.size() - 3) + lines.get(lines.size() - 4) + lines.get(lines.size() - 5);
        StringBuilder removeSkippingChar = new StringBuilder();
        line = ChatLib.removeFormatting(line);
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '\'' || c == ' '
                    || c == '(' || c == ')' || c == 'û')
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
        isInCrystalHollows = false;
        if (result.equals("Others")) {
            for (String map : crystalHollowsMaps) {
                if (line.contains(map)) {
                    result = map;
                    isInCrystalHollows = true;
                }
            }
        }

        if (result.equals("The Catacombs")) {
            Pattern pattern = Pattern.compile("The Catacombs \\((.*)\\)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                dungeon = matcher.group(1);
            }
        }
        return result;
    }

    public static String getDungeon() {
        return dungeon;
    }

    public static String getCurrentMap() {
        return currentMap;
    }

    public static void setCurrentMap(String map) {
        currentMap = map;
        set = true;
    }

    public static boolean isInSpiderDen() {
        return currentMap.equals("Spider's Den");
    }

    public static boolean isInDungeon() {
        return currentMap.equals("The Catacombs");
    }

    public static boolean isInMist() {
        return currentMap.equals("The Mist");
    }

    public static boolean isInDragon() {
        return currentMap.equals("Dragon's Nest");
    }

    public static boolean isInTheEnd() {
        return currentMap.equals("The End");
    }

    public static boolean isInVoidSepulture() {
        return currentMap.equals("Void Sepulture");
    }

    public static boolean isInEndIsland() {
        return isInDragon() || isInTheEnd() || isInVoidSepulture();
    }

    public static boolean isInGunpowderMines() {
        return currentMap.equals("Gunpowder Mines");
    }

    public static boolean isInHowlingCave() {
        return currentMap.equals("Howling Cave");
    }

    public static boolean isInCrystalHollows() {
        return isInCrystalHollows;
    }

    public static String getCurrentServer() {
        return currentServer;
    }

    public static String getSkyBlockID(ItemStack itemStack) {
        if (itemStack == null) return "";
        NBTTagCompound extraAttributes = itemStack.getSubCompound("ExtraAttributes", false);
        if (extraAttributes != null && extraAttributes.hasKey("id")) return extraAttributes.getString("id");
        return "";
    }

    public static boolean isInSkyblock() {
        return ChatLib.removeFormatting(ScoreBoard.title).contains("SKYBLOCK");
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!set || !XiaojiaAddons.isDebug())
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
        set = false;
    }
}
