package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSocket {
    private static PrintWriter out;
    private static BufferedReader in;
    private static boolean connected = false;

    public static void reconnect() {
        if (connected) {
            ChatLib.chat("Already connected!");
        } else {
            connect();
        }
    }

    public static void connect() {
        try {
            Socket socket = new Socket("47.94.243.9", 11051);
            connected = true;
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            new Thread(() -> {
                try {
                    boolean first = true;
                    while (true) {
                        StringBuilder sb = new StringBuilder();
                        int recv = in.read();
                        if (recv == -1) break;
                        if (first) {
                            first = false;
                            ChatLib.chat("Connected to xc server!");
                        }

                        // linux, '\n' at last char
                        // WARN: '\r' if windows server!
                        while (recv != 10) {
                            sb.append((char) recv);
                            recv = in.read();
                        }

                        String s = sb.toString();
                        ChatLib.debug(s);
                        JsonParser jsonParser = new JsonParser();
                        JsonObject singleObject = jsonParser.parse(s).getAsJsonObject();
                        int type = singleObject.get("type").getAsInt();
                        String message = singleObject.get("msg").getAsString();
                        String name = singleObject.get("name").getAsString();
                        ChatLib.xjchat(type, name, message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            }).start();
        } catch (IOException e) {
            disconnect();
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        ChatLib.chat("Disconnected from xc server. This may be caused by server updating. Trying to reconnect...");
        connected = false;
        connect();
    }

    public static void chat(String message) {
        out.println(message);
        out.flush();
    }
}
