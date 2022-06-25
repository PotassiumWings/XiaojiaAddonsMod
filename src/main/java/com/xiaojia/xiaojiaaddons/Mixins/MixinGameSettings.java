package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.CommandKeybind;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public class MixinGameSettings {
    @Inject(method = {"setOptionKeyBinding"}, at = @At("HEAD"))
    public void setOptionKeyBinding(KeyBinding p_151440_1_, int p_151440_2_, CallbackInfo ci) {
        CommandKeybind.saveKeybinds();
    }
}
