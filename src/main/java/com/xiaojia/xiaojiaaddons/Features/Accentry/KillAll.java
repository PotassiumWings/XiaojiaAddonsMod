package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class KillAll {
    private final KeyBind keyBind = new KeyBind("Kill All", Keyboard.KEY_NONE);
    private boolean should = false;
    private long lastClicked = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Kill All &aactivated" : "Kill All &cdeactivated");
        }
        if (!should) return;
        long cur = TimeUtils.curTime();
        if (cur - lastClicked > 1000 / Configs.AutoClickCPS) {
            lastClicked = cur;
            ArrayList<Entity> entities = new ArrayList<>();
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity.getName().contains(Configs.KillAllName) && !(entity.isDead)) {
                    entities.add(entity);
                }
            }
            entities.sort((a, b) -> (int) (MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
            entities.removeIf(e -> MathUtils.distanceSquareFromPlayer(e) > 7 * 7);
            for (int i = 0; i < 5; i++) {
                if (entities.size() <= i) return;
                Entity entity = entities.get(i);
                XiaojiaAddons.mc.playerController.attackEntity(getPlayer(), entity);
            }
        }
    }
}
