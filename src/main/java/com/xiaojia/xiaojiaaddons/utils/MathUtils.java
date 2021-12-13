package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class MathUtils {
    public static float partialTicks = 0;

    public static boolean equal(Vector3d a, Vector3d b) {
        double delta = distanceSquaredFromPoints(a.x, a.y, a.z, b.x, b.y, b.z);
        return (delta < 1e-5);
    }

    public static double yawPitchSquareFromPlayer(float x, float y, float z) {
        float yaw = getYaw(), pitch = getPitch();
        Tuple<Float, Float> res = ControlUtils.getFaceYawAndPitch(x, y, z);
        float toYaw = res.getFirst(), toPitch = res.getSecond();
        float deltaYaw = Math.abs(yaw - toYaw), deltaPitch = Math.abs(pitch - toPitch);
        deltaYaw = Math.min(deltaYaw, 360 - deltaYaw);
        return deltaYaw * deltaYaw + deltaPitch * deltaPitch;
    }

    public static boolean checkBlocksBetween(int x, int y, int z) {
        float px = getX(getPlayer()), py = getY(getPlayer()) + 1.5F, pz = getZ(getPlayer());
        float tx = x + 0.5F, ty = y + 0.5F, tz = z + 0.5F;
        float dx = tx - px, dy = ty - py, dz = tz - pz;
        int times = 20;
        for (int i = 0; i < times; i++) {
            float qx = px + dx * i / times;
            float qy = py + dy * i / times;
            float qz = pz + dz * i / times;
            if (!isBlockMinableOrAir(qx, qy, qz)) return false;
        }
        return true;
    }

    private static boolean isBlockMinableOrAir(float qx, float qy, float qz) {
        Block block = getWorld().getBlockState(new BlockPos(qx, qy, qz)).getBlock();
        boolean res = block == Blocks.air || block == Blocks.stone || block == Blocks.coal_ore ||
                block == Blocks.diamond_ore || block == Blocks.emerald_ore ||
                block == Blocks.gold_ore || block == Blocks.iron_ore ||
                block == Blocks.lapis_ore || block == Blocks.redstone_ore;
        return res;
    }

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

    public static double distanceSquareFromPlayer(BlockPos pos) {
        double tx = pos.getX(), ty = pos.getY(), tz = pos.getZ();
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
