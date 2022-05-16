package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class AutoFuse extends StepEvent {
    private static boolean isFusingBooks = false;

    public AutoFuse() {
        super(4);
    }

    public abstract String inventoryName();
    public abstract void clear();
    public abstract void add(ItemStack itemStack, int i);
    public abstract Pair<Integer, Integer> getNext(ArrayList<Boolean> vis);
    public abstract boolean canFuse();
    public abstract boolean checkFuse();
    public boolean enabled() {
        return true;
    }

    private static boolean haveItemsInAnvil() {
        return !isAir(29) || !isAir(33);
    }
    private static boolean fusingBooksNotFull() {
        return isAir(29) || isAir(33);
    }
    private static boolean fusingBooksFull() {
        return !fusingBooksNotFull();
    }
    private static boolean isAir(int slot) {
        ItemStack itemStack = ControlUtils.getItemStackInSlot(slot, false);
        if (itemStack != null && itemStack.hasDisplayName())
            return itemStack.getDisplayName().toLowerCase().contains("air");
        return true;
    }
    public static List<String> getItemLore(ItemStack itemStack) {
        return NBTUtils.getLore(itemStack);
    }

    @Override
    public void execute() {
        if (!Checker.enabled) return;
        if (!enabled()) return;
        if (!ControlUtils.getInventoryName().equals(inventoryName())) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (isFusingBooks || inventory == null) return;
        isFusingBooks = true;
        try {
            if (haveItemsInAnvil()) {
                isFusingBooks = false;
                return;
            }
            clear();
            for (int i = 54; i < 90; i++) {
                ItemStack item = inventory.getItemInSlot(i);
                add(item, i);
            }
            new Thread(() -> {
                try {
                    ArrayList<Boolean> vis = new ArrayList<>();
                    for (int p = 0; p < 90; p++) vis.add(false);

                    while (true) {
                        Pair<Integer, Integer> next = getNext(vis);
                        if (next == null || next.getKey() == -1) break;
                        int x = next.getKey(), y = next.getValue();

                        while (fusingBooksNotFull()) {
                            if (ControlUtils.getOpenedInventory().getSize() != 90) {
                                ChatLib.chat("You quit anvil!");
                                return;
                            }
                            while (fusingBooksNotFull()) {
                                try {
                                    Thread.sleep(60);
                                    ControlUtils.getOpenedInventory().click(x, true, "LEFT");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Thread.sleep(60);
                                    ControlUtils.getOpenedInventory().click(y, true, "LEFT");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Thread.sleep(60);
                            // sleep 500ms
                            int k = 0;
                            while (!canFuse()) {
                                Thread.sleep(5);
                                if (++k > 100) break;
                            }
                        }

                        if (fusingBooksFull() && checkFuse()) {
                            ControlUtils.getOpenedInventory().click(22);
                            ItemStack itemStack = ControlUtils.getOpenedInventory().getItemInSlot(13);
                            while (itemStack == null || !itemStack.hasDisplayName()
                                    || !itemStack.getDisplayName().contains(inventoryName())
                            ) {
                                Thread.sleep(150);
                                if (ControlUtils.getOpenedInventory().getSize() != 90) {
                                    ChatLib.chat("You quit anvil!");
                                    return;
                                }
                                ControlUtils.getOpenedInventory().click(22);
                                itemStack = ControlUtils.getOpenedInventory().getItemInSlot(13);
                            }
                        } else ChatLib.chat("WTF? Something wrong happened");
                        vis.set(x, true);
                        vis.set(y, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isFusingBooks = false;
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
