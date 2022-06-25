package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.PacketUtils;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoBack {
//    @SubscribeEvent
//    public void onReceive(ClientChatReceivedEvent event) {
//        if (!Checker.enabled) return;
//        if (!Configs.AutoBack) return;
//        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
//        String name = getPlayer().getName();
//        if (message.startsWith("死亡 >> 玩家 " + name) ||
//                message.matches("系统 >> .* 杀死了 " + name) ||
//                message.equals(name + " 被祭祀大人给炸毛了qwq")) {
//            CommandsUtils.addCommand("/back");
//        }
//    }


    @SubscribeEvent
    public void onTitlePacketReceived(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoBack) return;

        if (event.packet instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) event.packet;
            IChatComponent component = PacketUtils.getMessage(packet);
            if (component == null) return;
//            ChatLib.debug(component.getFormattedText());
            String title = ChatLib.removeFormatting(component.getUnformattedText());
            if (title.contains("你失败了")) {
                CommandsUtils.addCommand("/back");
            }
        }
    }
}
