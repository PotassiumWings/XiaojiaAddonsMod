package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class OneTick {
    private final KeyBind keyBind = new KeyBind("One Tick", Keyboard.KEY_NONE);
    private boolean should = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "One Tick &aactivated" : "One Tick &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onLeftClick(LeftClickEvent event) {
        if (!Checker.enabled) return;
        if (!should) return;
        if (ControlUtils.checkHoldingItem("Gloomlock Grimoire")) return;
        if (ControlUtils.checkHoldingItem("Gyrokinetic Wand")) return;

        int cur = ControlUtils.getHeldItemIndex();
        int whipSlot = HotbarUtils.soulwhipSlot;
        int aotsSlot = HotbarUtils.aotsSlot;
        if (Configs.SoulWhipWithAnything && whipSlot != -1) {
            ControlUtils.setHeldItemIndex(whipSlot);
            ControlUtils.rightClick();
            ControlUtils.setHeldItemIndex(cur);
            ChatLib.debug("using soulwhip, index at " + whipSlot);
        }
        if (Configs.AotsWithAnything && aotsSlot != -1) {
            ControlUtils.setHeldItemIndex(aotsSlot);
            ControlUtils.rightClick();
            ControlUtils.setHeldItemIndex(cur);
            ChatLib.debug("using aots, index at " + aotsSlot);
        }
    }
}
