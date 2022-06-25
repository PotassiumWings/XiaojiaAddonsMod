package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.NetUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Blaze {
    private static final ArrayList<String> states = new ArrayList<>(Arrays.asList(
            "SPIRIT", "CRYSTAL",
            "ASHEN", "AURIC"
    ));
    // pillar display
    private static final Display display = new Display();
    private static long lastSwapSPICRY = 0;
    private static long lastSwapASHAUR = 0;
    private AxisAlignedBB box = null;

    public Blaze() {
        display.setShouldRender(false);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("center");
    }

    public static void doSwap(String state) {
        ChatLib.debug("Current state: " + state);
        boolean shouldClick = false;
        int swapIndex = -1;
        ArrayList<String> daggerIds;
        boolean isASHAUR = state.equals("ASHEN") || state.equals("AURIC");
        if (isASHAUR)
            daggerIds = new ArrayList<>(Arrays.asList("FIREDUST_DAGGER", "BURSTFIRE_DAGGER", "HEARTFIRE_DAGGER"));
        else daggerIds = new ArrayList<>(Arrays.asList("MAWDUST_DAGGER", "BURSTMAW_DAGGER", "HEARTMAW_DAGGER"));

        for (int i = 0; i < 8; i++) {
            ItemStack itemStack = ControlUtils.getItemStackInSlot(i + 36, true);
            String sbId = NBTUtils.getSkyBlockID(itemStack);
            if (daggerIds.stream().anyMatch(e -> e.equals(sbId))) {
                List<String> lore = NBTUtils.getLore(itemStack);
                AttunedState curState = null;
                for (String formatted : lore) {
                    String unformatted = ChatLib.removeFormatting(formatted);
                    if (unformatted.equals("Attuned: Ashen")) curState = AttunedState.ASHEN;
                    if (unformatted.equals("Attuned: Auric")) curState = AttunedState.AURIC;
                    if (unformatted.equals("Attuned: Spirit")) curState = AttunedState.SPIRIT;
                    if (unformatted.equals("Attuned: Crystal")) curState = AttunedState.CRYSTAL;
                }
                if (curState != null) {
                    swapIndex = i;
                    shouldClick = !curState.toString().equals(state);
                    break;
                }
            }
        }
        ControlUtils.setHeldItemIndex(swapIndex);
        if (shouldClick) {
            long cur = TimeUtils.curTime();
            long delta = cur - (isASHAUR ? lastSwapASHAUR : lastSwapSPICRY);
            if (delta < Configs.BlazeHelperCD) return;

            if (isASHAUR) lastSwapASHAUR = cur;
            else lastSwapSPICRY = cur;

            NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(
                    new BlockPos(-1, -1, -1),
                    255,
                    ControlUtils.getHeldItemStack(),
                    0, 0, 0)
            );
        }
    }

    @SubscribeEvent
    public void onLeftClick(LeftClickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BlazeSlayerHelper) return;
        MovingObjectPosition moving = mc.objectMouseOver;
        if (moving == null) return;
        Entity entity = moving.entityHit;
        if (entity == null) return;

        box = entity.getEntityBoundingBox().addCoord(0, 1, 0);
        List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(entity, box);
        for (Entity possible : entitiesInRange) {
            if (!possible.hasCustomName()) continue;
            String name = possible.getCustomNameTag();
            for (String state : states) {
                if (name.contains(state)) {
                    doSwap(state);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.PillarDisplay) return;
        if (getWorld() == null) return;
        display.clearLines();
        display.setRenderLoc(Configs.PillarX, Configs.PillarY);
        display.setShouldRender(true);
        if (Configs.PillarTest) {
            DisplayLine line = new DisplayLine("&fPillar: " + "&6&l6s &c&l8 hits");
            line.setScale(1.51F * Configs.PillarScale / 20);
            display.addLine(line);
        }
        for (Entity entity : EntityUtils.getEntities()) {
            String name = ChatLib.removeFormatting(entity.getName());
            Pattern pattern = Pattern.compile("(\\d)s (\\d) hits");
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                DisplayLine line = new DisplayLine("&fPillar: " + entity.getName());
                line.setScale(1.51F * Configs.PillarScale / 20);
                display.addLine(line);
            }
        }
    }

//    @SubscribeEvent
//    public void onRenderWorld(RenderWorldLastEvent event) {
//        if (!Checker.enabled) return;
//        if (box == null) return;
//        GuiUtils.drawBoundingBox(box, 3, new Color(114, 189, 105));
//    }

    enum AttunedState {
        ASHEN, AURIC, CRYSTAL, SPIRIT
    }
}
