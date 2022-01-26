package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class TrapChestESP {

    private final HashSet<BlockPos> chests = new HashSet<>();
    private final HashSet<BlockPos> checked = new HashSet<>();
    private BlockPos lastChecked = null;
    private boolean isScanning = false;

    private static boolean isEnabled() {
        return (Checker.enabled && getPlayer() != null && getWorld() != null &&
                Configs.TrapChestESP && SkyblockUtils.isInDungeon());
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (isEnabled() && !this.isScanning &&
                (this.lastChecked == null || !this.lastChecked.equals(getPlayer().getPosition()))) {
            this.isScanning = true;
            (new Thread(() -> {
                BlockPos playerPosition = getPlayer().getPosition();
                this.lastChecked = playerPosition;
                for (int x = playerPosition.getX() - 15; x < playerPosition.getX() + 15; x++)
                    for (int y = playerPosition.getY() - 15; y < playerPosition.getY() + 15; y++)
                        for (int z = playerPosition.getZ() - 15; z < playerPosition.getZ() + 15; z++) {
                            BlockPos position = new BlockPos(x, y, z);
                            if (!this.checked.contains(position) && BlockUtils.getBlockAt(position) == Blocks.trapped_chest)
                                synchronized (chests) {
                                    chests.add(position);
                                }
                        }
                this.isScanning = false;
            })).start();
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (event.newBlock.getBlock() == Blocks.air)
            this.chests.remove(event.position);
        if (event.oldBlock.getBlock() == Blocks.air) {
            if (event.newBlock.getBlock() == Blocks.trapped_chest)
                this.chests.add(event.position);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (isEnabled())
            for (BlockPos blockPos : this.chests) {
                Color color = new Color(255, 0, 0);
                GuiUtils.enableESP();
                GuiUtils.drawBoxAtBlock(
                        blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
                        1, 1, 0F
                );
                GuiUtils.disableESP();
            }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        this.chests.clear();
        this.checked.clear();
        this.lastChecked = null;
    }
}
