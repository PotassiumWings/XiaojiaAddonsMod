package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NetUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class NoSlowdown {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInSkyblock()) return;
        try {
            ItemStack itemStack = ControlUtils.getHeldItemStack();
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR &&
                    itemStack != null &&
                    itemStack.getItem().getRegistryName().toLowerCase().contains("sword")) {
                String name = itemStack.getDisplayName();
                if (!Configs.NoSlowdownAll) {
                    boolean canCancel = false;
                    if (Configs.NoSlowdownRogue && name.contains("Rogue")) canCancel = true;
                    if (Configs.NoSlowdownWitherBlade && (name.contains("Hyperion") || name.contains("Astraea") ||
                            name.contains("Scylla") || name.contains("Valkyrie"))) canCancel = true;
                    if (Configs.NoSlowdownKatana && name.contains("Katana")) canCancel = true;
                    if (!canCancel) return;
                }
                event.setCanceled(true);
                if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(
                            new BlockPos(-1, -1, -1),
                            255,
                            itemStack,
                            0, 0, 0)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
