package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.KeyBind;

import java.util.ArrayList;

public class KeyBindUtils {
    public static final ArrayList<KeyBind> keyBinds = new ArrayList<>();

    public static void addKeyBind(KeyBind keyBind) {
        keyBinds.add(keyBind);
    }
}

