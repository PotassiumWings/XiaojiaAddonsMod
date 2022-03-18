package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class MonolithESP {

    private final ArrayList<BlockPos> spawns = new ArrayList<>(Arrays.asList(new BlockPos[] {
            new BlockPos(-15, 236, -92), new BlockPos(49, 202, -162),
            new BlockPos(56, 214, -25), new BlockPos(0, 170, 0),
            new BlockPos(150, 196, 190), new BlockPos(-64, 206, -63),
            new BlockPos(-93, 221, -53), new BlockPos(-94, 201, -30),
            new BlockPos(-9, 162, 109), new BlockPos(1, 183, 23),
            new BlockPos(61, 204, 181), new BlockPos(77, 160, 162),
            new BlockPos(92, 187, 131), new BlockPos(128, 187, 58)
    }));

    private BlockPos monolithPos;

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
//        if (!Configs.MonolithESP) return;

    }
}
