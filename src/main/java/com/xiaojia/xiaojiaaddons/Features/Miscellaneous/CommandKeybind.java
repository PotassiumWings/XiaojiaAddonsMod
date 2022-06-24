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
import java.util.ArrayList;
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
        if (Keybind.keybinds.size() == 0) {
            ChatLib.chat("You have no keybind.");
            return;
        }
        for (Keybind keybind : Keybind.keybinds) {
            IChatComponent chatComponent = new ChatComponentText(ChatLib.addColor("&9[XJA] > &e" + keybind.getCommand() + " &b(" + Keyboard.getKeyName(keybind.getBind().getKeyCode()) + ") &r&c&l[REMOVE]"));
            ChatStyle chatStyle = new ChatStyle();
            chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj keybind removeWithKey " + keybind.getBind().getKeyCode() + " " + keybind.getCommand()));
            chatStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to remove this keybind")));
            chatComponent.setChatStyle(chatStyle);
            mc.thePlayer.addChatMessage(chatComponent);
        }
    }

    public static void add(String command) {
        Keybind bind = new Keybind(command, 0);
        Keybind.keybinds.add(bind);
        saveKeybinds();
        ChatLib.chat("&aAdded&b keybind \"&e" + command + "&b\"!");
    }

    public static void remove(String command) {
        Keybind bind = Keybind.getKeybind(command, -1);
        if (bind == null) {
            ChatLib.chat("No such keybind! Are the cases matching correctly?");
            return;
        }
        mc.gameSettings.keyBindings = (KeyBinding[]) ArrayUtils.removeElement((Object[]) mc.gameSettings.keyBindings, bind.getBind());
        Keybind.keybinds.remove(bind);
        saveKeybinds();
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
        Keybind bind = Keybind.getKeybind(command, key);
        if (bind == null) {
            ChatLib.chat("No such keybind! Are the cases and keycode matching correctly?");
            return;
        }
        mc.gameSettings.keyBindings = (KeyBinding[])ArrayUtils.removeElement((Object[])mc.gameSettings.keyBindings, bind.getBind());
        Keybind.keybinds.remove(bind);
        saveKeybinds();
        ChatLib.chat("&cRemoved&b keybind \"&e" + command + "&b\" (" + Keyboard.getKeyName(bind.getBind().getKeyCode()) + ") !");
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        for (Keybind keybind : Keybind.keybinds)
            if (keybind.getBind().isPressed()) {
                //mc.ingameGUI.getChatGUI().addToSentMessages(keybind.getCommand());
                if (keybind.getCommand().startsWith("/") &&
                        ClientCommandHandler.instance.executeCommand(mc.thePlayer, keybind.getCommand()) != 0) continue;
                mc.thePlayer.sendChatMessage(keybind.getCommand());
            }
    }

    public static void loadKeybinds() {
        try {
            File xiaoJiaKeybinds = new File(mc.mcDataDir.getPath() + "/config/XiaoJiaAddonsKeybinds.cfg");
            if (!xiaoJiaKeybinds.exists()) {
                xiaoJiaKeybinds.createNewFile();
            } else {
                Reader reader = Files.newBufferedReader(Paths.get(mc.mcDataDir.getPath() + "/config/XiaoJiaAddonsKeybinds.cfg"));
                Type type = (new TypeToken<ArrayList<Keybind>>() {}).getType();
                ArrayList<Keybind> settingsFromConfig = (new Gson()).fromJson(reader, type);
                for (Keybind keybind : settingsFromConfig)
                    Keybind.keybinds.add(new Keybind(keybind.getCommand(), keybind.getBind().getKeyCode()));
            }
        } catch (Exception exception) {}
        KeyBinding h = new KeyBinding("Use /xj keybind to add keybinds!", 0, "Keybinds - XiaoJiaAddons");
        ClientRegistry.registerKeyBinding(h);

    }

    public static void saveKeybinds() {
        try {
            String json = (new Gson()).toJson(Keybind.keybinds);
            Path path = Paths.get(mc.mcDataDir.getPath() + "/config/XiaoJiaAddonsKeybinds.cfg");
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {}
    }
}
