package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketSendEvent extends Event {
    public final Packet<?> packet;

    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }
}
