package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Cube;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Line;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.PacketUtils;
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoBlaze {
    // principle: whatever being saved is the center of the block, not +0.25+1.5
    private final static List<BlazeInfo> blazes = new ArrayList<>();
    private final static List<Cube> blocks = new ArrayList<>();
    private final static HashMap<Vector3d, List<Vector3d>> transGraph = new HashMap<>();  // from a -> b: graph[a].contains(b)
    private final static List<Vector3d> places = new ArrayList<>();  // aotv
    public static Room room = null;
    public static StringBuilder log = new StringBuilder();
    public static StringBuilder tempLog = new StringBuilder();
    private static boolean lowFirst = false;
    private final KeyBind keyBind = AutoPuzzle.keyBind;
    public boolean should = false;
    private Thread shootingThread = null;
    private boolean tpPacketReceived = false;
    private boolean arrowShot = false;
    private double facingYaw = 10000;
    private double facingPitch = 10000;

    // slot
    private int aotvSlot;

    // v: middle of block
    public static Vector3d calculateSidePosToEtherWarp(double x, double y, double z) throws Exception {
        int[] dx = new int[]{1, -1, 0, 0};
        int[] dz = new int[]{0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            float tx = (float) x + dx[i];
            float tz = (float) z + dz[i];
            if (BlockUtils.isBlockAir(tx, (float) y, tz) && BlockUtils.isBlockAir(tx, (float) y - 1, tz))
                return new Vector3d(x + dx[i] / 2F, y, z + dz[i] / 2F);
        }
        throw new Exception();
    }

    public static Vector3d whereShouldIEtherWarpTo(double curY, double x, double y, double z) throws Exception {
        // h: half height of block
        double h = Math.round(y * 4) % 4 == 1 ? 0.25 : 0.5;
        if (curY >= y + 20) return new Vector3d(x, y + h, z);
        return calculateSidePosToEtherWarp(x, y, z);
    }

    // v: standing at v, v_y + 0.25
    private static Vector2d blazeCanHit(Vector3d v, int i, boolean usingTerminator) {
        BlazeInfo blazeInfo = blazes.get(i);
        double x = blazeInfo.cube.x;
        double y = blazeInfo.cube.y;
        double z = blazeInfo.cube.z;
        Vector2d invalid = new Vector2d(10000, 10000);
        Vector2d yawAndPitch = ShortbowUtils.getDirection(v.x, v.y + 0.25 + 1.62, v.z, x, y, z);
        log.append(String.format("trying blaze can hit: %.2f %.2f %.2f, %d", v.x, v.y, v.z, i));
        if (usingTerminator) {
            // check middle
            if (checkMiddle(v, i, yawAndPitch)) return yawAndPitch;

            // check left
            yawAndPitch = ShortbowUtils.getDirection(v.x, v.y + 0.25 + 1.62, v.z, x, y, z, false);
            Vector2d[] middleRightYawPitch = ShortbowUtils.getMiddleRightYawPitchByLeft(yawAndPitch);
            if (checkLeft(v, i, yawAndPitch)) return middleRightYawPitch[0];

            // check right
            Vector2d[] leftMiddleYawPitch = ShortbowUtils.getLeftMiddleYawPitchByRight(yawAndPitch);
            if (checkRight(v, i, yawAndPitch)) return leftMiddleYawPitch[1];

            return invalid;
        } else {
            if (!canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazeInfo.cube, true)) {
                log.append("can't hit, ????").append("\ntemp log for this:\n");
                log.append(String.format("blaze is at %.2f %.2f %.2f\n", x, y, z));
                log.append(String.format("vec is at %.2f %.2f %.2f\n", v.x, v.y + 0.25 + 1.62, v.z));
                log.append(String.format("facing at %.2f %.2f\n", yawAndPitch.x, yawAndPitch.y));
                log.append(tempLog);
                return invalid;
            }
            for (int j = i + 1; j < blazes.size(); j++)
                if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(j).cube, true)) {
                    log.append("can't hit, because it may shoot " + j + " th blaze!").append("\n");
                    return invalid;
                }
            // should check distance, but lazy
            for (Cube cube : blocks)
                if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, cube, true)) {
                    log.append(String.format("can't hit, because it may hit block at %.2f %.2f %.2f", cube.x, cube.y, cube.z)).append("\n");
                    return invalid;
                }
            return yawAndPitch;
        }
    }

    private static boolean checkMiddle(Vector3d v, int i, Vector2d yawAndPitch) {
        Vector2d[] leftRightYawPitch = ShortbowUtils.getLeftRightYawPitchByMiddle(yawAndPitch);
        if (!canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(i).cube, true)) {
            log.append(String.format("middle: can't hit, ???")).append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", blazes.get(i).cube.x, blazes.get(i).cube.y, blazes.get(i).cube.z));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", v.x, v.y + 0.25 + 1.62, v.z));
            log.append(String.format("facing at %.2f %.2f\n", yawAndPitch.x, yawAndPitch.y));
            log.append(tempLog);
            return false;
        }
        for (int j = i + 1; j < blazes.size(); j++) {
            log.append("check middle trying to hit " + j + " th blaze\n");
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, leftRightYawPitch[0], blazes.get(j).cube, false)) return false;
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(j).cube, true)) return false;
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, leftRightYawPitch[1], blazes.get(j).cube, false)) return false;
        }
        // should check distance, but lazy
        for (Cube cube : blocks)
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, cube, true))
                return false;
        return true;
    }

    private static boolean checkLeft(Vector3d v, int i, Vector2d yawAndPitch) {
        Vector2d[] middleRightYawPitch = ShortbowUtils.getMiddleRightYawPitchByLeft(yawAndPitch);
        if (!canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(i).cube, false)) {
            log.append(String.format("left: can't hit, ???")).append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", blazes.get(i).cube.x, blazes.get(i).cube.y, blazes.get(i).cube.z));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", v.x, v.y + 0.25 + 1.62, v.z));
            log.append(String.format("facing at %.2f %.2f\n", middleRightYawPitch[0].x, middleRightYawPitch[0].y));
            log.append(tempLog);
            return false;
        }
        for (int j = i + 1; j < blazes.size(); j++) {
            log.append("check left trying to hit " + j + " th blaze\n");
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(j).cube, false)) return false;
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, middleRightYawPitch[0], blazes.get(j).cube, true)) return false;
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, middleRightYawPitch[1], blazes.get(j).cube, false)) return false;
        }
        // should check distance, but lazy
        for (Cube cube : blocks)
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, cube, false))
                return false;
        return true;
    }

    private static boolean checkRight(Vector3d v, int i, Vector2d yawAndPitch) {
        Vector2d[] leftMiddleYawPitch = ShortbowUtils.getLeftMiddleYawPitchByRight(yawAndPitch);
        if (!canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(i).cube, false)) {
            log.append(String.format("right: can't hit, ???")).append("\ntemp log for this:\n");
            log.append(String.format("blaze is at %.2f %.2f %.2f\n", blazes.get(i).cube.x, blazes.get(i).cube.y, blazes.get(i).cube.z));
            log.append(String.format("vec is at %.2f %.2f %.2f\n", v.x, v.y + 0.25 + 1.62, v.z));
            log.append(String.format("facing at %.2f %.2f\n", leftMiddleYawPitch[1].x, leftMiddleYawPitch[1].y));
            log.append(tempLog);
            return false;
        }
        for (int j = i + 1; j < blazes.size(); j++) {
            log.append("check right trying to hit " + j + " th blaze\n");
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, leftMiddleYawPitch[0], blazes.get(j).cube, false)) return false;
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, leftMiddleYawPitch[1], blazes.get(j).cube, true)) return false;
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, blazes.get(j).cube, false)) return false;
        }
        // should check distance, but lazy
        for (Cube cube : blocks)
            if (canHit(v.x, v.y + 0.25 + 1.62, v.z, yawAndPitch, cube, false))
                return false;
        return true;
    }

    public static void test() {
        blazes.clear();
        lowFirst = true;
        blazes.add(new BlazeInfo(45, 51, 143, 10, null));
        blazes.add(new BlazeInfo(49, 59, 143, 100, null));
        System.out.println(checkMiddle(
                new Vector3d(40.5, 42.75, 143.5), 0,
                new Vector2d(-96.34, -59.24)
        ));
        System.out.println(log);
    }

    // isMiddle: is the middle arrow
    public static boolean canHit(double x, double y, double z, Vector2d yawAndPitch, Cube cube, boolean isMiddle) {
        double yaw = yawAndPitch.x;
        double pitch = yawAndPitch.y;
        // solve in xz plane
        tempLog = new StringBuilder();
        log.append(String.format("trying canHit with yaw: %.2f, pitch: %.2f", yaw, pitch)).append("\n");
        double PI = Math.PI;
        double sx = cube.x - cube.w, sz = cube.z - cube.w;
        double tx = cube.x + cube.w, tz = cube.z + cube.w;
        if (isInXZSquare(x, z, sx, sz, tx, tz)) return true;
        // calculate intersections with 4 edges of the square
        // z * cos(alpha) = sin(alpha) * (x - x0) + z0 * cos(alpha)
        double xzAlpha = (yaw + 90) * PI / 180;
        double tan = Math.tan(xzAlpha);
        double zSx = z + (sx - x) * tan, xSz = x + (sz - z) * (1 / tan);
        double zTx = z + (tx - x) * tan, xTz = x + (tz - z) * (1 / tan);
        ArrayList<Vector2d> intersects = new ArrayList<>();
        if (zSx >= sz && zSx < tz) intersects.add(new Vector2d(sx, zSx));
        if (zTx > sz && zTx <= tz) intersects.add(new Vector2d(tx, zTx));
        if (xSz > sx && xSz <= tx) intersects.add(new Vector2d(xSz, sz));
        if (xTz >= sx && xTz < tx) intersects.add(new Vector2d(xTz, tz));
        if (intersects.size() <= 1) {
            log.append("canHit false: intersect size too small").append("\n");
            return false;
        }
        if (intersects.size() != 2) {
            ChatLib.chat("NOOOOOOOOO");
            return false;
        }

        // solve in y-X plane
        sx = intersects.get(0).x;
        tx = intersects.get(1).x;
        sz = intersects.get(0).y;
        tz = intersects.get(1).y;

        double sX = Math.sqrt((x - sx) * (x - sx) + (z - sz) * (z - sz));
        double tX = Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        pitch = -PI / 180 * pitch;

        double sy = (cube.y - cube.h) - y;
        double ty = (cube.y + cube.h) - y;
        double sY = ShortbowUtils.getProjectileFunction(sX, pitch, isMiddle);
        double tY = ShortbowUtils.getProjectileFunction(tX, pitch, isMiddle);
        log.append(String.format("sx: %.2f, tx: %.2f, sz: %.2f, tz: %.2f", sx, tx, sz, tz)).append("\n");
        log.append(String.format("sX: %.2f, tX: %.2f, pitch: %.2f", sX, tX, pitch)).append("\n");
        log.append(String.format("sy: %.2f, ty: %.2f, sY: %.2f, tY: %.2f", sy, ty, sY, tY)).append("\n");
        // not exactly, but most of the case
        boolean res = MathUtils.isBetween(sY, sy, ty) || MathUtils.isBetween(tY, sy, ty) ||
                (MathUtils.isBetween(sy, sY, tY) && MathUtils.isBetween(ty, sY, tY));
        log.append("res: " + res + "\n");
        return res;
    }

    public static boolean isInXZSquare(double x, double z, double sx, double sz, double tx, double tz) {
        return MathUtils.isBetween(x, sx, tx) && MathUtils.isBetween(z, sz, tz);
    }

    public static void setRoom(Room blazeRoom) {
        room = blazeRoom;
    }

    // current player position
    private List<Vector3d> getPossibleEtherwarpPointsFromPlayer() throws Exception {
        ArrayList<Vector3d> res = new ArrayList<>();
        for (Vector3d vec : places) {
            if (canEtherwarpFromCur(vec)) {
                log.append("can go to " + vec).append("\n");
                res.add(vec);
            }
        }
        return res;
    }

    // from: -0.25
    private boolean canEtherwarp(Vector3d from, Vector3d to) throws Exception {
        return noBlocksBetween(
                new Vector3d(from.x, from.y + 0.25 + 1.54, from.z),
                whereShouldIEtherWarpTo(from.y + 0.25 + 1.54, to.x, to.y, to.z)
        );
    }

    // from: player
    private boolean canEtherwarpFromCur(Vector3d to) throws Exception {
        return noBlocksBetween(
                new Vector3d(getX(getPlayer()), getY(getPlayer()) + 1.54, getZ(getPlayer())),
                whereShouldIEtherWarpTo(getY(getPlayer()), to.x, to.y, to.z)
        );
    }

    private List<Vector3d> getPossibleEtherwarpPointsToSecret(Vector3d secret) throws Exception {
        ArrayList<Vector3d> res = new ArrayList<>();
        for (Vector3d vec : places) {
            if (canEtherwarp(vec, secret)) {
                log.append("can go from " + vec).append("\n");
                res.add(vec);
            }
        }
        return res;
    }

    private ArrayList<BlazeOrder> getOrderSequence(boolean usingTerminator) throws Exception {
        ArrayList<BlazeOrder> result = new ArrayList<>();
        int hit = 0;
        Vector3d lastTp = null;
        while (hit < blazes.size()) {
            Vector3d res = null;
            int count = hit;
            List<Vector3d> tryPlaces = new ArrayList<>();
            if (lastTp == null) tryPlaces = getPossibleEtherwarpPointsFromPlayer();
            else tryPlaces.addAll(transGraph.get(lastTp));

            // iterate over all places that can be tped on, find the max
            ArrayList<BlazeOrder> maxSequence = new ArrayList<>();
            for (Vector3d vec : tryPlaces) {
                int i = hit;
                ArrayList<BlazeOrder> tempSequence = new ArrayList<>();
                tempSequence.add(new BlazeOrder(vec.x, vec.y, vec.z));
                while (i < blazes.size()) {
                    Vector2d yawAndPitch = blazeCanHit(vec, i, usingTerminator);
                    if (yawAndPitch.x > 1000) break;
                    // add this edge case to avoid hitting block
                    if (yawAndPitch.y > 70) break;
                    tempSequence.add(new BlazeOrder(yawAndPitch.x, yawAndPitch.y, blazes.get(i).hpEntity));
                    log.append("Can hit: " + vec + ", " + i).append("\n");
                    i++;
                }
                if (i > count) {
                    count = i;
                    res = vec;
                    maxSequence = tempSequence;
                }
            }
            if (res == null) {
                ChatLib.chat("CANT FIND GOOD POSITION!");
                throw new Exception();
            }
            result.addAll(maxSequence);
            log.append(String.format("decided to tp to: %.2f %.2f %.2f\n\n",
                    maxSequence.get(0).x, maxSequence.get(0).y, maxSequence.get(0).z));
            hit = count;
            lastTp = res;
        }
        return result;
    }

    private void executeOrder(int slot, ArrayList<BlazeOrder> seq) throws Exception {
        long lastBlazeHitTime = 0;
        for (BlazeOrder todo : seq) {
            if (!should) break;
            if (todo.type == BlazeOrder.Type.WARP) {
                tpPacketReceived = false;
                etherWarpTo(new Vector3d(todo.x, todo.y, todo.z));
            } else {
                arrowShot = false;
                log.append("shooting to " + todo.yaw + ", " + todo.pitch).append("\n");
                ControlUtils.setHeldItemIndex(slot);
                ControlUtils.faceSlowly(todo.yaw, todo.pitch);
                facingYaw = todo.yaw;
                facingPitch = todo.pitch;
                double distance = Math.sqrt(MathUtils.distanceSquareFromPlayer(todo.entity));
                long estimate = MathUtils.floor(distance * 50 / 1.7);
                log.append(String.format("distance: %.2f, estimate: %d\n", distance, estimate));
                Thread.sleep(Configs.TurnShootDelay);
                while (TimeUtils.curTime() + estimate < lastBlazeHitTime + 50 && should)
                    Thread.sleep(20);
                if (!should) break;
                ControlUtils.leftClick();

                int cnt = 0;
                while (!arrowShot && should) {
                    if (cnt > 20) {
                        ControlUtils.leftClick();
                        cnt = 0;
                    }
                    Thread.sleep(20);
                    cnt++;
                }
                lastBlazeHitTime = estimate + TimeUtils.curTime();
                Thread.sleep(100);
                log.append("lastHitime: " + lastBlazeHitTime + "\n");
            }
        }
    }

    private Vector3d getSecretPoint() throws Exception {
        int y = lowFirst ? 118 : 68;
        int[] dx = new int[]{1, -1, -1, 1};
        int[] dz = new int[]{1, 1, -1, -1};
        for (int i = 0; i < 4; i++) {
            int tx, tz;
            tx = room.x + dx[i] * 5;
            tz = room.z + dz[i] * 8;
            if (BlockUtils.isBlockBedRock(tx, y, tz) && BlockUtils.isBlockWater(tx, y, tz - dz[i]))
                return new Vector3d(tx + 0.5, y + 0.75, tz + 0.5);
            tx = room.x + dx[i] * 8;
            tz = room.z + dz[i] * 5;
            if (BlockUtils.isBlockBedRock(tx, y, tz) && BlockUtils.isBlockWater(tx - dx[i], y, tz))
                return new Vector3d(tx + 0.5, y + 0.75, tz + 0.5);
        }
        throw new Exception();
    }

    private void grabSecret() throws Exception {
        Vector3d secret = getSecretPoint(); // -0.25
        log.append(String.format("\nsecret is at: %.2f %.2f %.2f\n", secret.x, secret.y, secret.z));
        // directly tp to secret
        if (canEtherwarpFromCur(secret)) {
            etherWarpTo(secret);
            return;
        }

        // get places that can tp to secret
        Vector3d startVec = new Vector3d(-1, -1, -1);
        transGraph.put(startVec, new ArrayList<>());
        List<Vector3d> endPositions = getPossibleEtherwarpPointsToSecret(secret);
        for (Vector3d pos : endPositions)
            transGraph.get(pos).add(secret);
        List<Vector3d> startPositions = getPossibleEtherwarpPointsFromPlayer();
        for (Vector3d pos : startPositions)
            transGraph.get(startVec).add(pos);
        // bfs
        HashMap<Vector3d, Integer> steps = new HashMap<>();
        HashMap<Vector3d, Vector3d> pre = new HashMap<>();
        Deque<Vector3d> queue = new ArrayDeque<>();
        queue.add(startVec);
        steps.put(startVec, 0);
        boolean found = false;
        while (!queue.isEmpty()) {
            Vector3d front = queue.pollFirst();
            int step = steps.get(front);
            if (found) break;
            for (Vector3d vec : transGraph.get(front)) {
                if (steps.getOrDefault(vec, 10000) > step + 1) {
                    steps.put(vec, step + 1);
                    pre.put(vec, front);
                    queue.offerLast(vec);
                    if (vec == secret) {
                        found = true;
                        break;
                    }
                }
            }
        }
        // gogogo
        if (!found) {
            ChatLib.chat("Can't find a path to get the secret!");
            throw new Exception();
        }
        Vector3d cur = secret;
        ArrayList<Vector3d> revertedPath = new ArrayList<>();
        while (cur != startVec) {
            revertedPath.add(cur);
            cur = pre.get(cur);
        }
        for (int i = revertedPath.size() - 1; i >= 0; i--) {
            Vector3d toWarp = revertedPath.get(i);
            etherWarpTo(toWarp);
        }
    }

    // toWarp: -0.25
    private void etherWarpTo(Vector3d toWarp) throws Exception {
        tpPacketReceived = false;
        Vector3d v = whereShouldIEtherWarpTo(getY(getPlayer()), toWarp.x, toWarp.y, toWarp.z);
        log.append(String.format("etherwarp to %.2f %.2f %.2f", toWarp.x, toWarp.y, toWarp.z)).append("\n");
        log.append(String.format("aim at %.2f %.2f %.2f", v.x, v.y, v.z)).append("\n");
        ControlUtils.setHeldItemIndex(aotvSlot);
        ControlUtils.etherWarp(v.x, v.y, v.z);

        int cnt = 0;
        while (!tpPacketReceived && should) {
            Thread.sleep(20);
            cnt++;
            if (cnt >= 50) {
                ChatLib.chat("Too long no packet, please try again.");
                throw new Exception();
            }
        }

        Thread.sleep(Configs.EtherWarpDelayAfter);
        if (MathUtils.distanceSquareFromPlayer(toWarp.x, toWarp.y + 0.25, toWarp.z) > 1) {
            ChatLib.chat("Failed to etherwarp!");
            log.append(String.format("Player is at %.2f %.2f %.2f", getX(getPlayer()), getY(getPlayer()), getZ(getPlayer()))).append("\n");
            throw new Exception();
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled || !Configs.AutoBlaze || !Dungeon.currentRoom.equals("Blaze")) {
            deactivate();
            return;
        }
        if (keyBind.isPressed()) {
            should = !should;
            if (should) ChatLib.chat("Auto Blaze &aactivated");
            else ChatLib.chat("Auto Blaze &cdeactivated");
        }
        if (!should || !Dungeon.isFullyScanned || room == null) {
            deactivate();
            return;
        }
        // in blaze, not cleared
        lowFirst = (room.core == 1103121487);
        if (shootingThread != null && shootingThread.isAlive()) return;
        shootingThread = new Thread(() -> {
            try {
                log.append("lowFirst: " + lowFirst).append("\n");
                calculatePlaces();
                calculateBlazes();
                calculateBlocks();
                boolean usingTerminator = true;
                aotvSlot = HotbarUtils.aotvSlot;
                int terminatorSlot = HotbarUtils.terminatorSlot;
                int shortBowSlot = HotbarUtils.shortBowSlot;
                int shootSlot;
                log.append("starting!").append("\n");
                if (aotvSlot == -1) {
                    ChatLib.chat("Requires aotv in hotbar.");
                    throw new Exception();
                }
                if (terminatorSlot != -1) shootSlot = terminatorSlot;
                else {
                    if (shortBowSlot == -1) {
                        ChatLib.chat("Requires terminator / shortbow in hotbar.");
                        throw new Exception();
                    }
                    shootSlot = shortBowSlot;
                    usingTerminator = false;
                }
                // killing blaze
                ArrayList<BlazeOrder> seq = getOrderSequence(usingTerminator);
                executeOrder(shootSlot, seq);

                if (Configs.AutoBlazeSecret && should) {
                    grabSecret();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                deactivate();
            }
        });
        shootingThread.start();
    }

    private void deactivate() {
        if (should) {
            should = false;
            ChatLib.chat("Auto Blaze &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onBlazeDeath(LivingDeathEvent event) {
        if (event.entity instanceof EntityBlaze) {
            log.append("blaze " + event.entity + " died!\n");
        }
    }

    public void calculatePlaces() throws Exception {
        int x = room.x, z = room.z;
        int[] dx = new int[]{7, -7, 0, 0};
        int[] dz = new int[]{0, 0, 7, -7};
        // places to aotv to
        // 79.5, 115.5
        int min2Y = 159, max2Y = 231;
        if (!lowFirst) {
            min2Y = 59;
            max2Y = 131;
        }
        for (int y2 = min2Y; y2 <= max2Y; y2 += 9) {
            // y2: stands there
            float y = y2 / 2F - 0.25F;
            // y: place to teleport to
            for (int i = 0; i < 4; i++) {
                float tx = x + dx[i] + 0.5F;
                float tz = z + dz[i] + 0.5F;
                if (!BlockUtils.isBlockAir(tx, y, tz) &&
                        BlockUtils.isBlockAir(tx, y + 1, tz)) {
                    places.add(new Vector3d(tx, y, tz));
                    log.append(String.format("add new place: %.2f %.2f %.2f", tx, y, tz)).append("\n");

                    Cube blockCube;
                    if (y2 % 2 == 0) blockCube = new Cube(tx, y2 / 2F - 0.5, tz, 0.5, 0.5);
                    else blockCube = new Cube(tx, y, tz, 0.5, 0.25);
                    blocks.add(blockCube);
                    log.append(String.format("block: %.2f %.2f %.2f", blockCube.x, blockCube.y, blockCube.z)).append("\n");
                }
            }
        }
        transGraph.clear();
        ArrayList<Line> lines = new ArrayList<>();
        for (Vector3d vecFrom : places) {
            transGraph.put(vecFrom, new ArrayList<>());
            for (Vector3d vecTo : places) {
                Vector3d startFrom = new Vector3d(vecFrom.x, vecFrom.y + 1.54 + 0.25, vecFrom.z);
                Vector3d endTo = whereShouldIEtherWarpTo(vecFrom.y + 0.25 + 1.54, vecTo.x, vecTo.y, vecTo.z);
                boolean noBlock = noBlocksBetween(startFrom, endTo);

                lines.add(new Line(startFrom, endTo, noBlock));
                if (noBlock) {
                    transGraph.get(vecFrom).add(vecTo);
                }
            }
        }
//        this.lines = lines;
    }

    // exact coords
    private boolean noBlocksBetween(Vector3d from, Vector3d to) {
        // calculate from from to to
        return BlockUtils.getNearestBlock(from, to) == null;
    }

    public void calculateBlazes() {
        List<BlazeInfo> newBlaze = new ArrayList<>();
        for (Entity entity : getWorld().loadedEntityList) {
            String name = entity.getName();
            if (name.contains("Blaze") && name.contains("/")) {
                String blazeName = ChatLib.removeFormatting(entity.getName());
                try {
                    int health = Integer.parseInt(blazeName.substring(blazeName.indexOf("/") + 1, blazeName.length() - 1));
                    newBlaze.add(new BlazeInfo(getX(entity), getY(entity) - 1, getZ(entity), health, entity));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
        blazes.clear();
        blazes.addAll(newBlaze);
        blazes.sort((a, b) -> lowFirst ? a.hp - b.hp : b.hp - a.hp);
        for (BlazeInfo blazeInfo : blazes) {
            log.append(String.format("blaze: %.2f %.2f %.2f %d", blazeInfo.cube.x, blazeInfo.cube.y, blazeInfo.cube.z, blazeInfo.hp)).append("\n");
        }
    }

    public void calculateBlocks() {
        List<Cube> newBlocks = new ArrayList<>();
        int startY = 71, endY = 118;
        if (!lowFirst) {
            startY = 20;
            endY = 69;
        }
        for (int i = startY; i < endY; i++) {
//            if (!BlockUtils.isBlockAir(room.x, i, room.z)) {
            newBlocks.add(new Cube(room.x + 0.5, i + 0.5, room.z + 0.5, 0.5, 0.5));
            log.append("block: " + room.x + ", " + i + ", " + room.z).append("\n");
//            }
        }
        blocks.clear();
        blocks.addAll(newBlocks);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        blazes.clear();
        blocks.clear();
        places.clear();
        setRoom(null);
        log = new StringBuilder();
    }

    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.packet;
            tpPacketReceived = true;
            log.append(PacketUtils.getPosLookPacket(packet));
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        if (entity instanceof EntityArrow) {
            double dis2 = MathUtils.distanceSquareFromPlayer(entity.posX, entity.posY, entity.posZ);
            if (dis2 < 5 * 5) {
                log.append(String.format("entity join: " + entity.toString() + ", %.2f", dis2)).append("\n");
                Vec3 vArrow = new Vec3(entity.posX - getX(getPlayer()), entity.posY - getY(getPlayer()), entity.posZ - getZ(getPlayer()));
                double facingAlpha = (facingYaw + 90) * Math.PI / 180;
                double facingBeta = -facingPitch * Math.PI / 180;
                Vec3 vFacing = new Vec3(
                        Math.cos(facingBeta) * Math.cos(facingAlpha),
                        Math.sin(facingBeta),
                        Math.cos(facingBeta) * Math.sin(facingAlpha)
                );
                double diff = Math.acos(vArrow.dotProduct(vFacing) / vArrow.lengthVector() / vFacing.lengthVector());

                double dz = entity.posZ - getZ(getPlayer());
                double dx = entity.posX - getX(getPlayer());
                double arrowAlpha = Math.atan2(dz, dx);
                double diffAlpha = Math.abs(arrowAlpha - facingAlpha);
                diffAlpha = Math.min(diffAlpha, 2 * Math.PI - diffAlpha);

                log.append(String.format("diff: %.2f, diffAlpha: %.2f\n", diff, diffAlpha));
                if (diff < 0.5 && diffAlpha < 0.1)
                    arrowShot = true;
            }
        }
    }

    static class BlazeInfo {
        Cube cube;
        int hp;
        Entity hpEntity;

        public BlazeInfo(double x, double y, double z, int hp, Entity entity) {
            cube = new Cube(x, y, z, 1 * Configs.BlazeScale / 100F, 0.3 * Configs.BlazeScale / 100F);
            this.hp = hp;
            this.hpEntity = entity;
        }
    }

    static class BlazeOrder {
        Type type;
        double yaw, pitch;
        double x, y, z;
        double w, h;
        Entity entity;

        public BlazeOrder(double x, double y, double z) {
            type = Type.WARP;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlazeOrder(double yaw, double pitch, Entity entity) {
            type = Type.SHOOT;
            this.yaw = yaw;
            this.pitch = pitch;
            this.entity = entity;
        }

        enum Type {
            WARP, SHOOT
        }
    }
}
