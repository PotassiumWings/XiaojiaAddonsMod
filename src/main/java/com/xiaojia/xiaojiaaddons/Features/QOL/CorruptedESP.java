package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class CorruptedESP extends RenderEntityESP {
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
        if (!Configs.CorruptedESP) return null;
        if (entity.getName().contains("Corrupted")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            String name = ChatLib.removeFormatting(entity.getName());
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
}
