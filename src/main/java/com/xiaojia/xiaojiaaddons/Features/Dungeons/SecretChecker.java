package com.xiaojia.xiaojiaaddons.Features.Dungeons;


import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Remote.API.ApiException;
import com.xiaojia.xiaojiaaddons.Features.Remote.API.HypixelPlayerData;
import com.xiaojia.xiaojiaaddons.Features.Remote.API.PhraseSecrets;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TabUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon.totalSecrets;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getUUIDFromName;


public class SecretChecker {
    private static final String rankPattern = "(?:\\[VIP\\+?] |\\[MVP\\+*] |\\[YOUTUBE] |\\[GM] |\\[ADMIN] |)";
    private static final String namePattern = "[a-zA-Z0-9_]+";
    private static final String infoPattern = "^Party Members \\((?<name>[0-9]+)\\)";
    private static final String leadPattern = "^Party Leader: " + rankPattern + "(?<name>" + namePattern + ") ●";
    private static final String rankNamePattern = "^" + rankPattern + "(?<name>" + namePattern + ")";

    boolean start = false;
    boolean end = false;
    int playerNum;
    ArrayList<String> players;
    ArrayList<Integer> secrets;

    public void fetch() {
        new Thread(() -> {
            for (String player : players) {
                try {
                    String uuid = getUUIDFromName(player);
                    HypixelPlayerData playerData = new HypixelPlayerData(uuid, Configs.APIKey);
                    PhraseSecrets secret = new PhraseSecrets(playerData.getPlayerData());
                    secrets.add(secret.getSecrets());
                    //ChatLib.chat(String.valueOf(secret.getSecrets()));
                } catch (ApiException e) {
                    ChatLib.chat(e.getMessage());
                }
            }
            if (secrets.size() == playerNum * 2) end = true;
        }).start();
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SecretChecker) return;
        if (event.type != 0) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message == null) return;
        if (message.equals("Dungeon starts in 4 seconds.")) {
            start = true;
            playerNum = -1;
            players = new ArrayList<>();
            secrets = new ArrayList<>();
            CommandsUtils.addCommand("/pl");
        } else if (start && players != null && players.size() == playerNum) {
            start = false;
            fetch();
        } else if(start && message.startsWith("Woah slow down, you're doing that too fast!")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            CommandsUtils.addCommand("/pl");
        } else if (start && message.startsWith("You are not currently in a party.")) {
            start = false;
            ChatLib.chat("Join a party to use Secret Checker!");
        } else if (start && message.startsWith("Party Members ")){
            Pattern pattern = Pattern.compile(infoPattern);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) playerNum = Integer.parseInt(matcher.group("name"));
        } else if (start && message.startsWith("Party Leader:")) {
            Pattern pattern = Pattern.compile(leadPattern);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) players.add(matcher.group("name"));
        } else if (start && message.startsWith("Party Moderators:")) {
            String str = (message + " ").replace("Party Moderators: ", "");
            String[] arrOfStr = str.split(" ● ");
            for (String s : arrOfStr) {
                Pattern pattern = Pattern.compile(rankNamePattern);
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) players.add(matcher.group("name"));
            }
        } else if (start && message.startsWith("Party Members:")) {
            String str = (message + " ").replace("Party Members: ", "");
            String[] arrOfStr = str.split(" ● ");
            for (String s : arrOfStr) {
                Pattern pattern = Pattern.compile(rankNamePattern);
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) players.add(matcher.group("name"));
            }
        }
        //if (event.message.toString().startsWith("&r&r&6>&e&lEXTRASTATS&6<")) CommandsUtils.addCommand("/showextrastats");
        //if (msg.startsWith("SecretsFound:"))
        if (message.equals("                             > EXTRA STATS <")) {
                if(players == null) return;
                if(secrets == null) return;
                fetch();
            }
        if (end) {
            end = false;
            Matcher matcher = Pattern.compile("Secrets Found: (\\d+)").matcher(TabUtils.getNames().get(31));
            if (matcher.find()) {
                if (Configs.MapEnabled) ChatLib.chat("&fTotal Secrets Found: (&b" + matcher.group(1) + "&f/&b" + totalSecrets + "&f)");
                else ChatLib.chat("&fSecrets Found: &b" + matcher.group(1));
            } else ChatLib.chat("&fSecrets Found:");
            for (int i = 0; i < playerNum; i++) {
                ChatLib.chat("&f" + players.get(i) + ": &b" + (secrets.get(i + playerNum) - secrets.get(i)));
            }
        }
    }
}
