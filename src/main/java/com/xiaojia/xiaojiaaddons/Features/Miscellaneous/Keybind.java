package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;

public class Keybind {
    public static ArrayList<Keybind> keybinds = new ArrayList<>();

    private String command;
    private KeyBinding bind;

    public Keybind(String command, int key) {
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

    public static Keybind getKeybind(String name, int key) {
        for (Keybind keybind : keybinds) {
            if (keybind.getCommand().equalsIgnoreCase(name) && (key == -1 || keybind.getBind().getKeyCode() == key))
                return keybind;
        }
        return null;
    }
}
