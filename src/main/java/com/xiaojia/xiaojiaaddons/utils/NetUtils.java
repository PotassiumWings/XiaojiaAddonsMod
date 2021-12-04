package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.network.Packet;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NetUtils {
    public static final <T extends net.minecraft.network.INetHandler> void sendPacket(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
