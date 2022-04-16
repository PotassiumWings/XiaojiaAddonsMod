package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoBuildFarm {
    private static final ArrayList<BlockPos> blocks = new ArrayList<>();
    private static final ArrayList<BlockPos> toRemoveBlocks = new ArrayList<>();
    private static BlockPos startPos = null;

    // TODO: mode
    public static void setFarmingPoint(int mode) {
        float x = getX(getPlayer());
        float y = getY(getPlayer()) - 0.01F;
        float z = getZ(getPlayer());
        blocks.clear();
        startPos = new BlockPos(x, y, z);
        int sx = startPos.getX();
        int sy = startPos.getY();
        int sz = startPos.getZ();
        for (int i = 2; i < 253; ) {
            int r = (sy + 333333 - i) % 3;
            if (r == 1) {
                blocks.add(new BlockPos(sx, i, sz + 1));
                toRemoveBlocks.add(new BlockPos(sx, i, sz));

                blocks.add(new BlockPos(sx, i + 1, sz));
                blocks.add(new BlockPos(sx, i + 1, sz - 1));
                blocks.add(new BlockPos(sx, i + 1, sz - 2));
                blocks.add(new BlockPos(sx, i + 1, sz - 3));

                toRemoveBlocks.add(new BlockPos(sx, i + 2, sz));
                i += 3;
            } else {
                i++;
            }
        }
        ChatLib.chat("Successfully set farming point!");
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoBuildFarm1) return;
        for (BlockPos pos : blocks) {
            GuiUtils.drawBoxAtBlock(pos, new Color(72, 50, 34, 120), 1, 1, 0);
        }
        for (BlockPos pos : toRemoveBlocks) {
            GuiUtils.drawBoxAtBlock(pos, new Color(72, 50, 34, 80), 1, 1, 0);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        blocks.clear();
        startPos = null;
    }
}
