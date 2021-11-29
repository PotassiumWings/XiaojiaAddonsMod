package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import net.minecraft.client.Minecraft;

public class FolderInput extends Button {
    public FolderSetting setting;

    public FolderInput(FolderSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        width = 300;
        height = 9;
        xPosition -= width;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.hovered) {
            this.setting.set(!this.setting.get(Boolean.class));
            return true;
        }
        return false;
    }
}
