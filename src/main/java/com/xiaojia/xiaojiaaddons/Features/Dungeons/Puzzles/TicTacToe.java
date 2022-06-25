package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TicTacToe {
    private static Room room;

    public static void setRoom(Room room1) {
        room = room1;
    }

    @SubscribeEvent
    public void onRightClickButton(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.TicTacToeSolver) return;

    }
}
