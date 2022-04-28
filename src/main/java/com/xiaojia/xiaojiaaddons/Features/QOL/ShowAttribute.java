package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowAttribute {
    private static final float nameScale = 0.8F;
    private static final float levelScale = 0.7F;
    private static final HashMap<ItemStack, String> cachedStrings = new HashMap<>();

    public static int getCacheSize() {
        return cachedStrings.size();
    }

    @SubscribeEvent
    public void onItemDrawn(ItemDrawnEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled || inventory == null || !Configs.ShowAttribute) return;
        ItemStack itemStack = event.itemStack;
        if (itemStack == null || !itemStack.hasDisplayName()) return;
        String name = ChatLib.removeFormatting(itemStack.getDisplayName()).toLowerCase();
        if (!name.startsWith("attribute shard")) return;
        String nameString = "";
        String levelString = "";
        if (cachedStrings.containsKey(itemStack)) {
            String p = cachedStrings.get(itemStack);
            nameString = p.split(":")[0];
            levelString = p.split(":")[1];
        } else {
            try {
                ArrayList<String> nameAndLevel = NBTUtils.getBookNameAndLevel(itemStack);
                if (nameAndLevel.size() != 2) return;

                nameString = nameAndLevel.get(0);
                levelString = nameAndLevel.get(1);
                cachedStrings.put(itemStack, nameString + ":" + levelString);
                if (cachedStrings.size() > 1000)
                    cachedStrings.clear();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (!nameString.toLowerCase().contains(Configs.ShowAttributeName.toLowerCase())) return;

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translate(event.x, event.y, 1f);
        GlStateManager.scale(nameScale, nameScale, 1.0);
        event.renderer.drawString(
                nameString.substring(0, Math.min(nameString.length(), 3)), 1F, 1F, 0xffffffff, true
        );
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(
                event.x + 16 - MathUtils.ceil(RenderUtils.getStringWidth(levelString) * levelScale),
                event.y + 16 - MathUtils.ceil(RenderUtils.getStringHeight(levelString) * levelScale),
                1f
        );
        GlStateManager.scale(levelScale, levelScale, 1.0);
        event.renderer.drawString(
                levelString, 0, 0, 0xffffffff, true
        );
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }
}
