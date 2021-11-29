package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MimicWarn {
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon() || !Configs.MimicWarn) return;
        if (event.entity instanceof EntityZombie && ((EntityZombie) (event.entity)).isChild()) { // baby zombie
            ChatLib.chat(event.entity.toString() + " dead");
            CommandsUtils.addCommand("/pc Mimic dead!");
        }
    }
}
