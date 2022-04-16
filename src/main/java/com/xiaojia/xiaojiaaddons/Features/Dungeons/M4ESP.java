package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.awt.Color;
import java.util.HashMap;

public class M4ESP extends RenderEntityESP {
    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return SkyblockUtils.isInDungeon();
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        String name = ChatLib.removeFormatting(entity.getName());
        if (entity instanceof EntityArmorStand &&
                (name.contains("Spirit Bear") && Configs.SpiritBearESPType != 0 ||
                        name.contains("Spirit Bow") && Configs.SpiritBowESPType != 0)) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Color color;
            float height;
            float yOffset;
            boolean isFilled;
            if (name.contains("Spirit Bow")) {
                color = ColorUtils.realColors[Configs.SpiritBowColor];
                height = 0.8F;
                yOffset = -1.5F;
                isFilled = Configs.SpiritBowESPType == 2;
            } else {
                color = ColorUtils.realColors[Configs.SpiritBearColor];
                height = 2F;
                yOffset = 1F;
                isFilled = Configs.SpiritBearESPType == 2;
            }
            hashMap.put("r", color.getRed());
            hashMap.put("g", color.getGreen());
            hashMap.put("b", color.getBlue());
            hashMap.put("a", color.getAlpha());
            hashMap.put("entity", entity);
            hashMap.put("yOffset", yOffset);
            hashMap.put("kind", name);
            hashMap.put("fontColor", 0);
            hashMap.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            hashMap.put("width", 0.4F);
            hashMap.put("height", height);
            hashMap.put("isESP", true);
            hashMap.put("isFilled", isFilled);
            return new EntityInfo(hashMap);
        }
        return null;
    }

    public boolean enabled() {
        return SkyblockUtils.isInDungeon() && Dungeon.bossEntry > Dungeon.runStarted && Dungeon.floorInt == 4;
    }
}
