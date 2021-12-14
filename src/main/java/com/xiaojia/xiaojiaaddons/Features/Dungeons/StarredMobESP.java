package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.RenderEntityModelEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.OutlineUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.world.WorldEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class StarredMobESP {
    private static final HashMap<Entity, Color> highlightedEntities = new HashMap<>();
    private final HashSet<Entity> checkedEntities = new HashSet<>();

    private static void highlightEntity(Entity entity, Color c) {
        highlightedEntities.put(entity, c);
    }

    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.StarredMobESP) return;
        if (!SkyblockUtils.isInDungeon()) return;
        Entity entity = event.entity;
        if (checkedEntities.contains(entity)) return;
        if (event.entity instanceof EntityArmorStand && entity.hasCustomName() &&
                entity.getName().contains("✯")) {
            checkedEntities.add(entity);
            List<Entity> possibleEntities = getWorld().getEntitiesInAABBexcluding(
                    entity, entity.getEntityBoundingBox().expand(0.0D, 3.0D, 0.0D),
                    e -> !(e instanceof EntityArmorStand)
            );
            if (!possibleEntities.isEmpty())
                highlightEntity(possibleEntities.get(0), Color.ORANGE);
        }
        if (highlightedEntities.containsKey(entity))
            OutlineUtils.outlineEntity(event, highlightedEntities.get(event.entity));
    }

    public void onWorldLoad(WorldEvent.Load event) {
        checkedEntities.clear();
        highlightedEntities.clear();
    }
}
