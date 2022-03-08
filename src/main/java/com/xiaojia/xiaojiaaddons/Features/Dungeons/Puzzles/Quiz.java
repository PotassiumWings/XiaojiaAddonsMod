package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Quiz {
    private static final String url = "https://cdn.jsdelivr.net/gh/Skytils/SkytilsMod-Data@main/solvers/oruotrivia.json";
    private static final HashMap<String, ArrayList<String>> solutions = new HashMap<>();

    private static String answer = null;
    private static ArrayList<String> answers = null;

    private static long lastFetch = 0;

    public static void fetch() {
        new Thread(() -> {
            try {
                String res = RemoteUtils.get(url, new ArrayList<>(), false);
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(res).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    String question = entry.getKey();
                    JsonArray arr = entry.getValue().getAsJsonArray();
                    solutions.put(question, new ArrayList<>());
                    for (JsonElement solution : arr) {
                        solutions.get(question).add(solution.getAsString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Configs.QuizSolver) return;
        if (TimeUtils.curTime() - lastFetch > 1000 * 60 * 10) {
            lastFetch = TimeUtils.curTime();
            fetch();
        }
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        if (!Configs.QuizSolver) return;
        if (!SkyblockUtils.isInDungeon()) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText()).trim();
        if (message.startsWith("[STATUE] Oruo the Omniscient: ") &&
                message.contains("answered Question #") &&
                message.endsWith("correctly!"))
            answer = null;
        // question
        if (message.equals("What SkyBlock year is it?")) {
            long started = Dungeon.runStarted;
            long cur = TimeUtils.curTime();
            long useTime = started < 5 ? cur : started;
            long year = (useTime - 1560276000) / 446400 + 1;
            answer = "Year " + year;
        } else {
            for (String question : solutions.keySet()) {
                if (message.contains(question))
                    answers = solutions.get(question);
            }
        }
        // find answer from answers
        if (answers != null && (message.startsWith("ⓐ") ||
                message.startsWith("ⓑ") || message.startsWith("ⓒ"))) {
            for (String ans : answers) {
                if (message.contains(ans))
                    answer = ans;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.QuizSolver) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK &&
                event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;
        Block block = BlockUtils.getBlockAt(event.pos);
        if (block != Blocks.stone_button && block != Blocks.double_stone_slab || answer == null) return;
        if (!Dungeon.currentRoom.equals("Quiz")) return;
        EntityArmorStand answerEntity = null;
        for (Entity entity : getWorld().loadedEntityList) {
            if (!(entity instanceof EntityArmorStand)) continue;
            if (!entity.hasCustomName()) continue;
            String name = entity.getCustomNameTag();
            if (name.contains(answer) && (name.contains("ⓐ") || name.contains("ⓑ") || name.contains("ⓒ"))) {
                answerEntity = (EntityArmorStand) entity;
                break;
            }
        }
        if (answerEntity == null) return;
        if (answerEntity.getDistanceSq(event.pos) > 2 * 2) {
            ChatLib.chat("Blocked wrong click in quiz!");
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTickRemove(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.QuizSolver) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (getWorld() == null) return;
        List<Entity> allEntities = getWorld().loadedEntityList;
        for (Entity entity : allEntities) {
            if (!(entity instanceof EntityArmorStand)) continue;
            if (!entity.hasCustomName()) continue;
            String name = entity.getCustomNameTag();
            if (name.contains("ⓐ") || name.contains("ⓑ") || name.contains("ⓒ"))
                if (answer != null && !name.contains(answer))
                    entity.posY = entity.lastTickPosY = 9999;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        answer = null;
        answers = null;
    }
}
