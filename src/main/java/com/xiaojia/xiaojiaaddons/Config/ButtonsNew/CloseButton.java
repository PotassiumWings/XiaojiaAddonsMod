package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class CloseButton extends GuiButton {
    ConfigGuiNew gui;

    public CloseButton(ConfigGuiNew gui, int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.gui = gui;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            getPlayer().closeScreen();
            return true;
        }
        return false;
    }
}
