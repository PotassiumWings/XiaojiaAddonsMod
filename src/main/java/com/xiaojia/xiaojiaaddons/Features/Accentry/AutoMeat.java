package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MerchantUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

enum EnumNPC {
    NONE, JY, XNH, CXK
}

public class AutoMeat {
    private Thread sellThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (should() != EnumNPC.NONE && (sellThread == null || !sellThread.isAlive())) {
            sellThread = new Thread(() -> {
                try {
                    int slot = 0;
                    for (int i = 3; i < 39; i++) {
                        EnumNPC npc = should();
                        if (npc == EnumNPC.NONE) return;
                        Inventory inventory = ControlUtils.getOpenedInventory();
                        if (inventory == null) return;
                        ItemStack itemStack = inventory.getItemInSlot(i);
                        if (itemStack == null ||
                                (npc == EnumNPC.JY && !itemStack.getDisplayName().contains("精盐肉块") &&
                                        !itemStack.getDisplayName().contains("寒冰碎片")) ||
                                (npc == EnumNPC.XNH && !itemStack.getDisplayName().contains("臭肉")) ||
                                (npc == EnumNPC.CXK && !itemStack.getDisplayName().contains("白羽鸡肉")) ||
                                itemStack.stackSize != 64) {
                            if (itemStack != null && npc == EnumNPC.JY && itemStack.getDisplayName().contains("浓缩精华")) {
                                inventory.click(0, true, "LEFT", 0);
                            } else continue;
                        }
                        inventory.click(i, false, "LEFT", 0);
                        inventory.click(slot, false, "LEFT", 0);
                        if (npc == EnumNPC.CXK) slot = 1 - slot;
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
        String name = MerchantUtils.getCurrentMerchant();
        if (name == null) return EnumNPC.NONE;
        return name.contains("精盐贤者") || name.contains("Villager") ? EnumNPC.JY :
                name.contains("小男孩") ? EnumNPC.XNH :
                        name.contains("蔡徐坤") ? EnumNPC.CXK :
                                EnumNPC.NONE;
    }
}