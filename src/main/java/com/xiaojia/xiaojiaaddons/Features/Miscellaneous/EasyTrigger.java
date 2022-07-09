package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyTrigger {
    private static final ArrayDeque<Long> queue = new ArrayDeque<>();

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.EasyTrigger) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (Configs.EasyTriggerPair1Enabled) {
            Pattern pattern = Pattern.compile(Configs.EasyTriggerPair1OnReceive);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) doCommand(Configs.EasyTriggerPair1DoCommand, 1);
        }
        if (Configs.EasyTriggerPair2Enabled) {
            Pattern pattern = Pattern.compile(Configs.EasyTriggerPair2OnReceive);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) doCommand(Configs.EasyTriggerPair2DoCommand, 1);
        }
        if (Configs.EasyTriggerPair3Enabled) {
            Pattern pattern = Pattern.compile(Configs.EasyTriggerPair3OnReceive);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) doCommand(Configs.EasyTriggerPair3DoCommand, 1);
        }
        if (Configs.EasyTriggerPair4Enabled) {
            Pattern pattern = Pattern.compile(Configs.EasyTriggerPair4OnReceive);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) doCommand(Configs.EasyTriggerPair4DoCommand, 1);
        }
        if (Configs.EasyTriggerPair5Enabled) {
            Pattern pattern = Pattern.compile(Configs.EasyTriggerPair5OnReceive);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) doCommand(Configs.EasyTriggerPair5DoCommand, 1);
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        long cur = TimeUtils.curTime();
        while (!queue.isEmpty() && cur - queue.getFirst() > 1000) {
            queue.pollFirst();
        }
    }

    public static void doCommand(String command, int trigger) {
        if (Configs.EasyTriggerDebugMode) ChatLib.chat("Easy Trigger - " + trigger + " triggered");
        if (queue.size() >= 5) {
            if (Configs.EasyTriggerDebugMode) ChatLib.chat("Easy Trigger - but queue length exceeded.");
            return;
        }
        queue.offerLast(TimeUtils.curTime());
        CommandsUtils.addCommand(command);
    }
}
