package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FolderInput extends Button {
    public FolderSetting setting;

    public FolderInput(FolderSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        width = 300;
        height = 9;
        xPosition -= width;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        GuiUtils.drawRotatedTexture(
                new ResourceLocation(XiaojiaAddons.MODID + ":chevron.png"),
                this.xPosition + this.width - this.height,
                this.yPosition,
                this.height,
                this.height,
                this.setting.get(Boolean.class) ? 180 : 0
        );
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX <= this.xPosition + this.width && mouseY <= this.yPosition + this.height) {
            this.setting.set(!this.setting.get(Boolean.class));
            return true;
        }
        return false;
    }
}
