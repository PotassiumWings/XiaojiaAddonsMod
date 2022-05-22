package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ApiKey {
    /*
     * automatically sets up the api key on hypixel when you type /api new
     */
    private static final Pattern API_KEY_PATTERN = Pattern.compile("^Your new API key is ([a-zA-Z0-9-]+)");

    public static void setApiKey(String key) {
        ChatLib.chat("Api key set successfully.");
        Configs.APIKey = key;
        Config.save();
    }

    @SubscribeEvent
    public void onChatMessage (ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        String message = event.message.getUnformattedText();
        if (message == null) return;
        Matcher matcherApiKey = API_KEY_PATTERN.matcher(message);
        if (matcherApiKey.matches()) setApiKey(matcherApiKey.group(1));
    }
}

