package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;

import java.util.HashMap;

public class GolemESP extends RenderEntityESP {
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
        if (!Checker.enabled) return null;
        if (!Configs.GolemESP) return null;
        if (entity instanceof EntityGolem) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("entity", entity);
            hashMap.put("yOffset", 0.0F);
            hashMap.put("kind", "Golem");
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 1.5F);
            hashMap.put("height", 3.0F);
            hashMap.put("fontColor", 0x33ff33);
            hashMap.put("isESP", true);
            return new EntityInfo(hashMap);
        }
        return null;
    }

    @Override
    public boolean enabled() {
        return true;
    }
}
