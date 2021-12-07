package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoCombine extends StepEvent {
    private static final HashMap<String, Integer> booksLevel = new HashMap<String, Integer>() {{
        put("Feather Falling", 10);
        put("Infinite Quiver", 10);
        put("Rejuvenate", 5);
        put("Ultimate Wise", 5);
        put("Wisdom", 5);
        put("Last Stand", 5);
        put("Combo", 5);
        put("Legion", 5);
        put("Overload", 5);
        put("Soul Eater", 5);
        put("Ultimate Jerry", 5);
        put("Bank", 5);
        put("No Pain No Gain", 5);
        put("Mana Steal", 3);
        put("Smarty Pants", 5);
    }};
    private static final HashMap<String, Integer> romanToInteger = new HashMap<String, Integer>() {{
        put("I", 1);
        put("II", 2);
        put("III", 3);
        put("IV", 4);
        put("V", 5);
        put("VI", 6);
        put("VII", 7);
        put("VIII", 8);
        put("IX", 9);
        put("X", 10);
    }};

    private static boolean isFusingBooks = false;

    public AutoCombine() {
        super(4);
    }

    private static int getEnchantLevel(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return romanToInteger.get(s);
        }
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

    private static boolean fusingBooksEqual() {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return false;
        return getItemLore(inventory.getItemInSlot(29)).get(1).equals(
                getItemLore(inventory.getItemInSlot(33)).get(1)
        );
    }

    private static List<String> getItemLore(ItemStack itemStack) {
        if (itemStack == null) return new ArrayList<>();
        return itemStack.getTooltip(getPlayer(), mc.gameSettings.advancedItemTooltips);
    }

    private static boolean isAir(int slot) {
        ItemStack itemStack = ControlUtils.getItemStackInSlot(slot, false);
        if (itemStack != null && itemStack.hasDisplayName())
            return itemStack.getDisplayName().toLowerCase().contains("air");
        return true;
    }

    @Override
    public void execute() {
        if (!Checker.enabled) return;
        if (!ControlUtils.getInventoryName().equals("Anvil")) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (isFusingBooks || inventory == null) return;
        isFusingBooks = true;
        try {
            if (haveItemsInAnvil()) {
                isFusingBooks = false;
                return;
            }
            ArrayList<BookTypePos> books = new ArrayList<>();
            for (int i = 54; i < 90; i++) {
                ItemStack item = inventory.getItemInSlot(i);
                if (item == null || !item.hasDisplayName()) continue;
                String itemName = ChatLib.removeFormatting(item.getDisplayName()).toLowerCase();
                if (!itemName.equals("enchanted book")) continue;
                // Feather Falling VI
                String bookName = ChatLib.removeFormatting(getItemLore(item).get(1));
                // "Feather", "Falling", "VI"
                ArrayList<String> bookNameSplit = new ArrayList<>(Arrays.asList(bookName.split(" ")));
                // "VI" -> 6
                String levelString = bookNameSplit.get(bookNameSplit.size() - 1);

                int level = getEnchantLevel(levelString);
                // "Feather Falling"
                bookName = bookName.substring(0, bookName.length() - levelString.length() - 1);
                if (booksLevel.containsKey(bookName) &&
                        level < booksLevel.get(bookName) &&
                        checkKindEnable(bookName)
                ) {
                    books.add(new BookTypePos(bookName, level, i));
                }
            }
            new Thread(() -> {
                try {
                    ArrayList<Boolean> vis = new ArrayList<>();
                    for (int p = 0; p < books.size(); p++) vis.add(false);

                    while (!books.isEmpty()) {
                        int x = -1, y = -1;
                        boolean found = false;
                        for (x = 0; x < books.size(); x++) {
                            if (vis.get(x)) continue;
                            for (y = x + 1; y < books.size(); y++) {
                                if (vis.get(y)) continue;
                                if (books.get(x).type.equals(books.get(y).type) &&
                                        books.get(x).level == books.get(y).level) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) break;
                        }
                        if (!found) break;
                        while (fusingBooksNotFull()) {
                            if (ControlUtils.getOpenedInventory().getSize() != 90) {
                                ChatLib.chat("You quit anvil!");
                                return;
                            }
                            while (fusingBooksNotFull()) {
                                try {
                                    Thread.sleep(60);
                                    ControlUtils.getOpenedInventory().click(books.get(x).slot, true, "LEFT");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Thread.sleep(60);
                                    ControlUtils.getOpenedInventory().click(books.get(y).slot, true, "LEFT");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Thread.sleep(60);
                            // sleep 500ms
                            int k = 0;
                            List<String> loreList = getItemLore(ControlUtils.getOpenedInventory().getItemInSlot(22));
                            while (loreList.size() <= 5 || !loreList.get(5).equals("§5§o§30 Exp Levels")) {
                                Thread.sleep(5);
                                k++;
                                loreList = getItemLore(ControlUtils.getOpenedInventory().getItemInSlot(22));
                                if (k > 100) break;
                            }
                        }
                        if (fusingBooksFull() && fusingBooksEqual()) {
                            ControlUtils.getOpenedInventory().click(22);
                            ItemStack itemStack = ControlUtils.getOpenedInventory().getItemInSlot(13);
                            while (itemStack == null || !itemStack.hasDisplayName()
                                    || !itemStack.getDisplayName().contains("Anvil")
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

    private boolean checkKindEnable(String bookName) {
        boolean res = false;
        bookName = "Combine" + bookName.replace(" ", "");
        try {
            res = Configs.class.getField(bookName).getBoolean(Configs.class);
        } catch (Exception e) {
            ChatLib.chat("e");
            e.printStackTrace();
        }
        return res;
    }
}

class BookTypePos {
    public String type;
    public int level;
    public int slot;

    public BookTypePos(String type, int level, int slot) {
        this.type = type;
        this.level = level;
        this.slot = slot;
    }
}