package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.GuiContainerEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiContainer.class})
public abstract class MixinGuiContainer {

    @Inject(method = "drawSlot", at = @At("HEAD"), cancellable = true)
    private void onDrawSlot(Slot slot, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.DrawSlotEvent.Pre(slot))) ci.cancel();
    }

    @Inject(method = "drawSlot", at = @At("RETURN"), cancellable = true)
    private void onDrawSlotPost(Slot slot, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new GuiContainerEvent.DrawSlotEvent.Post(slot))) ci.cancel();
    }
}
