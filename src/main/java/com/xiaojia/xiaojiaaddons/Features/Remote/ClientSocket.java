package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

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
                    authenticate();

                    boolean first = true;
                    while (true) {
                        StringBuilder sb = new StringBuilder();
                        int recv = in.read();
                        if (recv == -1) break;
                        if (first) {
                            first = false;
                            ChatLib.debug("Connected to xc server!");
                        }

                        // linux, '\n' at last char
                        // WARN: '\r' if windows server!
                        while (recv != 10) {
                            sb.append((char) recv);
                            recv = in.read();
                        }

                        String s = sb.toString();
                        ChatLib.debug(s);

                        // type 0-2, normal chat / puzzle fail / death
                        Pattern pattern = Pattern.compile("^\\{" +
                                "\"uuid\": \"(.*)\", " +
                                "\"name\": \"(.*)\", " +
                                "\"msg\": \"(.*)\", " +
                                "\"type\": \"(.*)\"}$"
                        );
                        Matcher matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            String uuid = matcher.group(1);
                            String name = matcher.group(2);
                            String message = matcher.group(3);
                            int type = Integer.parseInt(matcher.group(4));
                            assert (type == 0 || type == 1 || type == 2);
                            // chat
                            ChatLib.xjchat(type, name, message);
                            continue;
                        }

                        // type 3, show item
                        pattern = Pattern.compile("^\\{" +
                                "\"uuid\": \"(.*)\", " +
                                "\"name\": \"(.*)\", " +
                                "\"dis\": \"(.*)\", " +
                                "\"type\": \"(.*)\", " +
                                "\"nbt\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            String uuid = matcher.group(1);
                            String name = matcher.group(2);
                            String dis = matcher.group(3);
                            int type = Integer.parseInt(matcher.group(4));
                            String nbt = matcher.group(5);
                            assert (type == 3);
                            ChatLib.showItem(type, name, dis, nbt);
                            return;
                        }

                        // type 4, authenticate
                        pattern = Pattern.compile("^\\{" +
                                "\"uuid\": \"(.*)\", " +
                                "\"name\": \"(.*)\", " +
                                "\"type\": \"(.*)\", " +
                                "\"ver\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            String uuid = matcher.group(1);
                            String name = matcher.group(2);
                            int type = Integer.parseInt(matcher.group(3));
                            String ver = matcher.group(4);
                            assert (type == 4);
                            ChatLib.debug("Received ack.");
                        }
                    }
                } catch (Exception e) {
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
        ChatLib.debug("Disconnected from xc server. This may be caused by server updating. Trying to reconnect...");
        connected = false;
        connect();
    }

    public static void chat(String message) {
        out.println(message);
        out.flush();
    }

    public static void authenticate() {
        out.println(String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"type\": \"%d\", \"ver\": \"%s\"}",
                mc.getSession().getProfile().getId().toString(), mc.getSession().getUsername(), 4, XiaojiaAddons.VERSION));
        out.flush();
    }
}
