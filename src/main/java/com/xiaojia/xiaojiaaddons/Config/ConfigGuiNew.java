package com.xiaojia.xiaojiaaddons.Config;

import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.Button;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.ClearTextButton;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.CloseButton;
import com.xiaojia.xiaojiaaddons.Config.ButtonsNew.TextInput;
import com.xiaojia.xiaojiaaddons.Config.Setting.BooleanSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.Config.Setting.TextSetting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ConfigGuiNew extends GuiScreen {
    // wid, height
    private static final int guiWidth = 500;
    private static final int guiHeight = 260;
    private static final int titleHeight = 30;
    private static final int firstCategoryHeight = 23;
    private static final int secondCategoryWidth = 150;
    // settings
    private static ArrayList<Setting> settings;
    private static ArrayList<Setting> firstCategory;
    private static ArrayList<Setting> secondCategory;
    private static Setting selectedFirstCategory = null;
    private static Setting selectedSecondCategory = null;
    // search bar
    private static GuiTextField searchBar = null;
    // first line
    private final int iconHeight = 14;
    private final int gapBetween = 3;
    private final int insideGap = (iconHeight - 9) / 2 + 1;
    // search bar
    private final int searchWidth = 120;
    private final int searchHeight = 14;
    private final int secondGap = (firstCategoryHeight - searchHeight) / 2;
    // second category
    private final int lineHeight = 20;
    // third part
    private final int thirdGap = 10;
    private final int thirdGapBetween = 5;
    // color
    private final Color descriptionColor = new Color(0xaa, 0xaa, 0xaa, 240);
    // text inputs
    private final ArrayList<TextInput> textInputs = new ArrayList<>();
    // close icon
    private final int closeIconSize = 12;
    // GUI scale
    private final int startScale;
    // scroll
    private int secondScroll = 0;
    private int thirdScroll = 0;
    private int maxSecondScroll = 0;
    private int maxThirdScroll = 0;
    private int lastWidth = width;
    private int lastHeight = height;

    public ConfigGuiNew(int startScale) {
        getSettings();
        this.startScale = startScale;
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

        if (searchBar != null && !searchBar.getText().equals("")) {
            String search = searchBar.getText().toLowerCase();
            settings = new ArrayList<>();
            HashSet<String> selectedSettings = new HashSet<>();
            for (Setting setting : XiaojiaAddons.settings) {
                if (isFirstCategory(setting)) continue;
                if (setting.name.toLowerCase().contains(search) ||
                        setting.description.toLowerCase().contains(search) ||
                        selectedSettings.contains(setting.parent.name)) {
                    settings.add(setting);
                    selectedSettings.add(setting.name);
                }
            }
        }

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
        for (TextInput textInput : textInputs) {
            textInput.draw();
        }
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
        searchBar.drawTextBox();
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
            if (setting == selectedSecondCategory) {
                if (isInSecondCategory(setting.y + 1) || isInSecondCategory(setting.y + setting.height))
                    drawGradientRect(setting.x, Math.max(setting.y + 1, getStartY() + titleHeight + firstCategoryHeight),
                            setting.x + setting.width, Math.min(setting.y + setting.height, getStartY() + guiHeight),
                            new Color(0x35, 0xFF, 0x35, 0x68).getRGB(),
                            new Color(0x35, 0xFF, 0x35, 0x50).getRGB()
                    );
            }
            if (isInSecondCategory(setting.y + (lineHeight - 9) / 2 + 1, setting.y + (lineHeight - 9) / 2 + 1 + 9))
                drawString(mc.fontRendererObj, setting.name,
                        setting.x + (lineHeight - 9) / 2 + 1, setting.y + (lineHeight - 9) / 2 + 1,
                        -1
                );
            if (isInSecondCategory(setting.y + setting.height, setting.y + setting.height + 1))
                drawRect(setting.x, setting.y + setting.height, setting.x + setting.width, setting.y + setting.height + 1, -1);
        }
    }

    private boolean isInSecondCategory(int y1, int y2) {
        int sy = getStartY() + titleHeight + firstCategoryHeight;
        int ty = getStartY() + guiHeight;
        return y1 >= sy && y2 >= y1 && ty >= y2;
    }

    private boolean isInSecondCategory(int y1) {
        int sy = getStartY() + titleHeight + firstCategoryHeight;
        int ty = getStartY() + guiHeight;
        return y1 >= sy && ty >= y1;
    }

    private void drawSettings() {
        if (selectedSecondCategory != null) {
            int curX = getStartX() + (secondCategoryWidth + guiWidth) / 2;
            int curY = getStartY() + titleHeight + firstCategoryHeight + thirdGap + thirdScroll;
            for (String str : selectedSecondCategory.description.split("\n")) {
                if (isInThirdCategory(curY, curY + 9))
                    drawCenteredString(fontRendererObj, ChatLib.addColor(str), curX, curY, descriptionColor.getRGB());
                curY += 10;
            }
        }
        for (Setting setting : settings) {
            if (!(setting instanceof FolderSetting) && (isInThirdCategory(setting.y) || isInThirdCategory(setting.y + setting.height)))
                drawGradientRect(setting.x, Math.max(setting.y, getStartY() + titleHeight + firstCategoryHeight + 5),
                        setting.x + setting.width, Math.min(setting.y + setting.height, getStartY() + guiHeight),
                        new Color(25, 25, 25, 95).getRGB(),
                        new Color(25, 25, 25, 85).getRGB()
                );
            String str = setting.name;
            if (setting instanceof BooleanSetting && setting.get(Boolean.class)) str = "\u00a7a" + str;

            if (isInThirdCategory(setting.y + 4, setting.y + 4 + 10)) {
                if (setting instanceof FolderSetting)
                    drawCenteredString(fontRendererObj, str, getStartX() + (guiWidth + secondCategoryWidth) / 2, setting.y + 4, new Color(255, 255, 255, 255).getRGB());
                else
                    drawString(fontRendererObj, str, setting.x + 5, setting.y + 4, new Color(255, 255, 255, 255).getRGB());
            }
            int curX = setting.x + 4, curY = setting.y + 14 + 4;
            for (String line : setting.description.split("\n")) {
                if (isInThirdCategory(curY, curY + 10)) {
                    if (setting instanceof FolderSetting)
                        drawCenteredString(fontRendererObj, ChatLib.addColor(line), getStartX() + (guiWidth + secondCategoryWidth) / 2, curY, descriptionColor.getRGB());
                    else
                        drawString(fontRendererObj, ChatLib.addColor(line), curX, curY, descriptionColor.getRGB());
                }
                curY += 10;
            }
        }
    }

    private boolean isInThirdCategory(int y1, int y2) {
        int sy = getStartY() + titleHeight + firstCategoryHeight + 5;
        int ty = getStartY() + guiHeight;
        return y1 >= sy && y2 >= y1 && ty >= y2;
    }

    private boolean isInThirdCategory(int y1) {
        int sy = getStartY() + titleHeight + firstCategoryHeight + 5;
        int ty = getStartY() + guiHeight;
        return y1 >= sy && ty >= y1;
    }

    private void drawButtons(Minecraft mc, int mouseX, int mouseY) {
        for (GuiButton button : this.buttonList) {
            if (button instanceof Button) {
                ((Button) button).draw(mc, mouseX, mouseY);
            }
        }
    }

    @Override
    public void initGui() {
        this.textInputs.clear();
        this.buttonList.clear();

        // close icon
        int gap = (titleHeight - closeIconSize) / 2;
        this.buttonList.add(
                new CloseButton(
                        this, 0, getStartX() + guiWidth - gap - closeIconSize, getStartY() + gap,
                        closeIconSize, closeIconSize, ""
                )
        );

        // first category
        gap = (firstCategoryHeight - iconHeight) / 2;
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

        // second category
        int sy = getStartY() + titleHeight + firstCategoryHeight;
        int ty = getStartY() + guiHeight;
        curX = getStartX();
        curY = getStartY() + titleHeight + firstCategoryHeight + secondScroll;
        maxSecondScroll = 1 + 1;
        for (Setting setting : secondCategory) {
            setting.x = curX;
            setting.y = curY;
            setting.width = secondCategoryWidth;
            setting.height = lineHeight;
            if (curY >= sy && curY + lineHeight < ty)
                this.buttonList.add(Button.buttonFromSetting(this, setting, setting.x, setting.y));
            curY += lineHeight;
            maxSecondScroll += lineHeight;
        }

        // settings
        curX = getStartX() + secondCategoryWidth + thirdGap;
        curY = getStartY() + titleHeight + firstCategoryHeight + thirdGap + thirdScroll;
        maxThirdScroll = thirdGap;
        if (selectedSecondCategory != null && !selectedSecondCategory.description.equals("")) {
            int descriptionY = getLines(selectedSecondCategory.description) * 10 + 5;
            curY += descriptionY;
            maxThirdScroll += descriptionY;
        }
        for (Setting setting : settings) {
            setting.x = curX;
            setting.y = curY;
            setting.width = guiWidth - secondCategoryWidth - 2 * thirdGap;
            int line = getLines(setting.description);
            setting.height = 18 + 10 * line;
            if (curY >= sy && curY + 14 < ty) {
                if (setting instanceof TextSetting) {
                    TextInput textInput = new TextInput(this, (TextSetting) setting, setting.x, setting.y);
                    this.textInputs.add(textInput);
                    this.buttonList.add(new ClearTextButton(this, (TextSetting) setting, setting.x, setting.y));
                } else
                    this.buttonList.add(Button.buttonFromSetting(this, setting, setting.x, setting.y));
            }
            curY += setting.height + thirdGapBetween;
            maxThirdScroll += setting.height + thirdGapBetween;
        }
        if (searchBar == null || width != lastWidth || height != lastHeight) {
            searchBar = new GuiTextField(
                    1, fontRendererObj,
                    getStartX() + guiWidth - (titleHeight - 12) / 2 - searchWidth + searchHeight + 1,
                    getStartY() + titleHeight + secondGap + 2,
                    searchWidth - searchHeight, searchHeight - 1
            );
            searchBar.setMaxStringLength(10);
            searchBar.setEnableBackgroundDrawing(false);
            searchBar.setText("");
            searchBar.setFocused(true);
            lastWidth = width;
            lastHeight = height;
        }
    }

    protected void mouseClicked(int x, int y, int btn) throws IOException {
        super.mouseClicked(x, y, btn);
        searchBar.mouseClicked(x, y, btn);
        for (TextInput textInput : textInputs) {
            textInput.mouseClicked(x, y, btn);
        }
    }

    protected void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
        if (searchBar.isFocused()) {
            selectedFirstCategory = selectedSecondCategory = null;
            thirdScroll = secondScroll = 0;
            searchBar.textboxKeyTyped(c, i);
            getSettings();
            initGui();
        }
        for (TextInput textInput : textInputs) {
            if (textInput.isFocused()) {
                textInput.keyTyped(c, i);
            }
        }
    }

    public void onGuiClosed() {
        XiaojiaAddons.mc.gameSettings.guiScale = startScale;
        Config.save();
    }

    public void updateScreen() {
        super.updateScreen();
        searchBar.updateCursorCounter();
    }

    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        if (Mouse.getEventDWheel() != 0) {
            int delta = Integer.signum(Mouse.getEventDWheel()) * 10;
            if (mouseY >= getStartY() + titleHeight + firstCategoryHeight && mouseY <= getStartY() + guiHeight) {
                if (mouseX >= getStartX() && mouseX <= getStartX() + secondCategoryWidth) {
                    // in second part
                    secondScroll = MathHelper.clamp_int(secondScroll + delta,
                            Math.min(0, guiHeight - firstCategoryHeight - titleHeight - maxSecondScroll), 0);
                } else if (mouseX >= getStartX() + secondCategoryWidth && mouseX <= getStartX() + guiWidth) {
                    // in third part
                    thirdScroll = MathHelper.clamp_int(thirdScroll + delta,
                            Math.min(0, guiHeight - firstCategoryHeight - titleHeight - maxThirdScroll), 0);
                }
            }
            initGui();
        }
        super.handleMouseInput();
    }

    public void update(Setting setting, boolean val) {
        if (isFirstCategory(setting)) {
            secondScroll = thirdScroll = 0;
            searchBar.setText("");
            if (val) {
                selectedFirstCategory = setting;
                selectedSecondCategory = null;
            } else selectedFirstCategory = selectedSecondCategory = null;
        } else if (isSecondCategory(setting)) {
            thirdScroll = 0;
            searchBar.setText("");
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
