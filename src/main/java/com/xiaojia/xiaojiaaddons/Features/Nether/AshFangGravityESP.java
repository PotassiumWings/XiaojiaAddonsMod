package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.Color;
import java.util.HashMap;

public class AshFangGravityESP  extends RenderEntityESP {

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return Configs.AshfangGravityCenterESP;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return Configs.AshfangGravityCenterESP;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (isGravityCenter(entity)) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("r", 255);
            hashMap.put("g", 0);
            hashMap.put("b", 0);
            hashMap.put("a", 200);
            hashMap.put("entity", entity);
            hashMap.put("yOffset", -2.5F);
            hashMap.put("kind", "Gravity Center");
            hashMap.put("fontColor", 0xFF0000);
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 0.2F);
            hashMap.put("height", 0.4F);
            hashMap.put("isESP", true);
            hashMap.put("isFilled", true);
            return new EntityInfo(hashMap);
        }
        return null;
    }

    private boolean isGravityCenter(Entity entity) {
        if (!(entity instanceof EntityArmorStand)) return false;
        try {
            ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
            if (helm == null) return false;
            if (helm.getItem() != Items.skull || helm.getMetadata() != 3) return false;
            return helm.getTagCompound()
                    .getCompoundTag("SkullOwner")
                    .getString("Id")
                    .equals("c91c8e98-81b0-4656-8da1-f1742a663032");
        } catch (Exception e) {
            return false;
        }
    }
}
