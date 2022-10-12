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
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

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
        if (Configs.DisplayCoords)
            display.addLine(new DisplayLine("Coords: " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ()).setScale(Configs.DisplayScale / 20F));
        if (Configs.DisplayDay)
            display.addLine(new DisplayLine("Day " + worldDay).setScale(Configs.DisplayScale / 20F));
        if (Configs.DisplayPing)
            display.addLine(new DisplayLine("Ping: " + SkyblockUtils.getPing()).setScale(Configs.DisplayScale / 20F));
        if (Configs.FindFairyGrottoMap && SkyblockUtils.isInCrystalHollows())
            display.addLine(new DisplayLine("Jasper: " + FindFairy.getBlock()).setScale(Configs.DisplayScale / 20F));
        if (Configs.DisplayPacketReceived)
            display.addLine(new DisplayLine("Packet Received: " + PacketRelated.getReceivedQueueLength()).setScale(Configs.DisplayScale / 20F));
        if (Configs.DisplayPacketSent)
            display.addLine(new DisplayLine("Packet Sent: " + PacketRelated.getSentQueueLength()).setScale(Configs.DisplayScale / 20F));
        if (Configs.DisplayBricks) {
            int bricks = 0;
            Inventory inventory = ControlUtils.getOpenedInventory();
            if (inventory != null)
                for (ItemStack itemStack : inventory.getItemStacks())
                    if (itemStack != null && itemStack.getItem() == Items.brick)
                        bricks += itemStack.stackSize;
            display.addLine(new DisplayLine("Bricks: &c&l" + bricks).setScale(Configs.DisplayScale / 20F));
        }
        if (Configs.DisplayIronIngots) {
            int ironIngots = 0;
            Inventory inventory = ControlUtils.getOpenedInventory();
            if (inventory != null)
                for (ItemStack itemStack : inventory.getItemStacks())
                    if (itemStack != null && itemStack.getItem() == Items.iron_ingot)
                        ironIngots += itemStack.stackSize;
            display.addLine(new DisplayLine("Iron Ingots: &0&l" + ironIngots).setScale(Configs.DisplayScale / 20F));
        }
        String timer = Fishing.timer();
        if (!timer.equals(""))
            display.addLine(new DisplayLine("&cFishing Timer: " + timer).setScale(Configs.DisplayScale / 20F));
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
            display.addLine(new DisplayLine("yaw: " + MathUtils.getYaw()).setScale(Configs.DisplayScale / 20F));
            display.addLine(new DisplayLine("pitch: " + MathUtils.getPitch()).setScale(Configs.DisplayScale / 20F));

//            try {
//                Vec3 vec = XiaojiaAddons.mc.objectMouseOver.hitVec;
//                DisplayLine line5 = new DisplayLine(String.format("Moving Object: %.2f %.2f %.2f", vec.xCoord, vec.yCoord, vec.zCoord));
//                line5.setScale(Configs.DisplayScale / 20F);
//                display.addLine(line5);
//            } catch (Exception ignored) {}
            synchronized (DevMode.lines) {
                for (DisplayLine devLine : DevMode.lines)
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

            display.addLine(new DisplayLine(String.format("Hidden entity: %d", EntityQOL.getHiddenEntityCount())).setScale(Configs.DisplayScale / 20F));

//            DisplayLine line3 = new DisplayLine("pro: " + DevWater.process);
//            line2.setScale(Configs.DisplayScale / 20F);
//            display.addLine(line3);
//            DisplayLine line4 = new DisplayLine("sneaky queue: " + AutoSneakyCreeper.getSize());
//            line4.setScale(Configs.DisplayScale / 20F);
//            display.addLine(line4);
        }
        if (Configs.DisplayPlayers) {
            try {
                List<Entity> entities = EntityUtils.getEntities();
                entities.removeIf(e -> !EntityUtils.isPlayer(e));
                entities.sort((a, b) -> (int) (MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));

                display.addLine(new DisplayLine(String.format("&f&lNearby Players: (%d)", entities.size())).setScale(Configs.DisplayScale / 20F));
                entities.forEach(e -> display.addLine(new DisplayLine(" " + e.getName() + getDistanceString(e)).setScale(Configs.DisplayScale / 20F)));
            } catch (Exception ignored) {
            }
        }
    }

    private String getDistanceString(Entity e) {
        String color = "a";
        double dis = Math.sqrt(MathUtils.distanceSquareFromPlayer(e));
        if (dis < 10) color = "c";
        else if (dis < 50) color = "6";
        return String.format(": &%s&l%.2f", color, dis);
    }
}
