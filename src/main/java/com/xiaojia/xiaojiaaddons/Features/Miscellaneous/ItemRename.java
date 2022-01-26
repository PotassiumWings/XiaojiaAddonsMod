package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import joptsimple.internal.Strings;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ItemRename {
    public static HashMap<String, String> renameMap = new HashMap<>();
    public static String fileName = "config/XiaojiaAddonsItemRename.cfg";

    public static void process(String[] strings) {
        if (strings.length < 2) {
            ChatLib.chat(getUsage());
            return;
        }
        String command = strings[1];
        switch (command) {
            case "clearall":
                renameMap.clear();
                save();
                ChatLib.chat("&aSuccessfully removed all custom names!");
                break;
            case "clear":
                renameMap.remove(NBTUtils.getUUID(ControlUtils.getHeldItemStack()));
                save();
                ChatLib.chat("&aSuccessfully removed custom name of held item!");
                break;
            case "set":
                String name = Strings.join(Arrays.copyOfRange(strings, 2, strings.length), " ");
                String uuid = NBTUtils.getUUID(ControlUtils.getHeldItemStack());
                if (uuid.equals("")) ChatLib.chat("&cNo uuid for this item, can't rename!");
                else {
                    renameMap.put(uuid, name);
                    save();
                    ChatLib.chat("&aSuccessfully set custom name for held item!");
                }
                break;
            default:
                ChatLib.chat(getUsage());
        }
    }

    public static String getUsage() {
        return "&c/xj rename clear&b to clear custom name of held item.\n" +
                "&c/xj rename clearall&b to clear custom names of all items.\n" +
                "&c/xj rename set name&b to set custom name of held item.\n";
    }

    public static void load() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get(fileName));
                Type type = (new TypeToken<HashMap<String, String>>() {
                }).getType();
                HashMap<String, String> settingsFromConfig = (new Gson()).fromJson(reader, type);
                for (Map.Entry<String, String> fromConfig : settingsFromConfig.entrySet()) {
                    renameMap.put(fromConfig.getKey(), fromConfig.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("Error while loading item rename config file");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            String json = (new Gson()).toJson(renameMap);
            Files.write(Paths.get(fileName), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error while saving item rename config file");
            e.printStackTrace();
        }
    }
}
