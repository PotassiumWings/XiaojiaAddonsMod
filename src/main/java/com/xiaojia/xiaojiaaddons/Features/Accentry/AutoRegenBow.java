package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Events.UpdateEvent;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoRegenBow {
    public static Thread healingThread = null;
    public static boolean shouldFastBow = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoRegenBow) return;
        EntityPlayer player = getPlayer();
        if (player == null || player.getHealth() / player.getMaxHealth() > Configs.AutoRegenBowHP / 20.0) return;
        if (player.getFoodStats().getFoodLevel() <= 17 || AutoEat.autoEating) return;
        if (healingThread != null && healingThread.isAlive()) return;
        healingThread = new Thread(() -> {
            while (player.getHealth() / player.getMaxHealth() < Configs.AutoRegenBowHP2 / 20.0) {
                Inventory inventory = ControlUtils.getOpenedInventory();
                if (inventory == null || inventory.getSize() < 9) return;
                List<ItemStack> items = inventory.getItemStacks().subList(inventory.getSize() - 9, inventory.getSize());
                int curIndex = ControlUtils.getHeldItemIndex();

                boolean found = false;
                for (int i = 0; i < 9; i++) {
                    ItemStack item = items.get(i);
                    if (item == null) continue;
                    String name = item.getDisplayName();
                    if (name.contains("永生之弓")) {
                        found = true;
                        ControlUtils.setHeldItemIndex(i);
                        if (XiaojiaAddons.mc.playerController.sendUseItem(player, getWorld(), item)) {
                            XiaojiaAddons.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                        }
                        shouldFastBow = true;
                        ControlUtils.holdRightClick();
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            return;
                        } finally {
                            shouldFastBow = false;
                            ControlUtils.setHeldItemIndex(curIndex);
                            ControlUtils.releaseRightClick();
                        }
                        break;
                    }
                }
                if (!found) break;
            }
        });
        healingThread.start();
    }

    @SubscribeEvent()
    public void onRightClick(UpdateEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoRegenBow) return;
        if (!shouldFastBow) return;
        EntityPlayer player = getPlayer();
        if (player == null) return;

        ItemStack held = player.getItemInUse();
        if (held != null && held.getItem() instanceof ItemBow) {
            for (int i = 0; i < 35; i++) {
                if (PacketRelated.getSentQueueLength() > Configs.PacketThreshold) break;
                XiaojiaAddons.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(getPlayer().onGround));
            }
            player.stopUsingItem();
        }
    }
}
