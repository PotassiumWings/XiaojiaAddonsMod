package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.awt.Color;
import java.util.HashMap;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;

public class AshFangESP extends RenderEntityESP {
    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return !entityInfo.getKind().equals("");
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return !entityInfo.getKind().equals("");
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        String name = ChatLib.removeFormatting(entity.getDisplayName().getFormattedText());
        if (entity instanceof EntityArmorStand &&
                (name.contains("Ashfang Follower") && Configs.AshfangFollowerESP ||
                        name.contains("Ashfang Acolyte") && Configs.AshfangAcolyteESP ||
                        name.contains("Ashfang Underling") && Configs.AshfangUnderlingESP ||
                        name.contains("Blazing Soul") && Configs.BlazingSoulESP)) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Color color = null;
            String kind = "";
            if (name.contains("Ashfang Follower")) {
                color = ColorUtils.getColorFromCode("&8");
                kind = "Ashfang Follower";
            } else if (name.contains("Ashfang Acolyte")) {
                color = ColorUtils.getColorFromCode("&9");
                kind = "Ashfang Acolyte";
            } else if (name.contains("Ashfang Underling")) {
                color = ColorUtils.getColorFromCode("&c");
                kind = "Ashfang Underling";
            } else {
                color = new Color(255, 0, 0);
            }
            if (color == null) return null;

            hashMap.put("r", color.getRed());
            hashMap.put("g", color.getGreen());
            hashMap.put("b", color.getBlue());
            hashMap.put("a", 120);
            hashMap.put("entity", entity);
            hashMap.put("yOffset", 1F);
            hashMap.put("kind", kind);
            hashMap.put("fontColor", color.getRGB());
            hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
            hashMap.put("width", 0.4F);
            hashMap.put("height", 2F);
            hashMap.put("isESP", true);
            hashMap.put("isFilled", true);
            return new EntityInfo(hashMap);
        }
        return null;
    }

    public void dealWithEntityInfo(EntityInfo entityInfo) {
        if (!entityInfo.getKind().equals("")) return;
        Entity entity = entityInfo.getEntity();
        float x = getX(entity);
        float y = getY(entity);
        float z = getZ(entity);
        GuiUtils.drawLine(x, y, z, -484.5F, 137.5F, -1015.5F, new Color(255, 0, 0), 2);
    }

    public boolean enabled() {
        return SkyblockUtils.isInAshFang();
    }
}
