package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import net.minecraft.client.Minecraft;

public class SwitchInput extends Button {
    public BooleanSetting setting;

    public SwitchInput(ConfigGuiNew gui, BooleanSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        width = setting.width;
        height = setting.height;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY) {
        int startX = this.xPosition + width - 30;
        int startY = this.yPosition + 5;
        drawRect(startX, startY + 3, startX + 25, startY + 6, Colors.WHITE.getRGB());
        if (setting.get(Boolean.class))
            drawRect(startX + 25 - 9, startY, startX + 25, startY + 9, Colors.GREEN.getRGB());
        else drawRect(startX, startY, startX + 9, startY + 9, Colors.RED.getRGB());
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX <= this.xPosition + this.width && mouseY <= this.yPosition + this.height) {
            boolean val = !this.setting.get(Boolean.class);
            this.setting.set(val);
            gui.update(setting, val);
            return true;
        }
        return false;
    }
}
