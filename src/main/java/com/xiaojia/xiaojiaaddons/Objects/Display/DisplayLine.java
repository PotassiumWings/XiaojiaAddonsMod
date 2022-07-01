package com.xiaojia.xiaojiaaddons.Objects.Display;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import java.awt.Color;

public class DisplayLine {
    private String text;
    private float scale = 1F;
    private boolean shadow = false;

    public DisplayLine(String s) {
        text = ChatLib.addColor(s);
    }

    public void setText(String text) {
        this.text = text;
    }

    public DisplayLine setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public int getTextWidth() {
        return MathUtils.ceil(scale * RenderUtils.getStringWidth(text));
    }

    public int getHeight() {
        return MathUtils.ceil(scale * 9);
    }

    public void draw(int x, int y, int totalWidth, Background background,
                     int backgroundColor, int textColor, Align align) {
        int baseX = 0;
        int xOffset = 0;
        if (align == Align.CENTER) {
            baseX = x - totalWidth / 2;
            xOffset = baseX + (totalWidth - getTextWidth()) / 2;
        } else if (align == Align.LEFT) {
            baseX = x;
            xOffset = baseX;
        } else if (align == Align.RIGHT) {
            baseX = x - totalWidth;
            xOffset = baseX + (totalWidth - getTextWidth());
        }

        RenderUtils.start();
        if (background == Background.FULL)
            RenderUtils.drawRect(backgroundColor, baseX - 1, y - 1, totalWidth + 1, getHeight());
        else if (background == Background.PER_LINE && !text.equals(""))
            RenderUtils.drawRect(backgroundColor, xOffset - 1, y - 1, getTextWidth() + 1, getHeight());

        RenderUtils.drawString(text, scale, xOffset, y, new Color(textColor), shadow);
        RenderUtils.end();
    }
}
