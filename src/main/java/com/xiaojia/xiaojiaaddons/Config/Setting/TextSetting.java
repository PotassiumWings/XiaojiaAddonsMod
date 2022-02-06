package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;

public class TextSetting extends Setting {
    public TextSetting(Property annotation, Field field) {
        super(annotation, field);
    }

    @Override
    public boolean set(Object value) {
        return super.set(value);
    }
}
