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
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
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
    private final KeyBind keyBind = new KeyBind("Auto Blaze", Keyboard.KEY_NONE);
    public boolean should = false;
    private Thread shootingThread = null;
    private boolean tpPacketReceived = false;
    private boolean arrowShot = false;
    private double facingYaw = 10000;

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

    private static Vector3d diff(Vector3d from, Vector3d to) {
        return new Vector3d(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    private static Vector3d add(Vector3d a, Vector3d b) {
        return new Vector3d(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static Vector3d mul(double x, Vector3d v) {
        return new Vector3d(v.x * x, v.y * x, v.z * x);
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
        double xzAlpha = (yaw + 90)  * PI / 180;
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

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Auto Blaze &aactivated" : "Auto Blaze &cdeactivated");
        }
        if (!Configs.AutoBlaze || !should || !Dungeon.isFullyScanned || room == null) return;
        lowFirst = (room.core == 1103121487); // ??
        if (!Dungeon.currentRoom.equals("Blaze")) return;
        if (!room.checkmark.equals("")) return;
        // in blaze, not cleared
        if (shootingThread != null && shootingThread.isAlive()) return;
        shootingThread = new Thread(() -> {
            try {
                log.append("lowFirst: " + lowFirst).append("\n");
                calculatePlaces();
                calculateBlazes();
                calculateBlocks();
                boolean usingTerminator = true;
                int aotvSlot = HotbarUtils.aotvSlot;
                int terminatorSlot = HotbarUtils.terminatorSlot;
                int shortBowSlot = HotbarUtils.shortBowSlot;
                int slot;
                log.append("starting!").append("\n");
                if (aotvSlot == -1) {
                    ChatLib.chat("Requires aotv in hotbar.");
                    throw new Exception();
                }
                if (terminatorSlot == -1) {
                    if (shortBowSlot == -1) {
                        ChatLib.chat("Requires terminator / shortbow in hotbar.");
                        throw new Exception();
                    }
                    slot = shortBowSlot;
                    usingTerminator = false;
                } else {
                    slot = terminatorSlot;
                }
                int hit = 0;
                ArrayList<Sequence> seq = new ArrayList<>();

                Vector3d lastTp = null;
                while (hit < blazes.size()) {
                    Vector3d res = null;
                    int count = hit;
                    List<Vector3d> tryPlaces = new ArrayList<>();
                    if (lastTp == null) {
                        for (Vector3d vec : places) {
                            if (noBlocksBetween(
                                    new Vector3d(getX(getPlayer()), getY(getPlayer()) + 1.54, getZ(getPlayer())),
                                    whereShouldIEtherWarpTo(getY(getPlayer()), vec.x, vec.y, vec.z)
                            )) {
                                log.append("can go to " + vec).append("\n");
                                tryPlaces.add(vec);
                            }
                        }
                    } else {
                        tryPlaces.addAll(transGraph.get(lastTp));
                    }
                    // iterate over all places that can be tped on, find the max
                    ArrayList<Sequence> maxSequence = new ArrayList<>();
                    for (Vector3d vec : tryPlaces) {
                        int i = hit;
                        ArrayList<Sequence> tempSequence = new ArrayList<>();
                        tempSequence.add(new Sequence(vec.x, vec.y, vec.z));
                        while (i < blazes.size()) {
                            Vector2d yawAndPitch = blazeCanHit(vec, i, usingTerminator);
                            if (yawAndPitch.x > 1000) break;
                            // add this edge case to avoid hitting block
                            if (yawAndPitch.y > 70) break;
                            tempSequence.add(new Sequence(yawAndPitch.x, yawAndPitch.y, blazes.get(i).hpEntity));
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
                        return;
                    }
                    seq.addAll(maxSequence);
                    log.append(String.format("decided to tp to: %.2f %.2f %.2f\n\n",
                            maxSequence.get(0).x, maxSequence.get(0).y, maxSequence.get(0).z));
                    hit = count;
                    lastTp = res;
                }
                // go hit
                long lastBlazeHitTime = 0;
                for (Sequence todo : seq) {
                    if (!should) break;
                    if (todo.type == Sequence.Type.WARP) {
                        Vector3d v = whereShouldIEtherWarpTo(getY(getPlayer()), todo.x, todo.y, todo.z);
                        log.append(String.format("etherwarp to %.2f %.2f %.2f", todo.x, todo.y, todo.z)).append("\n");
                        log.append(String.format("aim at %.2f %.2f %.2f", v.x, v.y, v.z)).append("\n");
                        tpPacketReceived = false;
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
                        Thread.sleep(100);
                        if (MathUtils.distanceSquareFromPlayer(todo.x, todo.y + 0.25, todo.z) > 1) {
                            ChatLib.chat("Failed to etherwarp!");
                            log.append(String.format("Player is at %.2f %.2f %.2f", getX(getPlayer()), getY(getPlayer()), getZ(getPlayer()))).append("\n");
                            throw new Exception();
                        }
                    } else {
                        arrowShot = false;
                        log.append("shooting to " + todo.yaw + ", " + todo.pitch).append("\n");
                        ControlUtils.setHeldItemIndex(slot);
                        ControlUtils.faceSlowly(todo.yaw, todo.pitch);
                        facingYaw = todo.yaw;
                        double distance = Math.sqrt(MathUtils.distanceSquareFromPlayer(todo.entity));
                        long estimate = MathUtils.floor(distance * 50 / 1.7);
                        log.append(String.format("distance: %.2f, estimate: %d\n", distance, estimate));
                        Thread.sleep(200);
                        while (TimeUtils.curTime() + estimate < lastBlazeHitTime + 50 && should)
                            Thread.sleep(20);
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
                        log.append("lastHitime: " + lastBlazeHitTime + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (should) {
                    should = false;
                    ChatLib.chat("Auto Blaze &cdeactivated");
                }
            }
        });
        shootingThread.start();
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
        Vector3d v = new Vector3d();
        v.normalize(diff(from, to));
        double epsilon = 1e-5;
        Vector3d curV = from;
        while (true) {
            double xScale = (epsilon + MathUtils.ceil(curV.x) - curV.x) / v.x;
            double yScale = (epsilon + MathUtils.ceil(curV.y) - curV.y) / v.y;
            double zScale = (epsilon + MathUtils.ceil(curV.z) - curV.z) / v.z;
            if (v.x < 0) xScale = (MathUtils.floor(curV.x) - epsilon - curV.x) / v.x;
            if (v.y < 0) yScale = (MathUtils.floor(curV.y) - epsilon - curV.y) / v.y;
            if (v.z < 0) zScale = (MathUtils.floor(curV.z) - epsilon - curV.z) / v.z;

            double scale = xScale;
            if (yScale < scale) scale = yScale;
            if (zScale < scale) scale = zScale;
            curV = add(curV, mul(scale, v));
            if (MathUtils.floor(curV.x) == MathUtils.floor(to.x) &&
                    MathUtils.floor(curV.y) == MathUtils.floor(to.y) &&
                    MathUtils.floor(curV.z) == MathUtils.floor(to.z)) break;
            if (!BlockUtils.isBlockAir(curV.x, curV.y, curV.z)) return false;
        }
        return true;
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
            log.append(String.format("received packet! %.2f, %.2f; %.2f, %.2f, %.2f: ",
                    packet.getYaw(), packet.getPitch(), packet.getX(), packet.getY(), packet.getZ()));
            if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X)) log.append("X");
            if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y)) log.append("Y");
            if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z)) log.append("Z");
            if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT)) log.append(" XR");
            if (packet.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT)) log.append(" YR");
            log.append("\n");
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        if (entity instanceof EntityArrow) {
            double dis2 = MathUtils.distanceSquareFromPlayer(entity.posX, entity.posY, entity.posZ);
            if (dis2 < 5 * 5) {
                log.append(String.format("entity join: " + entity.toString() + ", %.2f", dis2)).append("\n");
                double dz = entity.posZ - getZ(getPlayer());
                double dx = entity.posX - getX(getPlayer());
                double arrowAlpha = Math.atan2(dz, dx);
                double facingAlpha = (facingYaw + 90) * Math.PI / 180;
                double diff = Math.abs(arrowAlpha - facingAlpha);
                diff = Math.min(diff, 2 * Math.PI - diff);
                log.append(String.format("arrow: %.2f, facing: %.2f, diff: %.2f\n", arrowAlpha, facingAlpha, diff));
                if (diff < 0.1)
                    arrowShot = true;
            }
        }
    }

    static class BlazeInfo {
        Cube cube;
        int hp;
        Entity hpEntity;

        public BlazeInfo(double x, double y, double z, int hp, Entity entity) {
            // width: tho it's 0.3, set it to 0.5 to reduce errors
            // height: 1 -> 1.2
            cube = new Cube(x, y, z, 1.2, 0.5);
            this.hp = hp;
            this.hpEntity = entity;
        }
    }

    static class Sequence {
        Type type;
        double yaw, pitch;
        double x, y, z;
        double w, h;
        Entity entity;

        public Sequence(double x, double y, double z) {
            type = Type.WARP;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Sequence(double yaw, double pitch, Entity entity) {
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
