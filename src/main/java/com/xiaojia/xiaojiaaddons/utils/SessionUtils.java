package com.xiaojia.xiaojiaaddons.utils;

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
            } catch (NoSuchFieldException e) {
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
            } catch (NoSuchFieldException e) {
                field = Session.class.getDeclaredField("field_148257_b");
            }
            field.setAccessible(true);
            return ((String) field.get(getSession())).replaceAll("-", "");
        } catch (Exception e) {
            return "null";
        }
    }

    public static String getName() {
        Field field;
        try {
            try {
                field = Session.class.getDeclaredField("username");
            } catch (NoSuchFieldException e) {
                field = Session.class.getDeclaredField("field_74286_b");
            }
            field.setAccessible(true);
            return (String) field.get(getSession());
        } catch (Exception e) {
            return "null";
        }
    }

    public static boolean isDev() {
        return getUUID().equals("1c6d48a96cb3465681382590ec82fa68");
    }
}
