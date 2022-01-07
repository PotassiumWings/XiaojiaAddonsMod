package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoIsland {
    private boolean evacuate = false;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoIsland) return;
        String message = event.message.getUnformattedText();
        if (message.equals("Evacuating to Hub...")) {
            evacuate = true;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (evacuate) {
            evacuate = false;
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    ChatLib.chat("Auto Island");
                    CommandsUtils.addCommand("/is");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
