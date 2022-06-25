package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.awt.Color;

public class SpongeESP extends BlockESP {
    @Override
    public Block getBlock() {
        return Blocks.sponge;
    }

    @Override
    public Color getColor() {
        return new Color(255, 242, 82, 120);
    }

    @Override
    public boolean isEnabled() {
        return Configs.SpongeESP;
    }
}
