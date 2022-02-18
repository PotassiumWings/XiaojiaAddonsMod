package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.CheckEntityRenderEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisableEntityRender {
    @SubscribeEvent
    public void onRenderEntity(CheckEntityRenderEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInSkyblock()) return;
        Entity entity = event.entity;
        if (Configs.DisableDying && entity instanceof EntityLivingBase &&
                (((EntityLivingBase) event.entity).getHealth() <= 0 || entity.isDead)) {
            event.setCanceled(true);
        }
    }
}
