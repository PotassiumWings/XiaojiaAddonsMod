package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void onPacketSend(Packet packet, CallbackInfo ci) {
        try {
            if (MinecraftForge.EVENT_BUS.post(new PacketSendEvent(packet)))
                ci.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
