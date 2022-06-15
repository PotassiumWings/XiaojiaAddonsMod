package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.DevMode;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Features.Skills.Fishing;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
        if (Configs.FindFairyGrottoMap && SkyblockUtils.isInCrystalHollows()) {
            DisplayLine line3 = new DisplayLine("Jasper: " + FindFairy.getBlock());
            line3.setScale(Configs.DisplayScale / 20F);
            display.addLine(line3);
        }
        if (Configs.DisplayPacketReceived) {
            DisplayLine line = new DisplayLine("Packet Received: " + PacketRelated.getReceivedQueueLength());
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (Configs.DisplayPacketSent) {
            DisplayLine line = new DisplayLine("Packet Sent: " + PacketRelated.getSentQueueLength());
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (Configs.DisplayBricks) {
            int bricks = 0;
            Inventory inventory = ControlUtils.getOpenedInventory();
            if (inventory != null)
                for (ItemStack itemStack : inventory.getItemStacks())
                    if (itemStack != null && itemStack.getItem() == Items.brick)
                        bricks += itemStack.stackSize;
            DisplayLine line = new DisplayLine("Bricks: &4" + bricks);
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        String timer = Fishing.timer();
        if (!timer.equals("")) {
            DisplayLine line = new DisplayLine("&cFishing Timer: " + timer);
            line.setScale(Configs.DisplayScale / 20F);
            display.addLine(line);
        }
        if (SessionUtils.isDev()) {
//            DisplayLine line = new DisplayLine("Name Cache size: " + ColorName.getCacheSize());
//            line.setScale(Configs.DisplayScale / 20F);
//            display.addLine(line);
//            DisplayLine bookCacheLine = new DisplayLine("Book Cache size: " + ShowBookName.getCacheSize());
//            bookCacheLine.setScale(Configs.DisplayScale / 20F);
//            display.addLine(bookCacheLine);
//            DisplayLine fixCacheLine = new DisplayLine("Fix Entity size: " + StarredMobESP.getSetSize());
//            fixCacheLine.setScale(Configs.DisplayScale / 20F);
//            display.addLine(fixCacheLine);
            DisplayLine line1 = new DisplayLine("yaw: " + MathUtils.getYaw());
            line1.setScale(Configs.DisplayScale / 20F);
            display.addLine(line1);
            DisplayLine line2 = new DisplayLine("pitch: " + MathUtils.getPitch());
            line2.setScale(Configs.DisplayScale / 20F);
            display.addLine(line2);

//            try {
//                Vec3 vec = XiaojiaAddons.mc.objectMouseOver.hitVec;
//                DisplayLine line5 = new DisplayLine(String.format("Moving Object: %.2f %.2f %.2f", vec.xCoord, vec.yCoord, vec.zCoord));
//                line5.setScale(Configs.DisplayScale / 20F);
//                display.addLine(line5);
//            } catch (Exception ignored) {}
            synchronized (DevMode.lines) {
                for (DisplayLine devLine: DevMode.lines)
                    display.addLine(devLine);
            }

//            EntityFishHook entity = getPlayer().fishEntity;
//            if (entity != null) {
//                DisplayLine line3 = new DisplayLine("Fish Entity: " + (entity.isInLava()? "Lava " : "") +
//                        (entity.isInWater()? "Water " : ""));
//                line3.setScale(Configs.DisplayScale / 20F);
//                display.addLine(line3);
//            }
//            DisplayLine line4 = new DisplayLine(String.format("%.2f",(TimeUtils.curTime() - Fishing.startPushing) / 1000F));
//            line4.setScale(Configs.DisplayScale / 20F);
//            display.addLine(line4);

            DisplayLine line4 = new DisplayLine(String.format("Hidden entity: %d", EntityQOL.getHiddenEntityCount()));
            line4.setScale(Configs.DisplayScale / 20F);
            display.addLine(line4);

//            DisplayLine line3 = new DisplayLine("pro: " + DevWater.process);
//            line2.setScale(Configs.DisplayScale / 20F);
//            display.addLine(line3);
//            DisplayLine line4 = new DisplayLine("sneaky queue: " + AutoSneakyCreeper.getSize());
//            line4.setScale(Configs.DisplayScale / 20F);
//            display.addLine(line4);
        }
    }
}
