package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatCopy {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ChatCopy) return;
        if (event.type == 2) return;
        String message = event.message.getFormattedText();
        String noColorMessage = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (noColorMessage.matches("^[-▬=]+$") && noColorMessage.length() >= 4) {
            return;
        }

        IChatComponent noColorComponent = new ChatComponentText(ChatLib.addColor("&r&8 [&0C"));
        ChatStyle noColorStyle = new ChatStyle();
        noColorStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj copy " + noColorMessage));
        noColorStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to copy this message")));
        noColorComponent.setChatStyle(noColorStyle);

        IChatComponent coloredComponent = new ChatComponentText(ChatLib.addColor("&8C]"));
        ChatStyle colorStyle = new ChatStyle();
        colorStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj copy " + message));
        colorStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(ChatLib.addColor("Click to copy this &1m&2e&3s&4s&5a&6g&ae"))));
        coloredComponent.setChatStyle(colorStyle);

        event.message.appendSibling(noColorComponent);
        event.message.appendSibling(coloredComponent);
//        System.out.println("received: " + message);
//        ChatStyle style = event.message.getChatStyle();
//        style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj copy " + message));
//        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to copy this message")));
//        event.message.setChatStyle(style);
    }
}
