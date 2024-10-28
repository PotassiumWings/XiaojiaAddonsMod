package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
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
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public abstract class BlockESP {
    private static long lastCheck = 0;
    public final Block block = getBlock();
    private final HashSet<BlockPos> blocks = new HashSet<>();
    private final Color color = getColor();
    private Thread scanThread = null;

    public abstract Block getBlock();

    public abstract Color getColor();

    public abstract boolean isEnabled();

    public int getYMin() {
        return 40;
    }

    public int getYMax() {
        return 240;
    }

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
                            for (int sy = getYMin(); sy <= getYMax(); sy++) {
                                if (!isEnabled()) return;
                                int cx = MathUtils.floor(getX(getPlayer())), cz = MathUtils.floor(getZ(getPlayer()));
                                if (Math.abs(cx - x) > 10 || Math.abs(cz - z) > 10) return;
                                for (int sx = x - r; sx <= x + r; sx++) {
                                    deal(sx, sy, z - r);
                                    deal(sx, sy, z + r);
                                }
                                for (int sz = z - r; sz <= z + r; sz++) {
                                    deal(x - r, sy, sz);
                                    deal(x + r, sy, sz);
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

    @SubscribeEvent
    public void onTickCheck(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (getWorld() == null) return;
        if (getPlayer() == null) return;
        if (TimeUtils.curTime() - lastCheck > 1000) {
            lastCheck = TimeUtils.curTime();
            synchronized (blocks) {
                blocks.removeIf(pos -> getPlayer().getDistanceSq(pos) < 100 * 100 && !check(pos.getX(), pos.getY(), pos.getZ()));
            }
        }
    }


    public void deal(int x, int y, int z) {
        if (check(x, y, z)) {
            synchronized (blocks) {
                blocks.add(new BlockPos(x, y, z));
            }
        } else if (blocks.contains(new BlockPos(x, y, z))) {
            synchronized (blocks) {
                blocks.remove(new BlockPos(x, y, z));
            }
        }
    }

    public boolean check(int x, int y, int z) {
        return BlockUtils.getBlockAt(x, y, z) == block;
    }

    public boolean enableESP() {
        return true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!isEnabled()) return;
        synchronized (blocks) {
            for (BlockPos pos : blocks) {
                if (enableESP())
                    GuiUtils.enableESP();
                GuiUtils.drawBoxAtBlock(pos, color, 1, 1, 0.0020000000949949026F);
                if (enableESP())
                    GuiUtils.disableESP();
            }
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (event.oldBlock.getBlock() == block && event.newBlock.getBlock() != block) {
            synchronized (blocks) {
                blocks.remove(event.position);
            }
        }
        if (event.oldBlock.getBlock() != block && event.newBlock.getBlock() == block) {
            synchronized (blocks) {
                blocks.add(event.position);
            }
        }
    }

    @SubscribeEvent
    public void onReset(WorldEvent.Load event) {
        synchronized (blocks) {
            blocks.clear();
        }
    }
}
