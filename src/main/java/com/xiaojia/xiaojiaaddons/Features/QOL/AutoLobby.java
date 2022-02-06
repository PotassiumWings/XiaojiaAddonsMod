package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoLobby {
    private final static double[] yCoordList = new double[]{-1, -1, -1, -1};
    private final static long[] timeList = new long[]{-1, -1, -1, -1};
    private static double accelerate = 0;
    private static double velocity = 0;
    private static double currentY = 0;
    private long lastTime = 0;
    private boolean isSending = false;

    public static double getAccelerate() {
        return accelerate;
    }

    public static double getVelocity() {
        return velocity;
    }

    public static double getCurrentY() {
        return currentY;
    }

    public static boolean isFalling() {
        return velocity < -8 && velocity + accelerate < -30;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (isSending) return;
        double y = getY(getPlayer());
        for (int i = 1; i < yCoordList.length; i++) {
            yCoordList[i - 1] = yCoordList[i];
            timeList[i - 1] = timeList[i];
        }
        yCoordList[yCoordList.length - 1] = y;
        timeList[timeList.length - 1] = TimeUtils.curTime();

        int index = yCoordList.length / 2;
        double x1 = yCoordList[index] - yCoordList[0];
        double x2 = yCoordList[yCoordList.length - 1] - yCoordList[index];
        double t1 = (timeList[index] - timeList[0]) / 1000F;
        double t2 = (timeList[yCoordList.length - 1] - timeList[index]) / 1000F;
        double v1 = x1 / t1;
        double v2 = x2 / t2;
        accelerate = (v2 - v1) / ((t1 + t2) / 2);
        velocity = v2;

        if (timeList[0] == -1) return;
        if (!Configs.AutoLobby) return;
        if (!SkyblockUtils.isInSkyblock()) return;
        if (SkyblockUtils.isInDungeon()) return;
        // getPing: ms, velocity: blocks/s
        currentY = y + velocity * SkyblockUtils.getPing() / 1000F;
        if (isFalling() && currentY < 15 && TimeUtils.curTime() - lastTime > 2000) {
            for (int ty = MathUtils.floor(y); ty >= 0; ty--) {
                if (!BlockUtils.isBlockAir(getX(getPlayer()), ty, getZ(getPlayer()))) {
                    return;
                }
            }
            ChatLib.chat("Detected falling to the void, auto /l");
            CommandsUtils.addCommand("/l");
            lastTime = TimeUtils.curTime();
        }
    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (ChatLib.removeFormatting(event.message.getUnformattedText()).startsWith("Sending to server ")) {
            isSending = true;
            for (int i = 0; i < yCoordList.length; i++) yCoordList[i] = timeList[i] = -1;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        isSending = false;
    }
}
