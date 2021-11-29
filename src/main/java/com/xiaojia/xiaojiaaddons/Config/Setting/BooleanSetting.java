package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;

public class BooleanSetting extends Setting {
    public Property.Type type;

    public BooleanSetting(Property annotation, Field field, Property.Type type) {
        super(annotation, field);
        this.type = type;
    }

    public boolean set(Object value) {
        try {
            for (Setting setting : sons) {
                setting.set(Boolean.FALSE);
            }
            return super.set(value);
        } catch (Exception e) {
            System.out.println("Filed to set " + name + " to " + value);
            e.printStackTrace();
            return false;
        }
    }
}
