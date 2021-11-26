package com.xiaojia.xiaojiaaddons.utils;

import java.util.HashMap;

public class KeyBindUtils {
    public static final HashMap<String, KeyBind> keyBinds = new HashMap<>();

    public static void addKeyBind(String description, KeyBind keyBind) {
        keyBinds.put(description, keyBind);
    }
}

