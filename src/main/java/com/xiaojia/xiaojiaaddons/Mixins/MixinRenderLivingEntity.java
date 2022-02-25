package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.RenderEntityModelEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

@Mixin({RendererLivingEntity.class})
public abstract class MixinRenderLivingEntity {
    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    private void renderModel(EntityLivingBase entity,
                             float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw,
                             float headPitch, float scaleFactor,
                             CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post(new RenderEntityModelEvent(
                entity, limbSwing, limbSwingAmount, ageInTicks,
                netHeadYaw, headPitch, scaleFactor, this.mainModel)))
            callbackInfo.cancel();
    }

    @Inject(method = "canRenderName", at = @At("HEAD"), cancellable = true)
    public void canRenderName(EntityLivingBase base, CallbackInfoReturnable<Boolean> cir) {
        if (Configs.RenderSelfName && base.getName().equals(getPlayer().getName())) {
            cir.setReturnValue(true);
        }
    }
}
