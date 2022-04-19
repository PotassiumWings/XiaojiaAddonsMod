package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
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
            new BlockPos(32, 158, -32),
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
            new BlockPos(-13, 156, 12),
            new BlockPos(-5, 157, 17),
            new BlockPos(1, 158, 16),
            new BlockPos(5, 155, 15),
            new BlockPos(8, 152, 25),

            // 34
            new BlockPos(25, 161, -24),
            new BlockPos(18, 164, -12),
            new BlockPos(13, 164, -5),
            new BlockPos(12, 160, 2)
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
            new Pair<>(30, 31), new Pair<>(31, 32), new Pair<>(32, 33), new Pair<>(33, 0),

            new Pair<>(34, 35), new Pair<>(35, 36), new Pair<>(36, 37), new Pair<>(37, 32),
            new Pair<>(19, 34)
    ));
    private static final HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
    private static final KeyBind keyBind = new KeyBind("Auto Sneaky Creeper", Keyboard.KEY_NONE);
    private static final ConcurrentLinkedDeque<Integer> indexes = new ConcurrentLinkedDeque<>();
    public static Image defaultIcon = new Image("defaultPlayerIcon.png");
    private static boolean should = false;
    private static BlockPos goingTo = null;
    private static int index = 0;
    private static boolean shouldShow = false;
    private static boolean tryingEnable = false;
    private static long lastForceClose = 0;
    private static StringBuilder log = new StringBuilder();
    private static HashSet<Integer> toBeKilled = new HashSet<>();

    static {
        for (int i = 0; i < positions.size(); i++)
            graph.put(i, new ArrayList<>());
        for (Pair<Integer, Integer> edge : edges) {
            graph.get(edge.getKey()).add(edge.getValue());
        }
    }

    private Thread runningThread = null;

    private static void stop() {
        ControlUtils.stopMoving();
        if (should) {
            should = false;
            ChatLib.chat("Auto Sneaky Creeper &cdeactivated");
            if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) return;
            new Thread(() -> {
                try {
                    tryingEnable = true;
                    ChatLib.chat("Waiting 2s for re-enable...");
                    Thread.sleep(2000);
                    if (TimeUtils.curTime() - lastForceClose > 2022) {
                        ChatLib.chat("Re-enabling...");
                        should = true;
                    } else {
                        should = false;
                    }
                    tryingEnable = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static int getSize() {
        return indexes.size();
    }

    // return added set for killed
    private static HashSet<Integer> dfs(int index, double cap, HashSet<Integer> killed, List<EntityCreeper> creepers) {
        if (cap < 0) return new HashSet<>();

        HashSet<Integer> result = new HashSet<>(killed);
        for (int next : graph.get(index)) {
            double dis = distanceBetween(index, next);
            HashSet<Integer> added = dfs(next, cap - dis, killed, creepers);
            HashSet<Integer> along = getCreepersAlong(index, next, killed, added, creepers);
            if (result.size() < added.size() + along.size()) {
                added.addAll(along);
                result = added;
            }
        }
        return result;
    }

    private static double distanceBetween(int from, int to) {
        return Math.sqrt(positions.get(from).distanceSq(positions.get(to)));
    }

    private static HashSet<Integer> getCreepersAlong(int s, int t, HashSet<Integer> s1, HashSet<Integer> s2, List<EntityCreeper> creepers) {
        BlockPos from = positions.get(s);
        BlockPos to = positions.get(t);
        HashSet<Integer> res = new HashSet<>();
        int MAX_DELTA = Configs.SneakySplit;
        for (int delta = 0; delta <= MAX_DELTA; delta++) {
            Vector3d v = new Vector3d(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5);
            Vector3d diff = new Vector3d(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
            diff.scale(delta * 1.0 / MAX_DELTA);
            v.add(diff);
            // calculate from v
            for (EntityCreeper creeper : creepers)
                if (creeper.getDistance(v.x, v.y, v.z) < Configs.SneakySearchRadius / 10F)
                    res.add(creeper.getEntityId());
        }
        return res;
    }

    private static List<EntityCreeper> getCreepers() {
        List<Entity> list = new ArrayList<>(getWorld().loadedEntityList);
        List<EntityCreeper> res = new ArrayList<>();
        for (Entity entity : list) {
            if (!(entity instanceof EntityCreeper)) continue;
            EntityCreeper e = ((EntityCreeper) entity);
            if (e.getHealth() < 1) continue;
            res.add(e);
        }
        return res;
    }

    private static void translateTo(double x, double z) {
        RenderUtils.translate(
                Configs.SNMapX + (x - -40) * (Configs.SNMapScale * 25 / 90F),
                Configs.SNMapY + (z - -45) * (Configs.SNMapScale * 25 / 90F)
        );
    }

    public static void printLog() {
        System.err.println("AutoSneakyCreeper Log:");
        System.err.println(log);
        System.err.println();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if ((!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) && should) stop();
        if (keyBind.isPressed()) {
            // re-enable
            if (!should && tryingEnable) {
                lastForceClose = TimeUtils.curTime();
                ChatLib.chat("Stopped from re-enabling.");
                return;
            }

            should = !should;
            if (should) {
                if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) stop();
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
                    BlockPos lastDetectPos = getPlayer().getPosition();
                    long lastTime = TimeUtils.curTime();

                    while (MathUtils.distanceSquareFromPlayer(
                            goingTo.getX(), getY(getPlayer()) + 1.5F, goingTo.getZ())
                            > 3.5 * 3.5 && should) {
                        BlockPos pos = getPlayer().getPosition();
                        if (pos.getX() != lastDetectPos.getX() || lastDetectPos.getZ() != pos.getZ()) {
                            lastDetectPos = pos;
                            lastTime = TimeUtils.curTime();
                        }
                        if (TimeUtils.curTime() - lastTime > 4000) ControlUtils.jump();
                        if (TimeUtils.curTime() - lastTime > 6000) {
                            Integer integer = indexes.pollLast();
                            if (integer == null) {
                                stop();
                                getPlayer().playSound("random.successful_hit", 1000, 1);
                                return;
                            }
                            index = integer;
                            goingTo = positions.get(index);
                            ChatLib.chat("Backwards 1 step!");
                            lastTime = TimeUtils.curTime();
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
                    if (should) {
                        indexes.offerLast(index);
                        if (indexes.size() > 100) indexes.pollFirst();
                        index = getNext(index);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        runningThread.start();
    }

    private int getNext(int index) {
        int res = -1;
        List<EntityCreeper> list = getCreepers();
        ArrayList<Integer> nextIndexes = new ArrayList<>();
        ArrayList<HashSet<Integer>> toKilledCreepers = new ArrayList<>();
        double MAX_LEN = Configs.SNMaxLen;
        log.append("Getting next: " + index + "\n");
        for (int next : graph.get(index)) {
            HashSet<Integer> along = getCreepersAlong(index, next, new HashSet<>(), new HashSet<>(), list);
            HashSet<Integer> search = dfs(next, MAX_LEN - distanceBetween(index, next), along, list);
            along.addAll(search);
            if (along.size() > res) {
                res = along.size();
                nextIndexes = new ArrayList<>();
                toKilledCreepers = new ArrayList<>();
                nextIndexes.add(next);
                toKilledCreepers.add(along);
            } else if (along.size() == res) {
                nextIndexes.add(next);
                toKilledCreepers.add(along);
            }
            log.append("   next: " + next + ", along: " + (along.size() - search.size())
                    + ", search: " + search.size() + ", size " + nextIndexes.size() + "\n");
        }
        int ind = (int) (nextIndexes.size() * Math.random());
        toBeKilled = toKilledCreepers.get(ind);
        return nextIndexes.get(ind);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) return;
        if (!Configs.SneakyCreeperMap) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        RenderUtils.start();
        RenderUtils.drawRect(new Color(0F, 0F, 0F, Configs.SNBackgroundAlpha / 255F).getRGB(),
                Configs.SNMapX, Configs.SNMapY,
                25 * Configs.SNMapScale, 25 * Configs.SNMapScale);
        int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));

        Vector2f size = new Vector2f(Configs.SNMapScale * Configs.SNHeadScale * 0.02f, Configs.SNMapScale * Configs.SNHeadScale * 0.02f);
        // player head
        RenderUtils.retainTransforms(false);
        translateTo(x, z);
        RenderUtils.translate(size.x / 2F, size.y / 2F);
        RenderUtils.rotate(getYaw() + 180);
        RenderUtils.translate(-size.x / 2F, -size.y / 2F);
        RenderUtils.drawImage(defaultIcon, 0, 0, size.x, size.y);
        // positions
        for (BlockPos pos : positions) {
            RenderUtils.retainTransforms(false);
            translateTo(pos.getX(), pos.getZ());
            RenderUtils.drawRect(new Color(0, 0, 255).getRGB(),
                    -Configs.SNMapScale / 2, -Configs.SNMapScale / 2,
                    Configs.SNMapScale, Configs.SNMapScale);
        }
        // creepers
        for (EntityCreeper creeper : getCreepers()) {
            Color color = new Color(0, 255, 0);
            if (toBeKilled.contains(creeper.getEntityId())) color = new Color(255, 255, 0);
            RenderUtils.retainTransforms(false);
            translateTo(getX(creeper), getZ(creeper));
            RenderUtils.drawRect(color.getRGB(),
                    -Configs.SNMapScale / 2, -Configs.SNMapScale / 2,
                    Configs.SNMapScale, Configs.SNMapScale);
        }
        if (goingTo != null) {
            RenderUtils.retainTransforms(false);
            translateTo(goingTo.getX(), goingTo.getZ());
            RenderUtils.drawRect(new Color(255, 0, 0).getRGB(),
                    -Configs.SNMapScale / 2, -Configs.SNMapScale / 2,
                    Configs.SNMapScale, Configs.SNMapScale);
        }
        RenderUtils.end();
    }

    private boolean existCreeperBeside() {
        return getCreepers().stream().anyMatch(e -> MathUtils.distanceSquareFromPlayer(e) < 5 * 5);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) return;
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

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        log = new StringBuilder();
        toBeKilled.clear();
        for (int i = 0; i < positions.size(); i++) {
            log.append("Graph log - " + i + " " + graph.get(i).size() + "\n");
        }
    }
}
