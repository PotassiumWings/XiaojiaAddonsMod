package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WaterUtils {
    public static final int width = 21;
    public static final int height = 24;
    public static final int gap = 15;
    private static final ArrayList<Pair<Integer, Integer>> yToFlag = new ArrayList<Pair<Integer, Integer>>() {{
        add(new Pair<>(1, 0));
        add(new Pair<>(5, 1));
        add(new Pair<>(10, 2));
        add(new Pair<>(15, 3));
        add(new Pair<>(19, 4));
    }};
    // places where blocks r at
    private static final ArrayList<Pair<Integer, Integer>> ca = new ArrayList<>();
    private static final ArrayList<Pair<Integer, Integer>> cla = new ArrayList<>();
    private static final ArrayList<Pair<Integer, Integer>> da = new ArrayList<>();
    private static final ArrayList<Pair<Integer, Integer>> ga = new ArrayList<>();
    private static final ArrayList<Pair<Integer, Integer>> ea = new ArrayList<>();
    private static final ArrayList<Pair<Integer, Integer>> qa = new ArrayList<>();
    private static final HashMap<EnumOperation, ArrayList<Pair<Integer, Integer>>> stateMap =
            new HashMap<EnumOperation, ArrayList<Pair<Integer, Integer>>>() {{
                put(EnumOperation.g, ga);
                put(EnumOperation.c, ca);
                put(EnumOperation.e, ea);
                put(EnumOperation.d, da);
                put(EnumOperation.cl, cla);
                put(EnumOperation.q, qa);
            }};
    public static String boardString = "";
    public static int bestTime;
    public static TreeMap<Integer, EnumOperation> operations = new TreeMap<>();

    public static Vector3d trigV, qv, gv, cv, dv, ev, clv;
    public static boolean raw = true;
    public static ArrayList<Vector3d> points;

    public static void calculateVectors(Room room, EnumFacing facing) {
        int x = room.x, z = room.z;
        points = new ArrayList<>();
        raw = true;
        if (facing == EnumFacing.xn || facing == EnumFacing.xp) {
            if (facing == EnumFacing.xp) {
                trigV = new Vector3d(x + 10 + 0.5, 60.2, z + 0.5);
                qv = new Vector3d(x - 5 + 0.5, 61.5, z + 5 + 0.2 + 0.5);
                gv = new Vector3d(x + 0.5, 61.5, z + 5 + 0.2 + 0.5);
                cv = new Vector3d(x + 5 + 0.5, 61.5, z + 5 + 0.2 + 0.5);
                dv = new Vector3d(x - 5 + 0.5, 61.5, z - 5 - 0.2 + 0.5);
                ev = new Vector3d(x + 0.5, 61.5, z - 5 - 0.2 + 0.5);
                clv = new Vector3d(x + 5 + 0.5, 61.5, z - 5 - 0.2 + 0.5);
                points.add(new Vector3d(x + 7 + 0.5, 59, z + 2 + 0.5));
                points.add(new Vector3d(x + 7 + 0.5, 59, z - 2 + 0.5));
                points.add(new Vector3d(x - 3 + 0.5, 59, z + 3 + 0.5));
                points.add(new Vector3d(x - 3 + 0.5, 59, z - 3 + 0.5));
            } else {
                trigV = new Vector3d(x - 10 + 0.5, 60.2, z + 0.5);
                qv = new Vector3d(x + 5 + 0.5, 61.5, z - 5 - 0.2 + 0.5);
                gv = new Vector3d(x + 0.5, 61.5, z - 5 - 0.2 + 0.5);
                cv = new Vector3d(x - 5 + 0.5, 61.5, z - 5 - 0.2 + 0.5);
                dv = new Vector3d(x + 5 + 0.5, 61.5, z + 5 + 0.2 + 0.5);
                ev = new Vector3d(x + 0.5, 61.5, z + 5 + 0.2 + 0.5);
                clv = new Vector3d(x - 5 + 0.5, 61.5, z + 5 + 0.2 + 0.5);
                points.add(new Vector3d(x - 7 + 0.5, 59, z - 2 + 0.5));
                points.add(new Vector3d(x - 7 + 0.5, 59, z + 2 + 0.5));
                points.add(new Vector3d(x + 3 + 0.5, 59, z - 3 + 0.5));
                points.add(new Vector3d(x + 3 + 0.5, 59, z + 3 + 0.5));
            }
        } else {
            if (facing == EnumFacing.zp) {
                trigV = new Vector3d(x + 0.5, 60.2, z + 10 + 0.5);
                qv = new Vector3d(x - 5 - 0.2 + 0.5, 61.5, z - 5 + 0.5);
                gv = new Vector3d(x - 5 - 0.2 + 0.5, 61.5, z + 0.5);
                cv = new Vector3d(x - 5 - 0.2 + 0.5, 61.5, z + 5 + 0.5);
                dv = new Vector3d(x + 5 + 0.2 + 0.5, 61.5, z - 5 + 0.5);
                ev = new Vector3d(x + 5 + 0.2 + 0.5, 61.5, z + 0.5);
                clv = new Vector3d(x + 5 + 0.2 + 0.5, 61.5, z + 5 + 0.5);
                points.add(new Vector3d(x + 2 + 0.5, 59, z + 7 + 0.5));
                points.add(new Vector3d(x - 2 + 0.5, 59, z + 7 + 0.5));
                points.add(new Vector3d(x + 3 + 0.5, 59, z - 3 + 0.5));
                points.add(new Vector3d(x - 3 + 0.5, 59, z - 3 + 0.5));
            } else {
                trigV = new Vector3d(x + 0.5, 60.2, z - 10 + 0.5);
                qv = new Vector3d(x + 5 + 0.2 + 0.5, 61.5, z + 5 + 0.5);
                gv = new Vector3d(x + 5 + 0.2 + 0.5, 61.5, z + 0.5);
                cv = new Vector3d(x + 5 + 0.2 + 0.5, 61.5, z - 5 + 0.5);
                dv = new Vector3d(x - 5 - 0.2 + 0.5, 61.5, z + 5 + 0.5);
                ev = new Vector3d(x - 5 - 0.2 + 0.5, 61.5, z + 0.5);
                clv = new Vector3d(x - 5 - 0.2 + 0.5, 61.5, z - 5 + 0.5);
                points.add(new Vector3d(x + 2 + 0.5, 59, z - 7 + 0.5));
                points.add(new Vector3d(x - 2 + 0.5, 59, z - 7 + 0.5));
                points.add(new Vector3d(x + 3 + 0.5, 59, z + 3 + 0.5));
                points.add(new Vector3d(x - 3 + 0.5, 59, z + 3 + 0.5));
            }
        }
        if (BlockUtils.getMetaAt(qv) > 5) raw = false;
        if (BlockUtils.getMetaAt(dv) > 5) raw = false;
        if (BlockUtils.getMetaAt(ev) > 5) raw = false;
        if (BlockUtils.getMetaAt(cv) > 5) raw = false;
        if (BlockUtils.getMetaAt(clv) > 5) raw = false;
        if (BlockUtils.getMetaAt(gv) > 5) raw = false;
    }

    public static EnumState[][] getBoard(Room room, EnumFacing facing) {
        EnumState[][] board = new EnumState[height][width];
        // get board, piston
        int sy = 60, ty = 83;
        if (facing == EnumFacing.xn || facing == EnumFacing.xp) {
            int x = facing == EnumFacing.xp ? room.x - 11 : room.x + 11;
            int deviceX = facing == EnumFacing.xp ? room.x - 12 : room.x + 12;
            int sz, tz;
            if (facing == EnumFacing.xp) {
                sz = room.z + 10;
                tz = room.z - 10;
                for (int z = sz; z >= tz; z--)
                    for (int y = sy; y <= ty; y++)
                        board[y - sy][sz - z] = WaterUtils.getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(deviceX, y, z));
            } else {
                sz = room.z - 10;
                tz = room.z + 10;
                for (int z = sz; z <= tz; z++)
                    for (int y = sy; y <= ty; y++)
                        board[y - sy][z - sz] = WaterUtils.getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(deviceX, y, z));
            }
        } else {
            int z = facing == EnumFacing.zp ? room.z - 11 : room.z + 11;
            int deviceZ = facing == EnumFacing.zp ? room.z - 12 : room.z + 12;
            int sx, tx;
            if (facing == EnumFacing.zn) {
                sx = room.x + 10;
                tx = room.x - 10;
                for (int x = sx; x >= tx; x--)
                    for (int y = sy; y <= ty; y++)
                        board[y - sy][sx - x] = WaterUtils.getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(x, y, deviceZ));
            } else {
                sx = room.x - 10;
                tx = room.x + 10;
                for (int x = sx; x <= tx; x++)
                    for (int y = sy; y <= ty; y++)
                        board[y - sy][x - sx] = WaterUtils.getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(x, y, deviceZ));
            }
        }

        board[23][10] = EnumState.w;
        getBoardString(board);
        return board;
    }

    public static void processBoard(EnumState[][] board) {
        // get board, piston
        ca.clear();
        ea.clear();
        da.clear();
        cla.clear();
        ga.clear();
        qa.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                EnumState state = board[i][j];
                if (state == EnumState.c || state == EnumState.cc) ca.add(new Pair<>(i, j));
                if (state == EnumState.e || state == EnumState.ce) ea.add(new Pair<>(i, j));
                if (state == EnumState.d || state == EnumState.cd) da.add(new Pair<>(i, j));
                if (state == EnumState.cl || state == EnumState.ccl) cla.add(new Pair<>(i, j));
                if (state == EnumState.g || state == EnumState.cg) ga.add(new Pair<>(i, j));
                if (state == EnumState.q || state == EnumState.cq) qa.add(new Pair<>(i, j));

                if (state == EnumState.c || state == EnumState.d || state == EnumState.e
                        || state == EnumState.q || state == EnumState.g || state == EnumState.cl)
                    board[i][j] = EnumState.E;
            }
        }
    }

    public static void getBoardString(EnumState[][] board) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String cur = board[i][j].toString();
                s.append(String.format("board[%d][%d]=EnumState.%s;", i, j, cur));
            }
        }
        boardString = s.toString();
    }

    public static void print(EnumState[][] board) {
        for (int i = height - 1; i >= 0; i--) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < width; j++) {
                String cur = board[i][j].toString();
                char c = cur.charAt(cur.length() - 1);
                if (c == 'E') c = ' ';
                s.append(c);
            }
            System.out.println(s);
        }
        System.out.println();
    }

    public static int getFlag(Room room, EnumFacing facing) {
        int flag = 0;
        if (facing == EnumFacing.xn || facing == EnumFacing.xp) {
            if (facing == EnumFacing.xn) flag = WaterUtils.getFlag(room.x + 4, room.z + 2, room.x, room.z + 2);
            else flag = WaterUtils.getFlag(room.x - 4, room.z + 2, room.x, room.z + 2);
        } else {
            if (facing == EnumFacing.zn) flag = WaterUtils.getFlag(room.x + 2, room.z + 4, room.x + 2, room.z);
            else flag = WaterUtils.getFlag(room.x + 2, room.z - 4, room.x + 2, room.z);
        }
//        System.out.println("flag: " + flag);
        return flag;
    }

    public static EnumFacing getFacing(Room room) {
        EnumFacing facing = null;
        if (BlockUtils.getBlockAt(room.x, 68, room.z - 9).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.zn;
        if (BlockUtils.getBlockAt(room.x, 68, room.z + 9).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.zp;
        if (BlockUtils.getBlockAt(room.x - 9, 68, room.z).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.xn;
        if (BlockUtils.getBlockAt(room.x + 9, 68, room.z).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.xp;
        return facing;
    }

    public static boolean isWater(EnumState state) {
        return state == EnumState.w || state == EnumState.w1 ||
                state == EnumState.w2 || state == EnumState.w3 ||
                state == EnumState.w4 || state == EnumState.w5 ||
                state == EnumState.w6 || state == EnumState.w7 ||
                state == EnumState.w8;
    }

    public static boolean isFlowWater(EnumState state) {
        return isWater(state) && state != EnumState.w;
    }

    public static boolean isBlock(EnumState state) {
        return state == EnumState.B || state == EnumState.cc || state == EnumState.cg ||
                state == EnumState.ccl || state == EnumState.cq || state == EnumState.cd ||
                state == EnumState.ce;
    }

    public static int compare(EnumState s1, EnumState s2) {
        int x = getWaterValueOf(s1), y = getWaterValueOf(s2);
        return x - y;
    }

    public static EnumState getLowerFormOfWater(EnumState s) {
        if (s == EnumState.w || s == EnumState.w8) return EnumState.w7;
        if (s == EnumState.w7) return EnumState.w6;
        if (s == EnumState.w6) return EnumState.w5;
        if (s == EnumState.w5) return EnumState.w4;
        if (s == EnumState.w4) return EnumState.w3;
        if (s == EnumState.w3) return EnumState.w2;
        if (s == EnumState.w2) return EnumState.w1;
        if (s == EnumState.w1) return EnumState.E;
        return EnumState.E;
    }

    public static int getWaterValueOf(EnumState s) {
        int res = 0;
        if (s == EnumState.w || s == EnumState.w8) return 8;
        if (s == EnumState.w7) return 7;
        if (s == EnumState.w6) return 6;
        if (s == EnumState.w5) return 5;
        if (s == EnumState.w4) return 4;
        if (s == EnumState.w3) return 3;
        if (s == EnumState.w2) return 2;
        if (s == EnumState.w1) return 1;
        return res;
    }

    public static int getFlag(int sx, int sz, int tx, int tz) {
        int cnt = 0, res = 0;
        for (int x = sx; ; x += (sx < tx ? 1 : -1)) {
            for (int z = sz; ; z += (sz < tz ? 1 : -1)) {
                res |= (1 << cnt) * (BlockUtils.getBlockAt(x, 57, z) == Blocks.piston_head ? 1 : 0);
                cnt++;
                if (z == tz) break;
            }
            if (x == tx) break;
        }
        return res;
    }

    public static EnumState getStateFromBlock(Block block, Block blockBehind) {
        if (Blocks.air.equals(block)) {
            if (Blocks.gold_block.equals(blockBehind)) {
                return EnumState.g;
            } else if (Blocks.emerald_block.equals(blockBehind)) {
                return EnumState.e;
            } else if (Blocks.hardened_clay.equals(blockBehind)) {
                return EnumState.cl;
            } else if (Blocks.diamond_block.equals(blockBehind)) {
                return EnumState.d;
            } else if (Blocks.coal_block.equals(blockBehind)) {
                return EnumState.c;
            } else if (Blocks.quartz_block.equals(blockBehind)) {
                return EnumState.q;
            }
            return EnumState.E;
        } else if (Blocks.gold_block.equals(block)) {
            return EnumState.cg;
        } else if (Blocks.emerald_block.equals(block)) {
            return EnumState.ce;
        } else if (Blocks.hardened_clay.equals(block)) {
            return EnumState.ccl;
        } else if (Blocks.diamond_block.equals(block)) {
            return EnumState.cd;
        } else if (Blocks.coal_block.equals(block)) {
            return EnumState.cc;
        } else if (Blocks.quartz_block.equals(block)) {
            return EnumState.cq;
        } else {
            return EnumState.B;
        }
    }

    public static boolean canExtendLeft(EnumState[][] state, int i, int j) {
        return !WaterUtils.isBlock(state[i][j - 1]) && WaterUtils.isBlock(state[i - 1][j]);
    }

    public static boolean canExtendRight(EnumState[][] state, int i, int j) {
        return !WaterUtils.isBlock(state[i][j + 1]) && WaterUtils.isBlock(state[i - 1][j]);
    }

    public static void dfs(EnumState[][] state, int time, HashMap<Integer, EnumOperation> order, int flag, boolean advancedDfs) {
        // advanced: move 2 things first
        if (advancedDfs && order.size() == 0) {
            for (EnumOperation operation : EnumOperation.values()) {
                if (operation == EnumOperation.trig || operation == EnumOperation.empty) continue;
                EnumState[][] newState = getStatesFromOperation(state, operation);
                order.put(0, operation);
                for (EnumOperation operation2 : EnumOperation.values()) {
                    if (operation2 == EnumOperation.trig || operation2 == EnumOperation.empty) continue;
                    if (operation == operation2) continue;
                    EnumState[][] newState2 = getStatesFromOperation(newState, operation2);
                    order.put(2, operation2);

                    order.put(4, EnumOperation.trig);
                    EnumState[][] newState3 = getStatesFromOperation(newState2, EnumOperation.trig);
                    dfs(newState3, 4, order, flag, advancedDfs);
                    order.remove(4);

                    order.remove(2);
                }
                order.remove(0);
            }
            return;
        }
        if (advancedDfs) {
            if (order.values().stream().filter(e -> !e.equals(EnumOperation.empty) && !e.equals(EnumOperation.trig)).count() > 5 ||
                    order.size() > 8) return;
        } else if (order.values().stream().filter(e -> !e.equals(EnumOperation.empty) && !e.equals(EnumOperation.trig)).count() >= 4 ||
                order.size() > 6) return;
        if (time >= bestTime) return;

        // simulate without further operations
        Pair<Integer, Integer> timeAndFlag = getTimeAndFlag(state);
        if (flag == timeAndFlag.getValue()) {
            bestTime = time;
            operations = new TreeMap<>(order);
            System.out.println("Found a solution: " + time);
            for (Map.Entry<Integer, EnumOperation> operation : operations.entrySet()) {
                System.out.println(operation.getKey() + ": " + operation.getValue());
            }
            return;
        }
        // do some operations
        for (int i = 0; i < gap; i++) {
            Pair<EnumState[][], Integer> stateAndFlag = simulate(state);
            state = stateAndFlag.getKey();
            flag ^= stateAndFlag.getValue();
        }
        time += gap;
        for (EnumOperation operation : EnumOperation.values()) {
            if ((operation != EnumOperation.trig && order.size() == (advancedDfs ? 2 : 0) ||
                    operation == EnumOperation.trig && order.size() > (advancedDfs ? 2 : 0))) continue;
            EnumState[][] newState = getStatesFromOperation(state, operation);
            order.put(time, operation);
            dfs(newState, time, order, flag, advancedDfs);
            order.remove(time);
        }
    }

    private static Pair<Integer, Integer> getTimeAndFlag(EnumState[][] state) {
        // simulate till end
        int flag = 0, cnt = 0;
        Pair<EnumState[][], Integer> stateAndFlag = simulate(state);
        while (!Arrays.deepEquals(state, stateAndFlag.getKey())) {
            state = stateAndFlag.getKey();
            flag ^= stateAndFlag.getValue();
            cnt++;
            stateAndFlag = simulate(state);
        }
        return new Pair<>(cnt, flag);
    }

    public static Vector3d getEtherwarpPointFor(EnumOperation operation) {
        Vector3d lever = getPosFor(operation);
        assert lever != null;
        double dis = 1e9;
        Vector3d res = null;
        for (Vector3d v : points) {
            double dist = MathUtils.distanceSquaredFromPoints(v, lever);
            if (dist < dis) {
                dis = dist;
                res = v;
            }
        }
        return res;
    }

    public static Vector3d getPosFor(EnumOperation operation) {
        if (operation == EnumOperation.c) return cv;
        if (operation == EnumOperation.q) return qv;
        if (operation == EnumOperation.cl) return clv;
        if (operation == EnumOperation.e) return ev;
        if (operation == EnumOperation.d) return dv;
        if (operation == EnumOperation.g) return gv;
        if (operation == EnumOperation.trig) return trigV;
        ChatLib.chat(operation + " ???");
        return null;
    }

    public static EnumState[][] getStatesFromOperation(EnumState[][] state, EnumOperation operation) {
        EnumState[][] newState = new EnumState[height][width];
        for (int i = 0; i < height; i++) System.arraycopy(state[i], 0, newState[i], 0, width);
        if (operation == EnumOperation.trig)
            newState[22][10] = newState[22][10] == EnumState.w ? EnumState.B : EnumState.w;
        else if (operation != EnumOperation.empty) {
            ArrayList<Pair<Integer, Integer>> coords = stateMap.get(operation);
            for (Pair<Integer, Integer> xy : coords) {
                int i = xy.getKey(), j = xy.getValue();
                EnumState t = newState[i][j];
                if (t == EnumState.cc || t == EnumState.ce || t == EnumState.cd ||
                        t == EnumState.ccl || t == EnumState.cq || t == EnumState.cg ||
                        t == EnumState.B)
                    newState[i][j] = EnumState.E;
                else {
                    if (operation == EnumOperation.c) newState[i][j] = EnumState.cc;
                    else if (operation == EnumOperation.d) newState[i][j] = EnumState.cd;
                    else if (operation == EnumOperation.e) newState[i][j] = EnumState.ce;
                    else if (operation == EnumOperation.g) newState[i][j] = EnumState.cg;
                    else if (operation == EnumOperation.q) newState[i][j] = EnumState.cq;
                    else if (operation == EnumOperation.cl) newState[i][j] = EnumState.ccl;
                }
            }
        }
        return newState;
    }

    // simulate one time, return state and flag
    public static Pair<EnumState[][], Integer> simulate(EnumState[][] state) {
        EnumState[][] newState = new EnumState[height][width];
        // only copies not water; water: above/aside with water; flow water
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!WaterUtils.isWater(state[i][j])) newState[i][j] = state[i][j];
                else if (WaterUtils.isFlowWater(state[i][j])) newState[i][j] = state[i][j];
                else if (j > 1 && WaterUtils.isWater(state[i][j - 1]) || j < width - 1 && WaterUtils.isWater(state[i][j + 1]) ||
                        i + 1 < height && WaterUtils.isWater(state[i + 1][j]))
                    newState[i][j] = state[i][j];
                else newState[i][j] = EnumState.E;
            }
        }
        newState[23][10] = EnumState.w;
        // determines where to flow
        for (int i = 1; i < height - 1; i++) {
            boolean hasWaterAbove = false;
            int beginW = -1, endW = -1;
            for (int j = 0; j < width; j++) {
                EnumState cur = state[i][j];
                // next layer is water
                if (WaterUtils.isWater(cur)) {
                    hasWaterAbove |= WaterUtils.isWater(state[i + 1][j]);
                    if (beginW == -1) beginW = j;
                    endW = j;

                    // straight down
                    if (!WaterUtils.isBlock(newState[i - 1][j])) {
                        if (i > 1 && WaterUtils.isBlock(newState[i - 2][j])) newState[i - 1][j] = EnumState.w8;
                        else newState[i - 1][j] = EnumState.w;
                    }
                } else {
                    // TODO: assumption: no single one block flow in the system
                    if (beginW == -1) continue;
                    boolean canLeft = false, canRight = false;
                    // instantly blocked, but still can extend
                    boolean justBlocked = !hasWaterAbove && beginW == endW && newState[i][beginW] == EnumState.w8;
                    if (hasWaterAbove && beginW == endW || justBlocked) {
                        int l1 = 0, r1 = 0;
                        boolean d1 = true, d2 = true; // if can flow down
                        while (canExtendLeft(newState, i, beginW - l1) && l1 < 5) l1++;
                        if (WaterUtils.isBlock(state[i - 1][beginW - l1])) d1 = false;
                        while (canExtendRight(newState, i, beginW + r1) && r1 < 5) r1++;
                        if (WaterUtils.isBlock(state[i - 1][beginW + r1])) d2 = false;
                        if (l1 == 0) l1 = 1000;
                        if (r1 == 0) r1 = 1000;
                        canLeft = ((d1 && !d2 || d1 == d2 && l1 <= r1) && canExtendLeft(newState, i, beginW));
                        canRight = ((!d1 && d2 || d1 == d2 && l1 >= r1) && canExtendRight(newState, i, beginW));
                        if (l1 == r1 && l1 == 1000)
                            canLeft = canRight = false;
                    } else {
                        // assumption: l1 = r1, can't left / right
                        canLeft = canExtendLeft(newState, i, beginW) && newState[i][beginW] != EnumState.w1 &&
                                compare(newState[i][beginW], newState[i][beginW + 1]) < 0;
                        canRight = canExtendRight(newState, i, endW) && newState[i][endW] != EnumState.w1 &&
                                compare(newState[i][endW], newState[i][endW - 1]) < 0;
                    }
                    if (canLeft) {
                        newState[i][beginW - 1] = getLowerFormOfWater(newState[i][beginW]);
                        if (newState[i][beginW - 1] != EnumState.E) beginW--;
                    }
                    if (canRight) {
                        newState[i][endW + 1] = getLowerFormOfWater(newState[i][endW]);
                        if (newState[i][endW + 1] != EnumState.E) endW++;
                    }
                    // Assumption: 2 flows from above, 1 flow disappears, keep it won't affect
                    //        |     |            ->                |
                    //  123456w65456w654321             123456w5456w654321
                    // should be    |         but TODO.
                    //  123456665456w654321
                    if (!hasWaterAbove && !justBlocked) {
                        EnumState max = EnumState.E;
                        for (int x = beginW; x <= endW; x++)
                            if (compare(max, newState[i][x]) < 0)
                                max = newState[i][x];
                        for (int x = beginW; x <= endW; x++)
                            if (compare(max, newState[i][x]) == 0)
                                newState[i][x] = getLowerFormOfWater(newState[i][x]);
                    }
                    hasWaterAbove = false;
                    beginW = endW = -1;
                }
            }
        }

        // calc return value
        int flag = 0;
        for (Pair<Integer, Integer> pr : yToFlag)
            if (!WaterUtils.isWater(state[0][pr.getKey()]) && WaterUtils.isWater(newState[0][pr.getKey()]))
                flag |= 1 << pr.getValue();
        return new Pair<>(newState, flag);
    }
}
