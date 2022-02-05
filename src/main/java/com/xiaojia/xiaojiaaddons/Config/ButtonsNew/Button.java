package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public abstract class Button extends GuiButton {
    public ConfigGuiNew gui;
    public Setting setting;

    public Button(ConfigGuiNew gui, Setting setting, int x, int y) {
        super(0, x, y, "");
        this.setting = setting;
        this.gui = gui;
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {}

    public abstract void draw(Minecraft mc, int mouseX, int mouseY);

    public static Button buttonFromSetting(ConfigGuiNew gui, Setting setting, int x, int y) {
        switch (setting.annotation.type()) {
            case BOOLEAN:
                return new SwitchInput(gui, (BooleanSetting) setting, x, y);
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
