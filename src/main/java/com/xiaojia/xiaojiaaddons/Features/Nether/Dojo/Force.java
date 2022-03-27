package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Force {
    @SubscribeEvent
    public void onLeftClick(LeftClickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ForceHelper) return;
        if (DojoUtils.getTask() != EnumDojoTask.FORCE) return;
        MovingObjectPosition pos = mc.objectMouseOver;
        Entity entity = pos.entityHit;
        if (entity == null) return;
        double x = getX(entity);
        double y = getY(entity);
        double z = getZ(entity);
        AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).expand(0.2, 0, 0.2).addCoord(0, 1, 0);
        List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(entity, box);
        for (Entity possible : entitiesInRange) {
            if (!possible.hasCustomName()) continue;
            String name = ChatLib.removeFormatting(possible.getCustomNameTag());
            ChatLib.debug(name);
            if (name.contains("-")) {
                event.setCanceled(true);
                ChatLib.chat("Blocked negative mob hit in Force Challenge.");
            }
        }
    }
}
