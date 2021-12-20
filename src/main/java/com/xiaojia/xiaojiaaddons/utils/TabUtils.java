package com.xiaojia.xiaojiaaddons.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class TabUtils {
    private static final Ordering<NetworkPlayerInfo> playerComparator = Ordering.from(new PlayerComparator());

    public static GuiPlayerTabOverlay getTabGui() {
        GuiIngame guiIngame = mc.ingameGUI;
        if (guiIngame != null) return guiIngame.getTabList();
        return null;
    }

    public static ArrayList<String> getNames() {
        GuiPlayerTabOverlay tabGui = getTabGui();
        ArrayList<String> res = new ArrayList<>();
        if (getPlayer() == null || tabGui == null) return res;
        Collection<NetworkPlayerInfo> list = getPlayer().sendQueue.getPlayerInfoMap();
        List<NetworkPlayerInfo> sortedList = playerComparator.sortedCopy(list);
        for (NetworkPlayerInfo info : sortedList) {
            res.add(ChatLib.removeFormatting(tabGui.getPlayerName(info)));
        }
        return res;
    }

    public static void printTab() {
        ArrayList<String> tabs = getNames();
        for (int i = 0; i < tabs.size(); i++) {
            ChatLib.chat(i + ": " + tabs.get(i));
        }
    }

    public static final class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        public int compare(NetworkPlayerInfo playerOne, NetworkPlayerInfo playerTwo) {
            ScorePlayerTeam teamOne = playerOne.getPlayerTeam();
            ScorePlayerTeam teamTwo = playerTwo.getPlayerTeam();
            String name1 = teamOne == null ? null : teamOne.getRegisteredName();
            String name2 = teamTwo == null ? null : teamTwo.getRegisteredName();
            return ComparisonChain.start().compareTrueFirst(
                    (playerOne.getGameType() != WorldSettings.GameType.SPECTATOR),
                    (playerTwo.getGameType() != WorldSettings.GameType.SPECTATOR)
            ).compare(
                    name1 == null ? "" : name1,
                    name2 == null ? "" : name2
            ).compare(
                    playerOne.getGameProfile().getName(),
                    playerTwo.getGameProfile().getName()
            ).result();
        }
    }
}
