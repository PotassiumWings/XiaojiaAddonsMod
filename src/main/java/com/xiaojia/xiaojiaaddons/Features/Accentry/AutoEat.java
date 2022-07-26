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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoEat {
    private long lastEat = 0;
    public static boolean autoEating = false;
    private Thread eatingThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoEat) return;
        EntityPlayer player = getPlayer();
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (player == null || inventory == null || inventory.getSize() < 9) return;

        int level = player.getFoodStats().getFoodLevel();
        long cur = TimeUtils.curTime();
        autoEating = level <= Configs.AutoEatHunger || eatingThread != null && eatingThread.isAlive();
        if (level <= Configs.AutoEatHunger && cur - lastEat > 5 * 1000) {
            if (AutoRegenBow.healingThread != null && AutoRegenBow.healingThread.isAlive()) {
                AutoRegenBow.healingThread.interrupt();
                return;
            }
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
            int curIndex = ControlUtils.getHeldItemIndex();
            ControlUtils.setHeldItemIndex(index);
            ItemStack held = inventory.getItemInSlot(inventory.getSize() - 9 + index);
            if (XiaojiaAddons.mc.playerController.sendUseItem(player, getWorld(), held)) {
                XiaojiaAddons.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
            }
            eatingThread = new Thread(() -> {
                try {
                    ControlUtils.holdRightClick();
                    Thread.sleep(200);
                    ControlUtils.setHeldItemIndex(curIndex);
                    ControlUtils.releaseRightClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                autoEating = false;
            });
            eatingThread.start();
        }
    }
}
