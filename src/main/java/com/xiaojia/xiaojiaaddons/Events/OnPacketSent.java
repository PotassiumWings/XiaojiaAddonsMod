package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnPacketSent extends Event {
    public Packet packet;

    public OnPacketSent(Packet packet) {
        this.packet = packet;
    }
}
