package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class EntityUtils {
    private static final ArrayList<String> summonItemIDs = new ArrayList<>(
            Arrays.asList("HEAVY_HELMET", "ZOMBIE_KNIGHT_HELMET", "SKELETOR_HELMET", "SUPER_HEAVY_HELMET")
    );

    public static String getHeadOwner(Entity entity) {
        if (!(entity instanceof EntityArmorStand)) return null;
        ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
        if (helm == null) return null;
        String owner = "";
        try {
            owner = helm.getTagCompound()
                    .getCompoundTag("SkullOwner")
                    .getString("Id");
        } catch (Exception ignored) {
        }
        return owner;
    }

    public static String getHeadTexture(Entity entity) {
        if (!(entity instanceof EntityArmorStand)) return null;
        ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
        if (helm == null) return null;
        try {
            NBTTagCompound helmTag = helm.getTagCompound();
            if (helmTag == null) return null;
            NBTTagCompound ownerTag = helmTag.getCompoundTag("SkullOwner");
            if (ownerTag == null) return null;
            NBTTagCompound propTag = ownerTag.getCompoundTag("Properties");
            if (propTag == null) return null;
            NBTTagList list = propTag.getTagList("textures", 10);
            if (list == null || list.tagCount() < 1) return null;
            NBTTagCompound texture = (NBTTagCompound) list.get(0);
            return texture.getString("Value");
        } catch (Exception ignored) {
        }
        return null;
    }

    public static List<Entity> getEntities() {
        World world = getWorld();
        if (world == null) return new ArrayList<>();
        ArrayList<Entity> res = new ArrayList<>(world.loadedEntityList);
        if (Configs.UnloadUnusedNPCEntity && SkyblockUtils.isInNether()) res.removeIf(entity ->
                (MathUtils.equal(getX(entity), 0) && MathUtils.equal(getY(entity), 0) && MathUtils.equal(getZ(entity), 0) &&
                        entity.getName().matches("\u00a7(d|5)[A-Z][a-z]+ "))
        );
        return res;
    }

    public static void tryRightClickEntity(Entity entity, double reach) {
        ControlUtils.face(getX(entity), getY(entity) + entity.height / 2, getZ(entity));

        Vec3 vec3 = getPlayer().getPositionEyes(1.0F);
        double d0 = XiaojiaAddons.mc.playerController.getBlockReachDistance();
        if (reach > d0) d0 = reach;
        Vec3 vec31 = getPlayer().getLook(1.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
        float f1 = entity.getCollisionBorderSize();
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f1, f1, f1);
        MovingObjectPosition moving = axisalignedbb.calculateIntercept(vec3, vec32);
        if (moving == null) {
            ChatLib.chat("Failed to click!");
            return;
        }

        Vec3 vec = new Vec3(
                moving.hitVec.xCoord - entity.posX,
                moving.hitVec.yCoord - entity.posY,
                moving.hitVec.zCoord - entity.posZ
        );
        XiaojiaAddons.mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, vec));
    }

    public static boolean isSummon(Entity entity) {
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

    public static boolean isPlayer(Entity entity) {
        if (entity instanceof EntityOtherPlayerMP) {
            return isPlayer((EntityOtherPlayerMP) entity);
        }
        return false;
    }

    public static boolean isPlayer(EntityOtherPlayerMP entity) {
        UUID uuid = entity.getUniqueID();
        return (uuid.version() == 3 || uuid.version() == 4) && entity.getName().matches("[a-zA-Z0-9_]+");
    }

    public static boolean isEmptyArmorStand(Entity entity) {
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

    public static boolean isGoldenFish(Entity entity) {
        String texture = EntityUtils.getHeadTexture(entity);
        return (texture != null && texture.equals("ewogICJ0aW1lc3RhbXAiIDogMTY0MzgzMTA2MDE5OCwKICAicHJvZmlsZUlkIiA6ICJiN2ZkYmU2N2NkMDA0NjgzYjlmYTllM2UxNzczODI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzEyMGNmM2MwYTQwZmM2N2UwZTVmZTBjNDZiMGFlNDA5YWM3MTAzMGE3NjU2ZGExN2IxMWVkMDAxNjQ1ODg4ZmUiCiAgICB9CiAgfQp9=="));
    }
}
