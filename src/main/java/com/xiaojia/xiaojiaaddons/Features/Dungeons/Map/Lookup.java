package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;

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

    public Room getRoomFromCoords(Coords coords, Dungeon dungeon) {
        Coords newCoords = this.getRoomCenterCoords(coords, dungeon);
        if (newCoords.x == -1) {
            ChatLib.chat("Non");
            return null;
        }
        Room room = new Room(newCoords.x, newCoords.z,
                new Data("Unknown", "normal", 0, new ArrayList<>()));
        Room foundRoom = getRoomDataFromHash(room.core);
        if (foundRoom != null) {
            room.name = foundRoom.name;
            room.type = foundRoom.type;
            room.secrets = foundRoom.secrets;
        }
        return room;
    }

    public Coords getRoomCenterCoords(Coords coords, Dungeon dungeon) {
        for (int x = dungeon.startX; x <= dungeon.startX + (dungeon.roomSize + 1) * 5; x += 16) {
            for (int z = dungeon.startZ; z <= dungeon.startZ + (dungeon.roomSize + 1) * 5; z += 16) {
                if (x % (dungeon.roomSize + 1) == 15 && z % (dungeon.roomSize + 1) == 15) {
                    if (MapUtils.isBetween(coords.x, x + 16, x - 16) &&
                            MapUtils.isBetween(coords.z, z + 16, z - 16)) {
                        return new Coords(x, z);
                    }
                }
            }
        }
        return new Coords(-1, -1);
    }
}
