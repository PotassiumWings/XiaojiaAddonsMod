package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class DevMode {
    public static final ArrayList<DisplayLine> lines = new ArrayList<>();

    private static String getLog(S2APacketParticles packetParticles) {
        StringBuilder s = new StringBuilder();
        for (int x : packetParticles.getParticleArgs())
            s.append(" ").append(x);

        return packetParticles.getParticleType() +
                String.format(" at %.2f %.2f %.2f, %.2f %.2f %.2f, %.2f, %d %b", packetParticles.getXCoordinate(),
                        packetParticles.getYCoordinate(), packetParticles.getZCoordinate(),
                        packetParticles.getXOffset(), packetParticles.getYOffset(), packetParticles.getZOffset(),
                        packetParticles.getParticleSpeed(), packetParticles.getParticleCount(),
                        packetParticles.isLongDistance()) +
                s;
    }

    private static String getLog(Entity entity) {
        String s = getClassLog(entity) + " "; // entity.getClass().toString() + " ";
        s += entity.hasCustomName() + " ";
        if (entity.hasCustomName()) s += entity.getName() + " ";
        s += MathUtils.getPosString(entity);
        return s;
    }

    private static String getClassLog(Entity entity) {
        String res = "";
        if (entity instanceof EntityArmorStand) {
            String name = entity.getDisplayName().getFormattedText();
            ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
            if (helm != null) res = String.format("AS(%s, %s)", name, helm.getDisplayName());
            else res = String.format("AS(%s)", name);
        } else if (entity instanceof EntityItem) {
            ItemStack item = ((EntityItem) entity).getEntityItem();
            res = String.format("EI(%s)", item.getDisplayName());
        } else if (entity instanceof EntityItemFrame) {
            ItemStack item = ((EntityItemFrame) entity).getDisplayedItem();
            if (item != null) res = String.format("EIF(%s)", item.getDisplayName());
        } else {
            res = entity.getClass().getSimpleName() + "(" + entity.getName() + ")";
        }
        return res;
    }

    @SubscribeEvent
    public void onReceive(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (!Configs.ParticleSpawnMessage) return;
        if (event.packet instanceof S2APacketParticles) {
            ChatLib.debug(getLog((S2APacketParticles) event.packet));
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (!Configs.EntityJoinEvent) return;
        ChatLib.debug(getLog(event.entity));
    }

    @SubscribeEvent
    public void onPacketSent(PacketSendEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (!Configs.PacketSent) return;
        ChatLib.debug(event.packet.getClass().toString());
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        synchronized (lines) {
            lines.clear();
            MovingObjectPosition pos = XiaojiaAddons.mc.objectMouseOver;
            if (pos == null) return;
            Entity entity = pos.entityHit;
            BlockPos block = pos.getBlockPos();
            if (entity != null) {
                DisplayLine line = new DisplayLine("Entity: " + getClassLog(entity));
                line.setScale(Configs.DisplayScale / 20F);
                lines.add(line);
            }
            if (block != null) {
                DisplayLine line = new DisplayLine("Block: " + BlockUtils.getBlockInfo(block));
                line.setScale(Configs.DisplayScale / 20F);
                lines.add(line);
            }
        }
    }
}
