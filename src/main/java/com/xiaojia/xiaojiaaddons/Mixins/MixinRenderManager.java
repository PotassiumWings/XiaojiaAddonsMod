package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.CheckEntityRenderEvent;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftForge.EVENT_BUS.post(new CheckEntityRenderEvent(entityIn, camera, camX, camY, camZ))) {
            cir.setReturnValue(false);
        }
    }
}
