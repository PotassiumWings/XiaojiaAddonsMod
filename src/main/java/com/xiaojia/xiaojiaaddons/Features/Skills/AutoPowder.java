package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getBlockY;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoPowder {
    private static final KeyBind keyBind = new KeyBind("Auto Powder", Keyboard.KEY_NONE);
    private final HashSet<BlockPos> solved = new HashSet<>();
    private boolean enabled = false;
    private BlockPos closestStone = null;
    private BlockPos closestChest = null;
    private boolean isOpeningChest = false;

    private boolean isMiningThreadRunning = false;
    private boolean isOpeningChestThreadRunning = false;
    private Vector3f particalPos = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowder) return;
        if (keyBind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "Auto Powder &aactivated" : "Auto Powder &cdeactivated");
        }
        if (!enabled) return;

        if (isMiningThreadRunning || isOpeningChestThreadRunning) return;

        // chest disappears
        if (isOpeningChest && getWorld().getBlockState(closestChest).getBlock() != Blocks.chest) {
            isOpeningChest = false;
        }
        closestStone = getClosest(-4, 4, 0, 2, -4, 4, Blocks.stone, new HashSet<>(), true);
        closestChest = getClosest(-4, 4, -1, 3, -4, 4, Blocks.chest, solved, false);

        if (particalPos != null) {
            isOpeningChest = true;
            isOpeningChestThreadRunning = true;

            ControlUtils.releaseLeftClick();
            System.err.println("Force Release");

            new Thread(() -> {
                Vector3f temp = (Vector3f) particalPos.clone();
                try {
                    ControlUtils.faceSlowly(particalPos.x, particalPos.y, particalPos.z);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isOpeningChestThreadRunning = false;
                    if (particalPos.equals(temp)) particalPos = null;
                }
            }).start();
        } else if (!isOpeningChest) {
            if (Math.random() < 0.05) {
                new Thread(() -> {
                    try {
                        ControlUtils.moveRandomly(400);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (closestStone != null) {
                if (isMiningThreadRunning) return;
                isMiningThreadRunning = true;
                new Thread(() -> {
                    try {
                        System.err.println("Start thread! " + closestStone);
                        ControlUtils.faceSlowly(closestStone.getX() + 0.5F, closestStone.getY() + 0.5F, closestStone.getZ() + 0.5F);
                        MovingObjectPosition objectPosition = mc.objectMouseOver;
                        if (objectPosition != null && objectPosition.typeOfHit.toString().equals("BLOCK")) {
                            BlockPos pos = objectPosition.getBlockPos();
                            if (pos.getY() >= getBlockY(getPlayer()) &&
                                    MathUtils.distanceSquareFromPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <=
                                            Math.pow(mc.playerController.getBlockReachDistance(), 2)
                            ) {
                                ControlUtils.holdLeftClick();
                                long cur = TimeUtils.curTime();
                                while (getWorld().getBlockState(pos).getBlock() != Blocks.air && TimeUtils.curTime() - cur < 200) {
                                    Thread.sleep(50);
                                }
                                ControlUtils.releaseLeftClick();
                                Thread.sleep(20);
                            }
                        }
                        ControlUtils.releaseLeftClick();
                    } catch (Exception e) {
                        stop();
                        e.printStackTrace();
                    } finally {
                        isMiningThreadRunning = false;
                        System.err.println("End thread! " + closestStone);
                    }
                }).start();
            }
        }
    }

    private BlockPos getClosest(int x1, int x2, int y1, int y2, int z1, int z2,
                                Block block, HashSet<BlockPos> illegal, boolean isStone) {
        BlockPos pos = getPlayer().getPosition();
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (int x = pos.getX() + x1; x <= pos.getX() + x2; x++) {
            for (int y = pos.getY() + y1; y <= pos.getY() + y2; y++) {
                for (int z = pos.getZ() + z1; z <= pos.getZ() + z2; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    if (getWorld().getBlockState(blockPos).getBlock() == block && !illegal.contains(blockPos) &&
                            MathUtils.distanceSquareFromPlayer(blockPos.getX() + 0.5F, blockPos.getY() + 0.5F, blockPos.getZ() + 0.5F) <=
                                    Math.pow(mc.playerController.getBlockReachDistance(), 2)
                    ) {
                        if (isStone && !MathUtils.checkBlocksBetween(x, y, z)) continue;
                        blocks.add(blockPos);
                    }
                }
            }
        }
        blocks.sort((BlockPos a, BlockPos b) -> MathUtils.yawPitchSquareFromPlayer(a.getX() + 0.5F, a.getY() + 0.5F, a.getZ() + 0.5F) >
                MathUtils.yawPitchSquareFromPlayer(b.getX() + 0.5F, b.getY() + 0.5F, b.getZ() + 0.5F) ? 1 : -1);
        if (blocks.isEmpty()) return null;
        int size = blocks.size();
        double rand = Math.random() * size;
        int fm = Math.min((int) (rand * rand * rand) + 1, size);
        return blocks.get(size / fm - 1);
    }

    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent event) {
        if (!Configs.AutoPowder || !enabled) return;
        // TODO: CH CHECK
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() == EnumParticleTypes.CRIT) {
                Vector3f pos = new Vector3f((float) packet.getXCoordinate(), (float) packet.getYCoordinate() - 0.1F, (float) packet.getZCoordinate());
                if (closestChest != null && MathUtils.distanceSquaredFromPoints(
                        pos.x, pos.y, pos.z,
                        closestChest.getX() + 0.5, closestChest.getY() + 0.5, closestChest.getZ() + 0.5
                ) < 1) {
                    particalPos = pos;
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!Configs.AutoPowder || !enabled) return;
        if (closestStone != null) {
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(
                    closestStone.getX(), closestStone.getY(), closestStone.getZ(),
                    65, 185, 65, 180, 1, 1, 0.01F
            );
            GuiUtils.disableESP();
        }
        if (closestChest != null) {
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(
                    closestChest.getX(), closestChest.getY(), closestChest.getZ(),
                    185, 65, 65, 180, 1, 1, 0.01F
            );
            GuiUtils.disableESP();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        solved.clear();
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Configs.AutoPowder || !enabled) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().contains("Treasure")) return;
        ControlUtils.releaseLeftClick();
        getPlayer().closeScreen();
        if (closestChest != null) solved.add(closestChest);
        isOpeningChest = false;
        System.err.println("Closing chest, stop opening chest");
    }

    private void stop() {
        if (enabled) {
            enabled = false;
            isMiningThreadRunning = false;
            isOpeningChest = isOpeningChestThreadRunning = false;
            getPlayer().playSound("random.successful_hit", 1000, 1);
            ControlUtils.releaseLeftClick();
            ChatLib.chat("Auto Powder &cdeactivated");
        }
    }
}
