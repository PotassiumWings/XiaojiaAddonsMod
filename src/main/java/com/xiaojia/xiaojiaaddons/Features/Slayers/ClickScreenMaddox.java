package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ClickScreenMaddox {
    static String lastMaddoxCommand = "";
    static long lastMaddoxTime = 0;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.contains(":")) return;
        if (message.contains("[OPEN MENU]")) {
            List<IChatComponent> listOfSiblings = event.message.getSiblings();
            for (IChatComponent sibling : listOfSiblings) {
                if (sibling.getUnformattedText().contains("[OPEN MENU]")) {
                    lastMaddoxCommand = sibling.getChatStyle().getChatClickEvent().getValue();
                    ChatLib.debug("last command: " + lastMaddoxCommand);
                    lastMaddoxTime = TimeUtils.curTime();
                }
            }
            if (Configs.ClickScreenMaddox)
                getPlayer().addChatMessage(new ChatComponentText("Open chat then click anywhere on-screen to open Maddox"));
        }
    }

    @SubscribeEvent
    public void onMouseInputPost(GuiScreenEvent.MouseInputEvent.Post event) {
        if (Mouse.getEventButton() == 0 && event.gui instanceof net.minecraft.client.gui.GuiChat &&
                Configs.ClickScreenMaddox && TimeUtils.curTime() - lastMaddoxTime < 10000)
            getPlayer().sendChatMessage(lastMaddoxCommand);
    }
}
