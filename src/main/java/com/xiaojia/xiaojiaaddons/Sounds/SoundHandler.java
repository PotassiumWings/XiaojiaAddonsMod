package com.xiaojia.xiaojiaaddons.Sounds;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class SoundHandler {
    public static final ArrayList<Sound> playing = new ArrayList<>();

    public static void playSound(Sound sound) {
        synchronized (playing) {
            playing.add(sound);
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        synchronized (playing) {
            for (Sound sound : playing) {
                XiaojiaAddons.mc.getSoundHandler().playSound(sound.sound);
            }
            playing.clear();
        }
    }
}
