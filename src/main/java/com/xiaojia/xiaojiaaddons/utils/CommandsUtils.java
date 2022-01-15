package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Deque;
import java.util.LinkedList;

public class CommandsUtils {
    private static final Deque<String> commandsQueue = new LinkedList<>();
    private static long lastSent = 0;

    private static final Deque<Long> sentQueue = new LinkedList<>();

    public static void addCommand(String command) {
        commandsQueue.offerLast(command);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        // < 3 commands in 3s
        while (!sentQueue.isEmpty() && TimeUtils.curTime() - sentQueue.getFirst() > 3000)
            sentQueue.pollFirst();
        if (sentQueue.size() > 3) return;

        if (TimeUtils.curTime() - lastSent > 200) {
            if (commandsQueue.size() > 0) {
                String command = commandsQueue.pollFirst();
                sentQueue.addLast(TimeUtils.curTime());
                ChatLib.say(command);
                lastSent = TimeUtils.curTime();
            }
        }
    }
}
