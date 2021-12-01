package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostQOL;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LayerCreeperCharge.class)
public abstract class MixinLayerCreeperCharge implements LayerRenderer<EntityCreeper> {

    ResourceLocation VISIBLE_CREEPER_ARMOR = new ResourceLocation(XiaojiaAddons.MODID, "creeper_armor.png");

    @ModifyArg(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderCreeper;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    private ResourceLocation modifyChargedCreeperLayer(ResourceLocation res) {
        if (!Checker.enabled) return res;
        if (!SkyblockUtils.isInMist() || Configs.VisibleGhost != GhostQOL.FILLED_OUTLINE_BOX) return res;
        return VISIBLE_CREEPER_ARMOR;
    }
}