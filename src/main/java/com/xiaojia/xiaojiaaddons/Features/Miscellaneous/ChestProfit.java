package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class ChestProfit {
    private static final String[] chestNames = new String[]{
            "Wood Chest", "Gold Chest", "Emerald Chest", "Diamond Chest", "Obsidian Chest", "Bedrock Chest"
    };

    private static String getStringProfit(double profit) {
        String res = "+";
        if (profit < 0) {
            res = "-";
            profit = -profit;
        }
        if (profit >= 1000000) return res + String.format("%.2fM", profit / 1000000);
        if (profit >= 1000) return res + String.format("%.2fK", profit / 1000);
        return res + String.format("%.0f", profit);
    }

    @SubscribeEvent
    public void onTick(GuiScreenEvent.DrawScreenEvent event) {
        if (!Checker.enabled || !SkyblockUtils.isInDungeon()) return;
        if (!Configs.ShowChestProfit) return;
        String name = ControlUtils.getOpenedInventoryName();
        if (Arrays.stream(chestNames).noneMatch(e -> e.equals(name))) return;
        try {
            Inventory inventory = ControlUtils.getOpenedInventory();
            ItemStack costItem = inventory.getItemInSlot(31);
            // 250,000 Coins
            double profit = 0;
            for (String str : NBTUtils.getLore(costItem)) {
                String costString = ChatLib.removeFormatting(str);
                if (costString.endsWith(" Coins")) {
                    profit = -Integer.parseInt(costString
                            .substring(0, costString.length() - 6)
                            .replaceAll(",", ""));
                }
            }
            // Profit
            for (int i = 9; i < 18; i++) {
                try {
                    profit += LowestBin.getItemValue(inventory.getItemInSlot(i));
                } catch (Exception ignored) {
                }
            }
            // Color
            String colorPrefix = "\u00a7c";
            if (profit > 700000) colorPrefix = "\u00a7a";
            else if (profit > 0) colorPrefix = "\u00a72";
            String displayString = colorPrefix + getStringProfit(profit);
            GuiUtils.drawStringAtRightUpOfDoubleChest(displayString);
        } catch (Exception e) {
        }
    }
}
