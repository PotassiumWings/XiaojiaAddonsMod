package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatCopy {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ChatCopy) return;
        if (event.type != 0) return;
        String message = event.message.getFormattedText();
        System.out.println("received: " + message);
        ChatStyle style = event.message.getChatStyle();
        style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj copy " + message));
        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to copy this message")));
        event.message.setChatStyle(style);
    }
}
