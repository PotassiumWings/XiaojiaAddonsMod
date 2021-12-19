package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapUpdater {
    private long lastMakeMap = 0;
    private long lastUpdate = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (TimeUtils.curTime() - lastMakeMap >= 500) {
            lastMakeMap = TimeUtils.curTime();
            new Thread(Dungeon::makeMap).start();
        }
        if (TimeUtils.curTime() - lastUpdate >= 200) {
            lastUpdate = TimeUtils.curTime();
            new Thread(Dungeon::updatePlayers).start();
            new Thread(Dungeon::updateRooms).start();
            new Thread(Dungeon::updateDoors).start();
        }
    }
}
