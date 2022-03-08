package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDropEvent;
import com.xiaojia.xiaojiaaddons.Events.WindowClickEvent;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ProtectItems {
    @SubscribeEvent
    public void onItemDrop(ItemDropEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ProtectItems) return;
        if (shouldProtect(event.itemStack))
            protect(event, "dropping");
    }

    @SubscribeEvent
    public void onWindowClick(WindowClickEvent event) {
        ItemStack itemStack = null;
        // click out of the window with item held
        if (event.slotId == -999 && event.mode == 0) itemStack = getPlayer().inventory.getItemStack();
        // q
        if (event.slotId != -999 && event.mode == 4 && event.mouseButtonClicked == 0)
            itemStack = ControlUtils.getOpenedInventory().getItemInSlot(event.slotId);
        if (itemStack != null && shouldProtect(itemStack))
            protect(event, "dropping");
    }

    @SubscribeEvent
    public void onWindowClickSelling(WindowClickEvent event) {
        ItemStack itemStack = null;
        if (ControlUtils.getOpenedInventoryName().equals("Trades")) {
            if (event.slotId != 49 && event.slotId != -999)
                itemStack = ControlUtils.getOpenedInventory().getItemInSlot(event.slotId);
        }
        if (itemStack != null && shouldProtect(itemStack))
            protect(event, "selling");
    }

    private boolean shouldProtect(ItemStack itemStack) {
        try {
            return LowestBin.getItemValue(itemStack) > Configs.ProtectItemValue * 1000;
        } catch (Exception e) {
            return false;
        }
    }

    private void protect(Event event, String reason) {
        event.setCanceled(true);
        ChatLib.chat("Stopped from " + reason + " that item.");
        getPlayer().playSound("note.bass", 1000, 1);
    }
}
