package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.awt.Color;

public class PrismarineESP extends BlockESP {
    @Override
    public Block getBlock() {
        return Blocks.prismarine;
    }

    @Override
    public Color getColor() {
        return new Color(68, 232, 218, 120);
    }

    @Override
    public boolean isEnabled() {
        return Configs.PrismarineESP;
    }
}

