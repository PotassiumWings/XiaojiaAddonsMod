package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;

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
}
