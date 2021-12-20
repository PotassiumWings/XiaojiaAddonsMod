package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Image;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class RenderUtils {
    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
    private static final Long colorized = null;
    private static final Integer drawMode = null;

    public static void drawRect(Color color, float x, float y, float width, float height) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        doColor(color);
        float[] pos = new float[]{x, y, x + width, y + height};
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);

        worldRenderer.pos(pos[0], pos[3], 0.0).endVertex();
        worldRenderer.pos(pos[2], pos[3], 0.0).endVertex();
        worldRenderer.pos(pos[2], pos[1], 0.0).endVertex();
        worldRenderer.pos(pos[0], pos[1], 0.0).endVertex();
        tessellator.draw();

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawImage(Image image, double x, double y, double width, double height) {
        if (colorized == null)
            GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.scale(1f, 1f, 50f);
        GlStateManager.bindTexture(image.getTexture().getGlTextureId());
        GlStateManager.enableTexture2D();

        worldRenderer.begin(drawMode == null ? 7 : drawMode, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
    }

    private static void doColor(long longColor) {
        int color = (int) longColor;
        if (colorized == null) {
            float a = (color >> 24 & 0xFF) / 255.0F;
            float r = (color >> 16 & 0xFF) / 255.0F;
            float g = (color >> 8 & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            GlStateManager.color(r, g, b, a);
        }
    }

    private static void doColor(Color color) {
        if (colorized == null) {
            GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
    }

    public static void translate(float x, float y, float z) {
        GlStateManager.translate(x, y, z);
    }

    public static void translate(float x, float y) {
        GlStateManager.translate(x, y, 0);
    }

    public static void scale(float x, float y) {
        GlStateManager.scale(x, y, 1F);
    }

    public static void translate(double x, double y) {
        translate((float) x, (float) y);
    }

    public static void rotate(float angle) {
        GL11.glRotatef(angle, 0.0F, 0.0F, 1.0F);
    }

    public static void drawStringWithShadow(String text, float x, float y) {
        mc.fontRendererObj.drawString(ChatLib.addColor(text), x, y, colorized == null ? -1 : colorized.intValue(), true);
    }

    public static void start() {
        GlStateManager.pushMatrix();
    }

    public static void end() {
        GlStateManager.popMatrix();
    }

    public static int getStringWidth(String str) {
        return mc.fontRendererObj.getStringWidth(str);
    }
}
