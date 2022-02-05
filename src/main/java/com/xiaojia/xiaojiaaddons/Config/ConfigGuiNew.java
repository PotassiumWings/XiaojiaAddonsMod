package com.xiaojia.xiaojiaaddons.Config;

import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.Button;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.util.ArrayList;

public class ConfigGuiNew extends GuiScreen {
    // settings
    private static ArrayList<Setting> settings;
    private static ArrayList<Setting> firstCategory;
    private static ArrayList<Setting> secondCategory;
    private static Setting selectedFirstCategory = null;
    private static Setting selectedSecondCategory = null;
    // wid, height
    private static final int guiWidth = 500;
    private static final int guiHeight = 260;
    private static final int titleHeight = 30;
    private static final int firstCategoryHeight = 23;
    private static final int secondCategoryWidth = 150;
    // first line
    private final int iconHeight = 14;
    private final int gapBetween = 3;
    private final int insideGap = (iconHeight - 9) / 2 + 1;
    // second category
    private final int lineHeight = 20;
    // third part
    private final int thirdGap = 10;
    private final int thirdGapBetween = 5;
    // color
    private final Color descriptionColor = new Color(0xaa, 0xaa, 0xaa, 240);

    public ConfigGuiNew() {
        getSettings();
    }

    private static void getSettings() {
        ArrayList<Setting> first = new ArrayList<>();
        ArrayList<Setting> second = new ArrayList<>();
        for (Setting setting : XiaojiaAddons.settings) {
            setting.x = setting.y = setting.width = setting.height = 0;
            if (isFirstCategory(setting)) {
                first.add(setting);
                setting.set(false);
            } else if (isSecondCategory(setting)) {
                second.add(setting);
                if (setting instanceof FolderSetting) setting.set(false);
            }
        }
        if (selectedFirstCategory != null) selectedFirstCategory.set(true);
        if (selectedSecondCategory != null) selectedSecondCategory.set(true);

        settings = selectedSecondCategory == null ? new ArrayList<>() : selectedSecondCategory.getSons();
        firstCategory = first;
        secondCategory = second;
    }

    private static boolean isFirstCategory(Setting setting) {
        return setting.parent == null;
    }

    private static boolean isSecondCategory(Setting setting) {
        return setting.parent != null && setting.parent == selectedFirstCategory && setting.parent.parent == null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        int startX = getStartX();
        int startY = getStartY();
        // 全局
        drawGradientRect(startX, startY, startX + guiWidth, startY + guiHeight,
                new Color(25, 25, 25, 65).getRGB(),
                new Color(25, 25, 25, 100).getRGB()
        );
        // 框1
        drawGradientRect(startX, startY, startX + guiWidth, startY + titleHeight,
                new Color(15, 15, 15, 180).getRGB(),
                new Color(15, 15, 15, 170).getRGB()
        );
        drawRect(startX, startY + titleHeight, startX + guiWidth, startY + titleHeight + 1,
                Colors.TRANSPARENT.getRGB()
        );
        // 框2
        drawGradientRect(startX, startY + titleHeight + 1, startX + guiWidth, startY + titleHeight + firstCategoryHeight,
                new Color(15, 15, 15, 120).getRGB(),
                new Color(15, 15, 15, 110).getRGB()
        );
        drawRect(startX, startY + titleHeight + firstCategoryHeight, startX + guiWidth, startY + titleHeight + firstCategoryHeight + 1,
                Colors.TRANSPARENT.getRGB()
        );
        // 左框
        drawGradientRect(startX, startY + titleHeight + firstCategoryHeight + 1, startX + secondCategoryWidth, startY + guiHeight,
                new Color(15, 15, 15, 110).getRGB(),
                new Color(15, 15, 15, 100).getRGB()
        );
        drawRect(startX + secondCategoryWidth, startY + titleHeight + firstCategoryHeight + 1, startX + secondCategoryWidth + 1, startY + guiHeight,
                Colors.TRANSPARENT.getRGB()
        );

        // Xiaojia Addons
        int closeIconSize = 12;
        int gap = (titleHeight - closeIconSize) / 2;
        String titleString = "XiaojiaAddons - " + XiaojiaAddons.VERSION;
        drawCenteredString(
                mc.fontRendererObj, titleString,
                startX + mc.fontRendererObj.getStringWidth(titleString) / 2 + gap,
                startY + titleHeight / 2 - 3,
                -1
        );
        // 关闭
        GuiUtils.drawTexture(new ResourceLocation(XiaojiaAddons.MODID + ":deny-16x16.png"),
                startX + guiWidth - gap - closeIconSize, startY + gap
                , closeIconSize, closeIconSize);
        // search bar
        int searchWidth = 70;
        int searchHeight = 12;
        int secondGap = (firstCategoryHeight - searchHeight) / 2;
        drawSearchBar(
                startX + guiWidth - gap - searchWidth, startY + titleHeight + secondGap,
                startX + guiWidth - gap, startY + titleHeight + firstCategoryHeight - secondGap,
                9
        );
        // first line
        drawFirstLine();
        // second category
        drawSecondCategory();
        // other settings
        drawSettings();
        drawButtons(mc, mouseX, mouseY);
    }

    private void drawSearchBar(int sx, int sy, int tx, int ty, int iconSize) {
        int gap = (ty - sy - iconSize) / 2;
        drawGradientRect(
                sx, sy, tx, ty,
                new Color(255, 255, 255, 165).getRGB(),
                new Color(255, 255, 255, 140).getRGB()
        );
        GuiUtils.drawTexture(new ResourceLocation(XiaojiaAddons.MODID + ":search.png"),
                sx + gap, sy + gap, iconSize, iconSize);
        drawRect(
                sx + gap * 2 + iconSize, sy + gap + iconSize,
                tx - gap, sy + gap + iconSize + 1,
                new Color(255, 255, 255, 255).getRGB()
        );
    }

