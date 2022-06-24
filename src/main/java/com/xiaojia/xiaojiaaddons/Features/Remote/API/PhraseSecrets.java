package com.xiaojia.xiaojiaaddons.Features.Remote.API;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xiaojia.xiaojiaaddons.utils.JsonUtils;

public class PhraseSecrets {
    private int secrets;

    public PhraseSecrets(JsonObject playerData) {
        if (playerData == null)
            return;

        JsonElement achievementElem = playerData.get("achievements");
        if (achievementElem == null)
            return;

        JsonObject achievementData = achievementElem.getAsJsonObject();
        this.secrets = JsonUtils.getInt(achievementData, "skyblock_treasure_hunter");
    }

    public int getSecrets() {
        return this.secrets;
    }
}
