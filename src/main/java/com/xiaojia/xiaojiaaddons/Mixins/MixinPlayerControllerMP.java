package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "getIsHittingBlock", at = @At("HEAD"), cancellable = true)
    public void onReset(CallbackInfoReturnable<Boolean> ci) {
        if (Configs.OldBlockBreak)
            ci.setReturnValue(false);
    }
}
