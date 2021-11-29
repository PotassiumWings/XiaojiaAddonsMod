package com.xiaojia.xiaojiaaddons.Config;

import com.xiaojia.xiaojiaaddons.Config.Buttons.Button;
import com.xiaojia.xiaojiaaddons.Config.Buttons.ScrollBar;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class ConfigGui extends GuiScreen {
    private static final int columnWidth = 300;
    private static int scrollOffset = 0;
    private static ArrayList<Setting> settings;
    private final boolean isScrolling = false;
    private ScrollBar scrollBar;
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
        System.out.println("Draw Screen -------------------------------------------------------");
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        drawCenteredString(
                mc.fontRendererObj,
                "XiaojiaAddons - " + XiaojiaAddons.VERSION,
                width / 2,
                50 - scrollOffset,
                -1
        );
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = settings.get(i);
            int x = getOffset() + setting.getIndent(0);
            int y = columnWidth / 3 + i * 17 - scrollOffset;
            if (setting.parent == null && i > 0)
                drawRect(x, y - 3, getOffset() + columnWidth, y - 2, Colors.TRANSPARENT.getRGB());
            if (setting.illegal) {
                // TODO: illegal png
                x += 13;
            }

            char color = 'f';
            if (setting instanceof BooleanSetting && setting.get(Boolean.class))
                color = 'a';
            if (setting instanceof FolderSetting && ((FolderSetting) setting).isSonEnabled())
                color = 'a';
            mc.fontRendererObj.drawString("\u00a7" + color + setting.name, x, y + 1, -1);
//            if (setting.note != null) {
//                int settingNameWidth = Shady.mc.field_71466_p.func_78256_a(setting.name + " ");
//                GlStateManager.func_179137_b(0.0D, 1.8D, 0.0D);
//                FontUtils.drawScaledString("+ setting.note, 0.8F, x + settingNameWidth, y + 1, false);
//                        GlStateManager.func_179137_b(0.0D, -1.8D, 0.0D);
//            }
        }
        if ((this.prevWidth != this.width || this.prevHeight != this.height))
            mc.displayGuiScreen(new ConfigGui());
        this.prevWidth = this.width;
        this.prevHeight = this.height;
    }

    @Override
    public void onGuiClosed() {
        Config.save();
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
        int contentHeight = settings.size() * 17;
        if (!pixels)
            scroll = scroll / viewport * contentHeight;
        if (contentHeight > viewport) {
            scrollOffset = MathHelper.clamp_int(scrollOffset + scroll, 0, contentHeight - viewport);
            initGui();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        for (int i = 0; i < settings.size(); i++) {
            Setting setting = getSettings().get(i);
            int x = getOffset() + columnWidth;
            int y = columnWidth / 3 + i * 15 - scrollOffset;
            this.buttonList.add(Button.buttonFromSetting(setting, x, y));
        }
//        int viewport = height - 110;
//        int contentHeight = settings.size() * 17;
//        int scrollbarX = getOffset() + columnWidth + 10;
//        scrollBar = new ScrollBar(viewport, contentHeight, scrollOffset, scrollbarX, isScrolling);
//        this.buttonList.add(this.scrollBar);
    }

    private int getOffset() {
        return (width - columnWidth) / 2;
    }
}
