package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class PacketUtils {
    public static String getPosLookPacket(S08PacketPlayerPosLook packet) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.2f, %.2f; %.2f, %.2f, %.2f: ",
                packet.getYaw(), packet.getPitch(), packet.getX(), packet.getY(), packet.getZ()));
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) sb.append("X");
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) sb.append("Y");
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) sb.append("Z");
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) sb.append(" XR");
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) sb.append(" YR");
        sb.append("\n");
        return sb.toString();
    }
}
