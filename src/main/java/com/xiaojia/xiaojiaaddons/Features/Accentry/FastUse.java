package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.PacketUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class FastUse {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.FastUse) return;
        EntityPlayer player = getPlayer();
        if (player == null) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;

        ItemStack held = ControlUtils.getHeldItemStack();
        if (held != null && held.getItem() instanceof ItemFood) {
            event.setCanceled(true);
            XiaojiaAddons.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(held));

        }
    }
}
