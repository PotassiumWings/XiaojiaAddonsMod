package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESP;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ShowLowestBin;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Features.Skills.Fishing;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
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
    public void renderString(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
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
        if (Configs.FindFairyGrottoMap) {
            DisplayLine line3 = new DisplayLine("Jasper: " + FindFairy.getBlock());
            line3.setScale(Configs.DisplayScale / 20F);
            display.addLine(line3);
        }
        String timer = Fishing.timer();
        if (!timer.equals("")) {
            DisplayLine line = new DisplayLine("&cFishing Timer: " + timer);
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (SessionUtils.getUUID().equals("1c6d48a96cb3465681382590ec82fa68")) {
            DisplayLine line = new DisplayLine("Name Cache size: " + ColorName.getCacheSize());
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
            DisplayLine bookCacheLine = new DisplayLine("Book Cache size: " + ShowBookName.getCacheSize());
            bookCacheLine.setScale(Configs.DisplayScale / 20F);
            display.addLine(bookCacheLine);
            DisplayLine fixCacheLine = new DisplayLine("Fix Entity size: " + StarredMobESP.getSetSize());
            fixCacheLine.setScale(Configs.DisplayScale / 20F);
            display.addLine(fixCacheLine);
            DisplayLine line1 = new DisplayLine("yaw: " + MathUtils.getYaw());
            line1.setScale(Configs.DisplayScale / 20F);
            display.addLine(line1);
            DisplayLine line2 = new DisplayLine("pitch: " + MathUtils.getPitch());
            line2.setScale(Configs.DisplayScale / 20F);
            display.addLine(line2);
            DisplayLine line5 = new DisplayLine("lastFetch: " + LowestBin.getLastUpdate() + "s");
            line5.setScale(Configs.DisplayScale / 20F);
            display.addLine(line5);
        }
    }
}
