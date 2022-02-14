package com.xiaojia.xiaojiaaddons.Features.Slayers;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.util.HashMap;
import java.util.Map;

public class Revenant extends RenderEntityESP {
    private static final HashMap<String, Integer> kindColorMap = new HashMap<String, Integer>() {{
        put("Revenant Sycophant", 0x7b68ee);
        put("Revenant Champion", 0xff00ff);
        put("Deformed Revenant", 0xff00ff);
        put("Atoned Champion", 0xdc143c);
        put("Atoned Revenant", 0xdc143c);
        put("Revenant Horror", 0xdc143c);
        put("Atoned Horror", 0xdc143c);
    }};

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return Configs.ShowRevMiniHP;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!Checker.enabled) return null;
        if (!Configs.RevMiniESP) return null;
        if (!(entity instanceof EntityArmorStand) || entity.getName() == null) return null;
        for (Map.Entry<String, Integer> entry : kindColorMap.entrySet()) {
            String kind = entry.getKey();
            int fontColor = entry.getValue();
            if (entity.getName().contains(kind)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("entity", entity);
                hashMap.put("height", 2F);
                hashMap.put("yOffset", 1F);
                hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_ARMORSTAND_HP);
                hashMap.put("scale", 2F);
                hashMap.put("kind", kind);
                hashMap.put("isESP", true);
                hashMap.put("fontColor", fontColor);
                return new EntityInfo(hashMap);
            }
        }
        return null;
    }
}
