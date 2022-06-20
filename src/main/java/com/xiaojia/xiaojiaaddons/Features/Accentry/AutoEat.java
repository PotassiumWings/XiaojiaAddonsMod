package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoEat {
    private long lastEat = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoEat) return;
        EntityPlayer player = getPlayer();
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (player == null || inventory == null || inventory.getSize() < 9) return;

        int level = player.getFoodStats().getFoodLevel();
        long cur = TimeUtils.curTime();
        if (level == 0 && cur - lastEat > 5 * 1000) {
            int index = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = inventory.getItemInSlot(inventory.getSize() - 9 + i);
                if (itemStack != null && itemStack.getItem() instanceof ItemFood) {
                    index = i;
                    break;
                }
            }
            if (index == -1) return;
            lastEat = cur;
            ControlUtils.setHeldItemIndex(index);
            ItemStack held = player.inventory.getCurrentItem();
            if (XiaojiaAddons.mc.playerController.sendUseItem(player, getWorld(), held)) {
                XiaojiaAddons.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
        }
    }
}
