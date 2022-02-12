package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class HoldRightClick {
    private static final ArrayList<String> terminatorNames = new ArrayList<String>() {{
        add("Terminator");
    }};
    private static final ArrayList<String> rogueNames = new ArrayList<String>() {{
        add("Rogue");
    }};
    private static final KeyBind useKeyBind = new KeyBind(mc.gameSettings.keyBindUseItem);
    private long systemTimeTerm;
    private long systemTimeRogue;
    private double currentSpeed;
    private boolean isRightClicking = false;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        while (systemTimeTerm < Minecraft.getSystemTime() + 1000 / Configs.TerminatorCPS) {
            systemTimeTerm += 1000 / Configs.TerminatorCPS;
            executeTerminator();
        }
        while (systemTimeRogue < Minecraft.getSystemTime() + 1000 / Configs.RogueCPS) {
            systemTimeRogue += 1000 / Configs.RogueCPS;
            executeRogue();
        }
    }

    public void executeTerminator() {
        if (!Checker.enabled) return;
        if (!Configs.TerminatorAutoRightClick) return;
        if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem(terminatorNames)) {
            ControlUtils.rightClick();
            if (XiaojiaAddons.isDebug()) ChatLib.chat("rightClick!");
        }
    }

    public void executeRogue() {
        if (!Checker.enabled) return;
        if (!Configs.RogueAutoRightClick) return;
        if (useKeyBind.isKeyDown() && ControlUtils.checkHoldingItem(rogueNames)) {
            if (!isRightClicking) {
                isRightClicking = true;
                currentSpeed = getPlayer().capabilities.getWalkSpeed() * 1000;
            }
            if (currentSpeed < Configs.MaxSpeed) {
                ControlUtils.rightClick();
                if (XiaojiaAddons.isDebug()) ChatLib.chat("rightClick!");
                currentSpeed += SkyblockUtils.isInDungeon() ? 3.33 : 10;
            }
        } else {
            isRightClicking = false;
        }
    }
}
