package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import net.minecraft.client.gui.GuiButton;

public abstract class Button extends GuiButton {
    public Setting setting;

    public Button(Setting setting, int x, int y) {
        super(0, x, y, "");
        this.setting = setting;
    }

    public static Button buttonFromSetting(Setting setting, int x, int y) {
        switch (setting.annotation.type()) {
            case BOOLEAN:
                return new SwitchInput((BooleanSetting) setting, x, y);
            case CHECKBOX:
                return new CheckBoxInput((BooleanSetting) setting, x, y);
            case FOLDER:
                return new FolderInput((FolderSetting) setting, x, y);
            case NUMBER:
                return new NumberInput((NumberSetting) setting, x, y);
            case SELECT:
                return new SelectInput((SelectSetting) setting, x, y);
        }
        return null;
    }
}
