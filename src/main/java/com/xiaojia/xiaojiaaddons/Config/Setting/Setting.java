package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;

public class Setting {
    public String name;
    public String description;
    public boolean illegal;
    public Property annotation;
    public Field field;
    public ParentSetting parent = null;

    public Setting(Property annotation, Field field) {
        this.annotation = annotation;
        this.field = field;
        this.name = annotation.name();
        this.illegal = annotation.illegal();
        this.description = annotation.description();
    }

    public int getIndent(int startingIndent) {
        return getIndent(startingIndent, this);
    }

    public int getIndent(int startingIndent, Setting setting) {
        if (setting.parent != null) return setting.getIndent(startingIndent + 10, setting.parent);
        return startingIndent;
    }

    public <T> T get(Class<T> type) {
        try {
            return type.cast(this.field.get(Object.class));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean set(Object value) {
        try {
            this.field.set(value.getClass(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
