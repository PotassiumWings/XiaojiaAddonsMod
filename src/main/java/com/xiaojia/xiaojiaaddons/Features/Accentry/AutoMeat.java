package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class AutoMeat {
    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (XiaojiaAddons.isDebug()) {
            Inventory inventory = ControlUtils.getOpenedInventory();
            if (inventory == null) return;
            List<ItemStack> stacks = inventory.getItemStacks();
            ChatLib.chat(inventory.getName());
            for (int i = 0; i < stacks.size(); i++) {
                ItemStack itemStack = stacks.get(i);
                if (itemStack == null) continue;
                ChatLib.chat(i + ": " + itemStack.getDisplayName() + ", " + itemStack.stackSize);
            }
            ChatLib.chat("---------------------");
        }
    }
}