    private void drawFirstLine() {
        for (Setting setting : firstCategory) {
            Color color1 = new Color(0x55, 0x55, 0x55, 0xa8);
            Color color2 = new Color(0x55, 0x55, 0x55, 0xa0);
            if (setting == selectedFirstCategory) {
                color1 = new Color(0x35, 0x35, 0xff, 0x68);
                color2 = new Color(0x35, 0x35, 0xff, 0x50);
            }
            drawGradientRect(setting.x, setting.y,
                    setting.x + setting.width, setting.y + setting.height,
                    color1.getRGB(), color2.getRGB()
            );
            drawString(mc.fontRendererObj, setting.name, setting.x + insideGap, setting.y + insideGap, -1);
        }
    }

    private void drawSecondCategory() {
        for (Setting setting : secondCategory) {
            if (setting == selectedSecondCategory)
                drawGradientRect(setting.x, setting.y + 1,
                        setting.x + setting.width, setting.y + setting.height,
                        new Color(0x35, 0xFF, 0x35, 0x68).getRGB(),
                        new Color(0x35, 0xFF, 0x35, 0x50).getRGB()
                );
            drawString(mc.fontRendererObj, setting.name,
                    setting.x + (lineHeight - 9) / 2 + 1, setting.y + (lineHeight - 9) / 2 + 1,
                    -1
            );
            drawRect(setting.x, setting.y + setting.height, setting.x + setting.width, setting.y + setting.height + 1, -1);
        }
    }

    private void drawSettings() {
        if (selectedSecondCategory != null) {
            int curX = getStartX() + (secondCategoryWidth + guiWidth) / 2;
            int curY = getStartY() + titleHeight + firstCategoryHeight + 10;
            for (String str: selectedSecondCategory.description.split("\n")) {
                drawCenteredString(fontRendererObj, ChatLib.addColor(str), curX, curY, descriptionColor.getRGB());
                curY += 10;
            }
        }
        for (Setting setting : settings) {
            drawGradientRect(setting.x, setting.y,
                    setting.x + setting.width, setting.y + setting.height,
                    new Color(25, 25, 25, 95).getRGB(),
                    new Color(25, 25, 25, 85).getRGB()
            );
            String str = setting.name;
            if (setting instanceof BooleanSetting && setting.get(Boolean.class)) str = "\u00a7a" + str;

            drawString(fontRendererObj, str, setting.x + 5, setting.y + 4, new Color(255, 255, 255, 255).getRGB());
            int curX = setting.x + 4, curY = setting.y + 14 + 4;
            for (String line : setting.description.split("\n")) {
                drawString(fontRendererObj, ChatLib.addColor(line), curX, curY, descriptionColor.getRGB());
                curY += 10;
            }
        }
    }

    private void drawButtons(Minecraft mc, int mouseX, int mouseY) {
        for (GuiButton button: this.buttonList) {
            if (button instanceof Button) {
                ((Button) button).draw(mc, mouseX, mouseY);
            }
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int gap = (firstCategoryHeight - iconHeight) / 2;
        int curX = getStartX() + gap, curY = getStartY() + titleHeight + gap;
        int insideGap = (iconHeight - fontRendererObj.FONT_HEIGHT) / 2;
        for (Setting setting : firstCategory) {
            int lineWidth = mc.fontRendererObj.getStringWidth(setting.name);
            setting.x = curX;
            setting.y = curY;
            setting.width = lineWidth + 2 * insideGap;
            setting.height = iconHeight;
            this.buttonList.add(Button.buttonFromSetting(this, setting, setting.x, setting.y));
            curX += lineWidth + 2 * insideGap + gapBetween;
        }

        curX = getStartX();
        curY = getStartY() + titleHeight + firstCategoryHeight;
        for (Setting setting : secondCategory) {
            setting.x = curX;
            setting.y = curY;
            setting.width = secondCategoryWidth;
            setting.height = lineHeight;
            this.buttonList.add(Button.buttonFromSetting(this, setting, setting.x, setting.y));
            curY += lineHeight;
        }

        curX = getStartX() + secondCategoryWidth + thirdGap;
        curY = getStartY() + titleHeight + firstCategoryHeight + thirdGap;
        if (selectedSecondCategory != null && !selectedSecondCategory.description.equals("")) {
            curY += getLines(selectedSecondCategory.description) * 10 + 5;
        }
        for (Setting setting : settings) {
            setting.x = curX;
            setting.y = curY;
            setting.width = guiWidth - secondCategoryWidth - 2 * thirdGap;
            int line = getLines(setting.description);
            setting.height = 18 + 10 * line;
            this.buttonList.add(Button.buttonFromSetting(this, setting, setting.x, setting.y));
            curY += setting.height + thirdGapBetween;
        }
    }

    public void update(Setting setting, boolean val) {
        ChatLib.chat("clicked " + setting.name);
        if (isFirstCategory(setting)) {
            if (val) {
                selectedFirstCategory = setting;
                selectedSecondCategory = null;
            }
            else selectedFirstCategory = selectedSecondCategory = null;
        } else if (isSecondCategory(setting)) {
            if (val) selectedSecondCategory = setting;
            else selectedSecondCategory = null;
        }
        getSettings();
        initGui();
    }

    private int getStartX() {
        return (width - guiWidth) / 2;
    }

    private int getStartY() {
        return (height - guiHeight) / 2;
    }

    private int getLines(String s) {
        if (s == null || s.equals("")) return 0;
        int cnt = 1;
        for (char c : s.toCharArray()) {
            if (c == '\n') cnt++;
        }
        return cnt;
    }
}
