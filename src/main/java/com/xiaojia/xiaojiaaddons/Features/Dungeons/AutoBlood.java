package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoBlood {
    private static final KeyBind keyBind = new KeyBind("Auto Blood", Keyboard.KEY_NONE);
    public static final String[] bloodMobs = new String[]{
            "Revoker", "Psycho", "Reaper", "Cannibal", "Mute",
            "Ooze", "Putrid", "Freak", "Leech", "Tear",
            "Parasite", "Flamer", "Skull", "Mr. Dead",
            "Vader", "Frost", "Walker", "WanderingSoul",
            "Bonzo", "Scarf", "Livid"
    };
    private static boolean enabled = false;
    private final ArrayList<Entity> killed = new ArrayList<>();
    private Entity target;
    private long faceTime = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "Auto Blood &aactivated" : "Auto Blood &cdeactivated");
        }
        if (!enabled) return;
        if (!Configs.AutoBlood || !SkyblockUtils.isInDungeon()) return;
        if (Dungeon.bossEntry > Dungeon.runStarted) return;
        if (target == null || killed.contains(target) || target.getDistanceToEntity(getPlayer()) > 20.0F || target.isDead) {
            target = null;
            faceTime = 0;
            // recalculate target
            for (Entity entity : getWorld().loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity.isDead || killed.contains(entity)) continue;
                if (entity.getDistanceToEntity(getPlayer()) > 20.0F) continue;
                for (String name : bloodMobs)
                    if (entity.getName().contains(name))
                        target = entity;
            }
        }
        if (target != null) {
            getPlayer().closeScreen();
            ControlUtils.face(target.posX, target.posY - Configs.AutoBloodYoffset * 0.1F, target.posZ);
            if (faceTime == 0) faceTime = TimeUtils.curTime();
            if (TimeUtils.curTime() - faceTime > Configs.AutoBloodCD) {
                faceTime = TimeUtils.curTime();
                ControlUtils.leftClick();
                killed.add(target);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        killed.clear();
    }
}
