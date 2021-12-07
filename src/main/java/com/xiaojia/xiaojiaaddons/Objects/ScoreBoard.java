package com.xiaojia.xiaojiaaddons.Objects;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ScoreBoard {
    public static boolean update = true;
    private static ArrayList<String> lines = new ArrayList<>();
    public static String title = "";

    public static ArrayList<String> getLines() {
        if (update) {
            updateLines();
            update = false;
        }
        return lines;
    }

    private static void updateLines() {
//        ChatLib.chat("updating lines...");
        ArrayList<String> newLines = new ArrayList<>();
        try {
            Scoreboard scoreBoard = getPlayer().getWorldScoreboard();
            title = scoreBoard.getObjectiveInDisplaySlot(1).getDisplayName();
            ArrayList<Score> list = new ArrayList<>(
                    scoreBoard.getSortedScores(scoreBoard.getObjectiveInDisplaySlot(1))
            );
            for (Score score : list) {
                String s = "";
                ScorePlayerTeam team = scoreBoard.getPlayersTeam(score.getPlayerName());
                try {
                    s += team.getColorPrefix();
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                s += score.getPlayerName();
                try {
                    s += team.getColorSuffix();
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                s = ChatFormatting.stripFormatting(s);
                newLines.add(s);
            }
            lines = newLines;
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        update = true;
    }
}
