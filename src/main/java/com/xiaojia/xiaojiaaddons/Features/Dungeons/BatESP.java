package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;

import java.util.HashMap;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;

public class BatESP extends RenderEntityESP {

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return Checker.enabled && (
                Configs.BatESPDungeons && SkyblockUtils.isInDungeon() ||
                        Configs.BatESPOutDungeons && !SkyblockUtils.isInDungeon()
        );
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!Checker.enabled) return null;
        if (!(entity instanceof EntityBat) || entity.isInvisible()) return null;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entity", entity);
        if (MathUtils.distanceSquareFromPlayer(getX(entity), getY(entity), getZ(entity)) < 144) {
            hashMap.put("r", 185);
            hashMap.put("g", 65);
            hashMap.put("b", 65);
        }
        hashMap.put("width", 0.4F);
        hashMap.put("height", 0.8F);
        hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
        hashMap.put("kind", "Bat");
        hashMap.put("fontColor", 0x33ff33);
        hashMap.put("isESP", true);
        hashMap.put("isFilled", true);
        return new EntityInfo(hashMap);
    }
}
