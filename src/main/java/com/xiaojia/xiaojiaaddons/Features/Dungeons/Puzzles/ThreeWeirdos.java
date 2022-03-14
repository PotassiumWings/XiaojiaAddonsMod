package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.EnumOperation;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.EnumState;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.WaterUtils;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.util.Map;

//public class ThreeWeirdos {
//    private final KeyBind keyBind = AutoPuzzle.keyBind;
//    private boolean should = false;
//    private static Room room;
//
//    @SubscribeEvent
//    public void onTickAuto(TickEndEvent event) {
//        if (!Checker.enabled) return;
//        if (!Configs.ThreeWeirdosSolver) return;
//        if (!Dungeon.currentRoom.equals("Three Weirdos")) return;
//        if (keyBind.isPressed()) {
//            should = !should;
//            if (should) ChatLib.chat("Auto Three Weirdos &aactivated");
//            else ChatLib.chat("Auto Water &cdeactivated");
//        }
//        if (!should || !Dungeon.isFullyScanned || room == null) {
//            deactivate();
//            return;
//        }
//
//        if (solveThread == null || !solveThread.isAlive()) {
//            solveThread = new Thread(() -> {
//                try {
//                    int aotvSlot = HotbarUtils.aotvSlot;
//                    if (aotvSlot == -1) {
//                        ChatLib.chat("Requires aotv in hotbar.");
//                        throw new Exception();
//                    }
//                    ControlUtils.setHeldItemIndex(aotvSlot);
//                    long lastTime = TimeUtils.curTime();
//                    int lastEntry = 0;
//                    Vector3d lastEtherWarp = null;
//                    for (Map.Entry<Integer, EnumOperation> entry : WaterUtils.operations.entrySet()) {
//                        if (entry.getValue().equals(EnumOperation.empty)) continue;
//                        int delta = (entry.getKey() - lastEntry) * 250;
//                        lastEntry = entry.getKey();
//                        EnumOperation operation = entry.getValue();
//                        Vector3d etherWarp = WaterUtils.getEtherwarpPointFor(operation);
//                        if (!etherWarp.equals(lastEtherWarp)) {
//                            etherWarpTo(etherWarp);
//                            lastEtherWarp = etherWarp;
//                        }
//                        // etherwarp, change direction
//                        ControlUtils.faceSlowly(WaterUtils.getPosFor(operation));
//                        // wait
//                        long deltaTime = TimeUtils.curTime() - lastTime;
//                        if (deltaTime < delta) Thread.sleep(delta - deltaTime);
//                        // right click
//                        if (BlockUtils.getBlockAt(BlockUtils.getLookingAtPos(5)) != Blocks.lever)
//                            throw new Exception("Not looking at levers");
//                        ControlUtils.rightClick();
//                        Thread.sleep(200);
//                        lastTime = TimeUtils.curTime();
//                    }
//                    deactivate();
//                } catch (Exception e) {
//                    deactivate();
//                    e.printStackTrace();
//                }
//            });
//            solveThread.start();
//        }
//    }
//
//    @SubscribeEvent
//    public void onPacketReceived(PacketReceivedEvent event) {
//        if (event.packet instanceof S08PacketPlayerPosLook) {
//            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.packet;
//            tpPacketReceived = true;
//        }
//    }
//
//    private void deactivate() {
//        if (should) {
//            should = false;
//            ChatLib.chat("Auto Water &cdeactivated");
//        }
//    }
//
//    @SubscribeEvent
//    public void onWorldLoad(WorldEvent.Load event) {
//        setRoom(null);
//        facing = null;
//        board = new EnumState[WaterUtils.height][WaterUtils.width];
//        lastFlag = -1;
//        map = null;
//    }
//}
