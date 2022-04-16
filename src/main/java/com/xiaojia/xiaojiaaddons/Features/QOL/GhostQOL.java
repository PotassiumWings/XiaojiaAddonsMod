package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.OutlineUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class GhostQOL extends RenderEntityESP {
    public static final String GHOST_STRING = "Ghost";
    private static final HashMap<String, Entity> runicGhostUUIDs = new HashMap<>();
    public static int OUTLINE_BOX = 1;
    public static int FILLED_OUTLINE_BOX = 2;
    public static int VANILLA_CREEPER = 3;

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInMist()) return;
        EntityLivingBase entity = event.entity;
        if (entity instanceof EntityCreeper) {
            if (Configs.VisibleGhost == OUTLINE_BOX) {
                render(entity, event.renderer, event.x, event.y, event.z);
            } else if (Configs.VisibleGhost == VANILLA_CREEPER) {
                entity.setInvisible(false);
            }
        }
    }

    @SubscribeEvent
    public void onTickRunicGhost(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInMist() || !Configs.ShowRunicGhost) return;

        List<Entity> list = getWorld().loadedEntityList;
        for (Entity entity : list) {
            if (!(entity instanceof EntityCreeper)) continue;
            String uuid = entity.getUniqueID().toString();
            if (((EntityCreeper) entity).getHealth() > 1000000.1 && !runicGhostUUIDs.containsKey(uuid)) {
                runicGhostUUIDs.put(uuid, entity);
            }
        }
    }

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityCreeper) || !runicGhostUUIDs.containsKey(entity.getUniqueID().toString()))
            return null;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entity", entity);
        hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_ENTITY_HP);
        hashMap.put("width", 0.35F);
        hashMap.put("height", 1.8F);
        hashMap.put("fontColor", 0x33ff33);
        hashMap.put("isFilled", true);
        hashMap.put("kind", GHOST_STRING);
        return new EntityInfo(hashMap);
    }

    @Override
    public boolean enabled() {
        return SkyblockUtils.isInMist();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        runicGhostUUIDs.clear();
    }

    private void render(EntityLivingBase entity, RendererLivingEntity<EntityLivingBase> renderer, double x, double y, double z) {
        // from RendererLivingEntity.doRenderer()
        float partialTicks = 1 - MathUtils.partialTicks;
        ModelBase mainModel = renderer.getMainModel();

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        mainModel.swingProgress = entity.getSwingProgress(partialTicks);

        try {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float f2 = f1 - f;

            float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            GlStateManager.translate((float) x, (float) y, (float) z);
            float f8 = (float) entity.ticksExisted + partialTicks;
            this.rotateCorpse(entity, f8, f, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
            float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
            if (entity.isChild()) f6 *= 3.0F;
            if (f5 > 1.0F) f5 = 1.0F;

            GlStateManager.enableAlpha();
            mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
            mainModel.setRotationAngles(f6, f5, f8, f2, f7, 0.0625F, entity);

            OutlineUtils.outlineEntity(mainModel, entity, f6, f5, f8, f2, f7, 0.0625F, new Color(85, 255, 85, 150), 7);
            GlStateManager.disableRescaleNormal();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    protected float interpolateRotation(float par1, float par2, float par3) {
        float f;
        for (f = par2 - par1; f < -180.0F; f += 360.0F) ;
        while (f >= 180.0F) f -= 360.0F;
        return par1 + par3 * f;
    }

    protected void rotateCorpse(EntityLivingBase bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (bat.deathTime > 0) {
            float f = ((float) bat.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);
            if (f > 1.0F) f = 1.0F;
            GlStateManager.rotate(f * 90, 0.0F, 0.0F, 1.0F);
        }
    }
}
