package com.xiaojia.xiaojiaaddons.utils;

import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class BlockUtils {
    private static final Block[] interactiveBlocks = new Block[]{
            Blocks.chest, Blocks.trapped_chest, Blocks.lever,
            Blocks.skull, Blocks.stone_button, Blocks.wooden_button
    };

    public static boolean isInteractive(Block block) {
        for (Block iblock : interactiveBlocks) if (iblock == block) return true;
        return false;
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

    public static IBlockState getBlockStateAt(BlockPos pos) {
        if (getWorld() == null || pos == null) return null;
        return getWorld().getBlockState(pos);
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
                if (XiaojiaAddons.isDebug()) ChatLib.chat(result);
                return result;
            } else return "owo!";
        }
        return "owo?";
    }

    public static boolean isBlockAir(float x, float y, float z) {
        Block block = getBlockAt(x, y, z);
        return block.getUnlocalizedName().toLowerCase().contains("air");
    }

    public static boolean isBlockSapling(float x, float y, float z) {
        Block block = getBlockAt(x, y, z);
        return block.getRegistryName().toLowerCase().contains("sapling");
    }
}
