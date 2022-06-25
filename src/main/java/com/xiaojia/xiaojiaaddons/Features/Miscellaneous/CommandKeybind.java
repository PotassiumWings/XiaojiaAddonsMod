package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class CommandKeybind {
    public static String getUsage() {
        return "&c/xj keybind add &ecommand&b to add keybind.\n" +
                "&c/xj keybind remove &ecommand&b to remove keybind.\n" +
                "&c/xj keybinds &bto list all keybinds.\n" +
                "&eKeybinds are set in Options -> Controls.";
    }

    public static void list() {
        if (XiaojiaKeyBind.keyBinds.size() == 0) {
            ChatLib.chat("You have no keybind.");
            return;
        }
        for (XiaojiaKeyBind keybind : XiaojiaKeyBind.keyBinds) {
            IChatComponent chatComponent = new ChatComponentText(ChatLib.addColor("&9[XJA] > &e" + keybind.getCommand() + " &b(" + Keyboard.getKeyName(keybind.getBind().getKeyCode()) + ") &r&c&l[REMOVE]"));
            ChatStyle chatStyle = new ChatStyle();
            chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj keybind removeWithKey " + keybind.getBind().getKeyCode() + " " + keybind.getCommand()));
            chatStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to remove this keybind")));
            chatComponent.setChatStyle(chatStyle);
            mc.thePlayer.addChatMessage(chatComponent);
        }
    }

    public static void add(String command) {
        XiaojiaKeyBind bind = new XiaojiaKeyBind(command, 0);
        if (XiaojiaKeyBind.keyBinds.contains(bind)) {
            ChatLib.chat("Keybind already exists!");
            return;
        }
        XiaojiaKeyBind.keyBinds.add(bind);
        saveKeyBinds();
        ChatLib.chat("&aAdded&b keybind \"&e" + command + "&b\"!");
    }

    public static void remove(String command) {
        XiaojiaKeyBind bind = XiaojiaKeyBind.getKeybind(command, -1);
        if (bind == null) {
            ChatLib.chat("No such keybind! Are the cases matching correctly?");
            return;
        }
        mc.gameSettings.keyBindings = (KeyBinding[]) ArrayUtils.removeElement((Object[]) mc.gameSettings.keyBindings, bind.getBind());
        XiaojiaKeyBind.keyBinds.remove(bind);
        saveKeyBinds();
        ChatLib.chat("&cRemoved&b keybind \"&e" + command + "&b\"!");
    }

    public static void remove(String command, String keyStr) {
        int key;
        try {
            key = Integer.parseInt(keyStr);
        } catch (Exception e) {
            ChatLib.chat("Not a number!");
            return;
        }
        XiaojiaKeyBind bind = XiaojiaKeyBind.getKeybind(command, key);
        if (bind == null) {
            ChatLib.chat("No such keybind! Are the cases and keycode matching correctly?");
            return;
        }
        mc.gameSettings.keyBindings = (KeyBinding[]) ArrayUtils.removeElement((Object[]) mc.gameSettings.keyBindings, bind.getBind());
        XiaojiaKeyBind.keyBinds.remove(bind);
        saveKeyBinds();
        ChatLib.chat("&cRemoved&b keybind \"&e" + command + "&b\" (" + Keyboard.getKeyName(bind.getBind().getKeyCode()) + ") !");
    }

    public static void loadKeyBinds() {
        try {
            File xiaoJiaKeyBinds = new File("config/XiaoJiaAddonsKeybinds.cfg");
            if (xiaoJiaKeyBinds.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get("config/XiaoJiaAddonsKeybinds.cfg"));
                Type type = (new TypeToken<HashMap<String, Integer>>() {
                }).getType();
                HashMap<String, Integer> settingsFromConfig = (new Gson()).fromJson(reader, type);
                for (Map.Entry<String, Integer> entry : settingsFromConfig.entrySet())
                    XiaojiaKeyBind.keyBinds.add(new XiaojiaKeyBind(entry.getKey(), entry.getValue()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        KeyBinding h = new KeyBinding("Use /xj keybind to add keybinds!", 0, "Addons - XiaojiaAddons KeyBind");
        ClientRegistry.registerKeyBinding(h);

    }

    public static void saveKeyBinds() {
        try {
            HashMap<String, Integer> convertedKeyBinds = new HashMap<>();
            for (XiaojiaKeyBind keyBind : XiaojiaKeyBind.keyBinds)
                convertedKeyBinds.put(keyBind.getCommand(), keyBind.getBind().getKeyCode());
            String json = (new Gson()).toJson(convertedKeyBinds);
            Path path = Paths.get("config/XiaoJiaAddonsKeybinds.cfg");
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
            ChatLib.chat("Saved! " + json);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        for (XiaojiaKeyBind keybind : XiaojiaKeyBind.keyBinds)
            if (keybind.getBind().isPressed()) {
                if (keybind.getCommand().startsWith("/") &&
                        ClientCommandHandler.instance.executeCommand(mc.thePlayer, keybind.getCommand()) != 0) continue;
                mc.thePlayer.sendChatMessage(keybind.getCommand());
            }
    }
}
