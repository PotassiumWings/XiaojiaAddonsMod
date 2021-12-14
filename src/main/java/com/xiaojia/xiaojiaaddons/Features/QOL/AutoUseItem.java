package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayDeque;

public class AutoUseItem {
    private static final KeyBind keyBind = new KeyBind("Auto Use Item", Keyboard.KEY_NONE);
    private final ArrayDeque<String> queue = new ArrayDeque<>();
    private boolean enabled = false;
    private long lastPlasma = 0;
    private long lastHealing = 0;
    private long lastGyro = 0;
    private long lastUse = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            enabled = !enabled;
            if (enabled) {
                ChatLib.chat("Auto Item &aactivated");
                if (Configs.PlasmaFluxCD != 0) ChatLib.chat("&bPlasma: &6" + Configs.PlasmaFluxCD);
                if (Configs.HealingWandCD != 0) ChatLib.chat("&bHealing Wand: &6" + Configs.HealingWandCD);
                if (Configs.GyroCD != 0) ChatLib.chat("&bGyrokinetic Wand: &6" + Configs.GyroCD);
            } else {
                ChatLib.chat("Auto Item &cdeactivated");
            }
        }
        if (!enabled) return;
        long cur = TimeUtils.curTime();
        try {

            if (Configs.PlasmaFluxCD != 0) {
                if (cur - lastPlasma >= 1000 * Configs.PlasmaFluxCD) {
                    lastPlasma = cur;
                    queue.add("Plasma");
                    queue.add(ControlUtils.getHeldItemIndex() + "");
                }
            }
            if (Configs.HealingWandCD != 0) {
                if (cur - lastHealing >= 1000 * Configs.HealingWandCD) {
                    lastHealing = cur;
                    queue.add("Healing");
                    queue.add(ControlUtils.getHeldItemIndex() + "");
                }
            }
            if (Configs.GyroCD != 0) {
                if (cur - lastGyro >= 1000 * Configs.GyroCD) {
                    lastGyro = cur;
                    queue.add("Gyro");
                    queue.add(ControlUtils.getHeldItemIndex() + "");
                }
            }
            if (cur - lastUse > 70 || !Configs.LegitAutoItem) {
                if (!queue.isEmpty()) {
                    boolean ok = true;
                    String item = queue.pollFirst();
                    int slot;
                    boolean rightClick = true;
                    switch (item) {
                        case "Plasma":
                            slot = HotbarUtils.plasmaSlot;
                            ok = ControlUtils.checkHotbarItem(slot, "Plasmaflux");
                            break;
                        case "Healing":
                            slot = HotbarUtils.healingwandSlot;
                            ok = ControlUtils.checkHotbarItem(slot, "Wand of");
                            break;
                        case "Gyro":
                            slot = HotbarUtils.gyroSlot;
                            ok = ControlUtils.checkHotbarItem(slot, "Gyro");
                            break;
                        default:
                            slot = Integer.parseInt(item);
                            rightClick = false;
                            break;
                    }
                    if (!ok) return;
                    lastUse = cur;
                    ControlUtils.setHeldItemIndex(slot);
                    if (rightClick) ControlUtils.rightClick();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
