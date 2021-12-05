package com.xiaojia.xiaojiaaddons.Features.Tests;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class GuiTest {
    private static boolean show = false;

    public static void changeShow() {
        show = !show;
        ChatLib.chat("" + show);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!show) return;
        Entity player = getPlayer();
        GuiUtils.drawBoxAtEntity(player, 0, 255, 0, 255, 0.5F, 1, 0);
//        GuiUtils.drawBoxAtBlock(MathUtils.getBlockX(player), MathUtils.getBlockY(player), MathUtils.getBlockZ(player), 255, 0, 0, 255, 1, 1, 0.01F);
//        GuiUtils.drawFilledBoxAtEntity(player, 0, 0, 255, 255, 0.2F, 1F, 0F);
//        GuiUtils.drawString("owo", 0, 100, 0, 0x003300, 0.5F, false);
//        GuiUtils.showTitle(ChatLib.addColor("&5owo"), "", 0, 5, 0);
    }
}
