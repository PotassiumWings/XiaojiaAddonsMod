package com.xiaojia.xiaojiaaddons.Features;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public abstract class RenderEntityESP {
    private List<EntityInfo> renderEntities = new ArrayList<>();

    @SubscribeEvent
    public final void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (getWorld() == null) return;
        try {
            List<EntityInfo> newEntities = new ArrayList<>();
            for (Entity entity : getEntities()) {
                EntityInfo info = getEntityInfo(entity);
                if (info == null) continue;
                newEntities.add(info);
            }
            renderEntities = newEntities;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Entity> getEntities() {
        return getWorld().loadedEntityList;
    }

    @SubscribeEvent
    public final void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.GeneralESP) return;
        try {
            for (EntityInfo entityInfo : renderEntities) {
                EntityInfo.EnumDraw draw = entityInfo.getDrawString();
                Entity entity = entityInfo.getEntity();
                String kind = entityInfo.getKind();
                String name = entity.getName();
                String drawString = "";
                boolean filled = entityInfo.isFilled();  // box filled / string

                // string
                if (draw == EntityInfo.EnumDraw.DRAW_KIND) drawString = entityInfo.getKind();
                else if (draw == EntityInfo.EnumDraw.DRAW_ARMORSTAND_HP)
                    drawString = DisplayUtils.getHPDisplayFromArmorStandName(name, kind);
                else if (draw == EntityInfo.EnumDraw.DRAW_ENTITY_HP)
                    drawString = DisplayUtils.hpToString(((EntityLivingBase) entity).getHealth());
                if (shouldDrawString(entityInfo))
                    GuiUtils.drawString(
                            drawString, getX(entity), getY(entity), getZ(entity),
                            entityInfo.getFontColor(),
                            entityInfo.getScale(),
                            true
                    );

                // esp box
                int r = entityInfo.getR(), g = entityInfo.getG(), b = entityInfo.getB();
                float width = entityInfo.getWidth(), height = entityInfo.getHeight();
                float yOffset = entityInfo.getyOffset();
                if (shouldRenderESP(entityInfo)) {
                    boolean shouldESP = entityInfo.isESP();
                    if (shouldESP) GuiUtils.enableESP();
                    if (!filled) GuiUtils.drawBoxAtEntity(entity, r, g, b, 100, width, height, yOffset);
                    else GuiUtils.drawFilledBoxAtEntity(entity, r, g, b, 100, width, height, yOffset);
                    if (shouldESP) GuiUtils.disableESP();
                }

                dealWithEntityInfo(entityInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // other things to esp (titles, etc) in RENDER WORLD EVENT
    public void dealWithEntityInfo(EntityInfo entityInfo) {
    }

    public abstract boolean shouldRenderESP(EntityInfo entityInfo);

    public abstract boolean shouldDrawString(EntityInfo entityInfo);

    // in TICK phase, return the entityInfo to be dealt with.
    public abstract EntityInfo getEntityInfo(Entity entity);
}
