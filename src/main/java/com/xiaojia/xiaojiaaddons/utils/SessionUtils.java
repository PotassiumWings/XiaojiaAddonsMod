package com.xiaojia.xiaojiaaddons.utils;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class SessionUtils {
    public static String getUUID() {
        return mc.getSession().getProfile().getId().toString().replace("-", "");
    }

    public static String getName() {
        return mc.getSession().getUsername();
    }

    public static boolean isDev() {
        return getUUID().equals("1c6d48a96cb3465681382590ec82fa68");
    }
}
