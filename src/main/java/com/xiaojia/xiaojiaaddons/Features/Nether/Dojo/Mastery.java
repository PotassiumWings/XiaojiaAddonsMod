package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashMap;

public class Mastery {
    private final HashMap<BlockPos, Long> countDown = new HashMap<>();

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.MasteryHelper) return;
        if (DojoUtils.getTask() != EnumDojoTask.MASTERY) return;
        long cur = TimeUtils.curTime();
        for (BlockPos pos : countDown.keySet()) {
            int delta = (int) (7000 - (cur - countDown.get(pos)));
            Color color = new Color(0, 0, 255);
            if (delta >= 4000 && delta < 7000) color = new Color((7000 - delta) * 255 / 3000, 255, 0);
            else if (delta >= 1000 && delta < 4000) color = new Color(255, 255 - (4000 - delta) * 255 / 3000, 0);
            else if (delta < 1000 && delta > 0) color = new Color(255, 0, 0);
            else ChatLib.debug(delta + "");
            GuiUtils.drawString(
                    String.format("%.2f s", delta / 1000F),
                    pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                    color.getRGB(),
                    1.5F, true
            );
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (!Checker.enabled) return;
        if (event.oldBlock.getBlock() != Blocks.wool && event.newBlock.getBlock() == Blocks.wool) {
            countDown.put(event.position, TimeUtils.curTime());
        } else if (event.oldBlock.getBlock() == Blocks.wool && event.newBlock.getBlock() != Blocks.wool) {
            countDown.remove(event.position);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        countDown.clear();
    }
}
