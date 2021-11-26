package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GhostBlock {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static EntityPlayerSP player = mc.thePlayer;

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (false) return;
        try {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (BlockUtils.isInteractive(mc.theWorld.getBlockState(event.pos).getBlock())) return;
                String heldItemName = ControlUtils.getHeldItemStack().getDisplayName();
                if (heldItemName.contains("Stonk") || heldItemName.contains("Pickaxe")) {
                    mc.theWorld.setBlockToAir(event.pos);
                    event.setCanceled(true);
                    ChatLib.chat("Created ghost block!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
