package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.isDebug;
import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;



public class ChestFiller {
    private static boolean enabled = false, done = false;

    private Thread pushingThread = null;
    private static int lastId = -1;

    private static void clear() {
        if (enabled) ChatLib.chat("Chest Filler &cdeactivated");
        lastId = -1;
        enabled = false;
        done = false;
    }

    public static void toggle() {
        if (!enabled) ChatLib.chat("Chest Filler &aactivated");
        else clear();
        enabled = !enabled;
    }

    public void click(Inventory inventory, int slot, int mode, int button, int incrementWindowId) {
        int windowId = inventory.getWindowId() + incrementWindowId;
        mc.playerController.windowClick(
                windowId, slot, button, mode, getPlayer()
        );
        if (isDebug()) ChatLib.debug(String.format("%d %d %d %d", windowId, slot, button, mode));
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        clear();
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;
        if (lastId == -1) lastId = inventory.getWindowId();
        else if (lastId != inventory.getWindowId()) {
            lastId = inventory.getWindowId();
            done = false;
        }
        if (enabled && !done && inventory.getName().equals("Large Chest")) {
            done = true;
            if (pushingThread == null || !pushingThread.isAlive()) {
                pushingThread = new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        if (ControlUtils.getOpenedInventory() == null || !inventory.getName().equals("Large Chest"))
                            return;
                        int i, amount;
                        boolean isEmpty = true;
                        HashSet<Slot> emptySlots = new HashSet<>();
                        for (i = 0; i < 54; i++) {
                            if (inventory.getItemInSlot(i) == null) emptySlots.add(inventory.getSlots().get(i));
                        }
                        //inv slots: 54-89
                        for (i = 54; i < 90; i++) {
                            if (inventory.getItemInSlot(i) == null) continue;
                            amount = inventory.getItemInSlot(i).stackSize;
                            if (inventory.getItemInSlot(i).getItem().equals(Item.getByNameOrId("minecraft:prismarine_crystals"))
                                    && amount >= emptySlots.size()) {
                                isEmpty = false;
                                break;
                            }
                        }
                        if (emptySlots.isEmpty()) return;
                        if (!isEmpty) {
                            click(inventory, i, 0, 0, 0);
                            Thread.sleep(200);
                            click(inventory, -999, 5, 4, 0);
                            for (Slot slot1 : emptySlots) click(inventory, slot1.slotNumber, 5, 5, 0);
                            click(inventory, -999, 5, 6, 0);
                            Thread.sleep(100);
                            click(inventory, i, 0, 0, 0);
                            Thread.sleep(100);
                            getPlayer().closeScreen();
                        } else {
                            ChatLib.chat("Please stack your items.");
                            clear();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pushingThread.start();
            }
        }
    }

    @SubscribeEvent
    public void onTickMove(TickEndEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if ((ControlUtils.getOpenedInventory() == null || !inventory.getName().equals("Large Chest"))
                && (pushingThread != null)) pushingThread.interrupt();
    }
}
