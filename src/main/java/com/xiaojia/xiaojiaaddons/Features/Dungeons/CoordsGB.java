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
            new BlockPos(54, 61, 80)
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
