package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Nether.BlockESP;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.awt.Color;

public class TitaniumESP extends BlockESP {
    @Override
    public Block getBlock() {
        return Blocks.stone;
    }

    public boolean check(int x, int y, int z) {
        IBlockState state = BlockUtils.getBlockStateAt(new BlockPos(x, y, z));
        if (state == null) return false;
        Block block = state.getBlock();
        int meta = BlockUtils.getMetaFromIBS(state);
        return (block == this.block && meta == 4);
    }

    @Override
    public Color getColor() {
        return new Color(68, 232, 218, 120);
    }

    @Override
    public boolean isEnabled() {
        return Configs.TitaniumESP;
    }
}
