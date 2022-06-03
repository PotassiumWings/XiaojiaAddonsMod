package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PacketReceive {
    public static final Deque<Long> queue = new ConcurrentLinkedDeque<>();

    public static int getQueueLength() {
        long cur = TimeUtils.curTime();
        synchronized (queue) {
            while (!queue.isEmpty() && cur - queue.getFirst() > 1000) {
                queue.pollFirst();
            }
            return queue.size();
        }
    }

    @SubscribeEvent
    public void onReceive(PacketReceivedEvent event) {
        synchronized (queue) {
            queue.add(TimeUtils.curTime());
        }
    }
}
