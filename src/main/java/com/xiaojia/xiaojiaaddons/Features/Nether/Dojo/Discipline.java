package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Discipline {
    @SubscribeEvent
    public void onLeftClick(LeftClickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.DisciplineHelper) return;
        if (DojoUtils.getTask() != EnumDojoTask.DISCIPLINE) return;
        MovingObjectPosition pos = mc.objectMouseOver;
        Entity entity = pos.entityHit;
        if (entity == null) return;
        double x = getX(entity);
        double y = getY(entity);
        double z = getZ(entity);
        AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).expand(0.1, 0, 0.1).addCoord(0, 2.5, 0);
        List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(entity, box);
        HashMap<String, Integer> indexMap = new HashMap<>();
        indexMap.put("Wood", 0);
        indexMap.put("Iron", 1);
        indexMap.put("Gold", 2);
        indexMap.put("Diamond", 3);
        for (Entity possible : entitiesInRange) {
            if (!possible.hasCustomName()) continue;
            String name = ChatLib.removeFormatting(possible.getCustomNameTag());
            ChatLib.debug(name);
            for (String s : indexMap.keySet()) {
                if (name.contains(s)) {
                    ControlUtils.setHeldItemIndex(indexMap.get(s));
                }
            }
        }
    }
}
