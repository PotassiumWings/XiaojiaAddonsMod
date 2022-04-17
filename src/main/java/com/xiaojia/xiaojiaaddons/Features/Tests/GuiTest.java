package com.xiaojia.xiaojiaaddons.Features.Tests;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.awt.Color;
import java.util.ArrayList;

public class GuiTest {
    private static final ArrayList<Vector3d> blocks = new ArrayList<>();

    public static void append(Vector3d pos) {
        synchronized (blocks) {
            blocks.add(pos);
        }
    }
    public static void clear() {
        synchronized (blocks) {
            blocks.clear();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.DevTracing) return;
//        Entity player = getPlayer();
        GuiUtils.enableESP();
        synchronized (blocks) {
            for (Vector3d pos : blocks) {
                GuiUtils.drawBoxAtPos(
                        (float) pos.x, (float)pos.y, (float)pos.z,
                        100, 160, 240, 255,
                        0.1F, 0.1F, 0
                );
            }
        }
//        GuiUtils.drawLine(0, 0, 0, 3, 4, 5, new Color(255, 0, 0), 2);
        GuiUtils.disableESP();
//        GuiUtils.drawBoxAtEntity(player, 0, 255, 0, 255, 0.5F, 1, 0);
//        GuiUtils.drawBoxAtBlock(MathUtils.getBlockX(player), MathUtils.getBlockY(player), MathUtils.getBlockZ(player), 255, 0, 0, 255, 1, 1, 0.01F);
//        GuiUtils.drawFilledBoxAtEntity(player, 0, 0, 255, 255, 0.2F, 1F, 0F);
//        GuiUtils.drawString("owo", 0, 100, 0, 0x003300, 0.5F, false);
//        GuiUtils.showTitle(ChatLib.addColor("&5owo"), "", 0, 5, 0);
    }
}
