package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.settings.KeyBinding;

public class KeyBind {
    private static KeyBinding keyBinding;

    public KeyBind(String description, int key) {
        keyBinding = new KeyBinding(description, key, "Addons - XiaojiaAddonsMod");
        KeyBindUtils.addKeyBind(description, this);
    }

    public boolean isPressed() {
        return keyBinding.isPressed();
    }

    public KeyBinding mcKeyBinding() {
        return keyBinding;
    }
}
