package com.xiaojia.xiaojiaaddons.Objects;

import net.minecraft.entity.Entity;

import java.util.HashMap;

public class EntityInfo {
    private final Entity entity;
    private final int r;
    private final int g;
    private final int b;
    private final float width;
    private final float height;
    private final float yOffset;
    private final EnumDraw drawString;
    private final String kind;
    private final int fontColor;
    private final float scale;
    private final boolean isFilled;
    private final boolean isESP;

    public EntityInfo(HashMap<String, Object> info) {
        this.entity = (Entity) info.get("entity");
        this.r = (int) info.getOrDefault("r", 0);
        this.g = (int) info.getOrDefault("g", 255);
        this.b = (int) info.getOrDefault("b", 0);
        this.width = (float) info.getOrDefault("width", 0.5F);
        this.height = (float) info.getOrDefault("height", 1F);
        this.yOffset = (float) info.getOrDefault("yOffset", 0);
        this.drawString = (EnumDraw) info.getOrDefault("drawString", EnumDraw.DONT_DRAW_STRING);
        this.kind = (String) info.get("kind");
        this.fontColor = (int) info.get("fontColor");
        this.scale = (float) info.getOrDefault("scale", 1F);
        this.isFilled = (boolean) info.getOrDefault("isFilled", false);
        this.isESP = (boolean) info.getOrDefault("isESP", false);
    }

    public Entity getEntity() {
        return entity;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getyOffset() {
        return yOffset;
    }

    public EnumDraw getDrawString() {
        return drawString;
    }

    public String getKind() {
        return kind;
    }

    public int getFontColor() {
        return fontColor;
    }

    public float getScale() {
        return scale;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isESP() {
        return isESP;
    }
}
