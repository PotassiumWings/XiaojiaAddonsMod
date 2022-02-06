package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;

public class TextSetting extends Setting {
    public String text;

    public TextSetting(Property annotation, Field field) {
        super(annotation, field);
        this.text = annotation.text();
    }

    public boolean set(Object value) {
        return super.set(value);
    }
}
