package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Property;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;

import java.lang.reflect.Field;

public class FolderSetting extends ParentSetting {
    public FolderSetting(Property annotation, Field field) {
        super(annotation, field);
    }

    public static boolean isEnabled(String name) {
        Setting setting = Config.getSetting(name, XiaojiaAddons.settings);
        return setting != null && ((FolderSetting) setting).isSonEnabled();
    }

    public boolean isSonEnabled() {
        for (Setting setting : sons) {
            if (setting instanceof BooleanSetting && setting.get(Boolean.class)) return true;
            if (setting instanceof FolderSetting && ((FolderSetting) setting).isSonEnabled()) return true;
        }
        return false;
    }
}
