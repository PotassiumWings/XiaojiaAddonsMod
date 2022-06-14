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

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class CommandKeybind {
    public static String getUsage() {
        return "&c/xj keybind add &ecommand&b to add keybind.\n" +
                "&c/xj keybind remove &ecommand&b to remove keybind.\n" +
                "&c/xj keybinds &bto list all keybinds.";
    }

    public static void list() {
        if (Keybind.keybinds.size() == 0) {
            ChatLib.chat("You have no keybind.");
            return;
        }
        for (Keybind keybind : Keybind.keybinds) {
            /*
            KeyEvent key = new KeyEvent;
            key.setKeyCode(keybind.getBind().getKeyCode());
            */
            IChatComponent chatComponent = new ChatComponentText(ChatLib.addColor("&9[XJA] > &e" + keybind.getCommand() + " &b(" + keybind.getBind().getKeyCode() + ") &r&c&l[REMOVE]"));
            ChatStyle chatStyle = new ChatStyle();
            chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xj keybind remove " + keybind.getCommand()));
            chatStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to remove this keybind")));
            chatComponent.setChatStyle(chatStyle);
            mc.thePlayer.addChatMessage(chatComponent);
        }
    }

    public static void add(String command) {
        Keybind bind = new Keybind(command);
        Keybind.keybinds.add(bind);
        saveKeybinds();
        ChatLib.chat("&aAdded&b keybind \"&e" + command + "&b\"!");
    }

    public static void remove(String command) {
        Keybind bind = Keybind.getKeybindByName(command);
        if (bind == null) {
            ChatLib.chat("No such keybind! Are the cases matching correctly?");
            return;
        }
        mc.gameSettings.keyBindings = (KeyBinding[])ArrayUtils.removeElement((Object[])mc.gameSettings.keyBindings, bind.getBind());
        Keybind.keybinds.remove(bind);
        saveKeybinds();
        ChatLib.chat("&cRemoved&b keybind \"&e" + command + "&b\"!");
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        for (Keybind keybind : Keybind.keybinds)
            if (keybind.getBind().isPressed()) {
                 //mc.ingameGUI.getChatGUI().addToSentMessages(keybind.getCommand());
                 mc.thePlayer.sendChatMessage(keybind.getCommand()); //todo: add client side commands support
            }
    }

    public static void loadKeybinds() {
        try {
            File XiaoJiaKeybinds = new File(mc.mcDataDir.getPath() + "/config/XiaoJiaAddonsKeybinds.cfg");
            if (!XiaoJiaKeybinds.exists()) {
                XiaoJiaKeybinds.createNewFile();
            } else {
                Reader reader = Files.newBufferedReader(Paths.get(mc.mcDataDir.getPath() + "/config/XiaoJiaAddonsKeybinds.cfg"));
                Type type = (new TypeToken<ArrayList<Keybind>>() {}).getType();
                ArrayList<Keybind> settingsFromConfig = (new Gson()).fromJson(reader, type);
                Keybind.keybinds.addAll(settingsFromConfig);
            }
        } catch (Exception exception) {}
        KeyBinding h = new KeyBinding("Use /xj keybind to add keybinds!", 0, "Keybinds - XiaoJiaAddons");
        ClientRegistry.registerKeyBinding(h);
       /* for (Keybind keybind : Keybind.keybinds)
            ClientRegistry.registerKeyBinding(keybind.getBind());*/
    }

    public static void saveKeybinds() {
        try {
            String json = (new Gson()).toJson(Keybind.keybinds);
            Path path = Paths.get(mc.mcDataDir.getPath() + "/config/XiaoJiaAddonsKeybinds.cfg");
            Files.write(path, json.getBytes(StandardCharsets.UTF_8)); //todo: save command only
        } catch (Exception exception) {}
    }
}
