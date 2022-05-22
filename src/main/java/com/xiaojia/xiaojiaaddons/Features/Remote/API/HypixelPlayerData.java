package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HypixelPlayerData {

    private final JsonObject playerData;
    private final String uuid;

    public HypixelPlayerData(String uuid, String apikey) throws ApiException {

        HttpClient httpclient = new HttpClient("https://api.hypixel.net/player?key=" + apikey + "&uuid=" + uuid);
        String rawresponse = httpclient.getrawresponse();

        if (rawresponse == null)
            throw new ApiException("No response from Hypixel's Api");

        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(rawresponse).getAsJsonObject();

        if (obj == null)
            throw new ApiException("Cannot parse response from Hypixel's Api");

        if (!obj.get("success").getAsBoolean()) {

            JsonElement element = obj.get("cause");
            String msg = element.getAsString();

            if (msg == null) {
                throw new ApiException("Failed to retreive data from Hypixel's Api for this player");
            } else {
                throw new ApiException(msg);
            }

        }

        JsonElement playerdataElem = obj.get("player");

        if (playerdataElem == null || !playerdataElem.isJsonObject()) {
            throw new ApiException("This player never joined Hypixel, it might be a nick.");
        }

        JsonObject playerdata = playerdataElem.getAsJsonObject();

        if (playerdata == null)
            throw new ApiException("An error occured while parsing data for this player on Hypixel's Api");

        this.playerData = playerdata;
        this.uuid = uuid;

    }

    public JsonObject getPlayerData() {
        return this.playerData;
    }

    public String getUuid() {
        return this.uuid;
    }
}