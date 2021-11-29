package com.xiaojia.xiaojiaaddons.Config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Config {
    private static final String fileName = "config/XiaojiaAddons.cfg";

    public static ArrayList<Setting> collect(Class<Configs> instance) {
        Field[] fields = instance.getDeclaredFields();
        ArrayList<Setting> settings = new ArrayList<>();
        for (Field field : fields) {
            Property annotation = field.getAnnotation(Property.class);
            if (annotation == null) continue;
            switch (annotation.type()) {
                case BOOLEAN:
                case CHECKBOX:
                    settings.add(new BooleanSetting(annotation, field, annotation.type()));
                    break;
                case NUMBER:
                    settings.add(new NumberSetting(annotation, field));
                    break;
                case SELECT:
                    settings.add(new SelectSetting(annotation, field));
                    break;
                case FOLDER:
                    settings.add(new FolderSetting(annotation, field));
                    break;
            }
        }
        for (Setting setting : settings) {
            if (!setting.annotation.parent().equals("")) {
                setting.parent = getSetting(setting.annotation.parent(), settings);
                if (setting.parent != null)
                    setting.parent.sons.add(setting);
            }
        }
        ArrayList<Setting> res = new ArrayList<>();
        for (Setting setting: settings) {
            if (setting.parent != null) continue;
            dfs(res, setting);
        }
        return res;
    }

    private static void dfs(ArrayList<Setting> settings, Setting currentSetting) {
        settings.add(currentSetting);
        for (Setting setting: currentSetting.sons) {
            dfs(settings, setting);
        }
    }

    public static Setting getSetting(String name, ArrayList<Setting> settings) {
        for (Setting setting : settings) {
            if (setting.name.equals(name))
                return setting;
        }
        return null;
    }

    public static void load() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get(fileName));
                Type type = (new TypeToken<HashMap<String, Object>>() {
                }).getType();
                HashMap<String, Object> settingsFromConfig = (new Gson()).fromJson(reader, type);
                for (Map.Entry<String, Object> fromConfig : settingsFromConfig.entrySet()) {
                    Setting beingUpdated = getSetting(fromConfig.getKey(), XiaojiaAddons.settings);
                    if (beingUpdated != null) {
                        if (beingUpdated instanceof NumberSetting || beingUpdated instanceof SelectSetting) {
                            beingUpdated.set(((Double) fromConfig.getValue()).intValue());
                        } else {
                            beingUpdated.set(fromConfig.getValue());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error while loading config file");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            HashMap<String, Object> convertedSettings = new HashMap<>();
            for (Setting setting : XiaojiaAddons.settings) {
                if (setting instanceof FolderSetting)
                    continue;
                convertedSettings.put(setting.name, setting.get(Object.class));
            }
            String json = (new Gson()).toJson(convertedSettings);
            Files.write(Paths.get(fileName), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error while saving config file");
            e.printStackTrace();
        }
    }
}
