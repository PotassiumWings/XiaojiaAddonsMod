package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;

public class Keybind {
    public static ArrayList<Keybind> keybinds = new ArrayList<>();

    private String command;
    //private int key;
    private KeyBinding bind;

    public Keybind(String command) {
        this.command = command;
        //this.key = key;
        this.bind = new KeyBinding(command, 0, "Keybinds - XiaoJiaAddons");
        ClientRegistry.registerKeyBinding(this.bind);
    }

    public Keybind(String command, int key) {
        this.command = command;
        //this.key = key;
        this.bind = new KeyBinding(command, key, "Keybinds - XiaoJiaAddons");
        ClientRegistry.registerKeyBinding(this.bind);
    }

    public String getCommand() {
        return this.command;
    }

   /* public int getKey() {
        return this.key;
    }

    public void setKey(int key1) {
        key = key1;
    }*/

    public KeyBinding getBind() {
        return this.bind;
    }

    public static Keybind getKeybindByName(String name) {
        for (Keybind keybind : keybinds) {
            if (keybind.getCommand().equalsIgnoreCase(name))
                return keybind;
        }
        return null;
    }

    public static Keybind getKeybind(String name, int key) {
        for (Keybind keybind : keybinds) {
            if (keybind.getCommand().equalsIgnoreCase(name) && keybind.getBind().getKeyCode() == key)
                return keybind;
        }
        return null;
    }
}
