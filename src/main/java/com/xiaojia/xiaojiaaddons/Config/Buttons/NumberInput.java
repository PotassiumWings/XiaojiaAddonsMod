package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import net.minecraft.client.Minecraft;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NumberInput extends Button {
    private final int minusWidth = mc.fontRendererObj.getStringWidth("-");
    private final int plusWidth = mc.fontRendererObj.getStringWidth("+");
    private final int gap = 3;
    public NumberSetting setting;
    private boolean minusHovered = false;
    private boolean plusHovered = false;

    public NumberInput(NumberSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        this.height = 10;
        updateText();
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        this.plusHovered = (mouseX >= this.xPosition - this.plusWidth - this.gap && mouseY >= this.yPosition && mouseX < this.xPosition && mouseY < this.yPosition + this.height);
        this.minusHovered = (mouseX >= this.xPosition - this.width && mouseY >= this.yPosition && mouseX < this.xPosition - this.width + this.minusWidth + this.gap && mouseY < this.yPosition + this.height);
        mc.fontRendererObj.drawString((this.minusHovered ? "\u00a7c" : "\u00a77") + "-", this.xPosition - this.width, this.yPosition, -1);
        mc.fontRendererObj.drawString(this.displayString, this.xPosition - this.width + this.minusWidth + this.gap, this.yPosition, -1);
        mc.fontRendererObj.drawString((this.plusHovered ? "\u00a7a" : "\u00a77") + "+", this.xPosition - this.plusWidth, this.yPosition, -1);
    }

    public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
        if (this.plusHovered || this.minusHovered) {
            if (this.plusHovered)
                this.setting.set(this.setting.get(Integer.class) + this.setting.step);
            if (this.minusHovered)
                this.setting.set(this.setting.get(Integer.class) - this.setting.step);
            updateText();
            return true;
        }
        return false;
    }

    public void updateText() {
        this.displayString = ((this.setting.prefix == null) ? "" : this.setting.prefix) + this.setting.get(Integer.class) + ((this.setting.suffix == null) ? "" : this.setting.suffix);
        this.width = mc.fontRendererObj.getStringWidth(this.displayString) + this.plusWidth + this.minusWidth + this.gap * 2;
    }
}
