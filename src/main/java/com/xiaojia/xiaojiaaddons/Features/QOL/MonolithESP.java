package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class MonolithESP {
    private final ArrayList<BlockPos> spawns = new ArrayList<>(Arrays.asList(
            new BlockPos(-15, 236, -92), new BlockPos(49, 202, -162),
            new BlockPos(56, 214, -25), new BlockPos(1, 170, 0),
            new BlockPos(150, 196, 190), new BlockPos(-64, 206, -63),
            new BlockPos(-91, 221, -53), new BlockPos(-94, 201, -30),
            new BlockPos(-10, 162, 109), new BlockPos(1, 183, 25),
            new BlockPos(61, 204, 181), new BlockPos(77, 160, 162),
            new BlockPos(91, 187, 131), new BlockPos(128, 187, 58)));

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.MonolithESP) return;
        if (!SkyblockUtils.isInDwarven()) return;
        BlockPos found = null;
        for (BlockPos pos : spawns) {
            for (BlockPos subPos:BlockPos.getAllInBox(pos.add(-4, -2, -4), pos.add(4, 2, 4))) {
                if (BlockUtils.getBlockAt(subPos) == Blocks.dragon_egg) {
                    found = subPos;
                    break;
                }
            }
            if (found != null) break;
        }

        GuiUtils.enableESP();
        if (found == null) {
            for (BlockPos pos : spawns) {
                GuiUtils.drawBoxAtBlock(pos, new Color(0, 255, 0, 120), 1, 1, 0.0020000000949949026F);
            }
        } else {
            GuiUtils.drawBoxAtBlock(found, new Color(255, 0, 0, 120), 1, 1, 0.0020000000949949026F);
        }
        GuiUtils.disableESP();
    }
}
