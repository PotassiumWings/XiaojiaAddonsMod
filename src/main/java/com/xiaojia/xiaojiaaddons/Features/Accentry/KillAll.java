package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class KillAll {
    private static boolean should = false;
    private final KeyBind keyBind = new KeyBind("Kill All", Keyboard.KEY_NONE);
    private long lastClicked = 0;

    public static void onPlayerNearby(Entity player) {
        if (!Configs.KillAllStop) return;
        int r = Configs.KillAllStopRadius;
        double dis = Math.sqrt(MathUtils.distanceSquareFromPlayer(player));
        if (dis > r) return;
        if (should) {
            should = false;
            getPlayer().playSound("random.successful_hit", 1000, 1);
            ChatLib.chat(String.format("Found player nearby, stopped. Player: %s, distance: %.2f", player.getName(), dis));
            ChatLib.chat("Kill All &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (getWorld() == null) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Kill All &aactivated" : "Kill All &cdeactivated");
        }
        if (!should) return;
        if (AutoRegenBow.shouldFastBow && Configs.StopKillAllWhenAutoRegen) return;
        if (PacketRelated.getReceivedQueueLength() == 0) return;
        long cur = TimeUtils.curTime();
        if (cur - lastClicked > 1000 / Configs.AutoClickCPS) {
            lastClicked = cur;
            ArrayList<Entity> entities = new ArrayList<>();
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity == getPlayer()) continue;
                String[] names = Configs.KillAllName.split(",");
                if (Arrays.stream(names).anyMatch(e -> entity.getName().contains(e)) && !(entity.isDead)) {
                    entities.add(entity);
                }
            }
            entities.sort((a, b) -> (int) (MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
            entities.removeIf(e -> MathUtils.distanceSquareFromPlayer(e) > 7 * 7);
            for (int i = 0; i < 5; i++) {
                if (entities.size() <= i) return;
                Entity entity = entities.get(i);
                if (Configs.KillAllLeft == 0) XiaojiaAddons.mc.playerController.attackEntity(getPlayer(), entity);
                else EntityUtils.tryRightClickEntity(entity, 7);
            }
        }
    }
}
