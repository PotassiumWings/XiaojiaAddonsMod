package com.xiaojia.xiaojiaaddons.Config.Buttons;

import com.xiaojia.xiaojiaaddons.Config.Colors;
import com.xiaojia.xiaojiaaddons.Config.Setting.FolderSetting;
import net.minecraft.client.Minecraft;

public class FolderInput extends Button {
    public FolderSetting setting;

    public FolderInput(FolderSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        width = 25;
        height = 9;
        xPosition -= width;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        drawRect(xPosition, yPosition + 3, xPosition + width, yPosition + 6, Colors.WHITE.getRGB());
        if (setting.get(Boolean.class))
            drawRect(xPosition + width - 9, yPosition, xPosition + width, yPosition + height, Colors.GREEN.getRGB());
        else drawRect(xPosition, yPosition, xPosition + 9, yPosition + height, Colors.RED.getRGB());

//        GuiUtils.drawRotatedTexture(
//                new ResourceLocation("xj:chevron.png"),
//                this.xPosition + this.width - this.height,
//                this.yPosition,
//                this.height,
//                this.height,
//                this.setting.get(Boolean.class) ? 180 : 0
//        );
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX <= this.xPosition + this.width && mouseY <= this.yPosition + this.height) {
            this.setting.set(!this.setting.get(Boolean.class));
//            System.out.println(this.setting.get(Boolean.class));
//            System.out.println(setting.name + "-----------------------------");
            return true;
        }
        return false;
    }
}
