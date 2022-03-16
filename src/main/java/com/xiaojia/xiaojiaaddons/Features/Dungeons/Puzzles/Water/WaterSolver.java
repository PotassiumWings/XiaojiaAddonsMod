package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoPuzzle;
import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class WaterSolver {
    public static Room room = null;
    private static EnumFacing facing = null;
    private static EnumState[][] board = new EnumState[WaterUtils.height][WaterUtils.width];
    private static EnumState[][] originBoard = new EnumState[WaterUtils.height][WaterUtils.width];
    private static int lastFlag;
    private static boolean should = false;
    private static boolean tpPacketReceived = false;
    private static Thread solveThread = null;
    // simulate
    private final KeyBind keyBind = AutoPuzzle.keyBind;

    public static void solve() throws InterruptedException {
        // facing
        facing = WaterUtils.getFacing(room);
        if (facing == null) return;
        System.err.println("facing: " + facing + ", x " + room.x + ", z " + room.z);
        board = WaterUtils.getBoard(room, facing);
        originBoard = new EnumState[WaterUtils.height][WaterUtils.width];
        for (int i = 0; i < WaterUtils.height; i++)
            for (int j = 0; j < WaterUtils.width; j++)
                originBoard[i][j] = board[i][j];
        WaterUtils.calculateVectors(room, facing);
        Thread.sleep(200);
        int flag = WaterUtils.getFlag(room, facing);
        while (flag == 0 && Dungeon.currentRoom.equals("Water Board")) {
            Thread.sleep(100);
            flag = WaterUtils.getFlag(room, facing);
        }
        if (flag == 0) {
            return;
        }
        System.err.println("flag: " + flag);

        Patterns.Operation operation = Patterns.getOperation(board, flag);
        WaterUtils.processBoard(board);
        WaterUtils.print(board);
        calc(flag, operation);
    }

    public static void calc(int flag, Patterns.Operation op) {
        // calc
        ChatLib.chat("Calculating possible solutions...");
        long startTime = TimeUtils.curTime();
        if (op != null && op.time < 150) {
            WaterUtils.operations = op.operations;
            ChatLib.chat(String.format("Estimate best solution: %.2fs (From Cache)", op.time * 0.25));
        } else {
            if (lastFlag != flag) {
                if (WaterUtils.raw) {
                    ChatLib.chat("This is a new pattern! Sent to server for calculation.");
                    upload(WaterUtils.boardString);
                } else
                    ChatLib.chat("Levers are flipped, so calculating without cache.");
                WaterUtils.operations = new TreeMap<>();
                WaterUtils.bestTime = 120;
                WaterUtils.dfs(board, -WaterUtils.gap, new HashMap<>(), flag, false);
                if (WaterUtils.bestTime == 120)
                    WaterUtils.dfs(board, -WaterUtils.gap, new HashMap<>(), flag, true);
                if (WaterUtils.bestTime != 120) {
                    System.err.println(Patterns.getPatternString(
                            originBoard, flag, WaterUtils.bestTime, WaterUtils.operations
                    ));
                }
                lastFlag = flag;
            }
            ChatLib.chat(String.format("Estimate best solution: %.2fs (%.2fs to calc)", WaterUtils.bestTime * 0.25,
                    (TimeUtils.curTime() - startTime) / 1000F));
        }
        for (Map.Entry<Integer, EnumOperation> operation : WaterUtils.operations.entrySet()) {
            if (operation.getValue().equals(EnumOperation.empty) || operation.getValue().equals(EnumOperation.trig))
                continue;
            ChatLib.chat("  " + operation.getKey() * 0.25 + "s: " + getMessageFromOperation(operation.getValue()));
        }
    }

    private static void upload(String boardString) {
        new Thread(() -> {
            String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"water\": \"%s\", \"type\": \"%d\"}",
                    SessionUtils.getUUID(), SessionUtils.getName(), boardString, 11);
            ClientSocket.chat(body);
        }).start();
    }

    public static String getMessageFromOperation(EnumOperation o) {
        if (o == EnumOperation.c) return "&0Coal";
        if (o == EnumOperation.cl) return "&4Clay";
        if (o == EnumOperation.e) return "&aEmerald";
        if (o == EnumOperation.d) return "&bDiamond";
        if (o == EnumOperation.q) return "&fQuartz";
        if (o == EnumOperation.g) return "&6Gold";
        if (o == EnumOperation.trig) return "Trig";
        return "";
    }

    public static void setRoom(Room waterRoom) {
        if (!Checker.enabled) return;
        if (!Configs.WaterSolver) return;
        room = waterRoom;
        if (room != null) {
            ChatLib.chat("set room: " + room.x + ", " + room.z);
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

    private static void etherWarpTo(Vector3d v) throws Exception {
        tpPacketReceived = false;
        ControlUtils.etherWarp(v);
        int cnt = 0;
        while (!tpPacketReceived && should) {
            Thread.sleep(20);
            cnt++;
            if (cnt >= 50) {
                ChatLib.chat("Too long no packet, please try again.");
                throw new Exception();
            }
        }
        Thread.sleep(Configs.EtherWarpDelayAfter);
    }

    public static void reset() {
        board = WaterUtils.getBoard(room, facing);
        WaterUtils.processBoard(board);
    }

    public static void printLog() {
        System.err.println("WaterSolver Log:");
        System.err.println(WaterUtils.boardString);
        System.err.println();
    }

    @SubscribeEvent
    public void onTickAuto(TickEndEvent event) {
        if (!Checker.enabled || !Configs.WaterSolver || !Dungeon.currentRoom.equals("Water Board")) {
            deactivate();
            return;
        }
        if (keyBind.isPressed()) {
            should = !should;
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
                    int aotvSlot = HotbarUtils.aotvSlot;
                    if (aotvSlot == -1) {
                        ChatLib.chat("Requires aotv in hotbar.");
                        throw new Exception();
                    }
                    ControlUtils.setHeldItemIndex(aotvSlot);
                    long lastTime = TimeUtils.curTime();
                    int lastEntry = 0;
                    Vector3d lastEtherWarp = null;
                    for (Map.Entry<Integer, EnumOperation> entry : WaterUtils.operations.entrySet()) {
                        if (entry.getValue().equals(EnumOperation.empty)) continue;
                        int delta = (entry.getKey() - lastEntry) * 250;
                        lastEntry = entry.getKey();
                        EnumOperation operation = entry.getValue();
                        Vector3d etherWarp = WaterUtils.getEtherwarpPointFor(operation);
                        if (!etherWarp.equals(lastEtherWarp)) {
                            etherWarpTo(etherWarp);
                            lastEtherWarp = etherWarp;
                        }
                        // etherwarp, change direction
                        ControlUtils.faceSlowly(WaterUtils.getPosFor(operation));
                        // wait
                        long deltaTime = TimeUtils.curTime() - lastTime;
                        if (deltaTime < delta) Thread.sleep(delta - deltaTime);
                        // right click
                        if (BlockUtils.getBlockAt(BlockUtils.getLookingAtPos(5)) != Blocks.lever)
                            throw new Exception("Not looking at levers");
                        ControlUtils.rightClick();
                        Thread.sleep(200);
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

    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.packet;
            tpPacketReceived = true;
        }
    }

    private void deactivate() {
        if (should) {
            should = false;
            ChatLib.chat("Auto Water &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        setRoom(null);
        facing = null;
        board = new EnumState[WaterUtils.height][WaterUtils.width];
        lastFlag = -1;
    }
}
