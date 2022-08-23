package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.AutoWalk;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

public class AutoMiNi extends AutoWalk {
    private static final ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>(Arrays.asList(
            new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
            new Pair<>(4, 5), new Pair<>(5, 6), new Pair<>(6, 7), new Pair<>(7, 8),
            new Pair<>(8, 9), new Pair<>(9, 10), new Pair<>(10, 11), new Pair<>(11, 0)
    ));
    public static BlockPos center = new BlockPos(326, 146, 200);

    @Override
    public ArrayList<BlockPos> getPositions() {
        return new ArrayList<>(Arrays.asList(
                center.add(-4, 0, -1),
                center.add(-3, 0, -3),
                center.add(-1, 0, -4),
                center.add(1, 0, -4),
                center.add(3, 0, -3),
                center.add(4, 0, -1),
                center.add(4, 0, 1),
                center.add(3, 0, 3),
                center.add(1, 0, 4),
                center.add(-1, 0, 4),
                center.add(-3, 0, 3),
                center.add(-4, 0, 1)
        ));
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getEdges() {
        return edges;
    }

    @Override
    public boolean enabled() {
        return Configs.AutoMiNi;
    }

    @Override
    public String getName() {
        return "Auto Mi Ni";
    }

    @Override
    public KeyBind getKeyBind() {
        return new KeyBind(getName(), Keyboard.KEY_NONE);
    }

    @Override
    public double getJudgeDistance() {
        return 2;
    }
}
