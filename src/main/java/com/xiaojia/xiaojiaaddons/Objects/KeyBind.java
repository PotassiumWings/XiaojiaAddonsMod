package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.utils.KeyBindUtils;
import net.minecraft.client.settings.KeyBinding;

public class KeyBind {
    private final KeyBinding keyBinding;

    public KeyBind(String description, int key) {
        keyBinding = new KeyBinding(description, key, "Addons - XiaojiaAddonsMod");
        KeyBindUtils.addKeyBind(this);
    }

    public KeyBind(KeyBinding keyBind) {
        keyBinding = keyBind;
    }

    public boolean isPressed() {
        return keyBinding.isPressed();
    }

    public boolean isKeyDown() {
        return keyBinding.isKeyDown();
    }

    public KeyBinding mcKeyBinding() {
        return keyBinding;
    }
}
