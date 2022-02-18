package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.GuiContainerEvent;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.StringUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class ShowBookName {
    private static final float nameScale = 0.8F;
    private static final float levelScale = 0.7F;
    private final boolean[] isBook = new boolean[120];
    private final String[] nameStrings = new String[120];
    private final String[] levelStrings = new String[120];

    private final HashMap<ItemStack, String> cachedStrings = new HashMap<>();

    @SubscribeEvent
    public void onSlotDraw(GuiContainerEvent.DrawSlotEvent.Post event) {
//        Inventory inventory = ControlUtils.getOpenedInventory();
//        if (!Checker.enabled || inventory == null || !Configs.ShowBookName) return;
//        int i = event.slot.slotNumber;
//        if (!isBook[i] || nameStrings[i] == null || levelStrings[i] == null) return;
//        List<Slot> invSlots = inventory.getSlots();
//        Slot slot = invSlots.get(i);
//        String nameString = nameStrings[i];
//        GuiUtils.drawStringOnSlot(
//                nameString, invSlots.size(), slot.xDisplayPosition, slot.yDisplayPosition,
//                nameScale, 0, 0
//        );
//
//        String levelString = levelStrings[i];
//        GuiUtils.drawStringOnSlot(
//                levelString, invSlots.size(), slot.xDisplayPosition, slot.yDisplayPosition,
//                levelScale, 8 - MathUtils.ceil(RenderUtils.getStringWidth(levelString) * levelScale),
//                8 - MathUtils.ceil(RenderUtils.getStringHeight(levelString) * levelScale)
//        );
    }

    @SubscribeEvent
    public void onGuiRender(GuiContainerEvent.ScreenDrawnEvent event) {
//        Inventory inventory = ControlUtils.getOpenedInventory();
//        if (!Checker.enabled || inventory == null || !Configs.ShowBookName) return;
//        List<Slot> invSlots = inventory.getSlots();
//        GuiUtils.drawStringOnSlot("owo", invSlots.size(), 0, 0, 1F, 0, 0);
//        for (int i = 0; i < invSlots.size(); i++) {
//            if (!isBook[i] || nameStrings[i] == null || levelStrings[i] == null) continue;
//            Slot slot = invSlots.get(i);
//            String nameString = nameStrings[i];
//            GuiUtils.drawStringOnSlot(
//                    nameString, invSlots.size(), slot.xDisplayPosition, slot.yDisplayPosition,
//                    nameScale, 1, 1
//            );
//
//            String levelString = levelStrings[i];
//            GuiUtils.drawStringOnSlot(
//                    levelString, invSlots.size(), slot.xDisplayPosition, slot.yDisplayPosition,
//                    levelScale, 16 - MathUtils.ceil(RenderUtils.getStringWidth(levelString) * levelScale),
//                    16 - MathUtils.ceil(RenderUtils.getStringHeight(levelString) * levelScale)
//            );
//        }
    }

    @SubscribeEvent
    public void onItemDrawn(ItemDrawnEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled || inventory == null || !Configs.ShowBookName) return;
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
            ArrayList<String> nameAndLevel = NBTUtils.getBookNameAndLevel(itemStack);
            boolean isUltimate = NBTUtils.isBookUltimate(itemStack);

            String colorPrefix = (isUltimate ? "\u00a7d\u00a7l" : "");
            String bookName = nameAndLevel.get(0);
            String compactName = bookName.substring(0, Math.min(bookName.length(), 3));
            if (isUltimate) compactName = bookName.replaceAll("[a-z ]", "");
            nameString = colorPrefix + compactName;
            levelString = StringUtils.getNumberFromRoman(nameAndLevel.get(1)) + "";
            cachedStrings.put(itemStack, nameString + " " + levelString);
        }

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translate(event.x, event.y, 1f);
        GlStateManager.scale(nameScale, nameScale, 1.0);
        event.renderer.drawString(
                nameString, 1F, 1F, 0xffffffff, true
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

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !Configs.ShowBookName || !(mc.currentScreen instanceof GuiContainer)) return;
        List<Slot> invSlots = ((GuiContainer) mc.currentScreen).inventorySlots.inventorySlots;
        for (int i = 0; i < invSlots.size(); i++) {
            ItemStack itemStack = invSlots.get(i).getStack();
            if (itemStack == null || !itemStack.hasDisplayName()) {
                isBook[i] = false;
                continue;
            }
            String name = ChatLib.removeFormatting(itemStack.getDisplayName()).toLowerCase();
            if (!name.startsWith("enchanted book")) {
                isBook[i] = false;
                continue;
            }
            isBook[i] = true;

            if (cachedStrings.containsKey(itemStack)) {
                String p = cachedStrings.get(itemStack);
                nameStrings[i] = p.split(" ")[0];
                levelStrings[i] = p.split(" ")[1];
                continue;
            }
            ArrayList<String> nameAndLevel = NBTUtils.getBookNameAndLevel(itemStack);
            boolean isUltimate = NBTUtils.isBookUltimate(itemStack);

            String colorPrefix = (isUltimate ? "\u00a7d\u00a7l" : "");
            String bookName = nameAndLevel.get(0);
            String compactName = bookName.substring(0, Math.min(bookName.length(), 3));
            if (isUltimate) compactName = bookName.replaceAll("[a-z ]", "");
            nameStrings[i] = colorPrefix + compactName;
            levelStrings[i] = StringUtils.getNumberFromRoman(nameAndLevel.get(1)) + "";
            cachedStrings.put(itemStack, nameStrings[i] + " " + levelStrings[i]);
        }
    }
}
