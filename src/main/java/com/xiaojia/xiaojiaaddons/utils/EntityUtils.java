package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class EntityUtils {
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
}
