package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Deque;
import java.util.LinkedList;

public class CommandsUtils {
    private static final Deque<String> commandsQueue = new LinkedList<>();
    private static long lastSent = 0;

    public static void addCommand(String command) {
        commandsQueue.offerLast(command);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (TimeUtils.curTime() - lastSent > 70) {
            if (commandsQueue.size() > 0) {
                String command = commandsQueue.pollFirst();
                ChatLib.say(command);
                lastSent = TimeUtils.curTime();
            }
        }
    }
}
