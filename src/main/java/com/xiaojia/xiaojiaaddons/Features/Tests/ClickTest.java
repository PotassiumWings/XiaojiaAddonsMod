package com.xiaojia.xiaojiaaddons.Features.Tests;

import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClickTest {
    private static boolean enabled = false;

    public static void setEnabled() {
        enabled = true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (enabled) {
            enabled = false;
            new Thread(() -> {
                try {
                    while (ControlUtils.getOpenedInventory() == null || !ControlUtils.getOpenedInventory().getName().equals("Large Chest")) {
                        Thread.sleep(5);
                    }
                    ControlUtils.getOpenedInventory().click(54, true, "LEFT");
                    Thread.sleep(500);
                    ControlUtils.getOpenedInventory().click(55, true, "LEFT");
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
