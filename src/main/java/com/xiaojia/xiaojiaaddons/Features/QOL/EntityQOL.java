package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Accentry.KillAll;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.Velocity;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class EntityQOL {
    private static int count;

    public static int getHiddenEntityCount() {
        return count;
    }


    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (SkyblockUtils.isInDungeon()) return;
        if (getWorld() == null) return;
        List<Entity> allEntities = EntityUtils.getEntities();
        count = 0;
        for (Entity entity : allEntities) {
            if (EntityUtils.isPlayer(entity)) {
                KillAll.onPlayerNearby(entity);
                Velocity.onPlayerNearby(entity);
                if (Configs.HidePlayers && MathUtils.distanceSquareFromPlayer(entity) <= Configs.HidePlayerRadius * Configs.HidePlayerRadius) {
                    entity.posY = entity.lastTickPosY = 9999;
                }
            } else if (EntityUtils.isSummon(entity)) {
                if (Configs.HideSummons) {
                    entity.posY = entity.lastTickPosY = 9999;
                }
            } else if (EntityUtils.isGoldenFish(entity)) {
                if (Configs.HideGoldenFish) {
                    entity.posY = entity.lastTickPosY = 9999;
                }
            } else if (EntityUtils.isEmptyArmorStand(entity)) {
                if (Configs.HideEmptyArmorStand) {
                    entity.posY = entity.lastTickPosY = 9999;
                    count++;
                }
            }
        }
    }


    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
//        if (!Checker.enabled) return;
//        if (SkyblockUtils.isInDungeon()) return;
//        if (Configs.HideSummons && isSummon(event.entity)) {
//            event.setCanceled(true);
//        }
//        if (Configs.HidePlayers && isPlayer(event.entity) &&
//                MathUtils.distanceSquareFromPlayer(event.entity) <= Configs.HidePlayerRadius * Configs.HidePlayerRadius) {
//            event.setCanceled(true);
//        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
//        if (!Checker.enabled) return;
//        Entity target = event.target;
//        if (Configs.ClickThroughSummons && isSummon(target) ||
//                Configs.ClickThroughPlayers && isPlayer(target)) {
//            float reach = mc.playerController.getBlockReachDistance();
//            Entity excludedEntity = mc.getRenderViewEntity();
//            Vec3 look = excludedEntity.getLook(0.0F);
//            AxisAlignedBB boundingBox = excludedEntity
//                    .getEntityBoundingBox()
//                    .addCoord(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach)
//                    .expand(1.0D, 1.0D, 1.0D);
//            List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(excludedEntity, boundingBox);
//            if (XiaojiaAddons.isDebug()) for (Entity entity : entitiesInRange) ChatLib.chat(entity.getName());
//            entitiesInRange.removeIf(entity -> !entity.canBeCollidedWith());
//            if (Configs.ClickThroughSummons) entitiesInRange.removeIf(EntityQOL::isSummon);
//            if (Configs.ClickThroughPlayers) entitiesInRange.removeIf(EntityQOL::isPlayer);
//            entitiesInRange.sort((Entity a, Entity b) -> MathUtils.distanceSquareFromPlayer(a) >= MathUtils.distanceSquareFromPlayer(b) ? 1 : -1);
//            if (entitiesInRange.size() > 0) {
//                event.setCanceled(true);
//                getPlayer().swingItem();
//                if (XiaojiaAddons.isDebug()) ChatLib.chat(entitiesInRange.get(0).toString());
//                if (XiaojiaAddons.isDebug()) ChatLib.chat("Attacking through summon!");
//                mc.playerController.attackEntity(getPlayer(), entitiesInRange.get(0));
//            }
//        }
    }
}
