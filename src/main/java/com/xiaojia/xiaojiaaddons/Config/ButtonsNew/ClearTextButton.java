package com.xiaojia.xiaojiaaddons.Config.ButtonsNew;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Config.Setting.TextSetting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ClearTextButton extends Button {
    public int sx;
    public int sy;

    public ClearTextButton(ConfigGuiNew gui, TextSetting setting, int x, int y) {
        super(gui, setting, x, y);
        sx = x + setting.width - 5 - TextInput.width - 15;
        sy = y + 4;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY) {
        GuiUtils.drawTexture(new ResourceLocation(XiaojiaAddons.MODID + ":trashbin.png"),
                sx, sy, 10, 10);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (hover(mouseX, mouseY)) {
            setting.set("");
            gui.update(setting, true);
            return true;
        }
        return false;
    }

    public boolean hover(int mouseX, int mouseY) {
        return mouseX >= sx && mouseX <= sx + 10 && mouseY >= sy && mouseY <= sy + 10;
    }
}
