package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class MathUtils {
    public static float partialTicks = 0;

    public static double distanceSquaredFromPoints(double x, double y, double z, double tx, double ty, double tz) {
        return (tx - x) * (tx - x) + (ty - y) * (ty - y) + (tz - z) * (tz - z);
    }

    public static double distanceSquareFromPlayer(double x, double y, double z) {
        double tx = getX(getPlayer()), ty = getY(getPlayer()), tz = getZ(getPlayer());
        return distanceSquaredFromPoints(x, y, z, tx, ty, tz);
    }

    public static double distanceSquareFromPlayer(Entity entity) {
        double tx = getX(entity), ty = getY(entity), tz = getZ(entity);
        return distanceSquareFromPlayer(tx, ty, tz);
    }

    public static float getX(Entity entity) {
        if (entity == null) return -10000;
        return (float) (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks);
    }

    public static float getY(Entity entity) {
        if (entity == null) return -10000;
        return (float) (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks);
    }

    public static float getZ(Entity entity) {
        if (entity == null) return -10000;
        return (float) (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

    public static int getBlockX(Entity entity) {
        return floor(getX(entity));
    }

    public static int getBlockY(Entity entity) {
        return floor(getY(entity));
    }

    public static int getBlockZ(Entity entity) {
        return floor(getZ(entity));
    }

    public static float getPitch() {
        if (getPlayer() == null) return 0;
        return getPlayer().rotationPitch;
    }

    public static float getYaw() {
        if (getPlayer() == null) return 0;
        return getPlayer().rotationYaw;
    }

    public static int floor(float x) {
        return (int) Math.floor(x);
    }

    public static int floor(double x) {
        return (int) Math.floor(x);
    }

    @SubscribeEvent
    public void onTick(RenderWorldLastEvent event) {
        partialTicks = event.partialTicks;
    }
}
