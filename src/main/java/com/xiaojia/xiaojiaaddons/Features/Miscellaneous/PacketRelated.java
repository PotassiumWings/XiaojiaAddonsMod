package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PacketRelated {
    public static final Deque<Long> receiveQueue = new ConcurrentLinkedDeque<>();
    public static final Deque<Long> sendQueue = new ConcurrentLinkedDeque<>();

    public static int getReceivedQueueLength() {
        long cur = TimeUtils.curTime();
        synchronized (receiveQueue) {
            while (!receiveQueue.isEmpty() && cur - receiveQueue.getFirst() > 1000) {
                receiveQueue.pollFirst();
            }
            return receiveQueue.size();
        }
    }

    public static int getSentQueueLength() {
        long cur = TimeUtils.curTime();
        synchronized (sendQueue) {
            while (!sendQueue.isEmpty() && cur - sendQueue.getFirst() > 1000) {
                sendQueue.pollFirst();
            }
            return sendQueue.size();
        }
    }

    @SubscribeEvent
    public void onReceive(PacketReceivedEvent event) {
        synchronized (receiveQueue) {
            receiveQueue.add(TimeUtils.curTime());
        }
    }

    @SubscribeEvent
    public void onSend(PacketSendEvent event) {
        synchronized (sendQueue) {
            sendQueue.add(TimeUtils.curTime());
        }
    }
}
