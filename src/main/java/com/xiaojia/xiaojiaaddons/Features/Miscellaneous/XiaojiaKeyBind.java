package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;

public class XiaojiaKeyBind {
    public static ArrayList<XiaojiaKeyBind> keyBinds = new ArrayList<>();

    private final String command;
    private final KeyBinding bind;

    public XiaojiaKeyBind(String command, int key) {
        this.command = command;
        this.bind = new KeyBinding(command, key, "Addons - XiaojiaAddons KeyBind");
        ClientRegistry.registerKeyBinding(this.bind);
    }

    public String getCommand() {
        return this.command;
    }

    public KeyBinding getBind() {
        return this.bind;
    }

    public static XiaojiaKeyBind getKeybind(String name, int key) {
        for (XiaojiaKeyBind keyBind : keyBinds) {
            if (keyBind.getCommand().equalsIgnoreCase(name) && (key == -1 || keyBind.getBind().getKeyCode() == key))
                return keyBind;
        }
        return null;
    }
}
