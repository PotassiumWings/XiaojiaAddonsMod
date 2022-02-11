package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class NearbyChestESP {
    private final HashSet<BlockPos> checked = new HashSet<>();
    private final HashSet<BlockPos> chests = new HashSet<>();

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!isEnabled()) return;
        EntityPlayer player = getPlayer();
        if (player == null) return;
        BlockPos pos = player.getPosition();
        for (int x = pos.getX() - 10; x <= pos.getX() + 10; x++) {
            for (int y = pos.getY() - 10; y <= pos.getY() + 10; y++) {
                for (int z = pos.getZ() - 10; z <= pos.getZ() + 10; z++) {
                    BlockPos position = new BlockPos(x, y, z);
                    if (!checked.contains(position) && !getWorld().isAirBlock(position)) {
                        if (getWorld().getBlockState(position).getBlock() == Blocks.chest) {
                            chests.add(position);
                        }
                    }
                    checked.add(position);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!isEnabled()) return;
        chests.removeIf(pos -> getWorld().getBlockState(pos).getBlock() != Blocks.chest);
        for (BlockPos pos : chests) {
            if (MathUtils.distanceSquareFromPlayer(pos) > 1000) continue;
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(
                    pos.getX(), pos.getY(), pos.getZ(),
                    185, 65, 65, 100, 1, 1, 0F
            );
            GuiUtils.disableESP();
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (!Checker.enabled) return;
        if (!isEnabled()) return;
        if (event.oldBlock.getBlock() == Blocks.chest && event.newBlock.getBlock() == Blocks.air) {
            chests.remove(event.position);
        }
        if (event.oldBlock.getBlock() != Blocks.chest && event.newBlock.getBlock() == Blocks.chest) {
            chests.add(event.position);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        chests.clear();
        checked.clear();
    }

    private boolean isEnabled() {
        return SkyblockUtils.isInCrystalHollows() && Configs.ChestESPCrystalHollows;
    }
}
