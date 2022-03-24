package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.NetUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Blaze {
    private AxisAlignedBB box = null;
    private static long lastSwapSPICRY = 0;
    private static long lastSwapASHAUR = 0;
    private static final ArrayList<String> states = new ArrayList<>(Arrays.asList(
            "SPIRIT", "CRYSTAL",
            "ASHEN", "AURIC"
    ));

    @SubscribeEvent
    public void onLeftClick(LeftClickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BlazeSlayerHelper) return;
        MovingObjectPosition moving = mc.objectMouseOver;
        Entity entity = moving.entityHit;
        if (entity == null) return;

        box = entity.getEntityBoundingBox().addCoord(0, 1, 0);
        List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(entity, box);
        for (Entity possible : entitiesInRange) {
            if (!possible.hasCustomName()) continue;
            String name = possible.getCustomNameTag();
            ChatLib.debug(name);
            for (String state : states) {
                if (name.contains(state)) {
                    doSwap(state);
                    return;
                }
            }
        }
    }

    public static void doSwap(String state) {
        ChatLib.debug("Current state: " + state);
        boolean shouldClick = false;
        int swapIndex = -1;
        String daggerId;
        boolean isASHAUR = state.equals("ASHEN") || state.equals("AURIC");
        if (isASHAUR) daggerId = "FIREDUST_DAGGER";
        else daggerId = "MAWDUST_DAGGER";

        for (int i = 0; i < 8; i++) {
            ItemStack itemStack = ControlUtils.getItemStackInSlot(i + 36, true);
            String sbId = NBTUtils.getSkyBlockID(itemStack);
            if (sbId.equals(daggerId)) {
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
            long delta = cur - (isASHAUR? lastSwapASHAUR:lastSwapSPICRY);
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
