package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3f;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleESP {
    private final ConcurrentHashMap<Vector3f, Integer> particalPosTTL = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (Configs.ParticleESP) return;
        synchronized (particalPosTTL) {
            for (ConcurrentHashMap.Entry<Vector3f, Integer> entry : particalPosTTL.entrySet()) {
                Vector3f pos = entry.getKey();
                GuiUtils.enableESP();
                GuiUtils.drawBoxAtPos(
                        pos.getX(), pos.getY(), pos.getZ(),
                        65, 185, 65, 100, 1, 1, 0.01F
                );
                GuiUtils.disableESP();
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ParticleESP) return;
        synchronized (particalPosTTL) {
            ConcurrentHashMap<Vector3f, Integer> temp = new ConcurrentHashMap<>();
            for (ConcurrentHashMap.Entry<Vector3f, Integer> entry : particalPosTTL.entrySet()) {
                Vector3f pos = entry.getKey();
                int ttl = entry.getValue();
                ttl += 1;
                if (ttl < 10) {
                    temp.put(pos, ttl);
                }
            }
            particalPosTTL.clear();
            particalPosTTL.putAll(temp);
        }
    }

    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ParticleESP) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            Vector3f pos = new Vector3f((float) packet.getXCoordinate(), (float) packet.getYCoordinate(), (float) packet.getZCoordinate());
            if (packet.getParticleType().getParticleName().equals(Configs.ParticleESPType)) {
                synchronized (particalPosTTL) {
                    particalPosTTL.put(pos, 1);
                }
            }
        }
    }
}
