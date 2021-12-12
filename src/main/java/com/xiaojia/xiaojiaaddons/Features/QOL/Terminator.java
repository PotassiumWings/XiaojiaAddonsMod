package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class Terminator {
    private static final ArrayList<String> terminatorNames = new ArrayList<String>() {{
        add("Terminator");
    }};
    private static final KeyBind useKeyBind = new KeyBind(mc.gameSettings.keyBindUseItem);
    private long systemTime;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        while (systemTime < Minecraft.getSystemTime() + 1000 / Configs.TerminatorCPS) {
            systemTime += 1000 / Configs.TerminatorCPS;
            execute();
        }
    }

    public void execute() {
        if (!Checker.enabled) return;
        if (!Configs.TerminatorAutoRightClick) return;
        if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem(terminatorNames)) {
            ControlUtils.rightClick();
            if (XiaojiaAddons.isDebug()) ChatLib.chat("rightClick!");
        }
    }
}
