package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class ConvergenceESP extends RenderEntityESP {
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
        if (!Configs.ConvergenceESP) return null;
        String name = ChatLib.removeFormatting(entity.getName());
        if (name.equals("Convergence Center")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("entity", entity);
            hashMap.put("yOffset", 0.0F);
            hashMap.put("kind", "Convergence Center");
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 3.0F);
            hashMap.put("height", 6.0F);
            hashMap.put("fontColor", 0xff3333);
            hashMap.put("isESP", true);
            return new EntityInfo(hashMap);
        }
        return null;
    }

    @Override
    public boolean enabled() {
        return SkyblockUtils.isInNether();
    }
}
