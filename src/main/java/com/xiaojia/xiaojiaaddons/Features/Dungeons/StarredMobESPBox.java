package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.awt.Color;
import java.util.HashMap;

public class StarredMobESPBox extends RenderEntityESP {
    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return SkyblockUtils.isInDungeon() && Configs.StarredMobESP == 2;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        String name = ChatLib.removeFormatting(entity.getName());
        if (entity instanceof EntityArmorStand && (name.contains("✯") ||
                name.contains("Shadow Assassin"))) {
            float height = 0;
            if (name.contains("Fel") || name.contains("Withermancer")) height = 2.8F;
            else height = 1.9F;
            HashMap<String, Object> hashMap = new HashMap<>();
            Color color = ColorUtils.realColors[Configs.StarredMobESPColor];
            hashMap.put("r", color.getRed());
            hashMap.put("g", color.getGreen());
            hashMap.put("b", color.getBlue());
            hashMap.put("a", color.getAlpha());
            hashMap.put("entity", entity);
            hashMap.put("yOffset", 1F);
            hashMap.put("kind", "Starred");
            hashMap.put("fontColor", 0);
            hashMap.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            hashMap.put("width", 0.45F);
            hashMap.put("height", height);
            hashMap.put("isESP", true);
            return new EntityInfo(hashMap);
        }
        return null;
    }
}
