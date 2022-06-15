package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AutoClick {
    private final KeyBind keyBind = new KeyBind("Auto Left Click", Keyboard.KEY_NONE);
    private boolean should = false;
    private long lastClicked = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoLeftClick) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Auto Left Click &aactivated" : "Auto Left Click &cdeactivated");
        }
        if (!should) return;
        // 40 cps
        long cur = TimeUtils.curTime();
        if (cur - lastClicked > 1000 / Configs.AutoClickCPS && PacketRelated.getReceivedQueueLength() != 0) {
            lastClicked = cur;
            ControlUtils.leftClick();
        }
    }
}
