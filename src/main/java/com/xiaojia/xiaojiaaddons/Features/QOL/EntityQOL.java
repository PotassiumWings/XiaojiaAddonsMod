package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class EntityQOL {
    private static final ArrayList<String> summonItemIDs = new ArrayList<>(
            Arrays.asList("HEAVY_HELMET", "ZOMBIE_KNIGHT_HELMET", "SKELETOR_HELMET", "SUPER_HEAVY_HELMET")
    );

    private static int count;

    public static int getHiddenEntityCount() {
        return count;
    }

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
        return (uuid.version() == 3 || uuid.version() == 4) && !entity.getName().contains(" ");
    }

    private static boolean isEmpty(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            EntityArmorStand as = (EntityArmorStand) entity;
            if (!as.isInvisible()) return false;
            if (as.hasCustomName()) return false;
            for (int i = 0; i < 5; i++)
                if (as.getEquipmentInSlot(i) != null)
                    return false;
            return true;
        }
        return false;
    }

    private static boolean isGoldenFish(Entity entity) {
        String texture = EntityUtils.getHeadTexture(entity);
        return (texture != null && texture.equals("ewogICJ0aW1lc3RhbXAiIDogMTY0MzgzMTA2MDE5OCwKICAicHJvZmlsZUlkIiA6ICJiN2ZkYmU2N2NkMDA0NjgzYjlmYTllM2UxNzczODI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzEyMGNmM2MwYTQwZmM2N2UwZTVmZTBjNDZiMGFlNDA5YWM3MTAzMGE3NjU2ZGExN2IxMWVkMDAxNjQ1ODg4ZmUiCiAgICB9CiAgfQp9=="));
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (SkyblockUtils.isInDungeon()) return;
        if (getWorld() == null) return;
        List<Entity> allEntities = getWorld().loadedEntityList;
        count = 0;
        for (Entity entity : allEntities) {
            if (isPlayer(entity)) {
                if (Configs.HidePlayers && MathUtils.distanceSquareFromPlayer(entity) <= Configs.HidePlayerRadius * Configs.HidePlayerRadius) {
                    entity.posY = entity.lastTickPosY = 9999;
                }
            } else if (isSummon(entity)) {
                if (Configs.HideSummons) {
                    entity.posY = entity.lastTickPosY = 9999;
                }
            } else if (isGoldenFish(entity)) {
                if (Configs.HideGoldenFish) {
                    entity.posY = entity.lastTickPosY = 9999;
                }
            } else if (isEmpty(entity)) {
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
