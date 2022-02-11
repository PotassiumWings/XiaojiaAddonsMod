package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class GolemAlert {
    private static long golemSpawnTime = 0;
    private final Display display = new Display();

    public GolemAlert() {
        display.setShouldRender(false);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("center");
    }

    public static void golemWarn() {
        golemSpawnTime = TimeUtils.curTime() + 20000;
        try {
            getPlayer().playSound("mob.irongolem.hit", 1000, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onReceive(ClientChatReceivedEvent event) {
        if (event.message.getUnformattedText().equals("The ground begins to shake as an Endstone Protector rises from below!")) {
            golemWarn();
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.GolemAlert) return;
        display.clearLines();
        display.setShouldRender(false);
        if (golemSpawnTime < TimeUtils.curTime()) return;
        long curTime = TimeUtils.curTime();
        long millisecondsRemaining = golemSpawnTime - curTime;
        display.setRenderLoc(RenderUtils.getScreenWidth() / 2, RenderUtils.getScreenHeight() / 2 - 40);
        display.setShouldRender(true);
        DisplayLine line1 = new DisplayLine("&fGolem Alert!");
        line1.setShadow(true);
        line1.setScale(1.51F);
        display.setLine(0, line1);

        DisplayLine line2 = new DisplayLine("&c" + makeTimer(millisecondsRemaining));
        line2.setShadow(true);
        line2.setScale(1.50F);
        display.setLine(1, line2);
    }

    private String makeTimer(long ms) {
        long seconds = ms / 1000;
        long milli = ms % 1000;
        return String.format("%02d:%03d", seconds, milli);
    }
}
