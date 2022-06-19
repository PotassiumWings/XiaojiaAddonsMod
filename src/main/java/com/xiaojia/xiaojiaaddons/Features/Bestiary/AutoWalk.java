package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public abstract class AutoWalk {
    private final HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
    private final ConcurrentLinkedDeque<Integer> indexes = new ConcurrentLinkedDeque<>();
    public Image defaultIcon = new Image("defaultPlayerIcon.png");
    public BlockPos goingTo = null;
    public int index = 0;
    public boolean shouldShow = false;
    public HashSet<Integer> toBeKilled = new HashSet<>();
    private boolean should = false;
    private boolean tryingEnable = false;
    private long lastForceClose = 0;
    private StringBuilder log = new StringBuilder();
    private BlockPos ghostBlockPos = new BlockPos(0, 0, 0);
    private KeyBind keyBind;
    private ArrayList<BlockPos> positions;
    private String name;
    private Thread runningThread = null;

    public int getSize() {
        return indexes.size();
    }

    public List<EntityCreeper> getCreepers() {
        List<Entity> list = EntityUtils.getEntities();
        List<EntityCreeper> res = new ArrayList<>();
        for (Entity entity : list) {
            if (!(entity instanceof EntityCreeper)) continue;
            EntityCreeper e = ((EntityCreeper) entity);
            if (e.getHealth() < 1) continue;
            res.add(e);
        }
        return res;
    }

    public abstract ArrayList<BlockPos> getPositions();

    public abstract ArrayList<Pair<Integer, Integer>> getEdges();

    public abstract boolean enabled();

    public abstract String getName();

    public abstract KeyBind getKeyBind();

    public abstract double getJudgeDistance();

    public void init() {
        positions = getPositions();
        for (int i = 0; i < positions.size(); i++)
            graph.put(i, new ArrayList<>());
        for (Pair<Integer, Integer> edge : getEdges()) {
            graph.get(edge.getKey()).add(edge.getValue());
        }
        name = getName();
        keyBind = getKeyBind();
    }

    private void stop() {
        ControlUtils.stopMoving();
        if (should) {
            should = false;
            ChatLib.chat(name + " &cdeactivated");
            if (!enabled()) return;
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

    // return added set for killed
    private HashSet<Integer> dfs(int index, double cap, HashSet<Integer> killed, List<EntityCreeper> creepers) {
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

    private double distanceBetween(int from, int to) {
        return Math.sqrt(positions.get(from).distanceSq(positions.get(to)));
    }

    private HashSet<Integer> getCreepersAlong(int s, int t, HashSet<Integer> s1, HashSet<Integer> s2, List<EntityCreeper> creepers) {
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

    public void printLog() {
        System.err.println(name + " Log:");
        System.err.println(log);
        System.err.println();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!enabled() && should) stop();
        if (keyBind.isPressed()) {
            // re-enable
            if (!should && tryingEnable) {
                lastForceClose = TimeUtils.curTime();
                ChatLib.chat("Stopped from re-enabling.");
                return;
            }

            should = !should;
            if (should) {
                if (!enabled()) stop();
                ChatLib.chat(name + " &aactivated");
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
                            > getJudgeDistance() * getJudgeDistance() && should) {
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
                                    BlockPos playerPos = getPlayer().getPosition();
                                    if (playerPos.distanceSq(ghostBlockPos) < 2 * 2) {
                                        try {
                                            ControlUtils.moveBackward(200);
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    ghostBlockPos = playerPos;
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

    private boolean existCreeperBeside() {
        return getCreepers().stream().anyMatch(e -> MathUtils.distanceSquareFromPlayer(e) < 5 * 5);
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
