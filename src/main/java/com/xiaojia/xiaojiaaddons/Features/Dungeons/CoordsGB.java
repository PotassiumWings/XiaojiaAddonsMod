package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class CoordsGB {
    //    private static final KeyBind keyBind = new KeyBind("Coords Ghost Block", Keyboard.KEY_NONE);
    private final BlockPos[] blockCoords = new BlockPos[]{
            new BlockPos(54, 63, 80),
            new BlockPos(54, 62, 80),
            new BlockPos(54, 61, 80),
            new BlockPos(75, 220, 31),
            new BlockPos(75, 220, 32),
            new BlockPos(99, 168, 43),
            new BlockPos(99, 168, 44),
            new BlockPos(99, 168, 46),
            new BlockPos(99, 168, 47),
            new BlockPos(99, 168, 47),
            new BlockPos(100, 168, 47),
            new BlockPos(100, 168, 46),
            new BlockPos(100, 168, 44),
            new BlockPos(100, 168, 43),
            new BlockPos(98, 168, 47),
            new BlockPos(98, 168, 46),
            new BlockPos(98, 168, 44),
            new BlockPos(98, 168, 43),

            new BlockPos(87, 167, 41),
            new BlockPos(88, 167, 41),
            new BlockPos(89, 167, 41),
            new BlockPos(90, 167, 41),
            new BlockPos(91, 167, 41),
            new BlockPos(92, 167, 41),
            new BlockPos(93, 167, 41),
            new BlockPos(94, 167, 41),
            new BlockPos(95, 167, 41),
            new BlockPos(90, 167, 40),
            new BlockPos(91, 167, 40),
            new BlockPos(92, 167, 40),
            new BlockPos(93, 167, 40),
            new BlockPos(94, 167, 40),
            new BlockPos(95, 167, 40),
            new BlockPos(90, 166, 40),
            new BlockPos(91, 166, 40),
            new BlockPos(92, 166, 40),
            new BlockPos(93, 166, 40),
            new BlockPos(94, 166, 40),
            new BlockPos(95, 166, 40),
            new BlockPos(90, 166, 41),
            new BlockPos(91, 166, 41),
            new BlockPos(92, 166, 41),
            new BlockPos(93, 166, 41),
            new BlockPos(94, 166, 41),
            new BlockPos(95, 166, 41),
            new BlockPos(94, 165, 41),
            new BlockPos(94, 165, 40),
            new BlockPos(95, 165, 41),
            new BlockPos(95, 165, 40),
    };
//    private boolean should = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.CoordsGB) return;
//        if (keyBind.isPressed()) {
//            should = !should;
//            ChatLib.chat(should ? "Coords GB &aactivated" : "Coords GB &cdeactivated");
//        }
//        if (!should) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (Dungeon.floorInt != 7) return;
        if (getWorld() == null) return;
        for (BlockPos pos : blockCoords) {
            getWorld().setBlockToAir(pos);
        }
    }
}
