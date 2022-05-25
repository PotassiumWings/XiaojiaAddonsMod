package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class BurrowHelper {
    private BlockPos solution;
    private boolean awaiting = false;
    private int particleCount = 0;
    private long lastItem = -1L;
    private Vec3 pos1 = null;
    private Vec3 pos2 = null;
    private Vec3 vec1 = null;
    private Vec3 vec2 = null;

    public void clear() {
        pos1 = null;
        pos2 = null;
        vec1 = null;
        vec2 = null;
        awaiting = false;
        solution = null;
        lastItem = -1L;
        particleCount = 0;
    }

    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BurrowHelper) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() == EnumParticleTypes.FIREWORKS_SPARK &&
                    MathUtils.equal(packet.getXOffset(), 0) &&
                    MathUtils.equal(packet.getYOffset(), 0) &&
                    MathUtils.equal(packet.getZOffset(), 0)) {
                particleCount++;
                if (awaiting) {
                    if (particleCount == 10 && pos1 == null) {
                        pos1 = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
                        awaiting = false;
                    } else if (particleCount == 10 && pos2 == null) {
                        pos2 = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
                        awaiting = false;
                    }
                } else {
                    if (vec1 == null && pos1 != null) {
                        vec1 = new Vec3(packet.getXCoordinate() - pos1.xCoord, packet.getYCoordinate() - pos1.yCoord, packet.getZCoordinate() - pos1.zCoord).normalize();
                    } else if (vec2 == null && pos2 != null) {
                        vec2 = new Vec3(packet.getXCoordinate() - pos2.xCoord, packet.getYCoordinate() - pos2.yCoord, packet.getZCoordinate() - pos2.zCoord).normalize();
                        calculateIntercept();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BurrowHelper) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (item == null) return;
            if (StringUtils.stripControlCodes(item.getDisplayName()).contains("Ancestral Spade")) {
                if (Configs.BlockAncestralSpade && System.currentTimeMillis() - lastItem < 4000) {
                    ChatLib.chat("Last trail hasn't disappeared yet, chill.");
                    event.setCanceled(true);
                    return;
                }
                double pitch = MathUtils.validPitch(MathUtils.getPitch());
                if (Configs.BlockInvalidClicks && !MathUtils.equal(pitch, 90) && !MathUtils.equal(pitch, -90) ) {
                    ChatLib.chat("Look straight up or down!");
                    event.setCanceled(true);
                    return;
                }
                awaiting = true;
                particleCount = 0;
                lastItem = System.currentTimeMillis();
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        clear();
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BurrowHelper) return;
        if (event.type != 0) return;
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        //&r&eYou dug out a Griffin Burrow! &r&7(?/4)&r
        //&r&eYou finished the Griffin burrow chain! &r&7(4/4)&r
        if (message.equals("You finished the Griffin burrow chain! (4/4)")) {
            clear();
        } else if (message.startsWith("You dug out a Griffin Burrow!")) {
            clear();
        }
    }

    // Thank you, Ollie, for doing this for me, so I don't have to
    private void calculateIntercept() {
        double p1x = pos1.xCoord;
        double p1z = pos1.zCoord;
        double v1x = vec1.xCoord;
        double v1z = vec1.zCoord;

        double p2x = pos2.xCoord;
        double p2z = pos2.zCoord;
        double v2x = vec2.xCoord;
        double v2z = vec2.zCoord;

        double a = v1z / v1x * p1x - p1z;
        double b = v2z / v2x * p2x - p2z;
        double x = (a - b) / (v1z / v1x - v2z / v2x);
        double z = v1z / v1x * x - a;
        solution = new BlockPos(x, calcHighestGrass(x, z), z);
        ChatLib.chat("Solution: (" + solution.getX() + ", " + solution.getZ() + ")");

        pos1 = pos2 = vec1 = vec2 = null;
    }

    private static double calcHighestGrass(double x, double z) {
        double y;
        for (y = 255; y > 0; y--) {
            BlockPos solution = new BlockPos(x, y, z);
            if (BlockUtils.getBlockAt(solution) == Blocks.grass || BlockUtils.getBlockAt(solution) == Blocks.dirt)
                break;
        }
        return y + 1;
    }

    private static long lastUpdate = 0;

    @SubscribeEvent
    public void onTickUpdate(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BurrowHelper) return;
        long cur = TimeUtils.curTime();
        if (solution != null && solution.getY() < 5 && cur - lastUpdate > 1000) {
            lastUpdate = cur;
            int x = solution.getX(), z = solution.getZ();
            solution = new BlockPos(x, calcHighestGrass(x, z), z);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (solution == null) return;
        GuiUtils.enableESP();
        GuiUtils.renderBeaconBeam(solution, 0xfc03ec, 0.5f);
        GuiUtils.drawBoxAtBlock(solution.down(), new Color(0x80FC03EC, true), 1, 1, 0.0020000000949949026F);
        GuiUtils.disableESP();
    }
}
