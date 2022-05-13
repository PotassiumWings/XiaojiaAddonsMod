package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class BorrowHelper {

    public static Block getBlockAt(BlockPos pos) {
        if(getWorld() == null || pos == null) return null;
        return getWorld().getBlockState(pos).getBlock();
    }
    private boolean awaiting = false;
    private int particleCount = 0;
    private long lastItem = -1L;
    private Vec3 pos1 = null;
    private Vec3 pos2 = null;
    private Vec3 vec1 = null;
    private Vec3 vec2 = null;

    BlockPos solution;
    public void clear() {
        GuiUtils.disableESP();
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
        if (!Configs.BurrowHelper) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() == EnumParticleTypes.FIREWORKS_SPARK &&
                    packet.getXOffset() == 0f && packet.getYOffset() == 0f && packet.getZOffset() == 0f) {
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
        double y;
        for (y = 255; y > 0; y--) {
            solution = new BlockPos(x, y, z);
            if (getBlockAt(solution) == Blocks.grass || getBlockAt(solution) == Blocks.dirt) {
                y++;
                solution = new BlockPos(x, y, z);
                break;
            }
        }
        ChatLib.chat("Solution: (" + solution.getX() + ", " + solution.getZ() + ")");


        pos1 = pos2 = vec1 = vec2 = null;

    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (solution == null) return;
        GuiUtils.enableESP();
        GuiUtils.renderBeaconBeam(solution, 0xfc03ec, 0.3f, event.partialTicks);
        GuiUtils.disableESP();
    }
}
