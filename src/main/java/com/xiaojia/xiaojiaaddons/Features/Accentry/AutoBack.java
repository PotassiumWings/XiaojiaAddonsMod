package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoBack {
    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoBack) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        String name = getPlayer().getName();
        if (message.startsWith("死亡 >> 玩家 " + name) ||
                message.matches("系统 >> .* 杀死了 " + name) ||
                message.equals(name + " 被祭祀大人给炸毛了qwq")) {
            CommandsUtils.addCommand("/back");
        }
    }
}
