package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.ConfigGui;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import net.minecraft.client.gui.GuiButton;

public abstract class Button extends GuiButton {
    public ConfigGui gui;
    public Setting setting;

    public Button(ConfigGui gui, Setting setting, int x, int y) {
        super(0, x, y, "");
        this.setting = setting;
        this.gui = gui;
    }

    public static Button buttonFromSetting(ConfigGui gui, Setting setting, int x, int y) {
        switch (setting.annotation.type()) {
            case BOOLEAN:
                return new SwitchInput(gui, (BooleanSetting) setting, x, y);
            case CHECKBOX:
                return new CheckBoxInput(gui, (BooleanSetting) setting, x, y);
            case FOLDER:
                return new FolderInput(gui, (FolderSetting) setting, x, y);
            case NUMBER:
                return new NumberInput(gui, (NumberSetting) setting, x, y);
            case SELECT:
                return new SelectInput(gui, (SelectSetting) setting, x, y);
        }
        return null;
    }
}
