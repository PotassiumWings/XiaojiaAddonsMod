package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;

import java.util.HashMap;

public class GhastESP extends RenderEntityESP {
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
        if (!Configs.GhastESP) return null;
        if (entity instanceof EntityGhast) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("entity", entity);
            hashMap.put("yOffset", 0.0F);
            hashMap.put("kind", "Ghast");
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 2.0F);
            hashMap.put("height", 4.0F);
            hashMap.put("fontColor", 0x33ff33);
            hashMap.put("isESP", true);
            return new EntityInfo(hashMap);
        }
        return null;
    }
}
