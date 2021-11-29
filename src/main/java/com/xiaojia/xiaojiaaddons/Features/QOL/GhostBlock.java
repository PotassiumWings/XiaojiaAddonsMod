package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class GhostBlock {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.GhostBlock) return;
        try {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (BlockUtils.isInteractive(mc.theWorld.getBlockState(event.pos).getBlock())) return;
                String heldItemName = ControlUtils.getHeldItemStack().getDisplayName();
                if (heldItemName.contains("Stonk") || heldItemName.contains("Pickaxe")) {
                    mc.theWorld.setBlockToAir(event.pos);
                    event.setCanceled(true);
                    if (XiaojiaAddons.isDebug()) ChatLib.chat("Created ghost block!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
