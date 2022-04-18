package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoSneakyCreeper {
    private static final ArrayList<BlockPos> positions = new ArrayList<>(Arrays.asList(
            new BlockPos(3, 152, 30),
            new BlockPos(-12, 153, 28),
            new BlockPos(-20, 154, 25),
            new BlockPos(-25, 154, 20),
            new BlockPos(-26, 153, 11),
            new BlockPos(-29, 153, 2),
            new BlockPos(-30, 153, -8),
            new BlockPos(-24, 154, -17),
            new BlockPos(-18, 155, -24),
            new BlockPos(-6, 153, -35),
            new BlockPos(1, 154, -38),
            new BlockPos(8, 155, -36),
            new BlockPos(16, 156, -33),
            new BlockPos(24, 157, -35),
            new BlockPos(32, 158, -31),
            new BlockPos(37, 157, -26),
            new BlockPos(35, 152, -12),
            new BlockPos(38, 152, -7),
            new BlockPos(40, 151, 3),
            new BlockPos(41, 150, 16),
            new BlockPos(31, 150, 26),
            new BlockPos(22, 152, 31)
    ));
    private static final KeyBind keyBind = new KeyBind("Auto Sneaky Creeper", Keyboard.KEY_NONE);
    private static boolean should = false;
    private static BlockPos goingTo = null;
    private Thread runningThread = null;
    private static int index = 0;

    private static boolean shouldShow = false;

    private static void stop() {
        ControlUtils.stopMoving();
        should = false;
        ChatLib.chat("Auto Sneaky Creeper &cdeactivated");
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if ((!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) && should) {
            stop();
        }
        if (keyBind.isPressed()) {
            should = !should;
            if (should) {
                if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) {
                    stop();
                }
                ChatLib.chat("Auto Sneaky Creeper &aactivated");
            } else {
                goingTo = null;
                stop();
            }
        }
        if (!should) return;
        if (runningThread != null && runningThread.isAlive()) return;
        runningThread = new Thread(() -> {
            try {
                ChatLib.chat("Starting a new Thread...");
                if (goingTo == null) {
                    shouldShow = true;
                    goingTo = positions.get(index);
                    if (MathUtils.distanceSquareFromPlayer(goingTo) > 4 * 4) {
                        ChatLib.chat("Go near the blue block.");
                        while (MathUtils.distanceSquareFromPlayer(goingTo) > 4 * 4)
                            Thread.sleep(100);
                    }
                    ChatLib.chat("Start moving automatically.");
                    ControlUtils.stopMoving();
                    shouldShow = false;
                    Thread.sleep(1000); // waiting for fall down

                    ControlUtils.face(positions.get((index + 1) % positions.size()));
                }
                while (should) {
                    index = (index + 1) % positions.size();
                    goingTo = positions.get(index);
                    ControlUtils.holdForward();
                    Thread facingThread = null;

                    // stuck detection
                    BlockPos lastPos = getPlayer().getPosition();
                    long lastTime = TimeUtils.curTime();

                    while (MathUtils.distanceSquareFromPlayer(goingTo) > 4 * 4 && should) {
                        BlockPos pos = getPlayer().getPosition();
                        if (!pos.equals(lastPos)) {
                            lastPos = pos;
                            lastTime = TimeUtils.curTime();
                        }
                        if (TimeUtils.curTime() - lastTime > 4000) {
                            ControlUtils.jump();
                        }
                        if (TimeUtils.curTime() - lastTime > 10000) {
                            stop();
                            getPlayer().playSound("random.successful_hit", 1000, 1);
                            return;
                        }

                        if (facingThread == null || !facingThread.isAlive()) {
                            facingThread = new Thread(() -> {
                                try {
                                    ControlUtils.faceSlowly(goingTo.getX(),
                                            getY(getPlayer()) + getPlayer().getEyeHeight(), goingTo.getZ());
                                } catch (Exception e) {
                                    stop();
                                    getPlayer().playSound("random.successful_hit", 1000, 1);
                                }
                            });
                            facingThread.start();
                        }

                        // stop if there's creeper nearby
                        while (existCreeperBeside() && should) {
                            ControlUtils.stopMoving();
                            Thread.sleep(20);
                        }
                        if (!should) return;
                        ControlUtils.holdForward();
                        // jump if need
                        if (goingTo.getY() > getY(getPlayer()))
                            ControlUtils.jump();
                        else ControlUtils.releaseJump();
                        // just move
                        Thread.sleep(20);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        runningThread.start();
    }

    private boolean existCreeperBeside() {
        List<Entity> list = new ArrayList<>(getWorld().loadedEntityList);
        for (Entity entity : list) {
            if (entity instanceof EntityCreeper && !entity.isDead &&
                    MathUtils.distanceSquareFromPlayer(entity) < 5 * 5) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (goingTo != null) {
            if (!shouldShow && !Configs.DevTracing)
                return;
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(goingTo,
                    new Color(0x3C, 0x3C, 0xDE, 200),
                    1, 1, 0.0020000000949949026F);
            GuiUtils.disableESP();
        } else {
            if (!Configs.DevTracing) return;
            GuiUtils.enableESP();
            for (BlockPos pos : positions) {
                GuiUtils.drawBoxAtBlock(pos,
                        new Color(0x3C, 0x3C, 0xDE, 200),
                        1, 1, 0.0020000000949949026F
                );
            }
            GuiUtils.disableESP();
        }
    }
}
