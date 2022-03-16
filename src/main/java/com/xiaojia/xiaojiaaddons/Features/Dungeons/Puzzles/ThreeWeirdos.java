package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Remote.RemoteUtils;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;
import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class ThreeWeirdos {
    private static final String url = "https://cdn.jsdelivr.net/gh/Skytils/SkytilsMod-Data@main/solvers/threeweirdos.json";
    private static String npc = null;
    private static final HashSet<String> solutions = new HashSet<>();

    private static long lastFetch = 0;

    public static void fetch() {
        new Thread(() -> {
            try {
                String res = RemoteUtils.get(url, new ArrayList<>(), false);
                JsonParser parser = new JsonParser();
                JsonArray arr = parser.parse(String.format("{\"res\": %s}", res)).getAsJsonObject().get("res").getAsJsonArray();
                synchronized (solutions) {
                    for (JsonElement element : arr) {
                        solutions.add(element.getAsString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        if (!Configs.ThreeWeirdosSolver) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (!Dungeon.currentRoom.equals("Three Weirdos")) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText()).trim();
        if (message.matches("PUZZLE SOLVED! [a-zA-Z0-9_]+ wasn't fooled by.*") ||
            message.matches("PUZZLE FAIL! [a-zA-Z0-9_]+ was fooled by \\w+! Yikes!")) {
            npc = null;
        }
        // question
        // [NPC] Ramsey: The reward is not in my chest. They are both lying.
        if (message.startsWith("[NPC]")) {
            Pattern pattern = Pattern.compile("\\[NPC] (\\w+): [a-zA-Z!'., ]+");
            Matcher matcher = pattern.matcher(message);
            if (!matcher.find()) return;
            String name = matcher.group(1);
            synchronized (solutions) {
                for (String solution : solutions) {
                    if (message.contains(solution)) {
                        npc = name;
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (TimeUtils.curTime() - lastFetch > 1000 * 60 * 20) {
            lastFetch = TimeUtils.curTime();
            fetch();
        }
        if (!Configs.ThreeWeirdosSolver) return;
        if (!Dungeon.currentRoom.equals("Three Weirdos")) return;
        if (npc == null) return;
        BlockPos pos = null;
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityArmorStand && entity.hasCustomName() &&
                    ChatLib.removeFormatting(entity.getCustomNameTag()).equals(npc)) {
                pos = new BlockPos(entity);
                break;
            }
        }
        if (pos == null) return;
        for (int dx = -5; dx <= 5; dx++) {
            for (int dz = -5; dz <= 5; dz++) {
                BlockPos pos1 = pos.add(dx, 0, dz);
                if (Math.abs(dx) + Math.abs(dz) != 1 && BlockUtils.getBlockAt(pos1) == Blocks.chest) {
                    mc.theWorld.setBlockToAir(pos1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        npc = null;
    }
}
