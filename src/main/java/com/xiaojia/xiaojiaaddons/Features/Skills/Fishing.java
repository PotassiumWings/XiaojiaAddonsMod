package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Sounds.SoundHandler;
import com.xiaojia.xiaojiaaddons.Sounds.Sounds;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.floor;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Fishing {
    public static long startPushing = 0;
    private static long startTime = 0;
    private final KeyBind autoMoveKeyBind = new KeyBind("Auto Move", Keyboard.KEY_NONE);
    private long lastReeledIn = 0;
    private boolean shouldMove = false;
    private long lastMove = 0;
    private Thread pushingThread = null;

    public static String timer() {
        if (!Configs.AutoMoveTimer) return "";
        if (startTime == 0) return "";
        int ms = (int) (TimeUtils.curTime() - startTime);
        int sec = ms / 1000;
        int min = sec / 60;
        int remSec = sec % 60;
        return String.format("%02d:%02d", min, remSec);
    }

    public static void warn(double val) {
        new Thread(() -> {
            double d = Math.random();
            int mf = (int) (Math.random() * 1000);
            ChatLib.debug("Rolled d: " + d);

            if (d < val) {
                ChatLib.chat("&d&lCRAZY RARE SOUND! &6BENK's roar &b(+" + mf + " ✯ Magic Find!)&r");
                SoundHandler.playSound(Sounds.bk());
            } else if (d < val * 2) {
                ChatLib.chat("&d&lCRAZY RARE SOUND! &6ICY FILL &b(+" + mf + " ✯ Magic Find!)&r");
                SoundHandler.playSound(Sounds.icyFill());
            } else SoundHandler.playSound(Sounds.destiny());
        }).start();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoMove) return;
        if (!Configs.SafeAutoMove) return;
        if (!shouldMove) return;
        stopMove();
    }

    private void cast() {
        lastReeledIn = TimeUtils.curTime();
        ControlUtils.rightClick();
    }

    private void reelIn() {
        try {
            ControlUtils.rightClick();
            if (Configs.FishingMode == 1) {
                // regular
                Thread.sleep(Configs.PullCastCD);
                cast();
            } else {
                // swap
                int wand = HotbarUtils.getIndex("ICE_SPRAY_WAND");
                int witherBlade = HotbarUtils.getIndex("HYPERION");
                if (witherBlade == -1) witherBlade = HotbarUtils.getIndex("SCYLLA");
                if (witherBlade == -1) witherBlade = HotbarUtils.getIndex("VALKYRIE");
                if (witherBlade == -1) witherBlade = HotbarUtils.getIndex("ASTRAEA");

                if (wand == -1 || witherBlade == -1) {
                    ChatLib.chat("Requires ice spray wand and wither blade in hotbar.");
                    return;
                }

                ControlUtils.setHeldItemIndex(wand, false);
                Thread.sleep(Configs.PullCastCD);
                ControlUtils.rightClick();
                ControlUtils.setHeldItemIndex(witherBlade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean canReelIn() {
        try {
            return getWorld().loadedEntityList.stream().anyMatch(e -> ChatLib.removeFormatting(e.getName()).equals("!!!"));
        } catch (Exception e) {
            ChatLib.chat("Wrong in rell in check");
            e.printStackTrace();
            return false;
        }
    }

    @SubscribeEvent
    public void onParticle(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPullRod) return;
        if (Configs.AutoPullRod2) return;
        if (!(event.packet instanceof S2APacketParticles)) return;
        S2APacketParticles packet = (S2APacketParticles) event.packet;
        if (packet.getParticleType() != EnumParticleTypes.WATER_BUBBLE &&
                packet.getParticleType() != EnumParticleTypes.FLAME) return;
        if (getPlayer() == null) return;
        if (getPlayer().fishEntity == null) return;
        EntityFishHook hook = getPlayer().fishEntity;
        if (!MathUtils.equal(packet.getXOffset(), 0.25) ||
                !MathUtils.equal(packet.getYOffset(), 0) ||
                !MathUtils.equal(packet.getZOffset(), 0.25) ||
                !MathUtils.equal(packet.getParticleSpeed(), 0.2)) return;
        if (packet.getParticleCount() != 6) return;

        if (MathUtils.distanceSquaredFromPoints(hook.posX, hook.posY, hook.posZ,
                packet.getXCoordinate(), hook.posY, packet.getZCoordinate()) < 1 &&
                Math.abs(hook.posY - packet.getYCoordinate()) < 2) {
            if (TimeUtils.curTime() - lastReeledIn > Configs.ReelCD) {
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
    public void onReceive(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoMove) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.matches(" ☠ [a-zA-Z0-9_]+ was killed by Lord Jawbus.")) {
            if (Configs.JawbusWarn) {
                warn(0.001);
            }
        }
        if (!shouldMove) return;
        if (message.equals("The Golden Fish escapes your hook but looks weakened.")) {
            lastReeledIn = TimeUtils.curTime();
            new Thread(this::cast).start();
        }
        if (message.equals("The Golden Fish is weak!")) {
            lastReeledIn = TimeUtils.curTime();
            new Thread(this::reelIn).start();
        }
        if (message.equals("NEW DISCOVERY")) {
            if (Configs.StopWhenNewDiscovery) {
                stopMove();
            }
        }
    }

    @SubscribeEvent
    public void onTickReel2(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPullRod) return;
        if (!Configs.AutoPullRod2) return;
        if (getPlayer() == null) return;
        if (getPlayer().fishEntity == null) return;

        if (TimeUtils.curTime() - lastReeledIn > Configs.ReelCD) {
            if (canReelIn()) {
                lastReeledIn = TimeUtils.curTime();
                new Thread(this::reelIn).start();
            }
        }
    }

    @SubscribeEvent
    public void onTickPushingThread(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!shouldMove) {
            if (pushingThread != null) pushingThread.interrupt();
            return;
        }
        if (pushingThread == null || !pushingThread.isAlive()) {
            pushingThread = new Thread(() -> {
                startPushing = TimeUtils.curTime();
                try {
                    Thread.sleep(Configs.FishCheckCD * 1000);
                    cast();
                    Thread.sleep(Configs.FishCheckCD * 1000);
                    cast();
                    Thread.sleep(Configs.FishCheckCD * 1000);
                    ChatLib.chat("Too long since last catch!");
                    getPlayer().playSound("random.successful_hit", 1000, 1);
                    stopMove();
                } catch (InterruptedException ignored) {
                }
            });
            pushingThread.start();
        }
    }

    private void stopMove() {
        shouldMove = false;
        startTime = 0;
        ChatLib.chat("Auto Move &cdeactivated");
        ControlUtils.stopMoving();
    }

    @SubscribeEvent
    public void onTickMove(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoMove) return;
        if (autoMoveKeyBind.isPressed()) {
            shouldMove = !shouldMove;
            if (shouldMove) {
                startTime = lastReeledIn = TimeUtils.curTime();
                ChatLib.chat("Auto Move &aactivated");
            } else {
                stopMove();
            }
        }
        if (!shouldMove) return;

        EntityFishHook hook = getPlayer().fishEntity;
        if (hook != null && (hook.isInLava() || hook.isInWater()) && pushingThread != null)
            pushingThread.interrupt();

        long cur = TimeUtils.curTime();
        if (Configs.AutoMoveRecast && cur - lastReeledIn >= 1000 * Configs.AutoMoveRecastTime) {
            lastReeledIn = TimeUtils.curTime();
            new Thread(this::reelIn).start();
        }
        if (cur - lastMove > Configs.AutoMoveCD * 1000) {
            lastMove = cur;
            if (Configs.MainLobbyAutoMove) {
                new Thread(() -> {
                    try {
                        ControlUtils.sneak();
                        ControlUtils.holdJump();
                        if (!shouldMove) {
                            ControlUtils.unSneak();
                            ControlUtils.releaseJump();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                new Thread(() -> {
                    try {
                        int choose = floor(Math.random() * 4);
                        if (Configs.AutoMoveSneak) ControlUtils.sneak();
                        int time = Configs.AutoMoveTime;
                        int moveTime = time + floor(Math.random() * time);
                        switch (choose) {
                            case 0: {
                                ControlUtils.moveLeft(moveTime);
                                Thread.sleep(100);
                                if (!shouldMove) return;
                                ControlUtils.moveRight(moveTime);
                                break;
                            }
                            case 1: {
                                ControlUtils.moveRight(moveTime);
                                Thread.sleep(100);
                                if (!shouldMove) return;
                                ControlUtils.moveLeft(moveTime);
                                break;
                            }
                            case 2: {
                                ControlUtils.moveForward(moveTime);
                                Thread.sleep(100);
                                if (!shouldMove) return;
                                ControlUtils.moveBackward(moveTime);
                                break;
                            }
                            case 3: {
                                ControlUtils.moveBackward(moveTime);
                                Thread.sleep(100);
                                if (!shouldMove) return;
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
        if (cur - startTime >= 280 * 1000 && Configs.AutoMoveTimer) {
            getPlayer().playSound("random.successful_hit", 1000, 1);
        }
    }
}
