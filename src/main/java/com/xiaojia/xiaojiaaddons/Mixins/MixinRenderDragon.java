package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.M7Dragon;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderDragon.class)
public abstract class MixinRenderDragon extends RenderLiving<EntityDragon> {

    public MixinRenderDragon(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Inject(method = "renderModel(Lnet/minecraft/entity/boss/EntityDragon;FFFFFF)V", at = @At("HEAD"))
    private void onRenderModel(EntityDragon entity, float f, float g, float h, float i, float j, float scaleFactor, CallbackInfo ci) {
        if (Configs.RemoveDragonHurtRender)
            entity.hurtTime = 0;
    }

    @Inject(method = "getEntityTexture", at = @At("HEAD"), cancellable = true)
    private void replaceEntityTexture(EntityDragon entity, CallbackInfoReturnable<ResourceLocation> cir) {
        M7Dragon.getEntityTexture(entity, cir);
    }
}
