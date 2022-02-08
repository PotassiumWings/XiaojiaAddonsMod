package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class DisplayDayAndCoords {
    private final Display display = new Display();

    public DisplayDayAndCoords() {
        display.setShouldRender(true);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("left");
    }

    @SubscribeEvent
    public void renderString(RenderGameOverlayEvent event) {
        if (!Checker.enabled) return;
//        if (!SkyblockUtils.isInSkyblock()) return;
        EntityPlayer player = getPlayer();
        BlockPos blockPos = MathUtils.getBlockPos();
        if (player == null || blockPos == null) return;
        int worldDay = MathUtils.floor(player.worldObj.getWorldTime() / 24000.0);
        display.clearLines();
        display.setRenderLoc(Configs.DisplayDayX, Configs.DisplayDayY);
        if (Configs.DisplayCoords) {
            DisplayLine line = new DisplayLine("Coords: " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (Configs.DisplayDay) {
            DisplayLine line = new DisplayLine("Day " + worldDay);
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (Configs.DisplayPing) {
            DisplayLine line = new DisplayLine("Ping: " + SkyblockUtils.getPing());
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (SessionUtils.getUUID().equals("1c6d48a96cb3465681382590ec82fa68")) {
            DisplayLine line = new DisplayLine("Cache size: " + ColorName.getCacheSize());
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
            DisplayLine line1 = new DisplayLine("motionY: " + SkyblockUtils.getMotionY());
            line1.setScale(Configs.DisplayScale / 20F);
            display.addLine(line1);
            DisplayLine line2 = new DisplayLine("velocity: " + SkyblockUtils.getVelocity());
            line2.setScale(Configs.DisplayScale / 20F);
            display.addLine(line2);
            DisplayLine line3 = new DisplayLine("v/m: " + SkyblockUtils.getVelocity() / SkyblockUtils.getMotionY());
            line3.setScale(Configs.DisplayScale / 20F);
            display.addLine(line3);
        }
    }
}
