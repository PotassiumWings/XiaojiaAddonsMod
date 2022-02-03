package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    protected DataWatcher dataWatcher;

    private String lastString = "";
    private String lastResult = "";

    @Inject(method = "getCustomNameTag", at = @At("HEAD"), cancellable = true)
    public void getCustomString(CallbackInfoReturnable<String> ci) {
        String str = this.dataWatcher.getWatchableObjectString(2);
        if (!Checker.enabled || !Configs.ColorNameNameTag) return;
        if (str.equals(lastString)) ci.setReturnValue(lastResult);
        else {
            String result = ColorName.addColorNameWithPrefix(str);
            lastResult = result;
            lastString = str;
            ci.setReturnValue(result);
        }
    }
}
