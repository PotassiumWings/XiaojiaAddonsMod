package com.xiaojia.xiaojiaaddons.Events;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

public class TickEndEvent extends Event {
    public static int[] timeConsumed = new int[1000];
    private static boolean owo = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            try {
//                MinecraftForge.EVENT_BUS.post(new TickEndEvent());
                Event tickEndEvent = new TickEndEvent();
                Field busIdField = EventBus.class.getDeclaredField("busID");
                busIdField.setAccessible(true);
                int busID = busIdField.getInt(MinecraftForge.EVENT_BUS);
                IEventListener[] listeners = tickEndEvent.getListenerList().getListeners(busID);
                int index = 0;
                long start = TimeUtils.curTime();
                for (; index < listeners.length; index++) {
                    int max = 1;
                    if (owo && !listeners[index].toString().matches(".*(HIGHEST|HIGH|NORMAL|LOW|LOWEST).*")) max = 100;
                    for (int i = 0; i < max; i++)
                        listeners[index].invoke(tickEndEvent);
                    long cur = TimeUtils.curTime();
                    timeConsumed[index] = (int) (cur - start);
                    start = cur;
                }
                if (owo) {
                    for (int i = 0; i < listeners.length; i++) {
                        ChatLib.chat(listeners[i].toString() + " " + timeConsumed[i]);
                    }
                    owo = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                owo = false;
            }
        }
    }

    public static void owo() {
        owo = !owo;
    }
}
