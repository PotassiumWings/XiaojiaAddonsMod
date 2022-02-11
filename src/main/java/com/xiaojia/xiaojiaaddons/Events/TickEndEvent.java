package com.xiaojia.xiaojiaaddons.Events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickEndEvent extends Event {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            try {
                MinecraftForge.EVENT_BUS.post(new TickEndEvent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
