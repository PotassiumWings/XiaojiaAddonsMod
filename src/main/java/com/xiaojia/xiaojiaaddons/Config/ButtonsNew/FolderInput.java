package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import net.minecraft.client.Minecraft;

public class FolderInput extends Button {
    public FolderSetting setting;

    public FolderInput(ConfigGuiNew gui, FolderSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        width = setting.width;
        height = setting.height;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY) {
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX <= this.xPosition + this.width && mouseY <= this.yPosition + this.height) {
            boolean val = !this.setting.get(Boolean.class);
            this.setting.setRecursively(val);
            gui.update(setting, val);
            return true;
        }
        return false;
    }
}
