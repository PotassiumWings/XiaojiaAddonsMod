package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.TextSetting;
import net.minecraft.client.Minecraft;

public class TextInput extends Button {
    public TextSetting setting;

    public TextInput(ConfigGuiNew gui, TextSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        width = setting.width;
        height = setting.height;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY) {

    }
}
