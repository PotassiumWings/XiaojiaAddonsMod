package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferBack {
    private static String fromPerson = null;
    private static final String rankPattern = "(?:\\[VIP\\+*] |\\[MVP\\+*] |)";
    private static final String namePattern = "[a-zA-Z0-9_]+";
    private static final String transferPattern = "^The party was transferred to " + rankPattern + namePattern + " by " + rankPattern + "(?<name>" + namePattern + ")";

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        Pattern pattern = Pattern.compile(transferPattern);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            fromPerson = matcher.group("name");
        }
    }

    public static void transferBack() {
        if (fromPerson != null)
            CommandsUtils.addCommand("/p transfer " + fromPerson);
    }
}
