package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoMeat {
    private Thread sellThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (should() && (sellThread == null || !sellThread.isAlive())) {
            sellThread = new Thread(() -> {
                try {
                    for (int i = 3; i < 39; i++) {
                        if (!should()) return;
                        Inventory inventory = ControlUtils.getOpenedInventory();
                        ItemStack itemStack = inventory.getItemInSlot(i);
                        if (itemStack == null || !itemStack.getDisplayName().contains("精盐肉块") ||
                                itemStack.stackSize != 64) continue;
                        inventory.click(i, false, "LEFT", 0);
                        inventory.click(0, false, "LEFT", 0);
                        inventory.click(2, true, "LEFT", 0);
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            sellThread.start();
        }
    }

    private boolean should() {
        if (!Checker.enabled || !Configs.AutoMeat) return false;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return false;
        if (XiaojiaAddons.isDebug()) ChatLib.chat(inventory.container.getClass() + ", " + inventory.getName());
        if (!(inventory.container instanceof ContainerMerchant)) return false;
        if (!inventory.getName().contains("精盐贤者")) return false;
        MerchantRecipe recipe = ((ContainerMerchant) inventory.container).getMerchantInventory().getCurrentRecipe();
        ItemStack itemStack = recipe.getItemToSell();
        return itemStack.getItem() == Item.getItemFromBlock(Blocks.beacon);
    }
}
