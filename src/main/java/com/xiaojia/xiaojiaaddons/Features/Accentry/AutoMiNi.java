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
    private static final ArrayList<BlockPos> positions = new ArrayList<>(Arrays.asList(
            new BlockPos(323, 146, 201),
            new BlockPos(325, 146, 201),
            new BlockPos(329, 146, 204),
            new BlockPos(331, 146, 200),
            new BlockPos(329, 146, 197),
            new BlockPos(324, 146, 196)
    ));

    private static final ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>(Arrays.asList(
            new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
            new Pair<>(4, 5), new Pair<>(5, 0)
    ));

    @Override
    public ArrayList<BlockPos> getPositions() {
        return positions;
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
        return 1;
    }
}
