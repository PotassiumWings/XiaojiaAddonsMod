package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.util.BlockPos;

public class AutoMiQi extends AutoMiNi {
    public void init() {
        center = new BlockPos(305, 146, 238);
        super.init();
    }

    @Override
    public boolean enabled() {
        return Configs.AutoMiQi;
    }

    @Override
    public String getName() {
        return "Auto Mi Qi";
    }
}
