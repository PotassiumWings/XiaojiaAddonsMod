package com.xiaojia.xiaojiaaddons.Features.Tests;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static net.minecraft.realms.RealmsMth.floor;

public class GuiTest {
    private static boolean show = false;

    public static void changeShow() {
        show = !show;
        ChatLib.chat("" + show);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!show) return;
        Entity player = Minecraft.getMinecraft().thePlayer;
        GuiUtils.drawBoxAtEntity(player, 0, 255, 0, 255, 0.5F, 1, 0);
        GuiUtils.drawBoxAtBlock(floor(getX(player) - 10), floor(getY(player) - 5), floor(getZ(player) - 10), 255, 0, 0, 255, 5, 5);
        GuiUtils.drawFilledBoxAtEntity(player, 0, 0, 255, 255, 0.2F, 0.2F, 0.2F);
        GuiUtils.drawString("owo", getX(player) - 10, getY(player) - 5, getZ(player) - 10, 0x003300, false, 1, false);
    }
}
