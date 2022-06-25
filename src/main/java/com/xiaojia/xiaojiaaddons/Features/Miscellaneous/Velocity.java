package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Velocity {
    private static long lastShot = 0;
    private static boolean enabled = true;
    private final KeyBind keyBind = new KeyBind("Velocity", Keyboard.KEY_NONE);

    public static boolean canDisableKnockBack() {
        return TimeUtils.curTime() - lastShot > Configs.VelocityCD && (!getPlayer().isInLava() || !SkyblockUtils.isInDungeon())
                && enabled && !wearingSpringBoots();
    }

    private static boolean wearingSpringBoots() {
        ItemStack boot = ControlUtils.getBoots();
        String id = NBTUtils.getSkyBlockID(boot);
        return id.equals("SPRING_BOOTS") || id.equals("TARANTULA_BOOTS") || id.equals("THORNS_BOOTS") ||
                id.equals("SPIDER_BOOTS");
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "Velocity &aactivated" : "Velocity &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = ControlUtils.getHeldItemStack();
            String sbId = NBTUtils.getSkyBlockID(item);
            if (sbId.equals("BONZO_STAFF") || sbId.equals("JERRY_STAFF") || sbId.equals("STARRED_BONZO_STAFF") ||
                sbId.equals("GRAPPLING_HOOK")) {
                lastShot = TimeUtils.curTime();
            }
        }
    }

    public static void onPlayerNearby(Entity player) {
        if (!Configs.VelocityStop) return;
        int r = Configs.VelocityStopRadius;
        if (MathUtils.distanceSquareFromPlayer(player) > r * r) return;
        if (enabled) {
            enabled = false;
            getPlayer().playSound("random.successful_hit", 1000, 1);
            ChatLib.chat("Velocity &cdeactivated");
        }
    }
}
