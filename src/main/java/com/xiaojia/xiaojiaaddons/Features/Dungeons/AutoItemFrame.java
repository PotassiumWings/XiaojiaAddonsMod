package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

enum Type {
    EMPTY, PATH, START, END
}

public class AutoItemFrame {
    private static final HashSet<MazeGrid> grid = new HashSet<>();
    private static final HashMap<Vector2i, Integer> neededRotation = new HashMap<>();
    private static BlockPos topLeft = new BlockPos(-2, 125, 79);
    private static BlockPos bottomRight = new BlockPos(-2, 121, 75);
    private static Iterable<BlockPos> iterableBox = BlockPos.getAllInBox(topLeft, bottomRight);
    private static ArrayList<BlockPos> box = new ArrayList<BlockPos>() {{
        iterableBox.forEach(this::add);
        sort((a, b) -> a.getY() == b.getY() ? b.getZ() - a.getZ() : b.getY() - a.getY());
    }};
    private Thread thread = null;

    public static void setPosition(int x, int y, int z) {
        topLeft = new BlockPos(x, y, z);
        bottomRight = new BlockPos(x, y - 4, z - 4);
        iterableBox = BlockPos.getAllInBox(topLeft, bottomRight);
        box = new ArrayList<BlockPos>() {{
            iterableBox.forEach(this::add);
            sort((a, b) -> a.getY() == b.getY() ? b.getZ() - a.getZ() : b.getY() - a.getY());
        }};

        grid.clear();
        neededRotation.clear();
    }

