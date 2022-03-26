package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Checker {
    public static boolean enabled = false;
    public static long connect = 0;
    public static long auth = 0;

    public static void onConnect() {
        connect = TimeUtils.curTime();
    }

    public static void onAuth() {
        // pass authenticate
        auth = TimeUtils.curTime();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        enabled = auth > connect;
    }
}
