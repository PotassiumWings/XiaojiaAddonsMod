package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getPitch;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoScatha {
    private static final KeyBind keyBind = new KeyBind("Auto Scatha", Keyboard.KEY_NONE);
    private boolean should = false;
    private Thread thread = null;

    private int y = 0;
    private float yaw, pitch;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoScatha) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Auto Scatha &aactivated" : "Auto Scatha &cdeactivated");
            if (!should) stop(false);
            if (should) {
                y = MathUtils.floor(getY(getPlayer()));
                yaw = fit(getYaw());
                pitch = 45;
                ChatLib.debug("getYaw: " + getYaw() + ", pitch: " + getPitch());
                ChatLib.debug("yaw: " + yaw+ ", pitch: " + pitch);
                ControlUtils.changeDirection(yaw, pitch);
                thread = new Thread(() -> {
                    try {
                        BlockPos lastBlockPos = MathUtils.getBlockPos();
                        long time = TimeUtils.curTime();
                        while (should) {
                            BlockPos cur = MathUtils.getBlockPos();
                            if (!cur.equals(lastBlockPos))  {
                                lastBlockPos = cur;
                                time = TimeUtils.curTime();
                            }
                            if (TimeUtils.curTime() - time > 5000) {
                                ChatLib.chat("You're not moving, I don't know why. So stopped");
                                stop();
                                return;
                            }

                            if ((Math.abs(getYaw() - yaw) > 1e-2 && Math.abs(getYaw() - yaw) < 360 - 1e-2) ||
                                    Math.abs(getPitch() - pitch) > 1e-2) {
                                ChatLib.chat("Detected yaw/pitch move, stopped.");
                                stop();
                                return;
                            }
                            getPlayer().closeScreen();
                            BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                            Block block = BlockUtils.getBlockAt(blockPos);
                            if (blockPos != null && blockPos.getY() >= y) ControlUtils.holdLeftClick();
                            else ControlUtils.releaseLeftClick();

                            if (blockPos != null && block != null
                                    && block.getRegistryName().contains("bedrock") && blockPos.getY() >= y) {
                                ChatLib.chat("Detected bedrock, stopped.");
                                stop();
                                return;
                            }
                            ControlUtils.holdForward();
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }
        if (!should) return;
        if (MathUtils.floor(getY(getPlayer())) != y) {
            ChatLib.chat("Y changed! Auto Scatha stoped. Remember to turn off mole!");
            stop();
        }
    }

    @SubscribeEvent
    public void onReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoScatha) return;
        if (ChatLib.removeFormatting(event.message.getUnformattedText()).trim()
                .equals("You hear the sound of something approaching...")) {
            ChatLib.chat("Worm spawned!");
            stop();
        }
    }

    private void stop() {
        stop(true);
    }

    private void stop(boolean play) {
        ControlUtils.releaseLeftClick();
        ControlUtils.releaseForward();
        if (play) getPlayer().playSound("random.successful_hit", 1000, 1);
        should = false;
        if (thread != null && thread.isAlive()) thread.interrupt();
    }

    private float fit(float yaw) {
        if (yaw >= 45 && yaw <= 135) return 90;
        if (yaw >= 135 || yaw <= -135) return -180;
        if (yaw >= -45 && yaw <= 45) return 0;
        return -90;
    }
}
