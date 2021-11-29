package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.Config.ConfigGui;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import net.minecraft.client.Minecraft;

public class SwitchInput extends Button {
    public BooleanSetting setting;

    public SwitchInput(ConfigGui gui, BooleanSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        width = 25;
        height = 9;
        xPosition -= width;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        drawRect(xPosition, yPosition + 3, xPosition + width, yPosition + 6, Colors.WHITE.getRGB());
        if (setting.get(Boolean.class))
            drawRect(xPosition + width - 9, yPosition, xPosition + width, yPosition + height, Colors.GREEN.getRGB());
        else drawRect(xPosition, yPosition, xPosition + 9, yPosition + height, Colors.RED.getRGB());
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX <= this.xPosition + this.width && mouseY <= this.yPosition + this.height) {
            this.setting.set(!this.setting.get(Boolean.class));
            gui.update();
            return true;
        }
        return false;
    }
}
