package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
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
        return TimeUtils.curTime() - lastShot > 1000 && !getPlayer().isInLava() && enabled;
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
            if (sbId.equals("BONZO_STAFF") || sbId.equals("JERRY_STAFF") || sbId.equals("STARRED_BONZO_STAFF")) {
                lastShot = TimeUtils.curTime();
            }
        }
    }
}
