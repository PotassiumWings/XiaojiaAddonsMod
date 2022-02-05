package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.NumberSetting;
import net.minecraft.client.Minecraft;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NumberInput extends Button {
    private final int minusWidth = mc.fontRendererObj.getStringWidth("-");
    private final int plusWidth = mc.fontRendererObj.getStringWidth("+");
    private final int superMinusWidth = mc.fontRendererObj.getStringWidth("--");
    private final int superPlusWidth = mc.fontRendererObj.getStringWidth("++");

    private final int gap = 3;
    private final int maxLen;
    public NumberSetting setting;

    public NumberInput(ConfigGuiNew gui, NumberSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        height = 10;
        width = mc.fontRendererObj.getStringWidth(setting.max + "");
        if (setting.prefix != null) width += mc.fontRendererObj.getStringWidth(setting.prefix);
        if (setting.suffix != null) width += mc.fontRendererObj.getStringWidth(setting.suffix);
        maxLen = width;

        width += plusWidth + minusWidth + gap * 2;
        width += superPlusWidth + superMinusWidth + gap * 2;
        xPosition += setting.width - 5;
        yPosition += 5;
        updateText();
    }

    private boolean superMinusHovered(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition - this.width && mouseY >= this.yPosition &&
                mouseX < this.xPosition - this.width + this.superMinusWidth + this.gap && mouseY < this.yPosition + this.height);
    }

    private boolean minusHovered(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition - this.width + this.superMinusWidth + this.gap && mouseY >= this.yPosition &&
                mouseX < this.xPosition - this.width + this.superMinusWidth + this.gap + this.minusWidth + this.gap && mouseY < this.yPosition + this.height);
    }

    private boolean plusHovered(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition - this.superPlusWidth - this.gap - this.plusWidth - this.gap && mouseY >= this.yPosition &&
                mouseX < this.xPosition - this.superPlusWidth - this.gap && mouseY < this.yPosition + this.height);
    }

    private boolean superPlusHovered(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition - this.superPlusWidth - this.gap && mouseY >= this.yPosition &&
                mouseX < this.xPosition && mouseY < this.yPosition + this.height);
    }

    public void draw(Minecraft mc, int mouseX, int mouseY) {
        if (setting.get(Integer.class) > setting.min)
            mc.fontRendererObj.drawString((
                            this.superMinusHovered(mouseX, mouseY) ? "\u00a7c" : "\u00a77") + "--",
                    this.xPosition - this.width, this.yPosition, -1
            );

        mc.fontRendererObj.drawString(
                (this.minusHovered(mouseX, mouseY) ? "\u00a7c" : "\u00a77") + "-",
                this.xPosition - this.width + this.gap + this.superMinusWidth, this.yPosition, -1
        );

        int left = this.xPosition - this.width + this.minusWidth + this.gap + this.superPlusWidth + this.gap;
        int length = mc.fontRendererObj.getStringWidth(displayString);

        mc.fontRendererObj.drawString(
                this.displayString,
                left + (maxLen - length) / 2, this.yPosition, -1
        );

        mc.fontRendererObj.drawString(
                (this.plusHovered(mouseX, mouseY) ? "\u00a7a" : "\u00a77") + "+",
                this.xPosition - this.plusWidth - this.gap - this.superPlusWidth, this.yPosition, -1
        );

        if (setting.get(Integer.class) < setting.max)
            mc.fontRendererObj.drawString(
                    (this.superPlusHovered(mouseX, mouseY) ? "\u00a7a" : "\u00a77") + "++",
                    this.xPosition - this.superPlusWidth, this.yPosition, -1
            );
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        int min = setting.min, max = setting.max, step = setting.step;
        int current = this.setting.get(Integer.class);
        int next;
        if (this.plusHovered(mouseX, mouseY)) next = current + step;
        else if (this.minusHovered(mouseX, mouseY)) next = current - step;
        else if (this.superPlusHovered(mouseX, mouseY)) next = current + step * 10;
        else if (this.superMinusHovered(mouseX, mouseY)) next = current - step * 10;
        else return false;

        if (next <= min) next = min;
        if (next >= max) next = max;

        this.setting.set(next);
        updateText();
        gui.update(setting, true);
        return true;
    }

    public void updateText() {
        this.displayString = ((this.setting.prefix == null) ? "" : this.setting.prefix) + this.setting.get(Integer.class) + ((this.setting.suffix == null) ? "" : this.setting.suffix);
    }
}
