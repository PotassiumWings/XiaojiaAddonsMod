package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class HideCreepers {
    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.HideCreepers) return;
        if (getWorld() == null) return;

        List<Entity> allEntities = getWorld().loadedEntityList;
        for (Entity entity : allEntities) {
            if (entity instanceof EntityCreeper) {
                entity.setDead();
            }
        }
    }
}
