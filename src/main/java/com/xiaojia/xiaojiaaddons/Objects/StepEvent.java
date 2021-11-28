package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class StepEvent {
    private long fps = 60;
    private long systemTime;

    public StepEvent(long fps) {
        this.systemTime = Minecraft.getSystemTime();
        if (fps > 500 || fps <= 0) {
            System.out.println("Cannot create StepEvent with this fps, set fps to 60 as default.");
            return;
        }
        this.fps = fps;
    }

    public abstract void execute();

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        while (systemTime < Minecraft.getSystemTime() + 1000 / fps) {
            systemTime += 1000 / fps;
            execute();
        }
    }
}
