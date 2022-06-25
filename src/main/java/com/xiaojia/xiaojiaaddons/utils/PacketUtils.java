package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Field;

public class PacketUtils {
    public static Field messageField = null;

    static {
        try {
            messageField = S45PacketTitle.class.getDeclaredField("field_179810_b");
        } catch (NoSuchFieldException e) {
            try {
                messageField = S45PacketTitle.class.getDeclaredField("message");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        messageField.setAccessible(true);
    }


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

    public static IChatComponent getMessage(S45PacketTitle packet) {
        try {
            return (IChatComponent) messageField.get(packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setMessage(S45PacketTitle packet, IChatComponent replace) {
        try {
            messageField.set(packet, replace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
