package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Deque;
import java.util.LinkedList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class CommandsUtils {
    private static final Deque<String> commandsQueue = new LinkedList<>();
    private static final Deque<Long> sentQueue = new LinkedList<>();
    private static long lastSent = 0;

    public static void addCommand(String command) {
        commandsQueue.offerLast(command);
    }

    public static void addCommand(String command, int limit) {
        if (commandsQueue.size() > limit) return;
        addCommand(command);
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
                if (!command.startsWith("/") ||
                        ClientCommandHandler.instance.executeCommand(mc.thePlayer, command) == 0)
                    mc.thePlayer.sendChatMessage(command);
                lastSent = TimeUtils.curTime();
            }
        }
    }
}
