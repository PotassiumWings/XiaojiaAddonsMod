package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import net.minecraft.block.Block;

import java.util.ArrayList;

public class MapUtils {
    public static final String[] colors = new String[]{
            "&a", "&b", "&c", "&d", "&e", "&f",
            "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9"
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

    public static boolean includes(ArrayList<Integer> arr, int x) {
        for (int y : arr) if (x == y) return true;
        return false;
    }

    public static boolean includes(String[] arr, String x) {
        for (String y : arr) if (x.equals(y)) return true;
        return false;
    }
}
