package com.xiaojia.xiaojiaaddons.utils;

import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class BlockUtils {
    private static final Block[] interactiveBlocks = new Block[]{
            Blocks.chest, Blocks.trapped_chest, Blocks.lever,
            Blocks.skull, Blocks.stone_button, Blocks.wooden_button
    };

    public static BlockPos getLookingAtPos(int dist) {
        Vector3d res = getLookingAtVector(dist);
        return new BlockPos(res.x, res.y, res.z);
    }

    public static Vector3d getLookingAtVector(int dist) {
        EntityPlayer player = getPlayer();
        Vec3 eye = player.getPositionEyes(MathUtils.partialTicks);
        Vec3 look = player.getLook(MathUtils.partialTicks);
        Vec3 farthest = eye.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist);
        return BlockUtils.getNearestBlock(eye, farthest);
    }

    public static boolean isInteractive(Block block) {
        for (Block iblock : interactiveBlocks) if (iblock == block) return true;
        return false;
    }

    public static boolean canGhostBlock(Block block) {
        if (isInteractive(block)) return false;
        return block != Blocks.bedrock;
    }

    public static Block getBlockAt(int x, int y, int z) {
        return getBlockAt(new BlockPos(x, y, z));
    }

    public static Block getBlockAt(float x, float y, float z) {
        return getBlockAt(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
    }

    public static Block getBlockAt(double x, double y, double z) {
        return getBlockAt(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
    }

    public static Block getBlockAt(BlockPos pos) {
        if (getWorld() == null || pos == null) return null;
        return getWorld().getBlockState(pos).getBlock();
    }

    public static Block getBlockAt(Vector3d pos) {
        return getBlockAt(pos.x, pos.y, pos.z);
    }

    public static IBlockState getBlockStateAt(BlockPos pos) {
        if (getWorld() == null || pos == null) return null;
        return getWorld().getBlockState(pos);
    }

    public static int getMetaAt(Vector3d pos) {
        IBlockState iBlockState = BlockUtils.getBlockStateAt(new BlockPos(pos.x, pos.y, pos.z));
        if (iBlockState == null) return -1;
        return iBlockState.getBlock().getMetaFromState(iBlockState);
    }

    public static void showBlockAt(int x, int y, int z) {
        Block block = getBlockAt(x, y, z);
        if (block instanceof BlockSkull) {
            ChatLib.chat("Skull!");
            ChatLib.chat(getTileProperty((BlockSkull) block, new BlockPos(x, y, z)));
        }
    }

    public static String getTileProperty(BlockSkull block, BlockPos blockPos) {
        TileEntitySkull tile = (TileEntitySkull) getWorld().getTileEntity(blockPos);
        if (tile != null && tile.getSkullType() == 3) {
            Property property = Iterables.getFirst(tile.getPlayerProfile().getProperties().get("textures"), null);
            if (property != null) {
                String result = property.getValue();
                return result;
            } else return "owo!";
        }
        return "owo?";
    }

    public static Vector3d getNearestBlock(Vec3 from, Vec3 to) {
        return getNearestBlock(
                new Vector3d(from.xCoord, from.yCoord, from.zCoord),
                new Vector3d(to.xCoord, to.yCoord, to.zCoord)
        );
    }

    public static BlockPos getNearestBlockPos(Vector3d from, Vector3d to) {
        Vector3d v = getNearestBlock(from, to);
        return new BlockPos(v.x, v.y, v.z);
    }

    public static Vector3d getNearestBlock(Vector3d from, Vector3d to) {
        // calculate from from to to
        Vector3d v = new Vector3d();
        v.normalize(MathUtils.diff(from, to));
        double epsilon = 1e-5;
        Vector3d curV = from;
        if (!isBlockAir(from.x, from.y, from.z))
            return from;
        while (true) {
            double xScale = (epsilon + MathUtils.ceil(curV.x) - curV.x) / v.x;
            double yScale = (epsilon + MathUtils.ceil(curV.y) - curV.y) / v.y;
            double zScale = (epsilon + MathUtils.ceil(curV.z) - curV.z) / v.z;
            if (v.x < 0) xScale = (MathUtils.floor(curV.x) - epsilon - curV.x) / v.x;
            if (v.y < 0) yScale = (MathUtils.floor(curV.y) - epsilon - curV.y) / v.y;
            if (v.z < 0) zScale = (MathUtils.floor(curV.z) - epsilon - curV.z) / v.z;

            double scale = xScale;
            if (yScale < scale) scale = yScale;
            if (zScale < scale) scale = zScale;
            curV = MathUtils.add(curV, MathUtils.mul(scale, v));
            if (MathUtils.floor(curV.x) == MathUtils.floor(to.x) &&
                    MathUtils.floor(curV.y) == MathUtils.floor(to.y) &&
                    MathUtils.floor(curV.z) == MathUtils.floor(to.z)) break;
            if (!BlockUtils.isBlockAir(curV.x, curV.y, curV.z))
                return curV;
        }
        return null;
    }

    public static boolean isBlockBedRock(double x, double y, double z) {
        Block block = getBlockAt(x, y, z);
        return block.getUnlocalizedName().toLowerCase().contains("bedrock");
    }

    public static boolean isBlockWater(double x, double y, double z) {
        Block block = getBlockAt(x, y, z);
        return block.getUnlocalizedName().toLowerCase().contains("water");
    }

    public static boolean isBlockAir(float x, float y, float z) {
        Block block = getBlockAt(x, y, z);
        return block.getUnlocalizedName().toLowerCase().contains("air");
    }

    public static boolean isBlockAir(double x, double y, double z) {
        Block block = getBlockAt(x, y, z);
        String lowerName = block.getUnlocalizedName().toLowerCase();
        return lowerName.contains("air") && !lowerName.matches(".*[_a-z]air[_a-z].*");
    }

    public static boolean isBlockSapling(float x, float y, float z) {
        Block block = getBlockAt(x, y, z);
        return block.getRegistryName().toLowerCase().contains("sapling");
    }

    public static boolean isBlockTeleportPad(int x, int y, int z) {
        Block block = getBlockAt(x, y, z);
        return block != null && Block.getIdFromBlock(block) == 120;
    }

    public static AxisAlignedBB getAABBOfBlock(BlockPos blockPos) {
        Block block = getBlockAt(blockPos);
        if (block == null) return null;
        return block.getSelectedBoundingBox(
                getWorld(),
                blockPos
        ).offset(
                -(mc.getRenderManager()).viewerPosX,
                -(mc.getRenderManager()).viewerPosY,
                -(mc.getRenderManager()).viewerPosZ
        ).expand(0.0010000000474974513D,
                0.0010000000474974513D,
                0.0010000000474974513D
        );
    }

    public static MovingObjectPosition watchingAt() {
        return getPlayer().rayTrace(mc.playerController.getBlockReachDistance(), 0F);
    }

    public static ArrayList<Face> getSurfaceMid(Vec3 from, BlockPos pos) {
        ArrayList<Face> res = new ArrayList<>();
        int px = pos.getX(), py = pos.getY(), pz = pos.getZ();
        double eps = 1e-6;
        // y
        if (from.yCoord < py)
            res.add(new Face(px, py + eps, pz, px + 1, py + eps, pz + 1));
        if (from.yCoord > py + 1)
            res.add(new Face(px, py + 1 - eps, pz, px + 1, py + 1 - eps, pz + 1));
        // x
        if (from.xCoord < px)
            res.add(new Face(px + eps, py, pz, px + eps, py + 1, pz + 1));
        if (from.xCoord > px + 1)
            res.add(new Face(px + 1 - eps, py, pz, px + 1 - eps, py + 1, pz + 1));
        // z
        if (from.zCoord < pz)
            res.add(new Face(px, py, pz + eps, px + 1, py + 1, pz + eps));
        if (from.zCoord > pz + 1)
            res.add(new Face(px, py, pz + 1 - eps, px + 1, py + 1, pz + 1 - eps));
        return res;
    }

    public static class Face {
        public Vec3 mid;
        public Vec3 v1, v2, v3, v4;
        double sx, sy, sz;
        double tx, ty, tz;

        public Face(double sx, double sy, double sz, double tx, double ty, double tz) {
            this.sx = sx;
            this.sy = sy;
            this.sz = sz;
            this.tx = tx;
            this.ty = ty;
            this.tz = tz;
            this.mid = new Vec3((sx + tx) / 2, (sy + ty) / 2, (sz + tz) / 2);
        }

        public String toString() {
            return mid.toString();
        }
    }
}
