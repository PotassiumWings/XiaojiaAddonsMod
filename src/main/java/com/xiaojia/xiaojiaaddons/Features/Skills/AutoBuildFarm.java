package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3d;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoBuildFarm {
    private static final ArrayList<BlockPos> blocksOne = new ArrayList<>();
    private static final ArrayList<BlockPos> blocksTwo = new ArrayList<>();
    private static final ArrayList<BlockPos> toRemoveBlocks = new ArrayList<>();
    private static final KeyBind keyBind = new KeyBind("Auto Build Farm", Keyboard.KEY_NONE);
    private static boolean isBuilding = false;
    private static boolean autoBuildThreadLock = false;
    private static BlockPos startPos = null;

    private static BlockPos currentBlockPos = null;
    private static BlockUtils.Face currentFacing = null;

    // TODO: mode
    public static void setFarmingPoint(int mode) {
        float x = getX(getPlayer());
        float y = getY(getPlayer()) - 0.01F;
        float z = getZ(getPlayer());
        blocksOne.clear();
        blocksTwo.clear();
        toRemoveBlocks.clear();

        startPos = new BlockPos(x, y, z);
        int sx = startPos.getX();
        int sy = startPos.getY();
        int sz = startPos.getZ();
        for (int i = 2; i < 253; ) {
            int r = (sy + 333333 - i) % 3;
            if (r == 1) {
                blocksOne.add(new BlockPos(sx, i, sz + 1));
                toRemoveBlocks.add(new BlockPos(sx, i, sz));

                blocksTwo.add(new BlockPos(sx, i + 1, sz - 3));
                blocksTwo.add(new BlockPos(sx, i + 1, sz - 2));
                blocksTwo.add(new BlockPos(sx, i + 1, sz - 1));
                blocksTwo.add(new BlockPos(sx, i + 1, sz));

                toRemoveBlocks.add(new BlockPos(sx, i + 2, sz));
                i += 3;
            } else {
                i++;
            }
        }
        Collections.reverse(blocksTwo);
        ChatLib.chat("Successfully set farming point!");
        ChatLib.chat("Build the horizontal blocks yourself and make sure no connected blocks within range. " +
                "After that, press keybind.");
    }

    private static BlockUtils.Face getFaceFromCenter(Vector3d v) {
        double eps = 1e-4;
        if (Math.abs((v.x - Math.round(v.x))) < eps) {
            return new BlockUtils.Face(v.x, v.y - 0.5, v.z - 0.5, v.x, v.y + 0.5, v.z + 0.5);
        }
        if (Math.abs((v.y - Math.round(v.y))) < eps) {
            return new BlockUtils.Face(v.x - 0.5, v.y, v.z - 0.5, v.x + 0.5, v.y, v.z + 0.5);
        }
        if (Math.abs((v.z - Math.round(v.z))) < eps) {
            return new BlockUtils.Face(v.x - 0.5, v.y - 0.5, v.z, v.x + 0.5, v.y + 0.5, v.z);
        }
        return null;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            isBuilding = !isBuilding;
            ChatLib.chat(isBuilding ? "Auto Build Farm - 1 &aactivated" : "Auto Build Farm - 1 &cdeactivated");
        }
        if (!Configs.AutoBuildFarm1) return;
        if (!isBuilding) return;
        if (autoBuildThreadLock) return;
        if (getPlayer() == null) return;
        autoBuildThreadLock = true;
        new Thread(() -> {
            try {
                ArrayList<BlockPos> blocks = new ArrayList<>(blocksOne);
                blocks.addAll(blocksTwo);
                OUT:
                for (BlockPos pos : blocks) {
                    currentBlockPos = pos;
                    while (BlockUtils.isBlockAir(pos)) {
                        ArrayList<BlockPos> nearest = new ArrayList<>(Arrays.asList(
                                pos.up(), pos.down(),
                                pos.east(), pos.west(),
                                pos.north(), pos.south())
                        );
                        boolean found = false;
                        Vector3d faceCenter = null;
                        for (BlockPos near : nearest) {
                            if (!BlockUtils.isBlockAir(near)) {
                                found = true;
                                faceCenter = new Vector3d(
                                        (near.getX() * 1.000001 + pos.getX() * 0.999999) / 2F + 0.5F,
                                        (near.getY() * 1.000001 + pos.getY() * 0.999999) / 2F + 0.5F,
                                        (near.getZ() * 1.000001 + pos.getZ() * 0.999999) / 2F + 0.5F
                                );
                                break;
                            }
                        }
                        if (!found) {
                            ChatLib.chat("Cannot find supporting block. " +
                                    "Make sure you're near the red block and all horizontal blocks have been placed.");
                            stop();
                            break OUT;
                        }
                        currentFacing = getFaceFromCenter(faceCenter);
                        int index = HotbarUtils.dirtWandSlot;
                        if (index == -1) {
                            ChatLib.chat("InfiniDirt Wand needed!");
                            stop();
                            break OUT;
                        }
                        Vector3d nearestVec = BlockUtils.getNearestBlock(
                                getPlayer().getPositionEyes(MathUtils.partialTicks),
                                currentFacing.mid,
                                false
                        );
                        if (nearestVec == null) {
                            ChatLib.chat("? currentFacing " + currentFacing + ", nearest null");
                            break OUT;
                        }
                        if (MathUtils.distanceSquareFromPlayer(nearestVec) > 22 * 22) {
                            ChatLib.chat("Get Closer!");
                            Thread.sleep(1000);
                            continue;
                        }
                        if (MathUtils.distanceSquaredFromPoints(nearestVec, faceCenter) > 0.5 * 0.5) {
                            ChatLib.chat("Face the orange face!");
                            Thread.sleep(1000);
                            continue;
                        }

                        ControlUtils.face(faceCenter.x, faceCenter.y, faceCenter.z);
                        ControlUtils.setHeldItemIndex(index);
                        ControlUtils.stopMoving();
                        Thread.sleep(250);
                        ControlUtils.rightClick();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            } finally {
                autoBuildThreadLock = false;
            }
        }).start();
    }

    private boolean enabled() {
        return isBuilding && Checker.enabled && Configs.AutoBuildFarm1;
    }

    private void stop() {
        stop(true);
    }

    private void stop(boolean b) {
        if (!isBuilding) return;
        if (getPlayer() != null && b)
            getPlayer().playSound("random.successful_hit", 1000, 1);
        isBuilding = false;
        ChatLib.chat("Auto Build Farm - 1 &cdeactivated");
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoBuildFarm1) return;
        ArrayList<BlockPos> positions = new ArrayList<>();
        positions.addAll(blocksOne);
        positions.addAll(blocksTwo);
        positions.addAll(toRemoveBlocks);
        for (BlockPos pos : positions) {
            if (BlockUtils.isBlockAir(pos.getX(), pos.getY(), pos.getZ()))
                GuiUtils.drawBoxAtBlock(pos, new Color(72, 50, 34, 80), 1, 1, 0);
        }
        GuiUtils.enableESP();
        if (currentBlockPos != null)
            GuiUtils.drawBoxAtBlock(currentBlockPos, new Color(255, 0, 0, 80), 1, 1, 0.0020000000949949026F);
        if (currentFacing != null)
            GuiUtils.drawFilledFace(currentFacing, new Color(224, 104, 51, 200));
        GuiUtils.disableESP();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        blocksOne.clear();
        blocksTwo.clear();
        toRemoveBlocks.clear();
        currentFacing = null;
        currentBlockPos = null;
        startPos = null;
    }
}
