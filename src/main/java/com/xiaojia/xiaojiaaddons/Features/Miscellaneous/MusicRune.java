package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class MusicRune {
    public static void play() {
        new Thread(() -> {
            int r = (int) (Math.random() * 10);
            try {
                getPlayer().playSound("note.harp", 300, r / 12F);
                Thread.sleep(80);
                getPlayer().playSound("note.harp", 300, (r + 3) / 12F);
                Thread.sleep(80);
                getPlayer().playSound("note.harp", 300, (r + 6) / 12F);
                Thread.sleep(80);
                getPlayer().playSound("note.harp", 300, (r + 9) / 12F);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
    }
}
