package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.util.HashMap;
import java.util.Map;

public class Voidgloom extends RenderEntityESP {
    private static final HashMap<String, Integer> kindColorMap = new HashMap<String, Integer>() {{
        put("Voidling Radical", 0xff00ff);
        put("Voidcrazed Maniac", 0xdc143c);
        put("Voidling Devotee", 0x7b68ee);
    }};

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return Configs.ShowEndermanMiniHP;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!Checker.enabled || !SkyblockUtils.isInEndIsland()) return null;
        if (!Configs.EndermanMiniESP) return null;
        if (!(entity instanceof EntityArmorStand) || entity.getName() == null) return null;
        for (Map.Entry<String, Integer> entry : kindColorMap.entrySet()) {
            String kind = entry.getKey();
            int fontColor = entry.getValue();
            if (entity.getName().contains(kind)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("entity", entity);
                hashMap.put("height", 3F);
                hashMap.put("yOffset", 1F);
                hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_ARMORSTAND_HP);
                hashMap.put("scale", 2F);
                hashMap.put("kind", kind);
                hashMap.put("fontColor", fontColor);
                return new EntityInfo(hashMap);
            }
        }
        return null;
    }
}
