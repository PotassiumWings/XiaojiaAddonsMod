package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.entity.IMerchant;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

public class AutoMeat {
    private Thread sellThread = null;
    private static Field merchantField = null;
    static {
        try {
            merchantField = ContainerMerchant.class.getDeclaredField("field_75178_e");
        } catch (NoSuchFieldException e) {
            try {
                merchantField = ContainerMerchant.class.getDeclaredField("theMerchant");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        merchantField.setAccessible(true);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (should() && (sellThread == null || !sellThread.isAlive())) {
            sellThread = new Thread(() -> {
                try {
                    for (int i = 3; i < 39; i++) {
                        if (!should()) return;
                        Inventory inventory = ControlUtils.getOpenedInventory();
                        if (inventory == null) return;
                        ItemStack itemStack = inventory.getItemInSlot(i);
                        if (itemStack == null || !itemStack.getDisplayName().contains("精盐肉块") ||
                                itemStack.stackSize != 64) continue;
                        inventory.click(i, false, "LEFT", 0);
                        inventory.click(0, false, "LEFT", 0);
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
        // recipe filled
        if (inventory.getItemInSlot(2) != null) return false;

        if (!(inventory.container instanceof ContainerMerchant)) return false;

        ContainerMerchant merchant = ((ContainerMerchant) inventory.container);
        String name = "";
        try {
            name = (((IMerchant) merchantField.get(merchant)).getDisplayName().getFormattedText());
        } catch (IllegalAccessException ignored) {
        }
        return name.contains("精盐贤者");
    }
}
