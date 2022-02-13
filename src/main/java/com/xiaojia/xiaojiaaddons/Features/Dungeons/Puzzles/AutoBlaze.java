package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;
import static com.xiaojia.xiaojiaaddons.utils.SkyblockUtils.getPing;

public class AutoBlaze {
    private final KeyBind keyBind = new KeyBind("Auto Blaze", Keyboard.KEY_NONE);
    public boolean should = false;
    public static Room room = null;

    private final static List<BlazeInfo> blazes = new ArrayList<>();
    private final static List<Cube> blocks = new ArrayList<>();
    private final static List<Vector3d> places = new ArrayList<>();  // aotv

    private static boolean lowFirst = false;
    private Thread shootingThread = null;
    private boolean tpPacketReceived = false;
    private boolean arrowShot = false;

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
                ChatLib.chat("lowFirst: " + lowFirst);
                calculatePlaces();
                calculateBlazes();
                calculateBlocks();
                int aotvSlot = HotbarUtils.aotvSlot;
                int terminatorSlot = HotbarUtils.terminatorSlot;
                ChatLib.chat("starting!");
                if (aotvSlot == -1) {
                    ChatLib.chat("Requires aotv in hotbar.");
                    throw new Exception();
                }
                if (terminatorSlot == -1) {
                    ChatLib.chat("Requires terminator in hotbar.");
                    throw new Exception();
                }
                int hit = 0;
                ArrayList<Sequence> seq = new ArrayList<>();
                while (hit < blazes.size()) {
                    Vector3d res = null;
                    int count = hit;
                    for (Vector3d vec : places) {
                        int i = hit;
                        while (i < blazes.size() && blazeCanHit(vec, i)) {
                            ChatLib.debug("Can hit: " + vec + ", " + i);
                            i++;
                        }
                        if (i > count) {
                            count = i;
                            res = vec;
                        }
                    }
                    if (res == null) {
                        ChatLib.chat("CANT FIND GOOD POSITION!");
                        return;
                    }
                    seq.add(new Sequence(res.x, res.y, res.z, 0.5, Math.round(res.y * 4) % 4 == 1 ? 0.25 : 0.5));
                    int i = hit;
                    while (i < count) {
                        BlazeInfo blazeInfo = blazes.get(i);
                        double x = blazeInfo.cube.x;
                        double y = blazeInfo.cube.y;
                        double z = blazeInfo.cube.z;
                        Vector2d yawAndPitch = ShortbowUtils.getDirection(res.x, res.y + 0.25 + 1.5, res.z, x, y, z);
                        seq.add(new Sequence(yawAndPitch.x, yawAndPitch.y));
                        i++;
                    }
                    hit = count;
                }
                // go hit
                for (Sequence todo : seq) {
                    if (todo.type == Sequence.Type.WARP) {
                        Vector3d v = calculateEtherWarpPoint(todo.x, todo.y, todo.z, todo.h);
                        ChatLib.chat("etherwarp to " + todo.x + ", " + todo.y + ", " + todo.z);
                        ChatLib.chat("aim at " + v.x + ", " + v.y + ", " + v.z);
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
                        Thread.sleep(getPing());
                        if (MathUtils.distanceSquareFromPlayer(todo.x, todo.y + 0.25, todo.z) > 1) {
                            ChatLib.chat("Failed to etherwarp!");
                            ChatLib.chat("Player is at " + getX(getPlayer()) + ", " + getY(getPlayer()) + ", " + getZ(getPlayer()));
                            throw new Exception();
                        }
                    } else {
                        arrowShot = false;
                        ChatLib.chat("shooting to " + todo.yaw + ", " + todo.pitch);
                        ControlUtils.setHeldItemIndex(terminatorSlot);
                        ControlUtils.faceSlowly(todo.yaw, todo.pitch);
                        Thread.sleep(getPing());
                        ControlUtils.rightClick();

                        int cnt = 0;
                        while (!arrowShot) {
                            Thread.sleep(20);
                            if (cnt > 10) {
                                ControlUtils.rightClick();
                                cnt = 0;
                            }
                            cnt++;
                        }
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                should = false;
                ChatLib.chat("Auto Blaze &cdeactivated");
            }
        });
        shootingThread.start();
    }

    public static void test() {
//        CommandsUtils.addCommand("/tp 0 5 0");
        blazes.clear();
        blazes.add(new BlazeInfo(9.8, 14.7, 10.2, 100));
        blazes.add(new BlazeInfo(5, 10, 5, 1000));
        lowFirst = true;
        ChatLib.chat(blazeCanHit(new Vector3d(0, 5, 0), 0) + "");
    }

    // v: middle of block
    // h: half height of block
    public static Vector3d calculateEtherWarpPoint(double x, double y, double z, double h) throws Exception {
        if (getY(getPlayer()) >= y) {
            return new Vector3d(x, y + h, z);
        }
        int[] dx = new int[]{1, -1, 0, 0};
        int[] dz = new int[]{0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            float tx = (float) x + dx[i];
            float tz = (float) z + dz[i];
            if (BlockUtils.isBlockAir(tx, (float) y, tz))
                return new Vector3d(x + dx[i] / 2F, y, z + dz[i] / 2F);
        }
        throw new Exception();
    }

    // v: standing at v, v_y + 0.25
    private static boolean blazeCanHit(Vector3d v, int i) {
        BlazeInfo blazeInfo = blazes.get(i);
        double x = blazeInfo.cube.x;
        double y = blazeInfo.cube.y;
        double z = blazeInfo.cube.z;
        Vector2d yawAndPitch = ShortbowUtils.getDirection(v.x, v.y + 0.25 + 1.5, v.z, x, y, z);
        double yaw = yawAndPitch.getX();
        double pitch = yawAndPitch.getY();
        if (!canHit(v.x, v.y + 0.25 + 1.5, v.z, yaw, pitch, blazeInfo.cube)) {
            ChatLib.chat("can't hit, ????");
            return false;
        }
        for (int j = i + 1; j < blazes.size(); j++)
            if (canHit(v.x, v.y + 0.25 + 1.5, v.z, yaw, pitch, blazes.get(j).cube)) {
                ChatLib.chat("can't hit, because it may shoot " + j + " the blaze!");
                return false;
            }
        for (Cube cube : blocks)
            if (canHit(v.x, v.y + 0.25 + 1.5, v.z, yaw, pitch, cube)) {
                ChatLib.chat("can't hit, because it may hit block at " + cube.x + ", " + cube.y + ", " + cube.z);
                return false;
            }
        return true;
    }

    public void calculatePlaces() {
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
                    ChatLib.chat("add new place: " + tx + ", " + y + ", " + tz);

                    Cube blockCube;
                    if (y2 % 2 == 0) blockCube = new Cube(tx, y2 / 2F - 0.5, tz, 0.5, 0.5);
                    else blockCube = new Cube(tx, y, tz, 0.5, 0.25);
                    blocks.add(blockCube);
                    ChatLib.chat("block: " + blockCube.x + ", " + blockCube.y + ", " + blockCube.z);
                }
            }
        }
    }

    public void calculateBlazes() {
        List<BlazeInfo> newBlaze = new ArrayList<>();
        for (Entity entity : getWorld().loadedEntityList) {
            String name = entity.getName();
            if (name.contains("Blaze") && name.contains("/")) {
                String blazeName = ChatLib.removeFormatting(entity.getName());
                try {
                    int health = Integer.parseInt(blazeName.substring(blazeName.indexOf("/") + 1, blazeName.length() - 1));
                    newBlaze.add(new BlazeInfo(getX(entity), getY(entity) - 1, getZ(entity), health));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
        blazes.clear();
        blazes.addAll(newBlaze);
        blazes.sort((a, b) -> lowFirst ? a.hp - b.hp : b.hp - a.hp);
        for (BlazeInfo blazeInfo : blazes) {
            ChatLib.chat("blaze: " + blazeInfo.cube.x + ", " + blazeInfo.cube.y + ", " + blazeInfo.cube.z + ": " + blazeInfo.hp);
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
            ChatLib.chat("block: " + room.x + ", " + i + ", " + room.z);
//            }
        }
        blocks.clear();
        blocks.addAll(newBlocks);
    }

    public static boolean canHit(double x, double y, double z, double yaw, double pitch, Cube cube) {
        // solve in xz plane
        ChatLib.debug("trying canHit with yaw: " + yaw + ", pitch: " + pitch);
        double PI = Math.PI;
        double sx = cube.x - cube.w, sz = cube.z - cube.w;
        double tx = cube.x + cube.w, tz = cube.z + cube.w;
        if (isInXZSquare(x, z, sx, sz, tx, tz)) return true;
        // calculate intersections with 4 edges of the square
        // z * cos(alpha) = sin(alpha) * (x - x0) + z0 * cos(alpha)
        double xzAlpha = yaw - 90;
        double tan = Math.tan(xzAlpha * PI / 180);
        double zSx = z + (sx - x) * tan, xSz = x + (sz - z) * (1 / tan);
        double zTx = z + (tx - x) * tan, xTz = x + (tz - z) * (1 / tan);
        ArrayList<Vector2d> intersects = new ArrayList<>();
        if (zSx >= sz && zSx < tz) intersects.add(new Vector2d(sx, zSx));
        if (zTx > sz && zTx <= tz) intersects.add(new Vector2d(tx, zTx));
        if (xSz > sx && xSz <= tx) intersects.add(new Vector2d(xSz, sz));
        if (xTz >= sx && xTz < tx) intersects.add(new Vector2d(xTz, tz));
        if (intersects.size() <= 1) {
            ChatLib.debug("canHit false: intersect size too small");
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
        double sY = ShortbowUtils.getProjectileFunction(sX, pitch);
        double tY = ShortbowUtils.getProjectileFunction(tX, pitch);
        ChatLib.debug("sx: " + sx + ", tx: " + tx + ", sz: " + sz + ", tz: " + tz);
        ChatLib.debug("sX: " + sX + ", tX: " + tX + ", pitch: " + pitch);
        ChatLib.debug("sy: " + sy + ", ty: " + ty + ", sY: " + sY + ", tY: " + tY);
        // not exactly, but most of the case
        return MathUtils.isBetween(sY, sy, ty) || MathUtils.isBetween(tY, sy, ty);
    }

    public static boolean isInXZSquare(double x, double z, double sx, double sz, double tx, double tz) {
        return MathUtils.isBetween(x, sx, tx) && MathUtils.isBetween(z, sz, tz);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        blazes.clear();
        blocks.clear();
        places.clear();
        setRoom(null);
    }

    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            tpPacketReceived = true;
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        if (entity instanceof EntityArrow) {
            double dis2 = MathUtils.distanceSquareFromPlayer(entity.posX, entity.posY, entity.posZ);
            ChatLib.debug("entity join: " + entity + ", " + dis2);
            if (dis2 < 5 * 5) {
                arrowShot = true;
            }
        }
    }

    public static void setRoom(Room blazeRoom) {
        room = blazeRoom;
    }

    static class BlazeInfo {
        Cube cube;
        int hp;

        public BlazeInfo(double x, double y, double z, int hp) {
            cube = new Cube(x, y, z, 1, 0.3);
            this.hp = hp;
        }
    }

    static class Cube {
        double x, y, z;
        double h, w;

        public Cube(double x, double y, double z, double h, double w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.h = h;
            this.w = w;
        }
    }

    static class Sequence {
        Type type;
        double yaw, pitch;
        double x, y, z;
        double w, h;

        enum Type {
            WARP, SHOOT
        }

        public Sequence(double x, double y, double z, double w, double h) {
            type = Type.WARP;
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            this.h = h;
        }

        public Sequence(double yaw, double pitch) {
            type = Type.SHOOT;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }
}
