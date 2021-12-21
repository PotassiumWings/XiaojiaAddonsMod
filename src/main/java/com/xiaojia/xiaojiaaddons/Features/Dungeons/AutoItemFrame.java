package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
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
import scala.actors.threadpool.Arrays;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoItemFrame {
    private static final BlockPos topLeft = new BlockPos(197, 125, 278);
    private static final BlockPos bottomRight = new BlockPos(197, 121, 274);
    private static final Iterable<BlockPos> iterableBox = BlockPos.getAllInBox(topLeft, bottomRight);
    private static final ArrayList<BlockPos> box = new ArrayList<BlockPos>() {{
        iterableBox.forEach(this::add);
        sort((a, b) -> a.getY() == b.getY() ? b.getZ() - a.getZ() : b.getY() - a.getY());
    }};
    private static final HashSet<MazeGrid> grid = new HashSet<>();
    private static final HashMap<Vector2i, Integer> neededRotation = new HashMap<>();

    private Thread thread = null;

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
                        grid.add(new MazeGrid(blockPos, type, mazePos));
                    }
                } else if (neededRotation.isEmpty()) {
                    // solve
                    ArrayList<MazeGrid> startPositions = new ArrayList<MazeGrid>(Arrays.asList(grid.stream().filter(e -> e.type == Type.START).toArray()));
                    ArrayList<MazeGrid> endPositions = new ArrayList<MazeGrid>(Arrays.asList(grid.stream().filter(e -> e.type == Type.END).toArray()));
                    for (MazeGrid start : startPositions) {
                        for (MazeGrid end : endPositions) {
                            HashMap<Vector2i, Integer> result = solve(start, end);
                            result.forEach(neededRotation::put);
                        }
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
                            ControlUtils.rightClick();
                            Thread.sleep(Configs.ArrowCD);
                        }
                        Thread.sleep(100);
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

    private HashMap<Vector2i, Integer> solve(MazeGrid start, MazeGrid end) {
        // from end dfs
        int[] dx = new int[]{0, 0, -1, 1};
        int[] dy = new int[]{-1, 1, 0, 0};
        int[] delta = new int[]{1, 5, 3, 7};
        ArrayDeque<MazeGrid> queue = new ArrayDeque<>();
        queue.add(end);
        MazeGrid[][] maze = new MazeGrid[5][5];
        boolean[][] vis = new boolean[5][5];
        for (MazeGrid mazeGrid : grid) maze[mazeGrid.gridPos.x][mazeGrid.gridPos.y] = mazeGrid;
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) vis[i][j] = false;
        HashMap<Vector2i, Integer> res = new HashMap<>();
        while (!queue.isEmpty()) {
            MazeGrid current = queue.pollFirst();
            Vector2i mazePos = current.gridPos;
            vis[mazePos.x][mazePos.y] = true;
            for (int i = 0; i < 4; i++) {
                int tx = mazePos.x + dx[i];
                int ty = mazePos.y + dy[i];
                if (tx >= 0 && tx < 5 && ty >= 0 && ty < 5 &&
                        !vis[tx][ty] && maze[tx][ty] != null && maze[tx][ty].type == Type.PATH) {
                    MazeGrid nextGrid = maze[tx][ty];
                    res.put(nextGrid.gridPos, delta[i]);
                    queue.add(nextGrid);
                }
            }
        }
        return res;
    }
}


class MazeGrid {
    public BlockPos pos = null;
    public Type type;
    public Vector2i gridPos;

    public int hashCode() {
        return gridPos.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof MazeGrid)) return false;
        return pos == ((MazeGrid) o).pos;
    }

    MazeGrid(BlockPos pos, Type type, Vector2i gridPos) {
        this.pos = pos;
        this.type = type;
        this.gridPos = gridPos;
    }
}

enum Type {
    EMPTY, PATH, START, END
}