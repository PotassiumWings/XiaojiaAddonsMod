package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class EntityQOL {
    private static final ArrayList<String> summonItemIDs = new ArrayList<>(
            Arrays.asList("HEAVY_HELMET", "ZOMBIE_KNIGHT_HELMET", "SKELETOR_HELMET")
    );

    private static boolean isSummon(Entity entity) {
        if (entity instanceof EntityPlayer)
            return entity.getName().equals("Lost Adventurer");
        if (entity instanceof EntityZombie ||
                entity instanceof EntitySkeleton)
            for (int i = 0; i < 5; i++) {
                ItemStack item = ((EntityMob) entity).getEquipmentInSlot(i);
                if (summonItemIDs.contains(NBTUtils.getSkyBlockID(item)))
                    return true;
            }
        return false;
    }

    private static boolean isPlayer(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {
            return isPlayer((EntityOtherPlayerMP) entity);
        }
        return false;
    }

    private static boolean isPlayer(EntityOtherPlayerMP entity) {
        UUID uuid = entity.getUniqueID();
        if (XiaojiaAddons.isDebug()) ChatLib.chat(uuid.version() + "");
        return (uuid.version() == 3 || uuid.version() == 4) && !entity.getName().contains(" ");
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (SkyblockUtils.isInDungeon()) return;
        if (getWorld() == null) return;
        List<Entity> allEntities = getWorld().loadedEntityList;
        for (Entity entity : allEntities) {
            if (isPlayer(entity)) {
                if (Configs.HidePlayers && MathUtils.distanceSquareFromPlayer(entity) <= Configs.HidePlayerRadius * Configs.HidePlayerRadius) {
                    entity.setDead();
                }
            } else if (isSummon(entity)) {
                if (Configs.HideSummons) {
                    entity.setDead();
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
