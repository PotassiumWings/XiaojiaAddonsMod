package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.floor;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getBlockX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getBlockZ;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class TeleportMaze {
    private final ArrayList<BlockPos> shouldNot = new ArrayList<>();
    private boolean justPassPad = false;
    private boolean shouldCheckPossible = true;
    private HashSet<BlockPos> possible = new HashSet<>();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        shouldNot.clear();
        possible.clear();
        justPassPad = false;
        shouldCheckPossible = false;
    }

    // tp pad (justPassPad) -> S08 -> check possible pads
    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.TeleportMazeSolver || !SkyblockUtils.isInDungeon()) return;
        if (!(event.packet instanceof S08PacketPlayerPosLook)) return;
        if (!justPassPad) return;
        justPassPad = false;
        shouldCheckPossible = true;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.TeleportMazeSolver || !SkyblockUtils.isInDungeon()) return;
        int x = getBlockX(getPlayer()), y = 69, z = getBlockZ(getPlayer());
        if (shouldCheckPossible) {
            shouldCheckPossible = false;
            double yaw = -getYaw() / (180 / Math.PI);
            Vector2i[] v = new Vector2i[]{
                    new Vector2i(1, -1), new Vector2i(1, 1),
                    new Vector2i(-1, -1), new Vector2i(-1, 1),
            };
            for (Vector2i vec : v) {
                int tx = x + vec.x, ty = y, tz = z + vec.y;
                if (BlockUtils.isBlockTeleportPad(tx, ty, tz)) shouldNot.add(new BlockPos(tx, ty, tz));
            }
            HashSet<BlockPos> possiblePads = new HashSet<>();
            for (int i = 3; i < 50; i++) {
                int sx = x + floor(Math.sin(yaw) * i), sy = y, sz = z + floor(Math.cos(yaw) * i);
                for (int dx = -1; dx <= 1; dx++)
                    for (int dz = -1; dz <= 1; dz++) {
                        int tx = sx + dx, ty = sy, tz = sz + dz;
                        if (BlockUtils.isBlockTeleportPad(tx, ty, tz)) {
                            possiblePads.add(new BlockPos(tx, ty, tz));
                        }
                    }
            }
            ChatLib.debug("possiblePad: ");
            for (BlockPos possiblePad : possiblePads) ChatLib.debug(possiblePad.toString());

            if (possible.size() == 0) {
                possible = possiblePads;
            } else {
                possible = possible.stream().filter(possiblePads::contains).collect(Collectors.toCollection(HashSet::new));
                if (possible.size() == 0) {
                    ChatLib.debug("owo");
                    shouldCheckPossible = true;
                }
            }
            ChatLib.debug("possible: ");
            for (BlockPos possiblePad : possible) ChatLib.debug(possiblePad.toString());
        }

        y = floor(getY(getPlayer()) - 0.5);
        if (BlockUtils.isBlockTeleportPad(x, y, z)) {
            BlockPos blockPos = new BlockPos(x, y, z);
            if (!shouldNot.contains(blockPos)) shouldNot.add(blockPos);
            justPassPad = true;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        for (BlockPos blockPos : shouldNot) GuiUtils.drawBoundingBoxAtBlock(blockPos, new Color(255, 0, 0));
        Color color = new Color(0, 0, 255);
        if (possible.size() == 1) color = new Color(0, 255, 0);
        for (BlockPos blockPos : possible) GuiUtils.drawBoundingBoxAtBlock(blockPos, color);
    }
}
