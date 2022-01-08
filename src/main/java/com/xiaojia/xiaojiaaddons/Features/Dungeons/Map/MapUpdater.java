package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapUpdater {
    private long lastMakeMap = 0;
    private long lastUpdate = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (TimeUtils.curTime() - lastMakeMap >= 500 && Configs.MapEnabled &&
                Dungeon.isInDungeon && Dungeon.isFullyScanned) {
            lastMakeMap = TimeUtils.curTime();
            new Thread(Dungeon::makeMap).start();
        }
        if (TimeUtils.curTime() - lastUpdate >= 200 && Configs.MapEnabled &&
                Dungeon.isInDungeon) {
            lastUpdate = TimeUtils.curTime();
            new Thread(() -> {
                try {
                    Dungeon.updatePlayers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Dungeon.updateRooms();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Dungeon.updateDoors();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
