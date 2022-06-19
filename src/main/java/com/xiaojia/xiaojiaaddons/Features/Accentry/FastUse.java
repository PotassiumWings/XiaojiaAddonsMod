package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.UpdateEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class FastUse {
    @SubscribeEvent()
    public void onRightClick(UpdateEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.FastUse) return;
        EntityPlayer player = getPlayer();
        if (player == null) return;

        ItemStack held = player.getItemInUse();
        if (held != null && held.getItem() instanceof ItemFood) {
            for (int i = 0; i < 35; i++) {
                XiaojiaAddons.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(getPlayer().onGround));
            }
            player.stopUsingItem();
            ChatLib.debug("Fast Use!");
        }
    }
}
