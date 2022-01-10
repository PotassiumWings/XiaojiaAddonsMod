package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Map {
    public static boolean calibrated = false;
    public static Vector2i startCorner = new Vector2i(5, 5);
    public static int roomSize = 16;

    public static void reset() {
        calibrated = false;
        startCorner = new Vector2i(5, 5);
        roomSize = 16;
    }

    public static void calibrate() {
        ChatLib.debug("in map: dungeon int " + Dungeon.floorInt);
        if (Dungeon.floorInt == -1) {
            if (XiaojiaAddons.isDebug()) ChatLib.chat("failed!");
            return;
        }
        if (Dungeon.totalRooms > 30) startCorner = new Vector2i(5, 5);
        else if (Dungeon.totalRooms == 30) startCorner = new Vector2i(16, 5);
        else if (Dungeon.totalRooms == 25) startCorner = new Vector2i(11, 11);

        if (Dungeon.floorInt <= 3 || Dungeon.totalRooms == 25) roomSize = 18;
        else roomSize = 16;

        if (Dungeon.floorInt == 1) startCorner = new Vector2i(22, 11);
        else if (Dungeon.floorInt == 2 || Dungeon.floorInt == 3) startCorner = new Vector2i(11, 11);
        else if (Dungeon.floorInt == 4 && Dungeon.totalRooms > 25) startCorner = new Vector2i(5, 16);

        ChatLib.debug("start corner: " + startCorner.x + ", " + startCorner.y);
        ChatLib.debug("roomsize: " + roomSize);
        ChatLib.debug("dungeon floor int: " + Dungeon.floorInt);
        calibrated = true;
    }

    public static ItemStack getMap() {
        ItemStack item = ControlUtils.getItemStackInSlot(8 + 36, true);
        if (item != null && Item.getIdFromItem(item.getItem()) == 358 &&
                item.getDisplayName().contains("Magical Map")) {
            return item;
        }
        return null;
    }

    public static MapData getMapData() {
        ItemStack map = getMap();
        if (map == null || !(map.getItem() instanceof ItemMap)) return null;
        return ((ItemMap) map.getItem()).getMapData(map, getWorld());
    }

    public static java.util.Map<String, Vec4b> getMapDecorators() {
        MapData mapData = getMapData();
        if (mapData == null) return null;
        return mapData.mapDecorations;
    }

    public static byte[] getMapColors() {
        MapData mapData = getMapData();
        if (mapData == null) return null;
        return mapData.colors;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        reset();
    }
}
