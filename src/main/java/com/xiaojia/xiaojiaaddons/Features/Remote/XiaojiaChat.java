package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class XiaojiaChat {

    private static int currentChatId;
    private static final String GET_CHAT_ID_URL = "/xja/chatid";
    private static final String GET_CHAT_URL = "/xja/chat";
    private static final String POST_CHAT_URL = "/xja/chat";
    private static Thread getThread = null;

    public static void init() {
        // join server
        getThread = new Thread(() -> {
            String chatIdString = RemoteUtils.get(GET_CHAT_ID_URL);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(chatIdString).getAsJsonObject();
            currentChatId = jsonObject.get("chatId").getAsInt();
        });
        getThread.start();
    }

    public static void ping() {
        new Thread(() -> System.out.println("ping: " + RemoteUtils.get("xjaddons_ping"))).start();
    }


    public static void chat(String message, int type) {
        message = message.replace("\u00a7", "&");
//        ChatLib.chat("msg: " + message + ", type: " + type + ".");
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"msg\": \"%s\", \"type\": \"%d\"}",
                getUUID(),getPlayer().getName(), message, type);
        new Thread(() -> RemoteUtils.post(POST_CHAT_URL, body)).start();
    }


    @SubscribeEvent
    public void onTick(TickEndEvent event) {
//        if (true) return;
        if (!Checker.enabled) return;
        if (getThread == null || !getThread.isAlive()) {
            getThread = new Thread(() -> {
                try {
                    List<BasicNameValuePair> list = new ArrayList<>();
                    list.add(new BasicNameValuePair("uuid", getUUID()));
                    list.add(new BasicNameValuePair("chatId", currentChatId + ""));

                    String chatIdString = RemoteUtils.get(GET_CHAT_URL, list);
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonElements = jsonParser.parse(chatIdString).getAsJsonObject().get("res").getAsJsonArray();
                    for (JsonElement element : jsonElements) {
                        JsonObject singleObject = element.getAsJsonObject();
                        int type = singleObject.get("type").getAsInt();
                        String message = singleObject.get("msg").getAsString();
                        String name = singleObject.get("name").getAsString();
                        int chatId = singleObject.get("chatId").getAsInt();
                        ChatLib.xjchat(type, name, message);
                        currentChatId = Math.max(currentChatId, chatId);
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            getThread.start();
        }
    }

    // puzzle fail
    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        String message = event.message.getFormattedText();
        String unformattedMessage = ChatLib.removeFormatting(event.message.getUnformattedText());
//        ChatLib.chat("message: " + message + ", unfor: " + unformattedMessage);
        if (unformattedMessage.startsWith("PUZZLE FAIL! " + getPlayer().getName())) {
            chat(message, 1);
        }
        if (unformattedMessage.startsWith(" ☠ You") && unformattedMessage.endsWith("and became a ghost.")) {
            chat(message.substring(11), 2);
        }
    }

    public static String getUUID() {
        return getPlayer().getUniqueID().toString().replace("-", "");
    }
}