    private static HashMap<Vector2i, Integer> getDis(MazeGrid start, MazeGrid end) {
        // dijkstra
        int[] dx = new int[]{0, 0, -1, 1};
        int[] dy = new int[]{-1, 1, 0, 0};
        int[] delta = new int[]{5, 1, 7, 3};
        MazeGrid[][] maze = new MazeGrid[5][5];
        int[][] dis = new int[5][5];
        int[][] from = new int[5][5]; // from what i
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) dis[i][j] = 1000;
        for (MazeGrid mazeGrid : grid) maze[mazeGrid.gridPos.x][mazeGrid.gridPos.y] = mazeGrid;
        dis[start.gridPos.x][start.gridPos.y] = 0;
        HashMap<MazeGrid, Integer> hashMap = new HashMap<>();
        hashMap.put(start, 0);
        PriorityQueue<MazeGrid> queue = new PriorityQueue<>(Comparator.comparingInt(hashMap::get));
        queue.add(start);
        while (!queue.isEmpty()) {
            MazeGrid top = queue.poll();
            int x = top.gridPos.x, y = top.gridPos.y;
            for (int i = 0; i < 4; i++) {
                int tx = x + dx[i], ty = y + dy[i];
                if (tx >= 0 && tx < 5 && ty >= 0 && ty < 5 && maze[tx][ty].type != Type.EMPTY
                        && dis[tx][ty] > dis[x][y] + 1) {
                    dis[tx][ty] = dis[x][y] + 1;
                    from[tx][ty] = i;
                    if (tx == end.gridPos.x && ty == end.gridPos.y) {
                        HashMap<Vector2i, Integer> res = new HashMap<>();
                        Vector2i cur = new Vector2i(tx, ty);
                        while (!(tx == start.gridPos.x && ty == start.gridPos.y)) {
                            int pre = from[tx][ty];
                            tx -= dx[pre];
                            ty -= dy[pre];
                            res.put(new Vector2i(tx, ty), delta[pre]);
                        }
                        return res;
                    }
                    hashMap.put(maze[tx][ty], dis[tx][ty]);
                    queue.add(maze[tx][ty]);
                }
            }
        }
        return new HashMap<>();
    }

    @SubscribeEvent
    public void onTickCheck(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoItemFrameArrows) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (MathUtils.distanceSquareFromPlayer(topLeft) > 25 * 25) return;

        if (thread != null && thread.isAlive()) return;
        thread = new Thread(() -> {
            try {
                if (grid.size() < 25) {
                    // scan all ItemFrames from world
                    ArrayList<EntityItemFrame> frames = new ArrayList<>();
                    List<Entity> list = getWorld().loadedEntityList;
                    for (Entity entity : list)
                        if (entity instanceof EntityItemFrame && box.contains(entity.getPosition())) {
                            Item displayedItem = ((EntityItemFrame) entity).getDisplayedItem().getItem();
                            if (displayedItem.equals(Items.arrow) ||
                                    displayedItem.equals(Item.getItemFromBlock(Blocks.wool))) {
                                frames.add((EntityItemFrame) entity);
                            }
                        }
                    for (int i = 0; i < 25; i++) {
                        Vector2i mazePos = new Vector2i(i / 5, i % 5);
                        BlockPos blockPos = box.get(i);
                        Type type = Type.EMPTY;
                        for (EntityItemFrame frame : frames) {
                            if (frame.getPosition().equals(blockPos)) {
                                ItemStack displayedItemStack = frame.getDisplayedItem();
                                if (displayedItemStack.getItem() == Items.arrow) type = Type.PATH;
                                else if (displayedItemStack.getItem() == Item.getItemFromBlock(Blocks.wool)) {
                                    if (displayedItemStack.getItemDamage() == 5) type = Type.START;
                                    else if (displayedItemStack.getItemDamage() == 14) type = Type.END;
                                }
                            }
                        }
                        MazeGrid mazeGrid = new MazeGrid(blockPos, type, mazePos);
                        grid.add(mazeGrid);
                        ChatLib.debug(mazeGrid.toString());
                    }
                } else if (neededRotation.isEmpty()) {
                    // solve
                    ArrayList<MazeGrid> startPositions = new ArrayList<MazeGrid>();
                    ArrayList<MazeGrid> endPositions = new ArrayList<MazeGrid>();
                    ArrayList<Boolean> endPositionMatched = new ArrayList<>();
                    grid.stream().filter(e -> e.type == Type.START).forEach(startPositions::add);
                    grid.stream().filter(e -> e.type == Type.END).forEach(e -> {
                        endPositions.add(e);
                        endPositionMatched.add(false);
                    });
                    ChatLib.debug("grid:");
                    grid.forEach(e -> ChatLib.debug(e.toString()));
                    ChatLib.debug("start:");
                    startPositions.forEach(e -> ChatLib.debug(e.toString()));
                    ChatLib.debug("end:");
                    endPositions.forEach(e -> ChatLib.debug(e.toString()));
                    for (MazeGrid start : startPositions) {
                        int minDis = 1000;
                        MazeGrid correspondEnd = new MazeGrid(null, Type.EMPTY, new Vector2i(-1, -1));
                        HashMap<Vector2i, Integer> correspondRotations = new HashMap<>();
                        int correspondEndIndex = -1;
                        for (int i = 0; i < endPositions.size(); i++) {
                            MazeGrid end = endPositions.get(i);
                            HashMap<Vector2i, Integer> arr = getDis(start, end);
                            int dis = arr.size();
                            if (dis == 0) continue;
                            if (dis < minDis || dis == minDis && correspondEnd.compareTo(end) < 0) {
                                ChatLib.debug("end: " + end.gridPos + ", corr: " + correspondEnd.gridPos);
                                ChatLib.debug("dis: " + dis + ", minDis: " + minDis);
                                minDis = dis;
                                correspondEnd = end;
                                correspondEndIndex = i;
                                correspondRotations = arr;
                            }
                        }
                        if (!neededRotation.isEmpty()) endPositionMatched.set(correspondEndIndex, true);
                        correspondRotations.forEach(neededRotation::put);
                    }
                    for (int i = 0; i < endPositions.size(); i++) {
                        MazeGrid end = endPositions.get(i);
                        if (endPositionMatched.get(i)) continue;
                        int minDis = 1000;
                        MazeGrid correspondStart = new MazeGrid(null, Type.EMPTY, new Vector2i(-1, -1));
                        HashMap<Vector2i, Integer> correspondRotations = new HashMap<>();
                        for (MazeGrid start : startPositions) {
                            HashMap<Vector2i, Integer> arr = getDis(start, end);
                            int dis = arr.size();
                            if (dis == 0) continue;
                            if (dis < minDis || dis == minDis && correspondStart.compareTo(end) < 0) {
                                ChatLib.debug("start: " + end.gridPos + ", corr: " + correspondStart.gridPos);
                                ChatLib.debug("dis: " + dis + ", minDis: " + minDis);
                                minDis = dis;
                                correspondStart = end;
                                correspondRotations = arr;
                            }
                        }
                        correspondRotations.forEach(neededRotation::put);
                    }
                } else {
                    // calculated, work!
                    if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
                        Entity entity = mc.objectMouseOver.entityHit;
                        if (!(entity instanceof EntityItemFrame)) return;

                        ItemStack displayedItem = ((EntityItemFrame) entity).getDisplayedItem();
                        if (displayedItem == null || !displayedItem.getUnlocalizedName().contains("arrow")) return;
                        int currentRotation = ((EntityItemFrame) entity).getRotation();
                        int index = box.indexOf(entity.getPosition());
                        Vector2i gridVec = new Vector2i(index / 5, index % 5);
                        int toClick = neededRotation.get(gridVec) - currentRotation;
                        if (toClick < 0) toClick += 8;
                        for (int i = 0; i < toClick; i++) {
                            if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit != entity) return;
                            if (neededRotation.get(gridVec) == ((EntityItemFrame) mc.objectMouseOver.entityHit).getRotation())
                                return;
                            ControlUtils.rightClick();
                            Thread.sleep(Configs.ArrowCD);
                        }
                        Thread.sleep(Configs.ArrowCDBetween);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        grid.clear();
        neededRotation.clear();
    }
}

class MazeGrid implements Comparable {
    public BlockPos pos = null;
    public Type type;
    public Vector2i gridPos;

    MazeGrid(BlockPos pos, Type type, Vector2i gridPos) {
        this.pos = pos;
        this.type = type;
        this.gridPos = gridPos;
    }

    public int hashCode() {
        return gridPos.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof MazeGrid)) return false;
        return pos == ((MazeGrid) o).pos;
    }

    public int compareTo(Object o) {
        if (o instanceof MazeGrid) {
            MazeGrid maze = (MazeGrid) o;
            return maze.gridPos.x * 5 + maze.gridPos.y - (gridPos.x * 5 + gridPos.y);
        }
        return 0;
    }

    public String toString() {
        return pos.toString() + ", " + type + ", " + gridPos;
    }
}