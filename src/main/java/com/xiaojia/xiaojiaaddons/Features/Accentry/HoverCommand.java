package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoverCommand {
    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ChatHoverCommand) return;
        if (event.type == 2) return;
        IChatComponent component = event.message;
        convert(component);
    }

    public void convert(IChatComponent component) {
        ChatStyle style = component.getChatStyle();
        if (style.getChatClickEvent() != null && style.getChatHoverEvent() == null) {
            if (style.getChatClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND) {
                String command = style.getChatClickEvent().getValue();
                style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(command)));
            }
        }
        component.getSiblings().forEach(this::convert);
    }
}
