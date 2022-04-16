package com.xiaojia.xiaojiaaddons.Tests;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class CopyWorldInfo {
    public static int x, y, z;
    public static int dx, dy, dz;
    public static ArrayList<IBlockState> blockStates;

    public static void copy(String xs, String ys, String zs, String dxs, String dys, String dzs) {
        blockStates = new ArrayList<>();
        new Thread(() -> {
            x = Integer.parseInt(xs);
            y = Integer.parseInt(ys);
            z = Integer.parseInt(zs);
            dx = Integer.parseInt(dxs);
            dy = Integer.parseInt(dys);
            dz = Integer.parseInt(dzs);
            for (int i = x; i <= x + dx; i++) {
                for (int j = y; j <= y + dy; j++) {
                    for (int k = z; k <= z + dz; k++) {
                        blockStates.add(BlockUtils.getBlockStateAt(new BlockPos(i, j, k)));
                    }
                }
            }
            ChatLib.chat(String.format("Successfully copied blocks from %d %d %d to %d %d %d (%d blocks).",
                    x, y, z, x + dx, y + dy, z + dz, dx * dy * dz));
        }).start();
    }

    public static void paste() {
        new Thread(() -> {
            int index = 0;
            ChatLib.say(String.format("/fill %d %d %d %d %d %d air", x, y, z, x + dx, y + dy, z + dz));
            for (int i = x; i <= x + dx; i++) {
                for (int j = y; j <= y + dy; j++) {
                    for (int k = z; k <= z + dz; k++) {
                        if (blockStates.get(index).getBlock() != Blocks.air)
                            ChatLib.say(String.format("/setblock %d %d %d %s %d", i, j, k,
                                    Block.blockRegistry.getNameForObject(blockStates.get(index).getBlock()),
                                    blockStates.get(index).getBlock().getMetaFromState(blockStates.get(index))));
//                        XiaojiaAddons.mc.thePlayer.getEntityWorld().setBlockState(new BlockPos(i, j, k), blockStates.get(index));
                        index++;
                    }
                }
            }
            ChatLib.chat(String.format("Successfully pasted blocks from %d %d %d to %d %d %d (%d blocks).",
                    x, y, z, x + dx, y + dy, z + dz, dx * dy * dz));
        }).start();
    }
}
