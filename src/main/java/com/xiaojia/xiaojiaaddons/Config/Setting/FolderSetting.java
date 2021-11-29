package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;

public class FolderSetting extends Setting {
    public FolderSetting(Property annotation, Field field) {
        super(annotation, field);
    }

    public boolean isSonEnabled() {
        for (Setting setting : sons) {
            if (setting instanceof BooleanSetting && setting.get(Boolean.class)) return true;
            if (setting instanceof FolderSetting && ((FolderSetting) setting).isSonEnabled()) return true;
        }
        return false;
    }

    public boolean setRecursively(boolean value) {
        boolean success = set(value);
        if (!value) {
            for (Setting setting : sons) {
                if (setting instanceof FolderSetting) {
                    success &= ((FolderSetting) setting).setRecursively(false);
                }
            }
        }
        return success;
    }
}
