package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnPacketReceived extends Event {
    public Packet packet;

    public OnPacketReceived(Packet packet) {
        this.packet = packet;
    }
}
