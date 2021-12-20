package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import java.awt.Color;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class MapUtils {
    public static final String[] colors = new String[]{
            "&a", "&b", "&c", "&d", "&e", "&f",
            "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9"
    };
    public static final Color[] realColors = new Color[]{
            new Color(0x55FF55),
            new Color(0x55FFFF),
            new Color(0xFF5555),
            new Color(0xFF55FF),
            new Color(0xFFFF55),
            new Color(0xFFFFFF),
            new Color(0x000000),
            new Color(0x0000AA),
            new Color(0x00AA00),
            new Color(0x00AAAA),
            new Color(0xAA0000),
            new Color(0xAA00AA),
            new Color(0xFFAA00),
            new Color(0xAAAAAA),
            new Color(0x555555),
            new Color(0x5555FF)
    };

    public static boolean isColumnAir(int x, int z) {
        for (int y = 140; y > 11; y--) {
            Block block = BlockUtils.getBlockAt(x, y, z);
            if (block != null && Block.getIdFromBlock(block) != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDoor(int x, int z) {
        return (isColumnAir(x + 4, z) && isColumnAir(x - 4, z) && !isColumnAir(x, z + 4) && !isColumnAir(x, z - 4))
                || (!isColumnAir(x + 4, z) && !isColumnAir(x - 4, z) && isColumnAir(x, z + 4) && isColumnAir(x, z - 4));
    }

    public static boolean isBetween(int a, int b, int c) {
        return (a - b) * (a - c) <= 0;
    }

    public static boolean chunkLoaded(Vector3i coords) {
        return getWorld().getChunkFromBlockCoords(
                new BlockPos(coords.x, coords.y, coords.z)
        ).isLoaded();
    }

    public static boolean includes(ArrayList<Integer> arr, int x) {
        for (int y : arr) if (x == y) return true;
        return false;
    }

    public static boolean includes(String[] arr, String x) {
        for (String y : arr) if (x.equals(y)) return true;
        return false;
    }
}
