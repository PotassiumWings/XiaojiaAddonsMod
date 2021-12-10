package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoPowder {
    private static final KeyBind keyBind = new KeyBind("Auto Powder", Keyboard.KEY_NONE);
    private final HashSet<BlockPos> solved = new HashSet<>();
    private boolean enabled = false;
    private BlockPos closestStone = null;
    private BlockPos closestChest = null;
    private boolean stopHardstone = false;
    private boolean doingHardstone = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (true) return;
        if (!Checker.enabled) return;
        if (!Configs.AutoPowder) return;
        if (keyBind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "Auto Powder &aactivated" : "Auto Powder &cdeactivated");
        }
        if (!enabled || doingHardstone) return;
        closestStone = getClosest(-6, 6, 0, 2, -6, 6, Blocks.stone, new HashSet<>());
        closestChest = getClosest(-5, 5, -5, 5, -5, 5, Blocks.chest, solved);
        if (closestStone != null) ChatLib.chat("stone: " + closestStone.toString());
        if (closestChest != null) ChatLib.chat("chest: " + closestChest.toString());
        if (closestChest != null) stopHardstone = true;
        if (stopHardstone) {
            if (getWorld().getBlockState(closestChest).getBlock() != Blocks.chest) {
                stopHardstone = false;
                if (XiaojiaAddons.isDebug()) ChatLib.chat("unlock");
            }
        }
        if (closestStone != null && !stopHardstone && !doingHardstone) {
            doingHardstone = true;
            new Thread(() -> {
                try {
                    ControlUtils.faceSlowly(closestStone);
                    MovingObjectPosition objectPosition = mc.objectMouseOver;
                    if (objectPosition != null && objectPosition.typeOfHit.toString().equals("BLOCK")) {
                        BlockPos pos = objectPosition.getBlockPos();
                        Block block = getWorld().getBlockState(pos).getBlock();
                        if (block == Blocks.stone || block == Blocks.coal_ore ||
                                block == Blocks.diamond_ore || block == Blocks.emerald_ore ||
                                block == Blocks.gold_ore || block == Blocks.iron_ore ||
                                block == Blocks.lapis_ore || block == Blocks.redstone_ore
                        ) {
                            ControlUtils.holdLeftClick();
                        } else {
                            ControlUtils.releaseLeftClick();
                            doingHardstone = false;
                        }
                    }
                } catch (Exception e) {
                    stop();
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private BlockPos getClosest(int x1, int x2, int y1, int y2, int z1, int z2, Block block, HashSet<BlockPos> illegal) {
        BlockPos pos = getPlayer().getPosition();
        ChatLib.chat("player: " + pos.toString());
        ArrayList<BlockPos> stones = new ArrayList<>();
        for (int x = pos.getX() + x1; x <= pos.getX() + x2; x++) {
            for (int y = pos.getY() + y1; y <= pos.getY() + y2; y++) {
                for (int z = pos.getY() + z1; z <= pos.getY() + z2; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    ChatLib.chat(getWorld().getBlockState(blockPos).getBlock().toString());
                    if (getWorld().getBlockState(blockPos).getBlock() == block && !illegal.contains(blockPos)) {
                        stones.add(blockPos);
                        ChatLib.chat(blockPos.toString());
                    }
                }
            }
        }
        double distance = 1000;
        BlockPos owo = null;
        for (BlockPos blockPos : stones) {
            double d2 = MathUtils.distanceSquareFromPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (d2 < distance && d2 < 6) {
                distance = d2;
                owo = blockPos;
            }
        }
        return owo;
    }

    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent event) {
        if (true) return;
        if (!Configs.AutoPowder || !enabled || doingHardstone) return;
        // TODO: CH CHECK
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() == EnumParticleTypes.CRIT) {
                BlockPos pos = new BlockPos(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
                if (closestChest != null && closestChest.distanceSq(pos) < 1) {
                    stopHardstone = true;
                    new Thread(() -> {
                        try {
                            ControlUtils.faceSlowly(pos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (true) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().contains("Treasure")) return;
        if (doingHardstone) {
            ControlUtils.releaseLeftClick();
            doingHardstone = false;
            ChatLib.chat("NONONONO");
            getPlayer().closeScreen();
        }

    }

    private void stop() {
        if (enabled) {
            enabled = false;
            doingHardstone = false;
            stopHardstone = false;

            ChatLib.chat("Auto Powder &cdeactivated");
        }
    }
}
