package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MathUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static float partialTicks = 0;

    public static double distanceSquaredFromPoints(double x, double y, double z, double tx, double ty, double tz) {
        return (tx - x) * (tx - x) + (ty - y) * (ty - y) + (tz - z) * (tz - z);
    }

    public static double distanceSquareFromPlayer(double x, double y, double z) {
        EntityPlayerSP player = mc.thePlayer;
        double tx = getX(player), ty = getY(player), tz = getZ(player);
        return distanceSquaredFromPoints(x, y, z, tx, ty, tz);
    }

    public static float getX(Entity entity) {
        return (float) (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks);
    }

    public static float getY(Entity entity) {
        return (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks);
    }

    public static float getZ(Entity entity) {
        return (float) (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

    @SubscribeEvent
    public void onTick(RenderWorldLastEvent event) {
        partialTicks = event.partialTicks;
    }
}
