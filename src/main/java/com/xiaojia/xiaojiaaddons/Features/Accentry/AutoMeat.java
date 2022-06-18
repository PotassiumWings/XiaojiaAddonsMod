package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

enum EnumNPC {
    NONE, JY, XNH
}

public class AutoMeat {
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

    private Thread sellThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (should() != EnumNPC.NONE && (sellThread == null || !sellThread.isAlive())) {
            sellThread = new Thread(() -> {
                try {
                    for (int i = 3; i < 39; i++) {
                        EnumNPC npc = should();
                        if (npc == EnumNPC.NONE) return;
                        Inventory inventory = ControlUtils.getOpenedInventory();
                        if (inventory == null) return;
                        ItemStack itemStack = inventory.getItemInSlot(i);
                        if (itemStack == null ||
                                (npc == EnumNPC.JY && !itemStack.getDisplayName().contains("精盐肉块")) ||
                                (npc == EnumNPC.XNH && !itemStack.getDisplayName().contains("臭肉")) ||
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

    private EnumNPC should() {
        if (!Checker.enabled || !Configs.AutoMeat) return EnumNPC.NONE;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return EnumNPC.NONE;
        // recipe filled
        if (inventory.getItemInSlot(2) != null) return EnumNPC.NONE;

        if (!(inventory.container instanceof ContainerMerchant)) return EnumNPC.NONE;

        ContainerMerchant merchant = ((ContainerMerchant) inventory.container);
        String name = "";
        try {
            name = (((IMerchant) merchantField.get(merchant)).getDisplayName().getFormattedText());
        } catch (IllegalAccessException ignored) {
        }
        return name.contains("精盐贤者") || name.contains("Villager") ? EnumNPC.JY :
                name.contains("小男孩") ? EnumNPC.XNH :
                        EnumNPC.NONE;
    }
}