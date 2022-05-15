package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
}
