package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Socket socket = new Socket("47.94.243.9", 11052);
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
                        while ((char) recv != '\n') {
                            sb.append((char) recv);
                            recv = in.read();
                        }

                        String s = sb.toString();
                        s = XiaojiaAddons.cipherUtils.decrypt(s);
                        ChatLib.debug(s);

                        // type 0-2, normal chat / puzzle fail / death;
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
                            continue;
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
                            continue;
                        }

                        // type 5 join, 6 left
                        pattern = Pattern.compile("^\\{" +
                                "\"uuid\": \"(.*)\", " +
                                "\"name\": \"(.*)\", " +
                                "\"type\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            String uuid = matcher.group(1);
                            String name = matcher.group(2);
                            int type = Integer.parseInt(matcher.group(3));
                            assert (type == 5 || type == 6);
                            if (type == 5) ChatLib.playerJoin(name);
                            else ChatLib.playerLeft(name);
                            continue;
                        }

                        // type 7, xc online
                        pattern = Pattern.compile("^\\{" +
                                "\"type\": \"(.*)\", " +
                                "\"msg\": \"((.|\\n)*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            int type = Integer.parseInt(matcher.group(1));
                            String msg = matcher.group(2);
                            assert (type == 7);
                            ChatLib.showXJCMessage(msg);
                            continue;
                        }

                        // type 8, color name
                        pattern = Pattern.compile("^\\{" +
                                "\"type\": \"(.*)\", " +
                                "\"rank\": \"(.*)\", " +
                                "\"color\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            int type = Integer.parseInt(matcher.group(1));
                            String rank = matcher.group(2);
                            String color = matcher.group(3);
                            assert (type == 8);
                            new Thread(() -> ColorName.setColorMap(rank, color)).start();
                            continue;
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
        synchronized (out) {
            try {
                message = XiaojiaAddons.cipherUtils.encrypt(message);
                out.println(message);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void authenticate() {
        String message = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"type\": \"%d\", \"ver\": \"%s\"}",
                SessionUtils.getUUID(), SessionUtils.getName(), 4, XiaojiaAddons.VERSION);
        chat(message);
    }
}
