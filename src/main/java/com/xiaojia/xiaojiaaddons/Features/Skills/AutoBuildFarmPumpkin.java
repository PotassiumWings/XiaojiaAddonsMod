package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoBuildFarmPumpkin {
    private static int from, to;
    private static final ArrayList<BlockPos> dirt = new ArrayList<>();
    private static final ArrayList<BlockPos> light = new ArrayList<>();

    public static void setFarmingPoint(int fromZ, int toZ) {
        float x = getX(getPlayer());
        float y = getY(getPlayer()) - 0.01F;
        float z = getZ(getPlayer());
        from = fromZ;
        to = toZ;
        dirt.clear();
        light.clear();

        BlockPos startPos = new BlockPos(x, y, z);
        int sx = startPos.getX(), sy = startPos.getY(), sz = startPos.getZ();
        for (int i = from; i < to; i++) {
            int r = (i + 5555 - sz) % 5;
            if (r == 0) {
                dirt.add(new BlockPos(sx, sy, i));
                dirt.add(new BlockPos(sx, sy - 1, i));
            } else if (r == 1 || r == 2) {
                dirt.add(new BlockPos(sx, sy, i));
            } else if (r == 3) {
                dirt.add(new BlockPos(sx, sy, i));
                dirt.add(new BlockPos(sx, sy - 1, i));
            } else {
                light.add(new BlockPos(sx, sy + 1, i));
                dirt.add(new BlockPos(sx, sy - 1, i));
            }
        }
        ChatLib.chat("Successfully set farming point!");
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for (BlockPos pos : dirt) {
            if (BlockUtils.isBlockAir(pos))
                GuiUtils.drawBoxAtBlock(pos, new Color(72, 50, 34, 120),1, 1, 0);
        }
        for (BlockPos pos : light) {
            if (BlockUtils.isBlockAir(pos))
                GuiUtils.drawBoxAtBlock(pos, new Color(0x3C, 0x3C, 0xDE, 120),1, 1, 0);
        }
    }

    @SubscribeEvent
    public void onChange(WorldEvent.Load event) {
        dirt.clear();
        light.clear();
    }
}
