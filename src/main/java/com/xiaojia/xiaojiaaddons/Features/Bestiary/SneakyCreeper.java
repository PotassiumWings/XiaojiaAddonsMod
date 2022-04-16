package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;

import java.util.HashMap;

public class SneakyCreeper extends RenderEntityESP {
    private static final String SNEAKYCREEPER_STRING = "Creeper";

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return Configs.SneakyCreeperDisplayBox;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return Configs.SneakyCreeperDisplayName;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityCreeper)) return null;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entity", entity);
        hashMap.put("width", 0.4F);
        hashMap.put("height", 1.85F);
        hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
        hashMap.put("kind", SNEAKYCREEPER_STRING);
        hashMap.put("fontColor", 0x33ff33);
        return new EntityInfo(hashMap);
    }

    @Override
    public boolean enabled() {
        return SkyblockUtils.isInGunpowderMines();
    }
}
