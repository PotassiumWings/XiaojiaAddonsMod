package com.xiaojia.xiaojiaaddons.Features.Dragons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.util.HashMap;

public class EnderCrystalESP extends RenderEntityESP {
    public static final String ENDERCRYSTAL_STRING = "Crystal";

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityEnderCrystal) || !Configs.CrystalESP) return null;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entity", entity);
        hashMap.put("r", 255);
        hashMap.put("g", 0);
        hashMap.put("b", 0);
        hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
        hashMap.put("kind", ENDERCRYSTAL_STRING);
        hashMap.put("fontColor", 0xdc143c);
        hashMap.put("isFilled", true);
        hashMap.put("isESP", true);
        return new EntityInfo(hashMap);
    }

    @Override
    public boolean enabled() {
        return SkyblockUtils.isInDragon();
    }
}
