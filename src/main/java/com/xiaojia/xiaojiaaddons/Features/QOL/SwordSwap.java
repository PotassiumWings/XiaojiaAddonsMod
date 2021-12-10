package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class SwordSwap extends StepEvent {
    private static final KeyBind keyBind = new KeyBind("Ghost SwordSwap", Keyboard.KEY_NONE);
    private static boolean should = false;

    public SwordSwap() {
        super(2);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Ghost SwordSwap &aactivated" : "Ghost SwordSwap &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.equals("You died!") && should) {
            should = false;
            ChatLib.chat("Ghost SwordSwap &cdeactivated");
        }
    }

    @Override
    public void execute() {
        if (!Checker.enabled) return;
        if (!Configs.GhostSwordSwap) return;
        if (!should) return;
        if (!HotbarUtils.checkSoulwhip() || !HotbarUtils.checkEmeraldBlade()) return;
        new Thread(() -> {
            try {
                ControlUtils.setHeldItemIndex(HotbarUtils.emeraldBladeSlot);
                ControlUtils.setHeldItemIndex(HotbarUtils.soulwhipSlot);
                ControlUtils.rightClick();
                if (false) {
                    Thread.sleep(new Random().nextInt(100) + 50);
                }
                ControlUtils.setHeldItemIndex(HotbarUtils.emeraldBladeSlot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
