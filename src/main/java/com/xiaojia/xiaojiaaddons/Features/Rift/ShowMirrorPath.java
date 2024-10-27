package com.xiaojia.xiaojiaaddons.Features.Rift;


import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class ShowMirrorPath {
    private static boolean isEnabled() {
        return (Checker.enabled && getPlayer() != null && getWorld() != null &&
                Configs.ShowMirrorPath && SkyblockUtils.isInRift());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!isEnabled()) return;
        BlockPos pos = getPlayer().getPosition();
        if (pos.getX() > -120 || pos.getY() < 35 || pos.getY() > 46 || pos.getZ() > -90 || pos.getZ() < -124) return;
        for (int x = -120; x >= -300; x--) {
            for (int y = 46; y <= 57; y++) {
                for (int z = -90; z >= -124; z--) {
                    BlockPos position = new BlockPos(x, y, z);
                    if (getWorld().getBlockState(position).getBlock() instanceof BlockPlanks) {
                        GuiUtils.drawBoxAtBlock(
                                x, 92 - y, z,
                                157, 249, 32, 90,
                                1, 1, 0.01F
                        );
                    }
                }
            }
        }
    }

}
