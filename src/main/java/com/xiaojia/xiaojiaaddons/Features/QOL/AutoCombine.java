package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.Objects.StepEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.StringUtils;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoCombine extends AutoFuse {
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
        put("Swarm", 5);
        put("Strong Mana", 10);
        put("Hardened Mana", 10);
        put("Ferocious Mana", 10);
        put("Mana Vampire", 10);
        put("Charm", 5);
        put("Corruption", 5);
    }};
    private static final ArrayList<BookTypePos> books = new ArrayList<>();

    public String inventoryName() {
        return "Anvil";
    }

    public void clear() {
        books.clear();
    }

    public void add(ItemStack item, int i) {
        if (item == null || !item.hasDisplayName()) return;
        String itemName = ChatLib.removeFormatting(item.getDisplayName()).toLowerCase();
        if (!itemName.equals("enchanted book")) return;

        ArrayList<String> nameAndLevel = NBTUtils.getBookNameAndLevel(item);
        if (nameAndLevel.size() != 2) return;
        // "Feather Falling"
        String bookName = nameAndLevel.get(0);
        String levelString = nameAndLevel.get(1);
        int level = Integer.parseInt(levelString);
        if (booksLevel.containsKey(bookName) &&
                level < booksLevel.get(bookName) &&
                checkKindEnable(bookName)
        ) {
            books.add(new BookTypePos(bookName, level, i));
        }
    }

    public Pair<Integer, Integer> getNext(ArrayList<Boolean> vis) {
        for (int x = 0; x < books.size(); x++) {
            if (vis.get(books.get(x).slot)) continue;
            for (int y = x + 1; y < books.size(); y++) {
                if (vis.get(books.get(y).slot)) continue;
                if (books.get(x).type.equals(books.get(y).type) &&
                        books.get(x).level == books.get(y).level) {
                    return new Pair<>(books.get(x).slot, books.get(y).slot);
                }
            }
        }
        return null;
    }

    public boolean canFuse() {
        List<String> loreList = getItemLore(ControlUtils.getOpenedInventory().getItemInSlot(22));
        return loreList.size() > 5 && ChatLib.removeFormatting(loreList.get(5)).equals("0 Exp Levels");
    }

    public boolean checkFuse() {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return false;
        return getItemLore(inventory.getItemInSlot(29)).get(1).equals(
                getItemLore(inventory.getItemInSlot(33)).get(1)
        );
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