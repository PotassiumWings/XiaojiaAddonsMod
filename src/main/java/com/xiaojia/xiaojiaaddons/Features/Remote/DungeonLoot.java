package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonLoot {
    private String floor;
    private int score = -1;
    private chestType type;
    private final List<String> loots = new ArrayList<>();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        if (!SkyblockUtils.isInDungeon()) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile("^ *The Catacombs - Floor (.*)");
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            floor = "F" + StringUtils.getNumberFromRoman(matcher.group(1));
            return;
        }

        pattern = Pattern.compile("^ *Master Mode Catacombs - Floor (.*)");
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            floor = "M" + StringUtils.getNumberFromRoman(matcher.group(1));
            return;
        }

        pattern = Pattern.compile("^ *Team Score: (\\d+)");
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            score = Integer.parseInt(matcher.group(1));
            loots.clear();
            return;
        }

        pattern = Pattern.compile("^  (WOOD|DIAMOND|EMERALD|OBSIDIAN|BEDROCK) CHEST REWARDS$");
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            type = chestType.valueOf(matcher.group(1));
            loots.clear();
            return;
        }

        pattern = Pattern.compile("^    ([^ ].*)$");
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            String loot = matcher.group(1).replace("RARE REWARD! ", "");
            if (score != -1) loots.add(loot);
            ChatLib.debug("found loot " + loot);
            return;
        }

        if (message.equals("") && loots.size() > 0) {
            XiaojiaChat.uploadLoot(floor, score, type.toString(), loots);
            score = -1;
            loots.clear();
        }
    }

    enum chestType {
        WOOD, DIAMOND, EMERALD, OBSIDIAN, BEDROCK;
    }
}
