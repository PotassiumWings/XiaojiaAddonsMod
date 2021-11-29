package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;

public class SelectSetting extends ParentSetting {
    public String[] options;

    public SelectSetting(Property annotation, Field field) {
        super(annotation, field);
        this.options = annotation.options();
    }

    public boolean set(Object value) {
        if (((Number) value).intValue() > this.options.length - 1)
            return super.set(0);
        if (((Number) value).intValue() < 0)
            return super.set(this.options.length - 1);
        return super.set(value);
    }
}
