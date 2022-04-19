package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
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

            new BlockPos(-22, 154, 5),
            new BlockPos(-11, 153, -2),
            new BlockPos(5, 151, -12),
            new BlockPos(17, 151, -9),
            new BlockPos(29, 151, -5),

            new BlockPos(-29, 153, 2),
            new BlockPos(-30, 153, -8),
            new BlockPos(-24, 154, -17),
            new BlockPos(-18, 155, -24),
            new BlockPos(-6, 153, -35),
            new BlockPos(1, 154, -38),
            new BlockPos(8, 155, -36),
            new BlockPos(16, 156, -33),

            new BlockPos(24, 157, -35),
            new BlockPos(33, 158, -30),
            new BlockPos(37, 157, -26),
            new BlockPos(35, 152, -12),

            new BlockPos(37, 152, -2),
            new BlockPos(40, 151, 3),
            new BlockPos(41, 150, 16),
            new BlockPos(31, 150, 26),
            new BlockPos(22, 152, 31),
            // 27
            new BlockPos(-16, 154, 3),
            new BlockPos(-13, 154, 8),
            new BlockPos(-5, 157, 17),
            new BlockPos(1, 158, 16),
            new BlockPos(5, 155, 15),
            new BlockPos(8, 152, 25)
    ));

    private static final ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>(Arrays.asList(
            new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
            new Pair<>(4, 5), new Pair<>(4, 10),
            new Pair<>(5, 27), new Pair<>(27, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 9),
            new Pair<>(10, 11), new Pair<>(11, 12), new Pair<>(12, 13), new Pair<>(13, 14),
            new Pair<>(14, 15), new Pair<>(15, 16), new Pair<>(16, 17), new Pair<>(17, 18),
            new Pair<>(18, 19), new Pair<>(19, 20), new Pair<>(20, 21),
            new Pair<>(9, 22), new Pair<>(21, 22),
            new Pair<>(22, 23), new Pair<>(23, 24), new Pair<>(24, 25), new Pair<>(25, 26),
            new Pair<>(26, 0),
            new Pair<>(7, 17),
            new Pair<>(27, 28), new Pair<>(28, 29), new Pair<>(29, 30),
            new Pair<>(30, 31), new Pair<>(31, 32), new Pair<>(32, 0)
    ));
    private static final KeyBind keyBind = new KeyBind("Auto Sneaky Creeper", Keyboard.KEY_NONE);
    private static boolean should = false;
    private static BlockPos goingTo = null;
    private static int index = 0;
    private static boolean shouldShow = false;
    private Thread runningThread = null;

    private static void stop() {
        ControlUtils.stopMoving();
        if (should) {
            should = false;
            ChatLib.chat("Auto Sneaky Creeper &cdeactivated");
            if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) {
                return;
            }
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    ChatLib.chat("Re-enabling...");
                    should = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
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
//                    if (MathUtils.distanceSquareFromPlayer(goingTo) > 4 * 4) {
//                        ChatLib.chat("Go near the blue block.");
//                        while (MathUtils.distanceSquareFromPlayer(goingTo) > 4 * 4)
//                            Thread.sleep(100);
//                    }
                    ChatLib.chat("Start moving automatically.");
                    ControlUtils.stopMoving();
                    shouldShow = false;
                    Thread.sleep(1000); // waiting for fall down

                    ControlUtils.face(positions.get(index));
                }
                while (should) {
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
                    if (should) index = getNext(index);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        runningThread.start();
    }

    private int getNext(int index) {
        ArrayList<Integer> going = new ArrayList<>();
        for (Pair<Integer, Integer> edge : edges) {
            if (edge.getKey() != index) continue;
            going.add(edge.getValue());
        }
        return going.get((int) (Math.random() * going.size()));
    }

    private boolean existCreeperBeside() {
        List<Entity> list = new ArrayList<>(getWorld().loadedEntityList);
        for (Entity entity : list) {
            if (entity instanceof EntityCreeper && ((EntityCreeper) entity).getHealth() > 1 &&
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
            GuiUtils.drawString(
                    index + "",
                    goingTo.getX() + 0.5F, goingTo.getY() + 1.2F, goingTo.getZ() + 0.5F,
                    new Color(0, 255, 0).getRGB(), 0.8F, true
            );
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
            for (Pair<Integer, Integer> edge : edges) {
                int i1 = edge.getKey(), i2 = edge.getValue();
                BlockPos p1 = positions.get(i1), p2 = positions.get(i2);
                GuiUtils.drawLine(
                        p1.getX() + 0.5F, p1.getY() + 0.5F, p1.getZ() + 0.5F,
                        p2.getX() + 0.5F, p2.getY() + 0.5F, p2.getZ() + 0.5F,
                        new Color(255, 0, 0), 2
                );
            }
            for (int index = 0; index < positions.size(); index++) {
                BlockPos pos = positions.get(index);
                GuiUtils.drawString(
                        index + "",
                        pos.getX() + 0.5F, pos.getY() + 1.2F, pos.getZ() + 0.5F,
                        new Color(0, 255, 0).getRGB(), 0.8F, true
                );
            }
            GuiUtils.disableESP();
        }
    }
}
