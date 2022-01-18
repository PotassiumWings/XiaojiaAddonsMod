package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class XiaojiaChat {
    private int currentChatId;
    private static final String GET_CHAT_ID_URL = "";  // TODO
    private Thread getThread = null;

    public void init() {
        // join server
//        currentChatId = RemoteUtils.get(GET_CHAT_ID_URL);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
//        if (getThread == null || !getThread.isAlive()) {
//            getThread = new Thread(() -> {
//
//            });
//            getThread.start();
//        }
    }
}
