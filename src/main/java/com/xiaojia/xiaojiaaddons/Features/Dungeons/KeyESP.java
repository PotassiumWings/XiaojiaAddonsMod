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

public class KeyESP extends RenderEntityESP {
    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return SkyblockUtils.isInDungeon() && Configs.KeyESPType != 0;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        String name = ChatLib.removeFormatting(entity.getName());
        if (entity instanceof EntityArmorStand && (name.contains("Wither Key") ||
                name.contains("Blood Key"))) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Color color = ColorUtils.realColors[Configs.WitherKeyColor];
            if (name.contains("Blood Key")) color = ColorUtils.realColors[Configs.BloodKeyColor];
            hashMap.put("r", color.getRed());
            hashMap.put("g", color.getGreen());
            hashMap.put("b", color.getBlue());
            hashMap.put("a", color.getAlpha());
            hashMap.put("entity", entity);
            hashMap.put("yOffset", -1.5F);
            hashMap.put("kind", "Key");
            hashMap.put("fontColor", 0);
            hashMap.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            hashMap.put("width", 0.4F);
            hashMap.put("height", 0.8F);
            hashMap.put("isESP", true);
            hashMap.put("isFilled", Configs.KeyESPType == 2);
            return new EntityInfo(hashMap);
        }
        return null;
    }
}
