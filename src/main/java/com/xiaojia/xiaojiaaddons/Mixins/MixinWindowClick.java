package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.WindowClickEvent;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinWindowClick {
    @Inject(method = "windowClick", at = @At("HEAD"), cancellable = true)
    private void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn,
                             CallbackInfoReturnable<ItemStack> cir) {
        if (XiaojiaAddons.isDebug())
            ChatLib.chat(String.format("windowClick %d %d %d %d", windowId, slotId, mouseButtonClicked, mode));
        if (MinecraftForge.EVENT_BUS.post(new WindowClickEvent(windowId, slotId, mouseButtonClicked, mode)))
            cir.setReturnValue(null);
    }
}
