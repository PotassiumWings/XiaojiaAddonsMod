package com.xiaojia.xiaojiaaddons.Features.Rift;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Nether.BlockESP;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.awt.*;

public class BarrierESP extends BlockESP {
    @Override
    public Block getBlock() {
        return Blocks.barrier;
    }

    @Override
    public Color getColor() {
        return new Color(68, 232, 218, 120);
    }

    @Override
    public boolean isEnabled() {
        return Configs.BarrierESP;
    }

    @Override
    public boolean enableESP() {
        return false;
    }
}
