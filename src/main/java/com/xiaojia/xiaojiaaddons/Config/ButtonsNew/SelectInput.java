package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.SelectSetting;
import net.minecraft.client.Minecraft;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class SelectInput extends Button {
    private final int leftWidth = mc.fontRendererObj.getStringWidth("<");
    private final int rightWidth = mc.fontRendererObj.getStringWidth(">");
    private final int gap = 3;
    private final int maxLen;
    public SelectSetting setting;

    public SelectInput(ConfigGuiNew gui, SelectSetting setting, int x, int y) {
        super(gui, setting, x, y);
        this.setting = setting;
        int mx = 0;
        for (String str : setting.options) {
            mx = Math.max(mx, mc.fontRendererObj.getStringWidth(str));
        }
        maxLen = mx;
        this.width = mx + this.rightWidth + this.leftWidth + this.gap * 2;
        height = 10;
        updateText();
        xPosition += setting.width - 5;
        yPosition += 5;
    }

    private boolean rightHovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition - this.rightWidth - this.gap && mouseY >= this.yPosition && mouseX < this.xPosition && mouseY < this.yPosition + this.height;
    }

    private boolean leftHovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition - this.width && mouseY >= this.yPosition && mouseX < this.xPosition - this.width + this.leftWidth + this.gap && mouseY < this.yPosition + this.height;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY) {
        mc.fontRendererObj.drawString((leftHovered(mouseX, mouseY) ? "\u00a7a" : "\u00a77") + "<", this.xPosition - this.width, this.yPosition, -1);
        int left = this.xPosition - this.width + this.leftWidth + this.gap;
        int length = mc.fontRendererObj.getStringWidth(displayString);
        mc.fontRendererObj.drawString(this.displayString, left + (maxLen - length) / 2, this.yPosition, -1);
        mc.fontRendererObj.drawString((rightHovered(mouseX, mouseY) ? "\u00a7a" : "\u00a77") + ">", this.xPosition - this.rightWidth, this.yPosition, -1);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (rightHovered(mouseX, mouseY) || this.leftHovered(mouseX, mouseY)) {
            if (rightHovered(mouseX, mouseY))
                this.setting.set(this.setting.get(Integer.class) + 1);
            if (leftHovered(mouseX, mouseY))
                this.setting.set(this.setting.get(Integer.class) - 1);
            updateText();
            gui.update(setting, true);
            return true;
        }
        return false;
    }

    public void updateText() {
        this.displayString = this.setting.options[this.setting.get(Integer.class)];
    }
}
