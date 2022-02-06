package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.TextSetting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class TextInput {
    public TextSetting setting;
    public GuiTextField textField;
    public int x;
    public int y;
    public int width;
    public int height;
    public GuiButton button;
    public ConfigGuiNew gui;

    public TextInput(ConfigGuiNew gui, TextSetting setting, int x, int y) {
        this.setting = setting;
        this.x = x + setting.width - 5 - 150;
        this.y = y + 5;
        this.width = 150;
        this.height = 10;
        this.gui = gui;

        textField = new GuiTextField(
                0, XiaojiaAddons.mc.fontRendererObj,
                this.x, this.y,
                this.width, this.height
        );
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(255);
        textField.setFocused(false);
        textField.setText(setting.get(String.class));
    }

    public void mouseClicked(int x, int y, int btn) {
        boolean isFocused = textField.isFocused();
        textField.mouseClicked(x, y, btn);
        if (isFocused && !textField.isFocused()) {
            gui.update(setting, true);
        }
    }

    public boolean isFocused() {
        return textField.isFocused();
    }

    public void setFocused(boolean n) {
        textField.setFocused(n);
    }

    public void keyTyped(char c, int i) {
        textField.textboxKeyTyped(c, i);
        setting.set(textField.getText());
    }

    public void draw() {
        textField.drawTextBox();
        Gui.drawRect(this.x, this.y + 9, this.x + this.width, this.y + 10, -1);
    }

    public void clearText() {
        textField.setText("");
    }
}
