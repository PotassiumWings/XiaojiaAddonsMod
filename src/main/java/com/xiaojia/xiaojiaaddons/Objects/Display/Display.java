package com.xiaojia.xiaojiaaddons.Objects.Display;

import com.xiaojia.xiaojiaaddons.utils.GuiUtils;

import java.util.ArrayList;

public class Display {
    private ArrayList<DisplayLine> lines = new ArrayList<>();
    private int renderX = 0;
    private int renderY = 0;
    private boolean shouldRender = true;
    private Order order = Order.DOWN;
    private Align align = Align.LEFT;
    private Background background = Background.NONE;

    private int backgroundColor = 0x50000000;
    private int textColor = 0xffffffff;

    public Display() {
        DisplayHandler.registerDisplay(this);
    }

    public void setShouldRender(boolean should) {
        shouldRender = should;
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    public void setBackground(String back) {
        background = Background.valueOf(back.toUpperCase());
    }

    public void setAlign(String a) {
        align = Align.valueOf(a.toUpperCase());
    }

    public void setOrder(String o) {
        order = Order.valueOf(o.toUpperCase());
    }

    public void setRenderLoc(int x, int y) {
        renderX = x;
        renderY = y;
    }

    public void setLine(int index, String line) {
        while(lines.size() <= index) lines.add(new DisplayLine(""));
        lines.set(index, new DisplayLine(line));
    }

    public void setLine(int index, DisplayLine line) {
        while(lines.size( ) <= index)lines.add(new DisplayLine(""));
        lines.set(index, line);
    }

    public void clearLines() {
        lines.clear();
    }

    public void addLine(String line) {
        setLine(lines.size(), line);
    }

    public void render() {
        if (!shouldRender || lines.isEmpty()) return;
        int i = 0;
        int width = 0;
        for (DisplayLine line: lines) width = Math.max(width, line.getTextWidth());
        for (DisplayLine line: lines) {
            line.draw(renderX, renderY + i, width, background, backgroundColor, textColor, align);
            if (order == Order.UP) i -= line.getHeight();
            else i += line.getHeight();
        }
//        if (background == Background.FULL)
//            GuiUtils.drawRect(backgroundColor, renderX - 1, renderY + i - 1, width + 1, 1);
//        else if (background == Background.PER_LINE)
//            GuiUtils.drawRect(backgroundColor, renderX - 1, renderY + i - 1, lines.get(0).getTextWidth() + 1, 1);
    }
}

enum Background {
    NONE, FULL, PER_LINE
}

enum Align {
    LEFT, CENTER, RIGHT
}

enum Order {
    UP, DOWN
}