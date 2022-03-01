package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;


import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class NoRotate {
    private boolean doneLoadingTerrain;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPosLookPacket(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!(event.packet instanceof S08PacketPlayerPosLook)) return;
        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.packet;
//        ChatLib.chat("Received " + PacketUtils.getPosLookPacket(packet));
        EntityPlayer player = getPlayer();
        if (!Configs.NoRotate || player == null) return;
        this.doneLoadingTerrain = true;
        // 0 pitch
        if (packet.getPitch() == 0.0D) return;

        event.setCanceled(true);
        double x = packet.getX(), y = packet.getY(), z = packet.getZ();
        float yaw = packet.getYaw(), pitch = packet.getPitch();
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) x += player.posX;
        else player.motionX = 0.0D;
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) y += player.posY;
        else player.motionY = 0.0D;
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) z += player.posZ;
        else player.motionZ = 0.0D;

        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) pitch += player.rotationPitch;
        if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) yaw += player.rotationYaw;
        player.setPositionAndRotation(x, y, z, player.rotationYaw, player.rotationPitch);
        C03PacketPlayer.C06PacketPlayerPosLook outPacket = new C03PacketPlayer.C06PacketPlayerPosLook(
                player.posX, (player.getEntityBoundingBox()).minY, player.posZ,
                yaw, pitch, false
        );
//        ChatLib.chat(String.format("Sent packet: %.2f %.2f; %.2f %.2f %.2f",
//                outPacket.getYaw(), outPacket.getPitch(),
//                outPacket.getPositionX(), outPacket.getPositionY(),outPacket.getPositionZ()
//        ));
        mc.getNetHandler().getNetworkManager().sendPacket(outPacket);
        if (!this.doneLoadingTerrain) {
            player.prevPosX = player.posX;
            player.prevPosY = player.posY;
            player.prevPosZ = player.posZ;
            player.closeScreen();
        }
    }


    @SubscribeEvent
    public void onRespawnPacket(PacketReceivedEvent event) {
        if (event.packet instanceof net.minecraft.network.play.server.S07PacketRespawn) {
//            ChatLib.chat("Received respawn packet!");
            this.doneLoadingTerrain = false;
        }
    }
}
