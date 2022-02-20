package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class CoordsGB {
//    private static final KeyBind keyBind = new KeyBind("Coords Ghost Block", Keyboard.KEY_NONE);
    private final BlockPos[] blockCoords = new BlockPos[]{
            new BlockPos(275, 220, 231),
            new BlockPos(275, 220, 232),
            new BlockPos(299, 168, 243),
            new BlockPos(299, 168, 244),
            new BlockPos(299, 168, 246),
            new BlockPos(299, 168, 247),
            new BlockPos(299, 168, 247),
            new BlockPos(300, 168, 247),
            new BlockPos(300, 168, 246),
            new BlockPos(300, 168, 244),
            new BlockPos(300, 168, 243),
            new BlockPos(298, 168, 247),
            new BlockPos(298, 168, 246),
            new BlockPos(298, 168, 244),
            new BlockPos(298, 168, 243),
            new BlockPos(287, 167, 240),
            new BlockPos(288, 167, 240),
            new BlockPos(289, 167, 240),
            new BlockPos(290, 167, 240),
            new BlockPos(291, 167, 240),
            new BlockPos(292, 167, 240),
            new BlockPos(293, 167, 240),
            new BlockPos(294, 167, 240),
            new BlockPos(295, 167, 240),
            new BlockPos(290, 167, 239),
            new BlockPos(291, 167, 239),
            new BlockPos(292, 167, 239),
            new BlockPos(293, 167, 239),
            new BlockPos(294, 167, 239),
            new BlockPos(295, 167, 239),
            new BlockPos(290, 166, 239),
            new BlockPos(291, 166, 239),
            new BlockPos(292, 166, 239),
            new BlockPos(293, 166, 239),
            new BlockPos(294, 166, 239),
            new BlockPos(295, 166, 239),
            new BlockPos(290, 166, 240),
            new BlockPos(291, 166, 240),
            new BlockPos(292, 166, 240),
            new BlockPos(293, 166, 240),
            new BlockPos(294, 166, 240),
            new BlockPos(295, 166, 240)
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
        for (BlockPos pos : blockCoords) {
            getWorld().setBlockToAir(pos);
        }
    }
}
