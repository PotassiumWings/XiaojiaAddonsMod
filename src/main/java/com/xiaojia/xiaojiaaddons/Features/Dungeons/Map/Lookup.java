package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.MathUtils;

import java.util.ArrayList;

public class Lookup {
    public static Room getRoomDataFromHash(int hash) {
        ArrayList<Data> rooms = RoomLoader.maps;
        for (Data room : rooms) {
            if (MapUtils.includes(room.cores, hash)) {
                return new Room(-1, -1, new Data(
                        room.name, room.type, room.secrets, room.cores
                ));
            }
        }
        return null;
    }

    public static Room getRoomFromCoords(Vector2i coords) {
        Vector2i newCoords = getRoomCenterCoords(coords);
        if (newCoords == null) return null;
        Room room = new Room(newCoords.x, newCoords.y,
                new Data("Unknown", "normal", 0, new ArrayList<>()));
        Room foundRoom = getRoomDataFromHash(room.core);
        if (foundRoom == null) return null;
        room.name = foundRoom.name;
        room.type = foundRoom.type;
        room.secrets = foundRoom.secrets;
        return room;
    }

    public static Vector2i getRoomCenterCoords(Vector2i coords) {
        for (int x = Dungeon.startX; x <= Dungeon.startX + (Dungeon.roomSize + 1) * 5; x += 32) {
            for (int z = Dungeon.startZ; z <= Dungeon.startZ + (Dungeon.roomSize + 1) * 5; z += 32) {
                if (MathUtils.isBetween(coords.x, x + 16, x - 16) &&
                        MathUtils.isBetween(coords.y, z + 16, z - 16)) {
                    return new Vector2i(x, z);
                }
            }
        }
        return null;
    }
}
