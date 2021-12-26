package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowHiddenMobs {

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        EntityLivingBase entity = event.entity;
        String name = entity.getCommandSenderEntity().getName();
        if (entity.isInvisible()) {
            if (Configs.ShowFels && entity instanceof EntityEnderman) entity.setInvisible(false);
            if (entity instanceof EntityPlayer) {
                if (Configs.ShowShadowAssassin && name.contains("Shadow Assassin")) entity.setInvisible(false);
                if (Configs.ShowStealthy) {
                    for (String bloodName : AutoBlood.bloodMobs)
                        if (name.contains(bloodName)) {
                            entity.setInvisible(false);
                            break;
                        }
                }
            }
        }
    }
}
