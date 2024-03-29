package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class RunicESP extends RenderEntityESP {
    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!Checker.enabled) return null;
        if (!Configs.RunicESP) return null;
        if (entity.getName().contains("\u00a75[")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            String name = entity.getName();
            hashMap.put("entity", entity);
            hashMap.put("yOffset", 0.0F);
            hashMap.put("kind", name);
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 1.0F);
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
