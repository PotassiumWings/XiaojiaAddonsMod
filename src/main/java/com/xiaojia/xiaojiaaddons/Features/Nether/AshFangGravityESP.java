package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
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

    @Override
    public boolean enabled() {
        return SkyblockUtils.isInNether();
    }

    private boolean isGravityCenter(Entity entity) {
        String texture = EntityUtils.getHeadTexture(entity);
        return texture != null && texture.equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWE2OWNjZjdhZDkwNGM5YTg1MmVhMmZmM2Y1YjRlMjNhZGViZjcyZWQxMmQ1ZjI0Yjc4Y2UyZDQ0YjRhMiJ9fX0=");
    }
}
