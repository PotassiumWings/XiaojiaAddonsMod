package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.Color;
import java.util.HashMap;

public class ShadowAssassinESP extends RenderEntityESP {
    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return SkyblockUtils.isInDungeon() && Configs.ShadowAssassinESP;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityPlayer)) return null;
        String name = ChatLib.removeFormatting(entity.getCommandSenderEntity().getName());
        if (name.contains("Shadow Assassin")) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Color color = ColorUtils.realColors[Configs.ShadowAssassinESPColor];
            hashMap.put("r", color.getRed());
            hashMap.put("g", color.getGreen());
            hashMap.put("b", color.getBlue());
            hashMap.put("a", color.getAlpha());
            hashMap.put("entity", entity);
            hashMap.put("kind", "SA");
            hashMap.put("fontColor", 0);
            hashMap.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
            hashMap.put("width", 0.45F);
            hashMap.put("height", 1.9F);
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
