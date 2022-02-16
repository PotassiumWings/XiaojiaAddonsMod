package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.RenderEntityModelEvent;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.OutlineUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class StarredMobESP extends RenderEntityESP {
    private static final HashMap<Entity, Color> highlightedEntities = new HashMap<>();
    public static HashSet<Entity> fixEntities = new HashSet<>();
    private final HashSet<Entity> checkedEntities = new HashSet<>();

    private static void highlightEntity(Entity entity, Color c) {
        highlightedEntities.put(entity, c);
    }

    public static void show() {
        for (Entity entity : fixEntities) {
            ChatLib.chat(entity.getCustomNameTag() + ": " + entity.isDead + ", " + MathUtils.getPosString(entity));
        }
    }

    @SubscribeEvent
    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (!Checker.enabled) return;
        if (Configs.StarredMobESP != 1) return;
        if (!SkyblockUtils.isInDungeon()) return;
        Entity entity = event.entity;
        if (checkedEntities.contains(entity)) return;
        if (entity instanceof EntityArmorStand && entity.hasCustomName() &&
                entity.getName().contains("✯")) {
            checkedEntities.add(entity);
            List<Entity> possibleEntities = getWorld().getEntitiesInAABBexcluding(
                    entity, entity.getEntityBoundingBox().expand(0.1D, 3.0D, 0.1D),
                    e -> !(e instanceof EntityArmorStand)
            );
            if (!possibleEntities.isEmpty()) {
                Color color = ColorUtils.realColors[Configs.StarredMobESPColor];
                highlightEntity(possibleEntities.get(0), color);
            } else {
                if (Configs.ShowBoxWhenBoundingNotWork) {
                    fixEntities.add(entity);
                }
            }
        }
        if (highlightedEntities.containsKey(entity)) {
            GuiUtils.enableESP();
            OutlineUtils.outlineEntity(event, highlightedEntities.get(event.entity), Configs.StarredMobESPOutlineLength);
            GuiUtils.disableESP();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        checkedEntities.clear();
        highlightedEntities.clear();
        fixEntities.clear();
    }

    @Override
    public List<Entity> getEntities() {
        if (fixEntities.isEmpty()) return new ArrayList<>();

        List<Entity> list = getWorld().loadedEntityList;
        ArrayList<Entity> res = new ArrayList<>();
        for (Entity entity : list) {
            if (fixEntities.contains(entity)) {
                res.add(entity);
            }
        }
        return res;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        String name = ChatLib.removeFormatting(entity.getName());
        float height = 0;
        if (name.contains("Fel") || name.contains("Withermancer")) height = 2.8F;
        else height = 1.9F;
        HashMap<String, Object> hashMap = new HashMap<>();
        Color color = ColorUtils.realColors[Configs.StarredMobESPColor];
        hashMap.put("r", color.getRed());
        hashMap.put("g", color.getGreen());
        hashMap.put("b", color.getBlue());
        hashMap.put("a", color.getAlpha());
        hashMap.put("entity", entity);
        hashMap.put("yOffset", 1F);
        hashMap.put("kind", "Starred");
        hashMap.put("fontColor", 0);
        hashMap.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
        hashMap.put("width", 0.45F);
        hashMap.put("height", height);
        hashMap.put("isESP", true);
        return new EntityInfo(hashMap);
    }

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }
}
