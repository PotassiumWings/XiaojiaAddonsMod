package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Farming {
    private static final KeyBind keyBind = new KeyBind("Auto Farming", Keyboard.KEY_NONE);
    private static boolean should = false;
    private static boolean autoFarmingThreadLock = false;

    private static void moveAccordingToRight(boolean right) {
        if (right) {
            ControlUtils.holdRight();
            ControlUtils.releaseLeft();
        } else {
            ControlUtils.holdLeft();
            ControlUtils.releaseRight();
        }
    }

    private static void stop() {
        if (!should) return;
        if (getPlayer() != null)
            getPlayer().playSound("random.successful_hit", 1000, 1);
        should = false;
        ChatLib.chat("Auto Farming &cdeactivated");
    }

    private static boolean enabled() {
        return should && Checker.enabled;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat("Auto Farming" + (should ? " &aactivated" : " &cdeactivated"));
        }
        if (!Configs.AutoFarm) return;
        if (!should) return;
        if (autoFarmingThreadLock) return;
        if (getPlayer() == null) return;
        autoFarmingThreadLock = true;
        new Thread(() -> {
            try {
                ControlUtils.changeDirection(180, 10);
                ControlUtils.stopMoving();
                ControlUtils.holdLeftClick();
                boolean right = getYaw() < 0;
                moveAccordingToRight(right);

                float y;
                while (enabled()) {
                    y = getY(getPlayer());
                    while (getY(getPlayer()) >= y && enabled()) {
                        moveAccordingToRight(right);
                        Thread.sleep(20);
                    }
                    if (!enabled()) break;
                    while (getY(getPlayer()) != y - 3 && getY(getPlayer()) < y + 200 && enabled())
                        Thread.sleep(20);
                    if (getY(getPlayer()) > y + 200) {
                        // tped up, sleep for the coords to be integer
                        Thread.sleep(1000);
                        right = getX(getPlayer()) < 0;
                    } else {
                        right = !right;
                    }
                }
                ControlUtils.releaseLeft();
                ControlUtils.releaseRight();
                ControlUtils.releaseLeftClick();
                stop();
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            } finally {
                ControlUtils.releaseForward();
                autoFarmingThreadLock = false;
            }
        }).start();
    }
}
