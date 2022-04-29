package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.M7Dragon;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.Velocity;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Shadow
    private WorldClient clientWorldController;

    @Inject(method = "handleEntityVelocity", cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"))
    public void handleEntityVelocity(S12PacketEntityVelocity packet, CallbackInfo ci) {
        if (!enabled()) return;
        Entity entity = getWorld().getEntityByID(packet.getEntityID());
        if (!(entity instanceof EntityPlayerSP)) return;
        double motionX = packet.getMotionX(), motionY = packet.getMotionY(), motionZ = packet.getMotionZ();
        double xz = Configs.VelocityXZ / 100F, y = Configs.VelocityY / 100F;
        if (Configs.VelocityXZ != 0 || Configs.VelocityY != 0) {
            entity.setVelocity(motionX * xz / 8000.0D, motionY * y / 8000.0D, motionZ * xz / 8000.0D);
        }
        ci.cancel();
    }

    @Inject(method = "func_147283_a", cancellable = true, remap = false,
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/Explosion;doExplosionB(B)V"))
    public void handleExplosion(S27PacketExplosion packet, CallbackInfo ci) {
        EntityPlayer player = getPlayer();
        if (player == null || !enabled()) return;
        if (Configs.VelocityXZ != 0 || Configs.VelocityY != 0) {
            double xz = Configs.VelocityXZ / 100F, y = Configs.VelocityY / 100F;
            player.motionX += packet.func_149149_c() * xz;
            player.motionY += packet.func_149144_d() * y;
            player.motionZ += packet.func_149147_e() * xz;
        }
        ci.cancel();
    }

    private boolean enabled() {
        return Checker.enabled && Velocity.canDisableKnockBack() && SkyblockUtils.isInSkyblock();
    }

    @Inject(method = "handleSpawnMob", at = @At("TAIL"))
    public void onHandleSpawnMobTail(S0FPacketSpawnMob packet, CallbackInfo ci) {
        Entity entity = clientWorldController.getEntityByID(packet.getEntityID());
        if (entity instanceof EntityDragon) {
            M7Dragon.onSpawnDragon((EntityDragon) entity);
        }
    }

    @Inject(method = "addToSendQueue", at = @At("HEAD"))
    public void onPacketSend(Packet packet, CallbackInfo ci) {
        try {
            MinecraftForge.EVENT_BUS.post(new PacketSendEvent(packet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
