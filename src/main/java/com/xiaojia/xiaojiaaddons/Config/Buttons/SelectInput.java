package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import net.minecraft.client.Minecraft;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class SelectInput extends Button {
    private final int leftWidth = mc.fontRendererObj.getStringWidth("<");
    private final int rightWidth = mc.fontRendererObj.getStringWidth(">");
    private final int gap = 3;
    public SelectSetting setting;
    private boolean leftHovered = false, rightHovered = false;

    public SelectInput(SelectSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        height = 10;
        updateText();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.rightHovered = (mouseX >= this.xPosition - this.rightWidth - this.gap && mouseY >= this.yPosition && mouseX < this.xPosition && mouseY < this.yPosition + this.height);
        this.leftHovered = (mouseX >= this.xPosition - this.width && mouseY >= this.yPosition && mouseX < this.xPosition - this.width + this.leftWidth + this.gap && mouseY < this.yPosition + this.height);
        mc.fontRendererObj.drawString((this.leftHovered ? "\u00a7a" : "\u00a77") + "<", this.xPosition - this.width, this.yPosition, -1);
        mc.fontRendererObj.drawString(this.displayString, this.xPosition - this.width + this.leftWidth + this.gap, this.yPosition, -1);
        mc.fontRendererObj.drawString((this.rightHovered ? "\u00a7a" : "\u00a77") + ">", this.xPosition - this.rightWidth, this.yPosition, -1);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.rightHovered || this.leftHovered) {
            if (this.rightHovered)
                this.setting.set(this.setting.get(Integer.class) + 1);
            if (this.leftHovered)
                this.setting.set(this.setting.get(Integer.class) - 1);
            updateText();
            return true;
        }
        return false;
    }

    public void updateText() {
        this.displayString = this.setting.options[this.setting.get(Integer.class)];
        this.width = mc.fontRendererObj.getStringWidth(this.displayString) + this.rightWidth + this.leftWidth + this.gap * 2;
    }
}
