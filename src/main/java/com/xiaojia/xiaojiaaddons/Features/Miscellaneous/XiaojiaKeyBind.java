package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.HashSet;

public class XiaojiaKeyBind {
    public static HashSet<XiaojiaKeyBind> keyBinds = new HashSet<>();

    private final String command;
    private final KeyBinding bind;

    public XiaojiaKeyBind(String command, int key) {
        this.command = command;
        this.bind = new KeyBinding(command, key, "Addons - XiaojiaAddons KeyBind");
        ClientRegistry.registerKeyBinding(this.bind);
    }

    public boolean equals(Object o) {
        if (o instanceof XiaojiaKeyBind) {
            return ((XiaojiaKeyBind) o).command.equals(command);
        }
        return false;
    }

    public int hashCode() {
        return command.hashCode();
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
