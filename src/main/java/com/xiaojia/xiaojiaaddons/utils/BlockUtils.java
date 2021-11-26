package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockUtils {
    private static final Block[] interactiveBlocks = new Block[]{
            Blocks.chest, Blocks.trapped_chest, Blocks.lever,
            Blocks.skull, Blocks.stone_button, Blocks.wooden_button
    };

    public static boolean isInteractive(Block block) {
        for (Block iblock: interactiveBlocks) if (iblock == block) return true;
        return false;
    }
}
