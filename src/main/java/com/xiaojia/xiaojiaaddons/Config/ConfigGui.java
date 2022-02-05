package com.xiaojia.xiaojiaaddons.Config;

import com.xiaojia.xiaojiaaddons.Config.Buttons.Button;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class ConfigGui extends GuiScreen {
    private static final int beginGap = 100;
    private static final int titleSettingGap = 10;
    private static final int blankGap = 200;

    private static final int columnWidth = 300;
    private static int scrollOffset = 0;
    private static ArrayList<Setting> settings;
    private final boolean isScrolling = false;
    private int prevHeight = -1, prevWidth = -1;

    public ConfigGui() {
        settings = getSettings();
    }

    private static ArrayList<Setting> getSettings() {
        ArrayList<Setting> res = new ArrayList<>();
        for (Setting setting : XiaojiaAddons.settings) {
            if (setting.parent == null) {
                res.add(setting);
            } else if (setting.parent.get(Boolean.class)) {
                res.add(setting);
            }
        }
        return res;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        drawCenteredString(
                mc.fontRendererObj,
                "XiaojiaAddons - " + XiaojiaAddons.VERSION,
                width / 2,
                beginGap - scrollOffset,
                -1
        );
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            int x = getOffset() + setting.getIndent(0);
            int y = beginGap + titleSettingGap + i * 15 - scrollOffset;
            if (setting.parent == null && i > 0)
                drawRect(x, y - 3, getOffset() + columnWidth, y - 2, Colors.TRANSPARENT.getRGB());
            if (setting.illegal) {
                GuiUtils.drawTexture(new ResourceLocation(XiaojiaAddons.MODID + ":warning.png"), x, y, 9, 9);
                x += 13;
            }

            char color = 'f';
            if (setting instanceof BooleanSetting && setting.get(Boolean.class))
                color = 'a';
            mc.fontRendererObj.drawString("\u00a7" + color + setting.name, x, y + 1, -1);

            int settingNameWidth = mc.fontRendererObj.getStringWidth(setting.name + " ");
            GlStateManager.translate(0.0D, 1.8D, 0.0D);
            GuiUtils.drawScaledString("\u00a77" + setting.description, 0.8F,
                    x + settingNameWidth, y + 1, false);
            GlStateManager.translate(0.0D, -1.8D, 0.0D);
        }
        if ((this.prevWidth != this.width || this.prevHeight != this.height)){
            mc.displayGuiScreen(new ConfigGui());
        }
        this.prevWidth = this.width;
        this.prevHeight = this.height;
    }

    @Override
    public void onGuiClosed() {
        Config.save();
    }

    public void update() {
        settings.clear();
        settings = getSettings();
        initGui();
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (Mouse.getEventDWheel() != 0) {
            scrollScreen(Integer.signum(Mouse.getEventDWheel()) * -10, true);
        }
        super.handleMouseInput();
    }

    private void scrollScreen(int scroll, boolean pixels) {
        int viewport = height - 110;
        int contentHeight = settings.size() * 15;
        if (!pixels)
            scroll = scroll / viewport * contentHeight;
        if (contentHeight > viewport - 2 * blankGap) {
//            System.out.printf("scrollOffset: %d, scroll: %d, contentHeight: %d, viewport: %d", scrollOffset, scroll, contentHeight, viewport);
            scrollOffset = MathHelper.clamp_int(scrollOffset + scroll, -blankGap, contentHeight - viewport + blankGap);
            initGui();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            int x = getOffset() + columnWidth;
            int y = beginGap + titleSettingGap + i * 15 - scrollOffset;
            this.buttonList.add(Button.buttonFromSetting(this, setting, x, y));
        }
    }

    private int getOffset() {
        return (width - columnWidth) / 2;
    }
}
