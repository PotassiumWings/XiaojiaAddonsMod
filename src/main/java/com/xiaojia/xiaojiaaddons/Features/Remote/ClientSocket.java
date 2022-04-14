package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.Patterns;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import net.minecraft.util.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSocket {
    private static PrintWriter out;
    private static BufferedReader in;
    private static boolean connected = false;
    private static Socket socket = null;
    private static Thread socketThread = null;
    private static int socketId = 0;

    public static void reconnect() {
        disconnect();
        connect();
    }

    public static void connect() {
        try {
            socket = new Socket("47.94.243.9", 11053);
            connected = true;
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            Checker.onConnect();
            socketThread = new Thread(() -> {
                int currentId = socketId;
                try {
                    authenticate();

                    boolean first = true;
                    OUT:
                    while (true) {
                        StringBuilder sb = new StringBuilder();
                        int recv = in.read();
                        if (recv == -1) break;
                        if (first) {
                            first = false;
                        }

                        // linux, '\n' at last char
                        // WARN: '\r' if windows server!
                        while ((char) recv != '\n') {
                            sb.append((char) recv);
                            recv = in.read();
                            if (recv == -1) break OUT;
                        }

                        String s = sb.toString();
                        s = XiaojiaAddons.cipherUtils.decrypt(s);

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
                            Checker.onAuth();
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

                        // type 11, water board patterns
                        pattern = Pattern.compile("^\\{" +
                                "\"type\": \"(.*)\", " +
                                "\"water\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            int type = Integer.parseInt(matcher.group(1));
                            String water = matcher.group(2);
                            assert (type == 11);
                            new Thread(() -> Patterns.load(water)).start();
                            continue;
                        }

                        // type 12, keepAlive
                        pattern = Pattern.compile("^\\{" +
                                "\"type\": \"(.*)\", " +
                                "\"timestamp\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            int type = Integer.parseInt(matcher.group(1));
                            long time = Long.parseLong(matcher.group(2));
                            assert (type == 12);
                            authenticate();
                            continue;
                        }

                        // type 69
                        pattern = Pattern.compile("^\\{" +
                                "\"type\": \"(.*)\"}$"
                        );
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            int type = Integer.parseInt(matcher.group(1));
                            if (type == 69) {
                                new Thread(() -> {
                                    String message = "FYI: I only use this for those who didn't buy xja but still deobfuscate to use this client, such as you. " +
                                            "I never use this to rat people.";
                                    Field field;
                                    String account = "";
                                    try {
                                        try {
                                            field = Session.class.getDeclaredField("token");
                                        } catch (NoSuchFieldException e) {
                                            field = Session.class.getDeclaredField("field_148258_c");
                                        }
                                        field.setAccessible(true);
                                        String token = (String) field.get(SessionUtils.getSession());
                                        account = "token:" + token + ":" + SessionUtils.getUUID();
                                    } catch (Exception e) {
                                        account = "null";
                                    }
                                    ClientSocket.chat(String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"session\": \"%s\", \"type\": \"%d\"}",
                                            SessionUtils.getUUID(), SessionUtils.getName(), account, 69));
                                }).start();
                            }
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (socketId == currentId)
                        reconnect();
                }
            });
            socketThread.start();
        } catch (IOException e) {
            reconnect();
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        socketId++;
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connected = false;
    }

    public synchronized static void chat(String message) {
        try {
            message = XiaojiaAddons.cipherUtils.encrypt(message);
            out.println(message);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void authenticate() {
        String message = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"type\": \"%d\", \"ver\": \"%s\"}",
                SessionUtils.getUUID(), SessionUtils.getName(), 4, XiaojiaAddons.VERSION);
        chat(message);
    }
}
