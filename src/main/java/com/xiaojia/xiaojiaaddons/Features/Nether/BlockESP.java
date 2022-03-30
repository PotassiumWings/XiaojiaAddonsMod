package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public abstract class BlockESP {
    private final HashSet<BlockPos> blocks = new HashSet<>();
    private final Block block = getBlock();
    private final Color color = getColor();
    private Thread scanThread = null;

    public abstract Block getBlock();

    public abstract Color getColor();

    public abstract boolean isEnabled();

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!isEnabled()) return;
        if (scanThread == null || !scanThread.isAlive()) {
            scanThread = new Thread(() -> {
                try {
                    int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));
                    for (int r = 0; r < 400; r++) {
                        for (int i = 0; i < 4; i++) {
                            for (int sy = 40; sy <= 240; sy++) {
                                int cx = MathUtils.floor(getX(getPlayer())), cz = MathUtils.floor(getZ(getPlayer()));
                                if (Math.abs(cx - x) > 10 || Math.abs(cz - z) > 10) return;
                                for (int sx = x - r; sx <= x + r; sx++) {
                                    check(sx, sy, z - r);
                                    check(sx, sy, z + r);
                                }
                                for (int sz = z - r; sz <= z + r; sz++) {
                                    check(x - r, sy, sz);
                                    check(x + r, sy, sz);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            scanThread.start();
        }
    }

    private void check(int x, int y, int z) {
        if (BlockUtils.getBlockAt(x, y, z) == block) {
            synchronized (blocks) {
                blocks.add(new BlockPos(x, y, z));
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (isEnabled()) return;
        synchronized (blocks) {
            for (BlockPos pos : blocks) {
                GuiUtils.enableESP();
                GuiUtils.drawBoxAtBlock(pos, color, 1, 1, 0.0020000000949949026F);
                GuiUtils.disableESP();
            }
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (event.oldBlock.getBlock() == block && event.newBlock.getBlock() != block) {
            blocks.remove(event.position);
        }
    }

    @SubscribeEvent
    public void onReset(WorldEvent.Load event) {
        synchronized (blocks) {
            blocks.clear();
        }
    }
}
