package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.awt.Color;
import java.io.IOException;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class BugReport extends GuiScreen {
    // wid, height
    private static final int guiWidth = 200;
    private static final int guiHeight = 12;

    private static GuiTextField reportBar = null;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int startX = getStartX();
        int startY = getStartY();
        // 全局
        drawGradientRect(startX, startY - 5, startX + guiWidth, startY + guiHeight,
                new Color(25, 25, 25, 65).getRGB(),
                new Color(25, 25, 25, 100).getRGB()
        );
        drawCenteredString(fontRendererObj, "Briefly introduce the bug:", width / 2, getStartY() - 25, -1);
        reportBar.drawTextBox();
    }

    @Override
    public void initGui() {
        reportBar = new GuiTextField(
                1, fontRendererObj,
                getStartX(), getStartY(),
                guiWidth, guiHeight
        );
        reportBar.setMaxStringLength(100);
        reportBar.setEnableBackgroundDrawing(false);
        reportBar.setText("");
        reportBar.setFocused(true);

        this.buttonList.add(new GuiButton(0, width / 2 - 100, getStartY() + 20, "Submit Report") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                if (mouseX >= this.xPosition && mouseY >= this.yPosition &&
                        mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
                    XiaojiaChat.bugReport(reportBar.getText());
                    getPlayer().closeScreen();
                    ChatLib.chat("Reporting bug...");
                    return true;
                }
                return false;
            }
        });
    }

    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        reportBar.mouseClicked(x, y, btn);
    }

    protected void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
        reportBar.textboxKeyTyped(c, i);
    }

    public void updateScreen() {
        super.updateScreen();
        reportBar.updateCursorCounter();
    }

    private int getStartX() {
        return (width - guiWidth) / 2;
    }

    private int getStartY() {
        return (height - guiHeight) / 2;
    }

}
