package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
public class Renderer {
    private static Long colorized;
    private static Integer drawMode;
    private static boolean retainTransforms;

    public static final void retainTransforms(boolean retain) {
        retainTransforms = retain;
        finishDraw();
    }

    public static final void finishDraw() {
        if (!retainTransforms) {
            colorized = null;
            drawMode = null;
            GL11.glPopMatrix();
            GL11.glPushMatrix();
        }
    }
    public static final void translate(float x, float y, float z) {
        GL11.glTranslated(x, y, z);
    }
    public static final void translate(float x, float y) {
        GL11.glTranslated(x, y, 0);
    }

    public static final void scale(float scaleX, float scaleY) {
        GL11.glScalef(scaleX, scaleY, 1.0F);
    }
    public static final FontRenderer getFontRenderer() {
        return mc.fontRendererObj;
    }

    public static final void drawStringWithShadow(String text, float x, float y) {
        Long long_ = colorized;
        int i = (int)long_.longValue();
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, (long_ == null) ? -1 : i, true);
        finishDraw();
    }

    public static int getStringWidth(String s) {
        return getFontRenderer().getStringWidth(ChatLib.addColor(s));
    }
}
