package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.awt.Color;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class GuiUtils {
    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

    public static void enableESP() {
        GlStateManager.disableCull();
        GlStateManager.disableDepth();
    }

    public static void disableESP() {
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    public static void drawScaledString(String string, float scale, int x, int y, boolean shadow) {
        GlStateManager.scale(scale, scale, scale);
        drawString(string, (int) (x / scale), (int) (y / scale), shadow);
    }

    // drawString in Gui
    public static void drawString(String text, int x, int y, boolean shadow) {
        drawString(text, x, y, shadow, Color.WHITE);
    }

    public static void drawString(String text, int x, int y, boolean shadow, Color color) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            mc.fontRendererObj.drawString(line, x, y, color.getRGB(), shadow);
            y += mc.fontRendererObj.FONT_HEIGHT + 1;
        }
    }

    public static void drawRect(int color, int x, int y, int width, int height) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    private static Vector2i getXYForSlot(int size, int xSlotPos, int ySlotPos) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int guiLeft = (sr.getScaledWidth() - 176) / 2;
        int guiTop = (sr.getScaledHeight() - 222) / 2;
        int x = guiLeft + xSlotPos;
        int y = guiTop + ySlotPos + (6 - (size - 36) / 9) * 9;
        return new Vector2i(x, y);
    }

    public static void drawStringAtRightUpOfDoubleChest(String string) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int guiRight = (sr.getScaledWidth() + 176) / 2;
        int guiTop = (sr.getScaledHeight() - 222) / 2;
        int x = guiRight - 8 - RenderUtils.getStringWidth(string);
        int y = guiTop + 6;
        GL11.glTranslated(0, 0, 1);
        mc.fontRendererObj.drawString(string, x, y, 0xffffffff, false);
        GL11.glTranslated(0, 0, -1);
    }

    public static void drawOnSlot(int size, int xSlotPos, int ySlotPos, int colour) {
        Vector2i v = getXYForSlot(size, xSlotPos, ySlotPos);
        int x = v.x, y = v.y;
        GL11.glTranslated(0, 0, 1);
        Gui.drawRect(x, y, x + 16, y + 16, colour);
        GL11.glTranslated(0, 0, -1);
    }

    public static void drawStringOnSlot(String name, int size, int xSlotPos, int ySlotPos, Color color) {
        Vector2i v = getXYForSlot(size, xSlotPos, ySlotPos);
        int x = v.x + 8 - RenderUtils.getStringWidth(name) / 2, y = v.y + 4;  // height is 9
        GL11.glTranslated(0, 0, 1);
        drawString(name, x, y, false, color);
        GL11.glTranslated(0, 0, -1);
    }

    public static void drawStringOnSlot(String name, int size, int xSlotPos, int ySlotPos, float scale, int offsetX, int offsetY) {
        Vector2i v = getXYForSlot(size, xSlotPos, ySlotPos);
        int x = v.x + offsetX, y = v.y + offsetY;
        GlStateManager.translate(0, 0, 1);
        GlStateManager.pushMatrix();
        drawScaledString(name, scale, x, y, false);
        GlStateManager.popMatrix();
        GlStateManager.translate(0, 0, -1);
    }

    public static void showTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        gui.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut);
        gui.displayTitle(null, ChatLib.addColor(subtitle), fadeIn, time, fadeOut);
        gui.displayTitle(null, null, fadeIn, time, fadeOut);
    }

    // drawString in 3D
    public static void drawString(String text, float x, float y, float z, int color, float scale, boolean increase) {
        float lScale = scale;
        RenderManager renderManager = mc.getRenderManager();
        FontRenderer fontRenderer = mc.fontRendererObj;
        Vector3f renderPos = getRenderPos(x, y, z);
        if (increase) {
            float f1 = renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z;
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

    public static void drawBoxAtBlock(int x, int y, int z, int r, int g, int b, int a, int width, int height, float delta) {
        drawFilledBoundingBoxAbsolute(x - delta, y - delta, z - delta, x + width + delta, y + height + delta, z + width + delta, r, g, b, a);
    }

    public static void drawBoxAtPos(float x, float y, float z, int r, int g, int b, int a, float width, float height, float delta) {
        drawFilledBoundingBoxAbsolute(x - delta, y - delta, z - delta, x + width + delta, y + height + delta, z + width + delta, r, g, b, a);
    }

    public static void drawBoxAtBlock(BlockPos blockPos, Color color, int width, int height, float delta) {
        drawBoxAtBlock(
                blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
                width, height, delta
        );
    }

    public static void drawBoundingBoxAtBlock(BlockPos blockPos, Color color) {
        drawBoxAt(
                blockPos.getX() + 0.5F, blockPos.getY() + 0.5F, blockPos.getZ() + 0.5F,
                color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
                0.5F, 0.5F
        );
    }

    public static void drawBoundingBoxAtPos(float x, float y, float z, Color color, float width, float height) {
        drawBoxAt(
                x, y, z,
                color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
                width, height
        );
    }

    public static void drawSelectionFilledBoxAtBlock(BlockPos pos, Color color) {
        AxisAlignedBB box = BlockUtils.getAABBOfBlock(pos);
        if (box == null) return;
        drawFilledBoundingBoxRelative(
                (float) box.minX, (float) box.minY, (float) box.minZ,
                (float) box.maxX, (float) box.maxY, (float) box.maxZ,
                color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()
        );
    }

    public static void drawSelectionBoundingBoxAtBlock(BlockPos pos, Color color) {
        AxisAlignedBB box = BlockUtils.getAABBOfBlock(pos);
        if (box == null) return;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(Configs.EtherwarpPointBoundingThickness);
        GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        RenderGlobal.drawSelectionBoundingBox(box);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glLineWidth(Configs.BoxLineThickness);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawLine(float sx, float sy, float sz, float tx, float ty, float tz, Color color, int lineWidth) {
        drawLineWithDepthAbsolute(sx, sy, sz, tx, ty, tz, color, lineWidth);
    }

    private static void drawLineWithDepthAbsolute(float sx, float sy, float sz, float tx, float ty, float tz, Color color, int lineWidth) {
        EntityPlayerSP player = getPlayer();
        float px = getX(player), py = getY(player), pz = getZ(player);
        sx -= px;
        sy -= py;
        sz -= pz;
        tx -= px;
        ty -= py;
        tz -= pz;
        drawLineWithDepthRelative(sx, sy, sz, tx, ty, tz, color, lineWidth);
    }

    private static void drawLineWithDepthRelative(float x, float y, float z, float tx, float ty, float tz, Color color, int lineWidth) {
        GlStateManager.pushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue());
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x, y, z).endVertex();
        worldRenderer.pos(tx, ty, tz).endVertex();

        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.popMatrix();
    }

    private static void drawFilledBoundingBoxAbsolute(float sx, float sy, float sz, float tx, float ty, float tz, int r, int g, int b, int a) {
        EntityPlayerSP player = getPlayer();
        float px = getX(player), py = getY(player), pz = getZ(player);
        sx -= px;
        sy -= py;
        sz -= pz;
        tx -= px;
        ty -= py;
        tz -= pz;
        drawFilledBoundingBoxRelative(sx, sy, sz, tx, ty, tz, r, g, b, a);
    }

    public static void drawFilledFace(BlockUtils.Face face, Color color) {
        drawFilledBoundingBoxAbsolute(
                (float) face.sx, (float) face.sy, (float) face.sz,
                (float) face.tx, (float) face.ty, (float) face.tz,
                color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()
        );
    }

    private static void drawFilledBoundingBoxRelative(float sx, float sy, float sz, float tx, float ty, float tz, int r, int g, int b, int a) {
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

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
        GL11.glPopMatrix();
    }

    public static final Vector3f getRenderPos(float x, float y, float z) {
        return new Vector3f(x - MathUtils.getX(mc.thePlayer), y - MathUtils.getY(mc.thePlayer), z - MathUtils.getZ(mc.thePlayer));
    }

    public static void drawBoundingBox(AxisAlignedBB box, int thickness, Color color) {
        EntityPlayerSP player = getPlayer();
        float px = getX(player), py = getY(player), pz = getZ(player);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-px, -py, -pz);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(thickness);
        RenderGlobal.drawOutlinedBoundingBox(box, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GlStateManager.translate(px, py, pz);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private static void drawBoxAt(float x, float y, float z, int r, int g, int b, int a, float width, float height) {
//        ChatLib.chat(String.format("%.2f %.2f %.2f %.2f %.2f %d %d %d %d", x, y, z, width, height, r,g,b,a));
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(Configs.BoxLineThickness);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        RenderManager renderManager = mc.getRenderManager();
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
        GlStateManager.color(r / 255.F, g / 255.F, b / 255.F, a / 255.F);
        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        worldRenderer.pos(x + width, y + height, z + width).endVertex();
        worldRenderer.pos(x + width, y + height, z - width).endVertex();
        worldRenderer.pos(x - width, y + height, z - width).endVertex();
        worldRenderer.pos(x - width, y + height, z + width).endVertex();
        worldRenderer.pos(x + width, y + height, z + width).endVertex();
        worldRenderer.pos(x + width, y, z + width).endVertex();
        worldRenderer.pos(x + width, y, z - width).endVertex();
        worldRenderer.pos(x - width, y, z - width).endVertex();
        worldRenderer.pos(x - width, y, z + width).endVertex();
        worldRenderer.pos(x - width, y, z - width).endVertex();
        worldRenderer.pos(x - width, y + height, z - width).endVertex();
        worldRenderer.pos(x - width, y, z - width).endVertex();
        worldRenderer.pos(x + width, y, z - width).endVertex();
        worldRenderer.pos(x + width, y + height, z - width).endVertex();
        worldRenderer.pos(x + width, y, z - width).endVertex();
        worldRenderer.pos(x + width, y, z + width).endVertex();
        worldRenderer.pos(x - width, y, z + width).endVertex();
        worldRenderer.pos(x - width, y + height, z + width).endVertex();
        worldRenderer.pos(x + width, y + height, z + width).endVertex();

        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glPopMatrix();
    }

    public static void drawRotatedTexture(ResourceLocation resourceLocation, int x, int y, int width, int height, int angle) {
        drawRotatedTexture(resourceLocation, x, y, width, height, width, height, 0, 0, angle);
    }

    public static void drawRotatedTexture(ResourceLocation loc, int x, int y, int width, int height, int textureWidth, int textureHeight, int textureX, int textureY, int angle) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + width / 2.0F, y + height / 2.0F, 0.0F);
        GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-x - width / 2.0F, -y - height / 2.0F, 0.0F);
        drawTexture(loc, x, y, width, height, textureWidth, textureHeight, textureX, textureY);
        GlStateManager.popMatrix();
    }

    public static void drawTexture(ResourceLocation loc, int x, int y, int width, int height, int textureWidth, int textureHeight, int textureX, int textureY) {
        mc.getTextureManager().bindTexture(loc);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, textureWidth, textureHeight);
    }

    public static void drawTexture(ResourceLocation loc, int x, int y, int width, int height) {
        drawTexture(loc, x, y, width, height, width, height, 0, 0);
    }
}
