package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class SpongeESP {
    private final HashSet<BlockPos> sponges = new HashSet<>();
    private Thread scanThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SpongeESP) return;
        if (scanThread == null || !scanThread.isAlive()) {
            scanThread = new Thread(() -> {
                try {
                    int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));
                    for (int r = 0; r < 400; r++) {
                        for (int i = 0; i < 4; i++) {
                            for (int sy = 40; sy <= 130; sy += 2) {
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
        if (BlockUtils.getBlockAt(x, y, z) == Blocks.sponge) {
            synchronized (sponges) {
                sponges.add(new BlockPos(x, y, z));
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SpongeESP) return;
        synchronized (sponges) {
            for (BlockPos pos : sponges) {
                GuiUtils.enableESP();
                GuiUtils.drawBoxAtBlock(pos, new Color(255, 242, 82, 120), 1, 1, 0.0020000000949949026F);
                GuiUtils.disableESP();
            }
        }
    }

    @SubscribeEvent
    public void onReset(WorldEvent.Load event) {
        synchronized (sponges) {
            sponges.clear();
        }
    }
}
