package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class DupedItems {
    private static final HashSet<String> dupedUuids = new HashSet<>();
    private static final String url = "https://api.ner.gg/duped";
    private static final double dupedScale = 0.7;
    private static final String dupedString = "\u00a7c\u00a7lDup";
    private long lastFetch = 0;

    public static void load() {
        try {
            ResourceLocation rooms = new ResourceLocation(XiaojiaAddons.MODID + ":dupedUuids.json");
            InputStream input = mc.getResourceManager().getResource(rooms).getInputStream();
            JsonParser parser = new JsonParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            JsonObject fullJson = parser.parse(reader).getAsJsonObject();
            update(fullJson);
        } catch (Exception ignored) {
        }
    }

    private static void update(JsonObject uuidsObject) {
        JsonArray uuidsArray = uuidsObject.getAsJsonArray("dupedUuids");
        for (JsonElement jsonElement : uuidsArray) {
            String uuid = jsonElement.getAsString();
            dupedUuids.add(uuid);
        }
    }

    public static boolean isItemDuped(ItemStack itemStack) {
        return dupedUuids.contains(NBTUtils.getUUID(itemStack));
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Configs.ShowDupedItems) return;
        if (TimeUtils.curTime() - lastFetch > 1000 * 60 * 10) {
            lastFetch = TimeUtils.curTime();
            new Thread(() -> {
                String res = RemoteUtils.get(url, new ArrayList<>(), false);
                JsonParser parser = new JsonParser();
                JsonObject fullJson = parser.parse(res).getAsJsonObject();
                update(fullJson);
            }).start();
        }
    }

    @SubscribeEvent
    public void onRenderItem(ItemDrawnEvent event) {
        if (!Configs.ShowDupedItems) return;
        ItemStack itemStack = event.itemStack;
        if (isItemDuped(itemStack)) {
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();

            GlStateManager.pushMatrix();
            GlStateManager.translate(
                    event.x,
                    event.y + 16 - MathUtils.ceil(RenderUtils.getStringHeight(dupedString) * dupedScale),
                    1.0);
            GlStateManager.scale(dupedScale, dupedScale, 1);
            event.renderer.drawString(
                    dupedString, 1F, 1F, 0xffffffff, true
            );
            GlStateManager.popMatrix();

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
}
