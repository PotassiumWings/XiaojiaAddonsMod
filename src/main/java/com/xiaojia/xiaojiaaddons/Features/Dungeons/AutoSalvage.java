package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSalvage {
    private long lastSalvage = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoClickSalvage) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;
        String name = inventory.getName();
        if (!name.equals("Salvage Dungeon Item")) return;
        ItemStack toSalvage = inventory.getItemInSlot(13);
        if (NBTUtils.isItemStarred(toSalvage)) return;
        ItemStack essence = inventory.getItemInSlot(31);
        if (essence != null && !essence.getItem().getRegistryName().contains("barrier") && TimeUtils.curTime() > lastSalvage + 200) {
            // salvage!
            lastSalvage = TimeUtils.curTime();
            inventory.click(31);
        }
    }
}
