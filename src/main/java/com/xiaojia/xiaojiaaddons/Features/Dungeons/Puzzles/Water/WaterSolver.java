package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoPuzzle;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


// TODO: DELETE THIS FROM ROOM.JSON
public class WaterSolver {
    public static Room room = null;
    private static EnumFacing facing = null;
    private static EnumState[][] board = new EnumState[WaterUtils.height][WaterUtils.width];
    private static int lastFlag;
    private static int process = 0;
    private static BufferedImage map = null;
    // simulate
    private final KeyBind devKeyBind = new KeyBind("Dev Water", Keyboard.KEY_NONE);
    private final KeyBind keyBind = AutoPuzzle.keyBind;
    private boolean should = false;
    private static long lastKey = 0;

    private static Thread solveThread = null;

    public static void solve() throws InterruptedException {
        process = 0;
        // facing
        facing = WaterUtils.getFacing(room);
        if (facing == null) return;
        System.err.println("facing: " + facing + ", x " + room.x + ", z " + room.z);
        board = WaterUtils.getBoard(room, facing);
        WaterUtils.calculateVectors(room, facing);
        int flag = WaterUtils.getFlag(room, facing);
        while (flag == 0) {
            Thread.sleep(100);
            flag = WaterUtils.getFlag(room, facing);
        }
        // calc
        ChatLib.chat("Calculating possible solutions...");
        long startTime = TimeUtils.curTime();
        if (lastFlag != flag) {
            WaterUtils.operations.clear();
            WaterUtils.bestTime = 120;
            WaterUtils.dfs(board, -WaterUtils.gap, new HashMap<>(), flag, false);
            if (WaterUtils.bestTime == 120)
                WaterUtils.dfs(board, -WaterUtils.gap, new HashMap<>(), flag, true);
            lastFlag = flag;
        }
        ChatLib.chat(String.format("Estimate best solution: %.2fs (%.2fs to calc)", WaterUtils.bestTime * 0.25,
                (TimeUtils.curTime() - startTime) / 1000F));
        for (Map.Entry<Integer, EnumOperation> operation : WaterUtils.operations.entrySet()) {
            if (operation.getValue().equals(EnumOperation.empty) || operation.getValue().equals(EnumOperation.trig))
                continue;
            ChatLib.chat("  " + operation.getKey() * 0.25 + "s: " + getMessageFromOperation(operation.getValue()));
        }
    }

    private static String getMessageFromOperation(EnumOperation o) {
        if (o == EnumOperation.c) return "&0Coal";
        if (o == EnumOperation.cl) return "&4Clay";
        if (o == EnumOperation.e) return "&aEmerald";
        if (o == EnumOperation.d) return "&bDiamond";
        if (o == EnumOperation.q) return "&fQuartz";
        if (o == EnumOperation.g) return "&6Gold";
        return "";
    }

    public static void setRoom(Room waterRoom) {
        if (!Checker.enabled) return;
        if (!Configs.WaterSolver) return;
        room = waterRoom;
        if (room != null) {
            solveThread =
                    new Thread(() -> {
                        try {
                            solve();
                        } catch (Exception ignored) {
                        }
                    });
            solveThread.start();
        }
    }

    @SubscribeEvent
    public void onTickAuto(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.WaterSolver) return;
        if (keyBind.isKeyDown()) {
            should = !should;
            if (!Dungeon.currentRoom.equals("Water Board")) should = false;
            if (should) ChatLib.chat("Auto Water &aactivated");
            else ChatLib.chat("Auto Water &cdeactivated");
        }
        if (!should || !Dungeon.isFullyScanned || room == null) {
            deactivate();
            return;
        }

        if (solveThread == null || !solveThread.isAlive()) {
            solveThread = new Thread(() -> {
                try {
                    long lastTime = TimeUtils.curTime();
                    int lastEntry = 0;
                    for (Map.Entry<Integer, EnumOperation> entry : WaterUtils.operations.entrySet()) {
                        int delta = entry.getKey() - lastEntry;
                        EnumOperation operation = entry.getValue();
                        lastEntry = entry.getKey();
                        // etherwarp, change direction
                        ControlUtils.etherWarp(WaterUtils.getEtherwarpPointFor(operation));
                        ControlUtils.faceSlowly(WaterUtils.getPosFor(operation));
                        // wait
                        long deltaTime = TimeUtils.curTime() - lastTime;
                        if (deltaTime < delta) Thread.sleep(delta - deltaTime);
                        // right click
                        if (BlockUtils.getBlockAt(BlockUtils.getLookingAtPos(5)) != Blocks.lever)
                            throw new Exception("Not looking at levers");
                        ControlUtils.rightClick();
                        lastTime = TimeUtils.curTime();
                    }
                    deactivate();
                } catch (Exception e) {
                    deactivate();
                    e.printStackTrace();
                }
            });
            solveThread.start();
        }
    }

    private void deactivate() {
        if (should) {
            should = false;
            ChatLib.chat("Auto Water &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (devKeyBind.isKeyDown()) {
            if (TimeUtils.curTime() - lastKey < 250) return;
            lastKey = TimeUtils.curTime();
            if (WaterUtils.operations.containsKey(process))
                board = WaterUtils.getStatesFromOperation(board, WaterUtils.operations.get(process));
            board = WaterUtils.simulate(board).getKey();
            process++;
            if (process >= 100) process = 0;

            BufferedImage newMap = new BufferedImage(WaterUtils.width, WaterUtils.height, BufferedImage.TYPE_4BYTE_ABGR);
            for (int i = 0; i < WaterUtils.height; i++) {
                for (int j = 0; j < WaterUtils.width; j++) {
                    Color color = null;
                    if (WaterUtils.isWater(board[i][j])) color = new Color(65, 65, 255);
                    else if (board[i][j] == EnumState.cc) color = new Color(0, 0, 0);
                    else if (board[i][j] == EnumState.ccl) color = new Color(180, 65, 65);
                    else if (board[i][j] == EnumState.cd) color = new Color(90, 240, 240);
                    else if (board[i][j] == EnumState.cg) color = new Color(255, 180, 0);
                    else if (board[i][j] == EnumState.ce) color = new Color(0, 180, 0);
                    else if (board[i][j] == EnumState.cq) color = new Color(255, 255, 255);
                    else if (WaterUtils.isBlock(board[i][j])) color = new Color(120, 120, 120);
                    else continue;
                    newMap.setRGB(j, WaterUtils.height - i - 1, color.getRGB());
                }
            }
            map = newMap;
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (Dungeon.currentRoom.equals("Water Board") && map != null)
            RenderUtils.drawImage(new Image(map), Configs.MapX, Configs.MapY, 25 * Configs.MapScale, 25 * Configs.MapScale);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        setRoom(null);
        facing = null;
        board = new EnumState[20][21];
        lastFlag = -1;
        map = null;
    }
}
