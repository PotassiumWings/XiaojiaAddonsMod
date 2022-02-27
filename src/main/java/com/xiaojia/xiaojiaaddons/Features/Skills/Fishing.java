package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.floor;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Fishing {
    private static long startTime = 0;
    private final KeyBind autoMoveKeyBind = new KeyBind("Auto Move", Keyboard.KEY_NONE);
    private long lastReeledIn = 0;
    private long lastBobberEnterLiquid = 0;
    private boolean oldBobberIsInLiquid = false;
    private double oldBobberY = 0;
    private boolean shouldMove = false;
    private long lastMove = 0;

    public static String timer() {
        if (startTime == 0) return "";
        int ms = (int) (TimeUtils.curTime() - startTime);
        int sec = ms / 1000;
        int min = sec / 60;
        int remSec = sec % 60;
        return String.format("%02d:%02d", min, remSec);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (Checker.enabled) return;
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

                if (Math.abs(bobber.motionX) < 0.01 && Math.abs(bobber.motionZ) < 0.01 &&
                        time - lastBobberEnterLiquid > 1500 && time - lastReeledIn > 3000 &&
                        bobber.isInWater()) {
                    double movement = bobber.posY - oldBobberY;
                    oldBobberY = bobber.posY;
                    double delta = -0.04;
                    if (movement < delta) {
                        lastReeledIn = time;
                        new Thread(this::reelIn).start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static final ArrayList<Vector3d> bubbles = new ArrayList<>();
//
//    @SubscribeEvent
//    public void onRenderWorld(RenderWorldLastEvent event) {
//        synchronized (bubbles) {
//            for (Vector3d bubble: bubbles) {
//                GuiUtils.drawBoundingBoxAtPos(
//                        (float) bubble.x, (float) bubble.y, (float) bubble.z, new Color(0, 255, 0),
//                        0.02F, 0.05F
//                );
//            }
//        }
//    }

    private void reelIn() {
        try {
            Thread.sleep(Configs.AutoPullRodCD);
            ControlUtils.rightClick();
            Thread.sleep(200);
            ControlUtils.rightClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void clearBubbles() {
//        bubbles.clear();
//    }

    @SubscribeEvent
    public void onParticle(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPullRod) return;
        if (!(event.packet instanceof S2APacketParticles)) return;
        S2APacketParticles packet = (S2APacketParticles) event.packet;
        if (packet.getParticleType() != EnumParticleTypes.WATER_WAKE &&
                packet.getParticleType() != EnumParticleTypes.SMOKE_NORMAL) return;
        if (getPlayer() == null) return;
        if (getPlayer().fishEntity == null) return;
        EntityFishHook hook = getPlayer().fishEntity;
        if (MathUtils.distanceSquaredFromPoints(hook.posX, hook.posY, hook.posZ,
                packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate()) < 0.1) {
            boolean flag = false;
            if (hook.isInWater()) flag = packet.getParticleSpeed() > 0.1 && packet.getParticleSpeed() < 0.3;
            if (hook.isInLava()) flag = true;
            if (flag && TimeUtils.curTime() - lastReeledIn > 3000) {
                lastReeledIn = TimeUtils.curTime();
                new Thread(this::reelIn).start();
            }
        }
//        ChatLib.chat(String.format("%.2f %.2f %.2f, %.2f %.2f %.2f; %d %.5f",
//                packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate(),
//                packet.getXOffset(), packet.getYOffset(), packet.getZOffset(),
//                packet.getParticleCount(), packet.getParticleSpeed()
//        ));
    }

    @SubscribeEvent
    public void onTickMove(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoMove) return;
        if (autoMoveKeyBind.isPressed()) {
            shouldMove = !shouldMove;
            if (shouldMove) {
                startTime = TimeUtils.curTime();
            } else {
                startTime = 0;
            }
            ChatLib.chat(shouldMove ? "Auto Move &aactivated" : "Auto Move &cdeactivated");
        }
        if (!shouldMove) return;
        long cur = TimeUtils.curTime();
        if (cur - lastMove > 2000) {
            lastMove = cur;
            new Thread(() -> {
                try {
                    int choose = floor(Math.random() * 4);
                    ControlUtils.sneak();
                    int moveTime = 100 + floor(Math.random() * 100);
                    switch (choose) {
                        case 0: {
                            ControlUtils.moveLeft(moveTime);
                            Thread.sleep(100);
                            ControlUtils.moveRight(moveTime);
                            break;
                        }
                        case 1: {
                            ControlUtils.moveRight(moveTime);
                            Thread.sleep(100);
                            ControlUtils.moveLeft(moveTime);
                            break;
                        }
                        case 2: {
                            ControlUtils.moveForward(moveTime);
                            Thread.sleep(100);
                            ControlUtils.moveBackward(moveTime);
                            break;
                        }
                        case 3: {
                            ControlUtils.moveBackward(moveTime);
                            Thread.sleep(100);
                            ControlUtils.moveForward(moveTime);
                            break;
                        }
                    }
                    Thread.sleep(100);

                    float theta = 0;
                    float DELTAYAW = 0.1F;
                    float DELTAPITCH = 0.2F;
                    float rawYaw = MathUtils.getYaw(), rawPitch = MathUtils.getPitch();
                    while (theta < 2 * Math.PI && shouldMove) {
                        float yaw = MathUtils.getYaw(), pitch = MathUtils.getPitch();
                        yaw += Math.sin(theta) * DELTAYAW + (Math.random() * DELTAYAW * 2 - DELTAYAW) / 4;
                        pitch += Math.cos(theta) * DELTAPITCH + (Math.random() * DELTAPITCH * 2 - DELTAPITCH) / 4;
                        theta += Math.PI / (8 + Math.random() * 25);
                        if (pitch > 90.0) pitch = 180 - pitch;
                        if (pitch < -90.0) pitch = -180 - pitch;
                        if (yaw >= 180.0) yaw -= 360;
                        if (yaw <= -180) yaw += 360;

                        ControlUtils.changeDirection(yaw, pitch);
                        Thread.sleep(20);
                        ControlUtils.checkDirection(yaw, pitch, true);
                    }
                    ControlUtils.changeDirection(
                            rawYaw + DELTAYAW - 2 * DELTAYAW * (float) Math.random(),
                            rawPitch + DELTAPITCH - 2 * DELTAPITCH * (float) Math.random()
                    );

                    if (!shouldMove)
                        ControlUtils.unSneak();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        if (cur - startTime >= 280 * 1000) {
            getPlayer().playSound("random.successful_hit", 1000, 1);
        }
    }
}
