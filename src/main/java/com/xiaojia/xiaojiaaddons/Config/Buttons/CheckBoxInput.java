package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.Config.ConfigGui;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.utils.FontUtils;
import net.minecraft.client.Minecraft;

public class CheckBoxInput extends Button {
    public BooleanSetting setting;

    public CheckBoxInput(ConfigGui gui, BooleanSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        width = height = 9;
        xPosition -= width;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, Colors.WHITE.getRGB());
        if (this.setting.get(Boolean.class)) {
            FontUtils.drawString("\u00a70x", this.xPosition + 2, this.yPosition, false);
        } else if (this.hovered) {
            FontUtils.drawString("\u00a7ax", this.xPosition + 2, this.yPosition, false);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            this.setting.set(!this.setting.get(Boolean.class));
            gui.update();
            return true;
        }
        return false;
    }
}
