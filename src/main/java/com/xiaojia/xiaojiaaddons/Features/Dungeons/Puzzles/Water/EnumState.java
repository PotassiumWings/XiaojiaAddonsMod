package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

public enum EnumState {
    // block, empty
    B, E,
    // coal, gold, quartz, diamond, emerald, clay (only use for calculating their positions)
    c, g, q, d, e, cl,
    // converted ones, blocks
    cc, cg, cq, cd, ce, ccl,
    // water, and 6 forms of water
    w, w1, w2, w3, w4, w5, w6
}
