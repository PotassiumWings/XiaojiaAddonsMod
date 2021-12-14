package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoBlood {
    private static final String[] bloodMobs = new String[]{
            "Revoker", "Psycho", "Reaper", "Cannibal", "Mute",
            "Ooze", "Putrid", "Freak", "Leech", "Tear",
            "Parasite", "Flamer", "Skull", "Mr. Dead",
            "Vader", "Frost", "Walker", "WanderingSoul",
            "Bonzo", "Scarf", "Livid"
    };
    private Entity target;
    private ArrayList<Entity> killed = new ArrayList<>();
    private long lastHitTime = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoBlood || !SkyblockUtils.isInDungeon()) return;
        if (target == null || killed.contains(target) || target.getDistanceToEntity(getPlayer()) > 20.0F) {
            target = null;
            // recalculate target
            for (Entity entity: getWorld().loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity.isDead || killed.contains(entity)) continue;
                if (entity.getDistanceToEntity(getPlayer()) > 20.0F) continue;
                for (String name: bloodMobs)
                    if (entity.getName().contains(name))
                        target = entity;
            }
        }
        if (target != null) {
            getPlayer().closeScreen();
            ControlUtils.face(target);
            killed.add(target);
            if (TimeUtils.curTime() - lastHitTime > Configs.AutoBloodCD) {
                lastHitTime = TimeUtils.curTime();
                ControlUtils.leftClick();
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        killed.clear();
    }
}
