package com.xiaojia.xiaojiaaddons.utils;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class SessionUtils {
    public static String getUUID() {
        return mc.getSession().getProfile().getId().toString();
    }

    public static String getName() {
        return mc.getSession().getUsername();
    }
}
