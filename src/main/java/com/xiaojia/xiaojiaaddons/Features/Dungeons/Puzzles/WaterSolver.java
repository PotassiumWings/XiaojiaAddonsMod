package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles;

import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


// TODO: DELETE THIS FROM ROOM.JSON
public class WaterSolver {
    private static final int width = 21;
    private static final int height = 24;
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
    private static final int gap = 15;
    public static Room room = null;
    private static EnumFacing facing = null;
    private static state[][] board = new state[height][width];
    private static TreeMap<Integer, EnumOperation> operations = new TreeMap<>();
    private static int bestTime;
    private static int lastFlag;

    public static void solve() throws InterruptedException {
        operations.clear();
        bestTime = 120;
        // facing
        if (BlockUtils.getBlockAt(room.x, 68, room.z - 9).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.zn;
        if (BlockUtils.getBlockAt(room.x, 68, room.z + 9).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.zp;
        if (BlockUtils.getBlockAt(room.x - 9, 68, room.z).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.xn;
        if (BlockUtils.getBlockAt(room.x + 9, 68, room.z).equals(Blocks.stone_brick_stairs)) facing = EnumFacing.xp;
        if (facing == null) return;
        System.err.println("facing: " + facing + ", x " + room.x + ", z " + room.z);
        // get board, piston
        int sy = 60, ty = 83;
        int flag = 0;
        if (facing == EnumFacing.xn || facing == EnumFacing.xp) {
            int x = facing == EnumFacing.xp ? room.x - 11 : room.x + 11;
            int deviceX = facing == EnumFacing.xp ? room.x - 12 : room.x + 12;
            int sz = room.z - 10, tz = room.z + 10;
            if (facing == EnumFacing.xn) flag = getFlag(room.x + 4, room.z + 2, room.x, room.z + 2);
            else flag = getFlag(room.x - 4, room.z + 2, room.x, room.z + 2);
            for (int z = sz; z <= tz; z++)
                for (int y = sy; y <= ty; y++)
                    board[y - sy][z - sz] = getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(deviceX, y, z));
        } else {
            int z = facing == EnumFacing.zp ? room.z - 11 : room.z + 11;
            int deviceZ = facing == EnumFacing.zp ? room.z - 12 : room.z + 12;
            int sx = room.z - 10, tx = room.z + 10;
            if (facing == EnumFacing.zn) flag = getFlag(room.x + 2, room.z + 4, room.x + 2, room.z);
            else flag = getFlag(room.x + 2, room.z - 4, room.x + 2, room.z);
            for (int x = sx; x <= tx; x++)
                for (int y = sy; y <= ty; y++)
                    board[y - sy][x - sx] = getStateFromBlock(BlockUtils.getBlockAt(x, y, z), BlockUtils.getBlockAt(x, y, deviceZ));
        }
        if (flag == 0) {
            Thread.sleep(100);
            solve();
            return;
        }
        board[23][10] = state.w;
        print2(board);
        System.err.println("flag: " + flag);
        // get board, piston
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
                    board[i][j] = WaterSolver.state.E;
            }
        }
        // calc
        bestTime = 120;
        print(board);
        ChatLib.chat("Calculating possible solutions...");
        long startTime = TimeUtils.curTime();
        if (lastFlag != flag) {
            dfs(board, -gap, new HashMap<>(), flag);
            lastFlag = flag;
        }
        ChatLib.chat(String.format("Estimate best solution: %.2fs (%.2fs to calc)", bestTime * 0.25, (TimeUtils.curTime() - startTime) / 1000F));
        for (Map.Entry<Integer, EnumOperation> operation : operations.entrySet()) {
            if (operation.getValue().equals(EnumOperation.empty) || operation.getValue().equals(EnumOperation.trig)) continue;
            ChatLib.chat("  " + operation.getKey() * 0.25 + "s: " + getMessageFromOperation(operation.getValue()));
        }
    }

    private static String getMessageFromOperation(EnumOperation o) {
        if (o == EnumOperation.c) return "&0Coal";
        if (o == EnumOperation.cl) return "&4Clay";
        if (o == EnumOperation.e) return "&aEmerald";
        if (o == EnumOperation.d) return "&bDiamond";
        if (o == EnumOperation.q) return "&fQuartz";
        if (o == EnumOperation.g) return "&6Gold";
        return "";
    }

