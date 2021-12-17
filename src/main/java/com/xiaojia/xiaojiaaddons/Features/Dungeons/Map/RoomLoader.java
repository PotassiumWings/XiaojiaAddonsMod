package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
public class RoomLoader {
    public static ArrayList<Data> maps = new ArrayList<>();

    public static void load() {
        try {
            ResourceLocation rooms = new ResourceLocation(XiaojiaAddons.MODID + ":rooms.json");
            InputStream input = mc.getResourceManager().getResource(rooms).getInputStream();
            JsonParser parser = new JsonParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            JsonObject fullJson = parser.parse(reader).getAsJsonObject();
            JsonArray roomsArray = fullJson.getAsJsonArray("rooms");
            for (JsonElement room: roomsArray) {
                Data data = new Data();
                JsonObject roomObject = room.getAsJsonObject();
                data.name = roomObject.get("name").getAsString();
                data.type = roomObject.get("type").getAsString();
                data.secrets = roomObject.get("secrets").getAsInt();
                data.cores = new ArrayList<>();
                JsonArray coresArray = roomObject.getAsJsonArray("cores");
                for (JsonElement element : coresArray) {
                    data.cores.add(element.getAsInt());
                }
                maps.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
