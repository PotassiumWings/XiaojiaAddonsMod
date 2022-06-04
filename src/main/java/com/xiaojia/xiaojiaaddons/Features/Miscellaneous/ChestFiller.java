package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ChestFiller {
    private static boolean enabled = false, done = false, six = false;
    private Thread pushingThread = null;
    private static int lastId = -1;

    private static String name = null;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void disable() {
        if (enabled) ChatLib.chat("Chest Filler &cdisabled");
        lastId = -1;
        enabled = false;
        done = false;
        name = null;
        six = false;
    }

    public static void enable(String input1, boolean input2) {
        name = input1;
        six = input2;
        ChatLib.chat("Chest Filler &aenabled&r (&b" + name + "&r)");
        enabled = true;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        disable();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        Inventory inv = ControlUtils.getOpenedInventory();
        if (inv == null) return;
        if (lastId == -1) lastId = inv.getWindowId();
        else if (lastId != inv.getWindowId()) {
            lastId = inv.getWindowId();
            done = false;
        }
        if (enabled && !done && inv.getName().equals("Large Chest")) {
            done = true;
            if (pushingThread == null || !pushingThread.isAlive()) {
                pushingThread = new Thread(() -> {
                    try {
                        int i, amount;
                        boolean isEmpty = true;
                        Inventory inventory = ControlUtils.getOpenedInventory();
                        HashSet<Slot> emptySlots = new HashSet<>();
                        for (i = 0; i < 54; i++) {
                            if (six && i == 5) continue;
                            if (inventory.getItemInSlot(i) == null) emptySlots.add(inventory.getSlots().get(i));
                        }
                        //inv slots: 54-89
                        for (i = 54; i < 90; i++) {
                            ItemStack item = inventory.getItemInSlot(i);
                            if (item == null) continue;
                            amount = item.stackSize;
                            if (StringUtils.stripControlCodes(item.getDisplayName()).toLowerCase().contains(name.toLowerCase())
                                    && amount >= emptySlots.size()) {
                                isEmpty = false;
                                break;
                            }
                        }
                        if (emptySlots.isEmpty() && inventory.getItemInSlot(5) != null) return;
                        if (!isEmpty) {
                            click(inventory, i, 0, 0, 0);
                            click(inventory, -999, 5, 4, 0);
                            for (Slot slot1 : emptySlots) click(inventory, slot1.slotNumber, 5, 5, 0);
                            click(inventory, -999, 5, 6, 0);
                            Thread.sleep(50);
                            if (inventory.getItemInSlot(5) == null) click(inventory, 5, 0, 1, 0);
                            getPlayer().closeScreen();
                        } else {
                            ChatLib.chat("Not enough items.");
                            disable();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pushingThread.start();
            }
        }
    }

    public void click(Inventory inventory, int slot, int mode, int button, int incrementWindowId) {
        int windowId = inventory.getWindowId() + incrementWindowId;
        XiaojiaAddons.mc.playerController.windowClick(
                windowId, slot, button, mode, getPlayer()
        );
    }
}
