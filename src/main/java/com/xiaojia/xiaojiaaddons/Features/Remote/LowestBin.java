package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LowestBin {
    private static final String url = "https://api.skytils.gg/api/auctions/lowestbins";
    private static final HashMap<String, Double> prices = new HashMap<>();
    private static long lastCheckedTime = 0;
    private static long lastSuccessTime = 0;

    public static int getLastUpdate() {
        return (int) ((TimeUtils.curTime() - lastSuccessTime) / 1000);
    }

    public static double getItemValue(ItemStack itemStack) throws Exception {
        String sbId = NBTUtils.getSkyBlockID(itemStack);
        if (sbId.equals("")) throw new Exception();
        String res = sbId;
        if (sbId.equals("PET")) {
            String str = NBTUtils.getStringFromExtraAttributes(itemStack, "petInfo");
            JsonObject jsonObject = (new Gson()).fromJson(str, JsonObject.class);
            if (jsonObject.has("type") && jsonObject.has("tier"))
                res = "PET-" + jsonObject.get("type").getAsString()
                        + "-" + jsonObject.get("tier").getAsString();
        } else if (sbId.equals("ENCHANTED_BOOK")) {
            Pair<String, Integer> enchant = NBTUtils.getFirstEnchantment(itemStack);
            if (enchant != null)
                res = "ENCHANTED_BOOK-" + enchant.getKey().toUpperCase() + "-" + enchant.getValue();
        } else if (sbId.equals("POTION")) {
            String potion = NBTUtils.getStringFromExtraAttributes(itemStack, "potion");
            int potionLevel = NBTUtils.getIntFromExtraAttributes(itemStack, "potion_level");
            res = "POTION-" + potion.toUpperCase() + "-" + potionLevel;
            if (NBTUtils.getBooleanFromExtraAttributes(itemStack, "enhanced"))
                res += "-ENHANCED";
            if (NBTUtils.getBooleanFromExtraAttributes(itemStack, "extended"))
                res += "-EXTENDED";
            if (NBTUtils.getBooleanFromExtraAttributes(itemStack, "splash"))
                res += "-SPLASH";
        } else if (sbId.equals("RUNE")) {
            Pair<String, Integer> rune = NBTUtils.getFirstRune(itemStack);
            if (rune != null)
                res = "RUNE-" + rune.getKey().toUpperCase() + "-" + rune.getValue();
        }

        if (prices.containsKey(res)) {
            return prices.get(res);
        }
        throw new Exception();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        long cur = TimeUtils.curTime();
        if (cur - lastCheckedTime >= 60000) {
            lastCheckedTime = cur;
            new Thread(() -> {
                try {
                    if (Configs.FetchLowestBin) {
                        String lbData = RemoteUtils.get(url, new ArrayList<>(), false);
                        Type type = (new TypeToken<HashMap<String, String>>() {
                        }).getType();
                        HashMap<String, String> settingsFromConfig = (new Gson()).fromJson(lbData, type);
                        for (Map.Entry<String, String> fromConfig : settingsFromConfig.entrySet()) {
                            prices.put(fromConfig.getKey(), Double.valueOf(fromConfig.getValue()));
                        }
                        lastSuccessTime = TimeUtils.curTime();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
