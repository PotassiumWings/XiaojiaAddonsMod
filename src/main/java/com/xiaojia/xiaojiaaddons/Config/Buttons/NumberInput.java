package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.ConfigGui;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import net.minecraft.client.Minecraft;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NumberInput extends Button {
    private final int minusWidth = mc.fontRendererObj.getStringWidth("-");
    private final int plusWidth = mc.fontRendererObj.getStringWidth("+");
    private final int gap = 3;
    public NumberSetting setting;

    public NumberInput(ConfigGui gui, NumberSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        this.height = 10;
        updateText();
    }

    private boolean minusHovered(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition - this.width && mouseY >= this.yPosition && mouseX < this.xPosition - this.width + this.minusWidth + this.gap && mouseY < this.yPosition + this.height);
    }

    private boolean plusHovered(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition - this.plusWidth - this.gap && mouseY >= this.yPosition && mouseX < this.xPosition && mouseY < this.yPosition + this.height);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        mc.fontRendererObj.drawString((this.minusHovered(mouseX, mouseY) ? "\u00a7c" : "\u00a77") + "-", this.xPosition - this.width, this.yPosition, -1);
        mc.fontRendererObj.drawString(this.displayString, this.xPosition - this.width + this.minusWidth + this.gap, this.yPosition, -1);
        mc.fontRendererObj.drawString((this.plusHovered(mouseX, mouseY) ? "\u00a7a" : "\u00a77") + "+", this.xPosition - this.plusWidth, this.yPosition, -1);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.plusHovered(mouseX, mouseY) || this.minusHovered(mouseX, mouseY)) {
            int min = setting.min, max = setting.max, step = setting.step;
            int current = this.setting.get(Integer.class);
            int next;

            if (this.plusHovered(mouseX, mouseY)) next = current + step;
            else next = current - step;
            if (next <= min) next = min;
            if (next >= max) next = max;

            this.setting.set(next);
            updateText();
            gui.update();
            return true;
        }
        return false;
    }

    public void updateText() {
        this.displayString = ((this.setting.prefix == null) ? "" : this.setting.prefix) + this.setting.get(Integer.class) + ((this.setting.suffix == null) ? "" : this.setting.suffix);
        this.width = mc.fontRendererObj.getStringWidth(this.displayString) + this.plusWidth + this.minusWidth + this.gap * 2;
    }
}
