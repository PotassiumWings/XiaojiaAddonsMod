package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class OneTick {
    private final KeyBind keyBind = new KeyBind("Left Click One Tick", Keyboard.KEY_NONE);
    private final KeyBind useKeyBind = new KeyBind("One Tick", Keyboard.KEY_NONE);
    private boolean should = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Left Click One Tick &aactivated" : "Left Click One Tick &cdeactivated");
        }
        // 40 cps
        if (useKeyBind.isKeyDown()) {
            oneTick();
        }
    }

    @SubscribeEvent
    public void onLeftClick(LeftClickEvent event) {
        if (!Checker.enabled) return;
        if (!should) return;
        if (ControlUtils.checkHoldingItem("Gloomlock Grimoire")) return;
        if (ControlUtils.checkHoldingItem("Gyrokinetic Wand")) return;
        if (ControlUtils.checkHoldingItem("Terminator")) return;
        oneTick();
    }

    private void oneTick() {
        int cur = ControlUtils.getHeldItemIndex();
        int whipSlot = HotbarUtils.soulwhipSlot;
        int aotsSlot = HotbarUtils.aotsSlot;
        int termSlot = HotbarUtils.terminatorSlot;
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
        if (Configs.TerminatorWithAnything && termSlot != -1) {
            ControlUtils.setHeldItemIndex(termSlot);
            ControlUtils.rightClick();
            ControlUtils.setHeldItemIndex(cur);
            ChatLib.debug("using term, index at " + termSlot);
        }
    }
}
