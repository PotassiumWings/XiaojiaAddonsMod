package com.xiaojia.xiaojiaaddons.Mixins;

import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin({FMLHandshakeMessage.ModList.class})
public abstract class MixinModList {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "modList", at = @At("HEAD"), remap = false)
    private void removeMod(CallbackInfoReturnable ci) {
        this.modTags.remove("xiaojiaaddons");
    }
}
