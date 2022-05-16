package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowBookName {
    private static final float nameScale = 0.8F;
    private static final float levelScale = 0.7F;
    private static final HashMap<ItemStack, String> cachedStrings = new HashMap<>();

    public static int getCacheSize() {
        return cachedStrings.size();
    }

    @SubscribeEvent
    public void onItemDrawn(ItemDrawnEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled || inventory == null || !Configs.ShowBookName) return;
        if (inventory.getName().contains("Superpairs (")) return;
        ItemStack itemStack = event.itemStack;
        if (itemStack == null || !itemStack.hasDisplayName()) return;
        String name = ChatLib.removeFormatting(itemStack.getDisplayName()).toLowerCase();
        if (!name.startsWith("enchanted book")) return;
        String nameString = "";
        String levelString = "";
        if (cachedStrings.containsKey(itemStack)) {
            String p = cachedStrings.get(itemStack);
            nameString = p.split(" ")[0];
            levelString = p.split(" ")[1];
        } else {
            try {
                ArrayList<String> nameAndLevel = NBTUtils.getBookNameAndLevel(itemStack);
                if (nameAndLevel.size() != 2) return;
                boolean isUltimate = NBTUtils.isBookUltimate(itemStack);

                String colorPrefix = (isUltimate ? "\u00a7d\u00a7l" : "");
                String bookName = nameAndLevel.get(0);
                String compactName = bookName.substring(0, Math.min(bookName.length(), 3));
                if (isUltimate) compactName = bookName.replaceAll("[a-z ]", "");
                nameString = colorPrefix + compactName;
                levelString = nameAndLevel.get(1);
                cachedStrings.put(itemStack, nameString + " " + levelString);
                if (cachedStrings.size() > 1000)
                    cachedStrings.clear();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        GuiUtils.drawNameAndLevel(event.renderer,nameString, levelString, event.x, event.y, nameScale, levelScale);
    }
}
