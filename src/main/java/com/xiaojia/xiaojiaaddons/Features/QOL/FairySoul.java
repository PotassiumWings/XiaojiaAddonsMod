package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class FairySoul extends RenderEntityESP {

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return Configs.FairySoulESP;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return Configs.FairySoulESP;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityArmorStand)) return null;
        EntityArmorStand armorStand = (EntityArmorStand) entity;
        if (isFairySoul(armorStand)) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("entity", entity);
            hashMap.put("yOffset", -2F);
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 0.35F);
            hashMap.put("height", 0.7F);
            hashMap.put("fontColor", 0x33ff33);
            hashMap.put("isFilled", true);
            hashMap.put("isESP", true);
            hashMap.put("kind", "Fairy Soul");
            return new EntityInfo(hashMap);
        }
        return null;
    }

    @Override
    public boolean enabled() {
        return true;
    }

    private boolean isFairySoul(EntityArmorStand entity) {
        try {
            ItemStack helm = entity.getEquipmentInSlot(4);
            if (helm == null) return false;
            if (helm.getItem() != Items.skull || helm.getMetadata() != 3) return false;
            return helm.getTagCompound()
                    .getCompoundTag("SkullOwner")
                    .getString("Id")
                    .equals("57a4c8dc-9b8e-3d41-80da-a608901a6147");
        } catch (Exception e) {
            return false;
        }
    }
}