    private static void print2(state[][] board) {
        for (int i = 0; i < height; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < width; j++) {
                String cur = board[i][j].toString();
                s.append(String.format("board[%d][%d]=awa.state.%s;", i, j, cur));
            }
            System.err.print(s);
        }
        System.err.println();
    }

    private static void print(state[][] board) {
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
        System.out.println("");
    }

    private static void dfs(state[][] state, int time, HashMap<Integer, EnumOperation> order, int flag) {
        if (order.values().stream().filter(e -> !e.equals(EnumOperation.empty) && !e.equals(EnumOperation.trig)).count() > 5 ||
                order.size() > 7) return;
//        if (order.size() > 5) return;
        if (time >= bestTime) return;
        // simulate without further operations
//        System.out.println(flag);
//        print(state);
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
            Pair<state[][], Integer> stateAndFlag = simulate(state);
            state = stateAndFlag.getKey();
            flag ^= stateAndFlag.getValue();
        }
        time += gap;
        for (EnumOperation operation : EnumOperation.values()) {
            if ((operation != EnumOperation.trig && order.size() == 0 ||
                    operation == EnumOperation.trig && order.size() > 0)) continue;
//            if (!(order.size() == 0 && operation == EnumOperation.trig ||
//                    order.size() == 1 && operation == EnumOperation.empty ||
//                    order.size() == 2 && operation == EnumOperation.empty ||
//                    order.size() == 3 && operation == EnumOperation.e ||
//                    order.size() == 4 && operation == EnumOperation.q ||
//                    order.size() == 5 && operation == EnumOperation.empty ||
//                    order.size() == 6 && operation == EnumOperation.cl
//            )) continue;
            state[][] newState = new state[height][width];
            for (int i = 0; i < height; i++) System.arraycopy(state[i], 0, newState[i], 0, width);
            order.put(time, operation);
            if (operation == EnumOperation.trig)
                newState[22][10] = newState[22][10] == WaterSolver.state.w ? WaterSolver.state.B : WaterSolver.state.w;
            else if (operation != EnumOperation.empty) {
                ArrayList<Pair<Integer, Integer>> coords = stateMap.get(operation);
                for (Pair<Integer, Integer> xy : coords) {
                    int i = xy.getKey(), j = xy.getValue();
                    state t = newState[i][j];
                    if (t == WaterSolver.state.cc || t == WaterSolver.state.ce || t == WaterSolver.state.cd ||
                            t == WaterSolver.state.ccl || t == WaterSolver.state.cq || t == WaterSolver.state.cg ||
                            t == WaterSolver.state.B)
                        newState[i][j] = WaterSolver.state.E;
                    else {
                        if (operation == EnumOperation.c) newState[i][j] = WaterSolver.state.cc;
                        else if (operation == EnumOperation.d) newState[i][j] = WaterSolver.state.cd;
                        else if (operation == EnumOperation.e) newState[i][j] = WaterSolver.state.ce;
                        else if (operation == EnumOperation.g) newState[i][j] = WaterSolver.state.cg;
                        else if (operation == EnumOperation.q) newState[i][j] = WaterSolver.state.cq;
                        else if (operation == EnumOperation.cl) newState[i][j] = WaterSolver.state.ccl;
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
        Pair<state[][], Integer> stateAndFlag = simulate(state);
        while (!Arrays.deepEquals(state, stateAndFlag.getKey())) {
            state = stateAndFlag.getKey();
            flag ^= stateAndFlag.getValue();
            cnt++;
            stateAndFlag = simulate(state);
        }
        return new Pair<>(cnt, flag);
    }

    // simulate one time, return state and flag
    private static Pair<state[][], Integer> simulate(state[][] state) {
        state[][] newState = new state[height][width];
        // only copies not water; water: above/aside with water
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!isWater(state[i][j])) newState[i][j] = state[i][j];
                else if (j > 1 && isWater(state[i][j - 1]) || j < width - 1 && isWater(state[i][j + 1]) ||
                        i + 1 < height && isWater(state[i + 1][j]))
                    newState[i][j] = state[i][j];
                else newState[i][j] = WaterSolver.state.E;
            }
        }
        newState[23][10] = WaterSolver.state.w;
//        print(newState);
        // determines where to flow
        for (int i = 1; i < height - 1; i++) {
            int beginJ = -1, endJ = -1;
            int beginW = -1, endW = -1;
            for (int j = 0; j < width; j++) {
                state cur = state[i][j];
                // next layer is water
                if (isWater(cur)) {
                    if (isWater(state[i + 1][j])) {
                        if (beginJ == -1) beginJ = j;
                        endJ = j;
                    }
                    if (beginW == -1) beginW = j;
                    endW = j;

                    // straight down
                    if (!isBlock(newState[i - 1][j]))
                        newState[i - 1][j] = WaterSolver.state.w;
                    // beside
                } else {
                    if (beginJ != -1) {
                        // calculate [x - startJ - endJ - y]

                        // beginJ: if left < right, then should go left
                        int left = 0, right = 0;
                        while (canExtendLeft(newState, i, beginJ - left)) left++;
                        while (canExtendRight(newState, i, beginJ + right)) right++;
                        if (left == 0) left = 1000;
                        if (right == 0) right = 1000;
                        if (left < right && canExtendLeft(newState, i, beginW))
                            newState[i][beginW - 1] = newState[i][beginW];

                        left = 0;
                        right = 0;
                        while (canExtendLeft(newState, i, endJ - left)) left++;
                        while (canExtendRight(newState, i, endJ + right)) right++;
                        if (left == 0) left = 1000;
                        if (right == 0) right = 1000;
                        if (left > right && canExtendRight(newState, i, endW))
                            newState[i][endW + 1] = newState[i][endW];
                    } else if (beginW != -1) {
                        // no upper, compare left / right
                        int left = 0, right = 0;
                        while (canExtendLeft(newState, i, beginW - left)) left++;
                        while (canExtendRight(newState, i, endW + right)) right++;
                        if (left == 0) left = 1000;
                        if (right == 0) right = 1000;
                        if (left < right) {
                            if (canExtendLeft(newState, i, beginW)) newState[i][beginW - 1] = newState[i][beginW];
                        } else if (left != 1000 && left == right) {
                            if (canExtendLeft(newState, i, beginW)) newState[i][beginW - 1] = newState[i][beginW];
                            if (canExtendRight(newState, i, endW)) newState[i][endW + 1] = newState[i][beginW];
                        } else if (left > right) {
                            if (canExtendRight(newState, i, endW)) newState[i][endW + 1] = newState[i][beginW];
                        }
                    }
                    beginJ = endJ = -1;
                    beginW = endW = -1;
                }
            }
        }
//        print(newState);
        // calculate decrease
        for (int i = 0; i < height - 1; i++) {
            boolean waterAboveCurrentWaterFlow = false;
            state maxState = WaterSolver.state.E;
            int cnt = 0; // consecutive water count
            for (int j = 0; j < width; j++) {
                state cur = newState[i][j];
                if (isWater(cur)) {
                    waterAboveCurrentWaterFlow |= isWater(newState[i + 1][j]);
                    if (compare(maxState, cur) < 0) maxState = cur;
                    cnt++;
                } else {
                    // do the calculation stuff
                    if (waterAboveCurrentWaterFlow) {
                        for (int k = j - 1; k >= 0 && isWater(newState[i][k]); k--) {
                            newState[i][k] = WaterSolver.state.w;
                        }
                    } else {
                        if (cnt != 1)
                            for (int k = j - 1; k >= 0 && isWater(newState[i][k]); k--) {
                                newState[i][k] = getLowerFormOfWater(maxState);
                            }
                    }
                    // reset
                    waterAboveCurrentWaterFlow = false;
                    maxState = WaterSolver.state.E;
                    cnt = 0;
                }
            }
        }
//        print(newState);

        // calc return value
        int flag = 0;
        for (Pair<Integer, Integer> pr : yToFlag)
            if (!isWater(state[0][pr.getKey()]) && isWater(newState[0][pr.getKey()]))
                flag |= 1 << pr.getValue();
        return new Pair<>(newState, flag);
    }

    private static boolean canExtendLeft(state[][] state, int i, int j) {
        return !isBlock(state[i][j - 1]) && isBlock(state[i - 1][j]);
    }

    private static boolean canExtendRight(state[][] state, int i, int j) {
        return !isBlock(state[i][j + 1]) && isBlock(state[i - 1][j]);
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

    public static void setRoom(Room waterRoom) {
        room = waterRoom;
        if (room != null)
            new Thread(() -> {
                try {
                    solve();
                } catch (Exception ignored) {
                }
            }).start();
    }

    private static int getFlag(int sx, int sz, int tx, int tz) {
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

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        setRoom(null);
        facing = null;
        board = new state[20][21];
        lastFlag = -1;
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
        trig, e,
        c, g, q, d, cl, empty
    }
}
