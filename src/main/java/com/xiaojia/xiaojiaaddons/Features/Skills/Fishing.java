package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Fishing {
    private long lastReeledIn = 0;
    private long lastBobberEnterLiquid = 0;
    private boolean oldBobberIsInLiquid = false;
    private boolean bobberInLava = false;
    private double oldBobberY = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPullRod) return;
        long time = TimeUtils.curTime();
        ItemStack item = ControlUtils.getHeldItemStack();
        if (item == null) return;

        try {
            if (!item.getItem().getRegistryName().equals("minecraft:fishing_rod")) return;
            EntityFishHook bobber = getPlayer().fishEntity;
            if (bobber != null) {
                if ((bobber.isInWater()) && !oldBobberIsInLiquid) lastBobberEnterLiquid = time;
                oldBobberIsInLiquid = bobber.isInWater();
                bobberInLava = bobber.isInLava();

                if (Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01 &&
                        time - lastBobberEnterLiquid > 1500 && time - lastReeledIn > 3000) {
                    double movement = bobber.posY - oldBobberY;
                    oldBobberY = bobber.posY;
                    double delta = -0.04;
                    if (movement < delta) {
                        lastReeledIn = time;
                        new Thread(() -> {
                            try {
                                Thread.sleep(20);
                                ControlUtils.rightClick();
                                Thread.sleep(200);
                                ControlUtils.rightClick();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldMove = false;
    private long lastMove = 0;
    private final KeyBind autoMoveKeyBind = new KeyBind("Auto Move", Keyboard.KEY_NONE);

    @SubscribeEvent
    public void onTickMove(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoMove) return;
        if (autoMoveKeyBind.isPressed()) {
            shouldMove = !shouldMove;
            ChatLib.chat(shouldMove ? "Auto Move &aactivated" : "Auto Move &cdeactivated");
        }
        if (!shouldMove) return;
        long cur = TimeUtils.curTime();
        if (cur - lastMove > 2000) {
            lastMove = cur;
            new Thread(() -> {
                try {
                    int choose = MathUtils.floor(Math.random() * 4);
                    float yaw  = MathUtils.getYaw(), pitch = MathUtils.getPitch();
                    float newYaw = (float) (yaw + Math.random() * 5);
                    if (newYaw >= 180) newYaw -= 360;
                    ControlUtils.sneak();
                    switch (choose) {
                        case 0: {
                            ControlUtils.moveLeft(100);
                            Thread.sleep(100);
                            ControlUtils.moveRight(100);
                            break;
                        }
                        case 1: {
                            ControlUtils.moveRight(100);
                            Thread.sleep(100);
                            ControlUtils.moveLeft(100);
                            break;
                        }
                        case 2: {
                            ControlUtils.moveForward(100);
                            Thread.sleep(100);
                            ControlUtils.moveBackward(100);
                            break;
                        }
                        case 3: {
                            ControlUtils.moveBackward(100);
                            Thread.sleep(100);
                            ControlUtils.moveForward(100);
                            break;
                        }
                    }
                    Thread.sleep(100);
                    ControlUtils.faceSlowly(newYaw, pitch, false);
                    Thread.sleep(100);
                    ControlUtils.faceSlowly(yaw, pitch, false);
                    ControlUtils.unSneak();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
