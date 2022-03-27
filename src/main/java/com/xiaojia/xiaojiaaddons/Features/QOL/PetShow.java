package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashMap;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class PetShow {
    private static final HashMap<Integer, Entity> entities = new HashMap<>();

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.PetShow) return;
        for (Entity entity : entities.values()) {
            if (getWorld().getEntityByID(entity.getEntityId()) == null) continue;
            AxisAlignedBB box = entity.getEntityBoundingBox().expand(1.05, 0.4, 1.05);
            GuiUtils.drawBoundingBox(box, 5, new Color(0xE9FFDB));
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (getWorld() == null) return;
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity.hasCustomName()) {
                String name = entity.getCustomNameTag();
                if (name.contains("'s"))
                    entities.put(entity.getEntityId(), entity);
            }
        }
    }

    @SubscribeEvent
    public void onWorld(WorldEvent.Load event) {
        entities.clear();
    }
}
