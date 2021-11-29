package com.xiaojia.xiaojiaaddons.Config.Setting;

import com.xiaojia.xiaojiaaddons.Config.Property;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ParentSetting extends Setting {
    public ArrayList<Setting> sons = new ArrayList<>();

    public ParentSetting(Property annotation, Field field) {
        super(annotation, field);
    }

    public ArrayList<Setting> getSon(ArrayList<Setting> settings) {
        ArrayList<Setting> res = new ArrayList<>();
        for (Setting setting : settings) {
            if (setting.parent == this)
                res.add(setting);
        }
        return res;
    }
}
