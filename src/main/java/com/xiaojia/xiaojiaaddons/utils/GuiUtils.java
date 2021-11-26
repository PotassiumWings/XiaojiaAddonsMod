package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;

public class GuiUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

    public static void showTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        gui.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut);
        gui.displayTitle(null, ChatLib.addColor(subtitle), fadeIn, time, fadeOut);
        gui.displayTitle(null, null, fadeIn, time, fadeOut);
    }

    public static void drawString(String text, float x, float y, float z, int color, boolean renderBlackBox, float scale, boolean increase) {
        float lScale = scale;
        RenderManager renderManager = mc.getRenderManager();
        FontRenderer fontRenderer = mc.fontRendererObj;
        Vector3f renderPos = getRenderPos(x, y, z);
        if (increase) {
            float f1 = renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z;
            boolean bool = false;
            float distance = (float) Math.sqrt(f1);
            float multiplier = distance / 120.0F;
            lScale *= 0.45F * multiplier;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glPushMatrix();
        GL11.glTranslatef(renderPos.x, renderPos.y, renderPos.z);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-lScale, -lScale, lScale);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        int textWidth = fontRenderer.getStringWidth(text);
        if (renderBlackBox) {
            int j = textWidth / 2;
            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos((-j - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((-j - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((j + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((j + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        fontRenderer.drawString(text, -textWidth / 2, 0, color);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }

    public static void drawBoxAtEntity(Entity entity, int r, int g, int b, int a, float width, float height, float yOffset) {
        float x = getX(entity), y = getY(entity) - yOffset * height, z = getZ(entity);
        drawBoxAt(x, y, z, r, g, b, a, width, height);
    }

    public static void drawFilledBoxAtEntity(Entity entity, int r, int g, int b, int a, float width, float height, float yOffset) {
        float x = getX(entity), y = getY(entity) - yOffset * height, z = getZ(entity);
        drawFilledBoundingBoxAbsolute(x - width, y, z - width, x + width, y + height, z + width, r, g, b, a);
    }

    public static void drawBoxAtBlock(int x, int y, int z, int r, int g, int b, int a, int width, int height) {
        float delta = 0.01F;
        x -= delta;
        y -= delta;
        z -= delta;
        width += 2 * delta;
        height += 2 * delta;
        drawFilledBoundingBoxAbsolute(x, y, z, x + width, y + height, z + width, r, g, b, a);
    }

    private static void drawFilledBoundingBoxAbsolute(float sx, float sy, float sz, float tx, float ty, float tz, int r, int g, int b, int a) {
        EntityPlayerSP player = mc.thePlayer;
        float px = getX(player), py = getY(player), pz = getZ(player);
        sx -= px;
        sy -= py;
        sz -= pz;
        tx -= px;
        ty -= py;
        tz -= pz;
        drawFilledBoundingBoxRelative(sx, sy, sz, tx, ty, tz, r, g, b, a);
    }

    private static void drawFilledBoundingBoxRelative(float sx, float sy, float sz, float tx, float ty, float tz, int r, int g, int b, int a) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        GlStateManager.color(r / 255.F, g / 255.F, b / 255.F, a / 255.F);

        //vertical
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(sx, sy, sz).endVertex();
        worldRenderer.pos(tx, sy, sz).endVertex();
        worldRenderer.pos(tx, sy, tz).endVertex();
        worldRenderer.pos(sx, sy, tz).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(sx, ty, tz).endVertex();
        worldRenderer.pos(tx, ty, tz).endVertex();
        worldRenderer.pos(tx, ty, sz).endVertex();
        worldRenderer.pos(sx, ty, sz).endVertex();
        tessellator.draw();

        GlStateManager.color(r / 255.F * 0.8F, g / 255.F * 0.8F, b / 255.F * 0.8F, a / 255.F);

        //x
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(sx, sy, tz).endVertex();
        worldRenderer.pos(sx, ty, tz).endVertex();
        worldRenderer.pos(sx, ty, sz).endVertex();
        worldRenderer.pos(sx, sy, sz).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(tx, sy, sz).endVertex();
        worldRenderer.pos(tx, ty, sz).endVertex();
        worldRenderer.pos(tx, ty, tz).endVertex();
        worldRenderer.pos(tx, sy, tz).endVertex();
        tessellator.draw();

        GlStateManager.color(r / 255.F * 0.9F, g / 255.F * 0.9F, b / 255.F * 0.9F, a / 255.F);

        //z
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(sx, ty, sz).endVertex();
        worldRenderer.pos(tx, ty, sz).endVertex();
        worldRenderer.pos(tx, sy, sz).endVertex();
        worldRenderer.pos(sx, sy, sz).endVertex();
        tessellator.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(sx, sy, tz).endVertex();
        worldRenderer.pos(tx, sy, tz).endVertex();
        worldRenderer.pos(tx, ty, tz).endVertex();
        worldRenderer.pos(sx, ty, tz).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static final Vector3f getRenderPos(float x, float y, float z) {
        return new Vector3f(x - MathUtils.getX(mc.thePlayer), y - MathUtils.getY(mc.thePlayer), z - MathUtils.getZ(mc.thePlayer));
    }

    private static void drawBoxAt(float x, float y, float z, int r, int g, int b, int a, float width, float height) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderManager renderManager = mc.getRenderManager();
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.color(r, g, b, a);

        worldRenderer.pos(x + width, y + height, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y + height, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y + height, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y + height, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y + height, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y + height, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y + height, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y, z - width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x - width, y + height, z + width).endVertex();
        worldRenderer.tex(0, 0);
        worldRenderer.pos(x + width, y + height, z + width).endVertex();
        worldRenderer.tex(0, 0);

        tessellator.draw();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
}
