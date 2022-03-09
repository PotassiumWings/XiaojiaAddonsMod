package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WaterSolver {
    private static final int width = 21;
    private static final int height = 24;
    private static final ArrayList<Pair<Integer, Integer>> yToFlag = new ArrayList<Pair<Integer, Integer>>() {{
        add(new Pair<>(1, 0));
        add(new Pair<>(5, 1));
        add(new Pair<>(10, 2));
        add(new Pair<>(14, 3));
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
    private static final int gap = 6;
    public static Room room = null;
    private static HashMap<Integer, EnumOperation> operations = new HashMap<>();
    private static EnumFacing facing = null;
    private static state[][] board = new state[height][width];
    private static int bestTime;

    public static void solve() {
        // facing
        if (BlockUtils.getBlockAt(room.x, 68, room.z - 9).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.zn;
        if (BlockUtils.getBlockAt(room.x, 68, room.z + 9).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.zp;
        if (BlockUtils.getBlockAt(room.x - 9, 68, room.z).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.xn;
        if (BlockUtils.getBlockAt(room.x + 9, 68, room.z).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.xp;
        if (facing == null) return;
        // get board, piston
        int sy = 60, ty = 83;
        int flag = 0;
        if (facing == EnumFacing.xn || facing == EnumFacing.xp) {
            int z = facing == EnumFacing.xp ? room.z - 11 : room.z + 11;
            int deviceZ = facing == EnumFacing.xp ? room.z - 12 : room.z + 12;
            int sx = room.x - 10, tx = room.x + 10;
            if (facing == EnumFacing.xn) flag = getFlag(room.x + 4, room.z + 2, room.x, room.z + 2);
            else flag = getFlag(room.x - 4, room.z + 2, room.x, room.z + 2);
            for (int x = sx; x <= tx; x++)
                for (int y = sy; y <= ty; y++)
                    board[y - sy][x - sx] = getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(x, y, deviceZ));
        } else {
            int x = facing == EnumFacing.zp ? room.x - 11 : room.x + 11;
            int deviceX = facing == EnumFacing.zp ? room.x - 12 : room.x + 12;
            int sz = room.z - 10, tz = room.z + 10;
            for (int z = sz; z <= tz; z++)
                for (int y = sy; y <= ty; y++)
                    board[y - sy][z - sz] = getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(deviceX, y, z));
        }
        ca.clear();
        ea.clear();
        da.clear();
        cla.clear();
        ga.clear();
        qa.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                state state = board[i][j];
                if (state == WaterSolver.state.c || state == WaterSolver.state.cc) ca.add(new Pair<>(i, j));
                if (state == WaterSolver.state.e || state == WaterSolver.state.ce) ea.add(new Pair<>(i, j));
                if (state == WaterSolver.state.d || state == WaterSolver.state.cd) da.add(new Pair<>(i, j));
                if (state == WaterSolver.state.cl || state == WaterSolver.state.ccl) cla.add(new Pair<>(i, j));
                if (state == WaterSolver.state.g || state == WaterSolver.state.cg) ga.add(new Pair<>(i, j));
                if (state == WaterSolver.state.q || state == WaterSolver.state.cq) qa.add(new Pair<>(i, j));

                if (state == WaterSolver.state.c || state == WaterSolver.state.d || state == WaterSolver.state.e
                        || state == WaterSolver.state.q || state == WaterSolver.state.g || state == WaterSolver.state.cl)
                    board[i][j] = WaterSolver.state.B;
            }
        }
        board[23][10] = state.w;
        // calc
        bestTime = 1000;
        dfs(board, 0, new HashMap<>(), flag);
    }

    private static void dfs(state[][] state, int time, HashMap<Integer, EnumOperation> order, int flag) {
        if (order.size() >= 6) return;
        if (time > bestTime) return;
        // simulate without further operations
        Pair<Integer, Integer> timeAndFlag = getTimeAndFlag(state);
        if (flag == timeAndFlag.getValue()) {
            bestTime = time;
            operations = new HashMap<>(order);
            return;
        }
        // do some operations
        for (int i = 0; i < gap; i++) {
            Pair<state[][], Integer> stateAndFlag = simulate(state);
            state = stateAndFlag.getKey();
            flag ^= stateAndFlag.getValue();
        }
        time += gap;
        for (EnumOperation operation : EnumOperation.values()) {
            state[][] newState = new state[height][width];
            for (int i = 0; i < height; i++) System.arraycopy(state[i], 0, newState[i], 0, width);
            order.put(time, operation);
            if (operation == EnumOperation.trig)
                newState[22][10] = newState[22][10] == WaterSolver.state.w ? WaterSolver.state.B : WaterSolver.state.w;
            else {
                ArrayList<Pair<Integer, Integer>> coords = stateMap.get(operation);
                for (Pair<Integer, Integer> xy : coords) {
                    int i = xy.getKey(), j = xy.getValue();
                    state t = newState[i][j];
                    if (t == WaterSolver.state.cc || t == WaterSolver.state.ce || t == WaterSolver.state.cd ||
                            t == WaterSolver.state.ccl || t == WaterSolver.state.cq || t == WaterSolver.state.cg)
                        newState[i][j] = WaterSolver.state.B;
                    else {
                        if (operation == EnumOperation.c) newState[i][j] = WaterSolver.state.cc;
                        if (operation == EnumOperation.d) newState[i][j] = WaterSolver.state.cd;
                        if (operation == EnumOperation.e) newState[i][j] = WaterSolver.state.ce;
                        if (operation == EnumOperation.g) newState[i][j] = WaterSolver.state.cg;
                        if (operation == EnumOperation.q) newState[i][j] = WaterSolver.state.cq;
                        if (operation == EnumOperation.cl) newState[i][j] = WaterSolver.state.ccl;
                    }
                }
            }

            dfs(newState, time, order, flag);
            order.remove(time);
        }
    }

    private static Pair<Integer, Integer> getTimeAndFlag(state[][] state) {
        // simulate till end
        int flag = 0, cnt = 0;
        while (notStable(state)) {
            Pair<state[][], Integer> stateAndFlag = simulate(state);
            state = stateAndFlag.getKey();
            flag ^= stateAndFlag.getValue();
            cnt++;
        }
        return new Pair<>(cnt, flag);
    }

    // simulate one time, return state and flag
    private static Pair<state[][], Integer> simulate(state[][] state) {
        state[][] newState = new state[height][width];
        // only copies not water
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!isWater(state[i][j])) newState[i][j] = state[i][j];
                else newState[i][j] = WaterSolver.state.E;
            }
        }
        // simulate, i from 1 cuz calculating flowing water
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                state cur = state[i][j];
                if (isWater(cur)) {
                    // straight down
                    if (!isBlock(newState[i - 1][j])) newState[i - 1][j] = WaterSolver.state.w;
                    else {
                        int left = 0, right = 0;
                        while (!isBlock(newState[i][j - left]) && isBlock(newState[i - 1][j - left + 1])) left++;
                        while (!isBlock(newState[i][j + right]) && isBlock(newState[i - 1][j + right - 1])) right++;
                        // blocked
                        if (left == right && left == 0) continue;
                        // flow to left
                        if (left < right) newState[i][j - 1] = state[i][j];
                        else if (left == right) newState[i][j - 1] = newState[i][j + 1] = state[i][j];
                        else newState[i][j + 1] = state[i][j];
                    }
                }
            }
        }
        // calculate decrease
        for (int i = 0; i < height - 1; i++) {
            boolean waterAboveCurrentWaterFlow = false;
            state maxState = WaterSolver.state.E;
            for (int j = 0; j < width; j++) {
                state cur = newState[i][j];
                if (isWater(cur)) {
                    waterAboveCurrentWaterFlow |= isWater(newState[i + 1][j]);
                    if (compare(maxState, cur) < 0) maxState = cur;
                } else {
                    // do the calculation stuff
                    if (waterAboveCurrentWaterFlow) {
                        for (int k = j - 1; isWater(newState[i][k]); k--) {
                            newState[i][k] = WaterSolver.state.w;
                        }
                    } else {
                        for (int k = j - 1; isWater(newState[i][k]); k--) {
                            newState[i][k] = getLowerFormOfWater(maxState);
                        }
                    }
                    // reset
                    waterAboveCurrentWaterFlow = false;
                    maxState = WaterSolver.state.E;
                }
            }
        }

        // calc return value
        int flag = 0;
        for (Pair<Integer, Integer> pr : yToFlag)
            if (!isWater(state[0][pr.getKey()]) && isWater(newState[0][pr.getKey()]))
                flag |= 1 << pr.getValue();
        return new Pair<>(newState, flag);
    }

    private static boolean notStable(state[][] state) {
        return !Arrays.deepEquals(state, simulate(state).getKey());
    }

    private static boolean hasWater(state[][] state) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isWater(state[i][j])) return true;
            }
        }
        return false;
    }

    private static boolean isWater(state state) {
        return state == WaterSolver.state.w || state == WaterSolver.state.w1 ||
                state == WaterSolver.state.w2 || state == WaterSolver.state.w3 ||
                state == WaterSolver.state.w4 || state == WaterSolver.state.w5 ||
                state == WaterSolver.state.w6;
    }

    private static boolean isBlock(state state) {
        return state == WaterSolver.state.B || state == WaterSolver.state.cc || state == WaterSolver.state.cg ||
                state == WaterSolver.state.ccl || state == WaterSolver.state.cq || state == WaterSolver.state.cd ||
                state == WaterSolver.state.ce;
    }

    public static void setRoom(Room waterRoom) {
        room = waterRoom;
        if (room != null)
            new Thread(WaterSolver::solve).start();
    }

    private static int getFlag(int sx, int sz, int tx, int tz) {
        int cnt = 0, res = 0;
        for (int x = sx; ; x += (sx < tx ? 1 : -1)) {
            for (int z = sz; ; z += (sz < tz ? 1 : -1)) {
                res |= (1 << cnt) & (BlockUtils.getBlockAt(x, 57, z) == Blocks.piston_head ? 1 : 0);
                cnt++;
                if (z == tz) break;
            }
            if (x == tx) break;
        }
        return res;
    }

    private static state getStateFromBlock(Block block, Block blockBehind) {
        if (Blocks.air.equals(block)) {
            if (Blocks.gold_block.equals(blockBehind)) {
                return state.g;
            } else if (Blocks.emerald_block.equals(blockBehind)) {
                return state.e;
            } else if (Blocks.hardened_clay.equals(blockBehind)) {
                return state.cl;
            } else if (Blocks.diamond_block.equals(blockBehind)) {
                return state.d;
            } else if (Blocks.coal_block.equals(blockBehind)) {
                return state.c;
            } else if (Blocks.quartz_block.equals(blockBehind)) {
                return state.q;
            }
            return state.E;
        } else if (Blocks.gold_block.equals(block)) {
            return state.cg;
        } else if (Blocks.emerald_block.equals(block)) {
            return state.ce;
        } else if (Blocks.hardened_clay.equals(block)) {
            return state.ccl;
        } else if (Blocks.diamond_block.equals(block)) {
            return state.cd;
        } else if (Blocks.coal_block.equals(block)) {
            return state.cc;
        } else if (Blocks.quartz_block.equals(block)) {
            return state.cq;
        } else {
            return state.B;
        }
    }

    private static int compare(state s1, state s2) {
        int x = getWaterValueOf(s1), y = getWaterValueOf(s2);
        return x - y;
    }

    private static state getLowerFormOfWater(state s) {
        if (s == state.w || s == state.w6) return state.w5;
        if (s == state.w5) return state.w4;
        if (s == state.w4) return state.w3;
        if (s == state.w3) return state.w2;
        if (s == state.w2) return state.w1;
        if (s == state.w1) return state.E;
        return state.E;
    }

    private static int getWaterValueOf(state s) {
        int res = 0;
        if (s == state.w || s == state.w6) return 6;
        if (s == state.w5) return 5;
        if (s == state.w4) return 4;
        if (s == state.w3) return 3;
        if (s == state.w2) return 2;
        if (s == state.w1) return 1;
        return res;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        setRoom(null);
        facing = null;
        board = new state[20][21];
    }

    enum EnumFacing {
        zp, zn, xp, xn
    }

    enum state {
        // block, empty
        B, E,
        // coal, gold, quartz, diamond, emerald, clay (only use for calculating their positions)
        c, g, q, d, e, cl,
        // converted ones, blocks
        cc, cg, cq, cd, ce, ccl,
        // water, and 6 forms of water
        w, w1, w2, w3, w4, w5, w6
    }

    enum EnumOperation {
        c, g, q, d, e, cl,
        trig
    }
}
