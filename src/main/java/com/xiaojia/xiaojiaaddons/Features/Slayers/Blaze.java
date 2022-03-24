package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
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

        box = entity.getEntityBoundingBox().expand(0.4, 0.4, 0.4);
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

    private void doSwap(String state) {
        ChatLib.debug("Current state: " + state);
        if (state.equals("ASHEN") || state.equals("AURIC")) {
            String id = "FIREDUST_DAGGER";
            for (int i = 0; i < 8; i++) {
                ItemStack itemStack = ControlUtils.getItemStackInSlot(i, true);
                if (NBTUtils.getSkyBlockID(itemStack).equals(id)) {
                    List<String> lore = NBTUtils.getLore(itemStack);
                    AttunedState curState = null;
                    for (String formatted : lore) {
                        String unformatted = ChatLib.removeFormatting(formatted);
                        if (unformatted.equals("Attuned: Ashen")) curState = AttunedState.ASHEN;
                        if (unformatted.equals("Attuned: Auric")) curState = AttunedState.AURIC;
                    }
                    if (curState != null) {
                        if (!curState.toString().equals(state)) {
                            ControlUtils.setHeldItemIndex(i);
                            ControlUtils.rightClick();
                            return;
                        }
                    }
                }
            }
        } else {
            String id = "MAWDUST_DAGGER";
            for (int i = 0; i < 8; i++) {
                ItemStack itemStack = ControlUtils.getItemStackInSlot(i, true);
                if (NBTUtils.getSkyBlockID(itemStack).equals(id)) {
                    List<String> lore = NBTUtils.getLore(itemStack);
                    AttunedState curState = null;
                    for (String formatted : lore) {
                        String unformatted = ChatLib.removeFormatting(formatted);
                        if (unformatted.equals("Attuned: Spirit")) curState = AttunedState.SPIRIT;
                        if (unformatted.equals("Attuned: Crystal")) curState = AttunedState.CRYSTAL;
                    }
                    if (curState != null) {
                        if (!curState.toString().equals(state)) {
                            ControlUtils.setHeldItemIndex(i);
                            ControlUtils.rightClick();
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (box == null) return;
        GuiUtils.drawBoundingBox(box, 3, new Color(114, 189, 105));
    }

    enum AttunedState {
        ASHEN, AURIC, CRYSTAL, SPIRIT
    }
}
