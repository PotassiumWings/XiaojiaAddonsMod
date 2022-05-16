package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class AutoAttribute extends AutoFuse {
    private static final ArrayList<AttributeTypePos> array1 = new ArrayList<>();
    private static final ArrayList<AttributeTypePos> array2 = new ArrayList<>();
    private static final ArrayList<AttributeTypePos> array3 = new ArrayList<>();

    public boolean enabled() {
        return Configs.FuseAttribute;
    }

    public String inventoryName() {
        return "Attribute Fusion";
    }

    public void clear() {
        array1.clear();
        array2.clear();
        array3.clear();
    }

    private static Pair<Integer, Integer> getLevels(ItemStack itemStack) {
        NBTTagCompound compound = NBTUtils.getCompoundFromExtraAttributes(itemStack, "attributes");
        if (compound == null) return null;
        String attribute1 = Configs.FuseAttribute1.toLowerCase().replaceAll(" ", "_");
        String attribute2 = Configs.FuseAttribute2.toLowerCase().replaceAll(" ", "_");
        if (!compound.hasKey(attribute1) && !compound.hasKey(attribute2)) return null;

        int level1 = 0, level2 = 0;
        if (compound.hasKey(attribute1)) level1 = compound.getInteger(attribute1);
        if (compound.hasKey(attribute2)) level2 = compound.getInteger(attribute2);
        return new Pair<>(level1, level2);
    }

    public void add(ItemStack itemStack, int i) {
        Pair<Integer, Integer> levels = getLevels(itemStack);
        if (levels == null) return;
        int level1 = levels.getKey(), level2 = levels.getValue();
        if (level1 != 0 && level2 != 0) array3.add(new AttributeTypePos(level1, level2, i));
        else if (level1 != 0) array1.add(new AttributeTypePos(level1, level2, i));
        else if (level2 != 0) array2.add(new AttributeTypePos(level1, level2, i));
    }

    public Pair<Integer, Integer> getNext(ArrayList<Boolean> vis) {
        Pair<Integer, Integer> next = getNext(array3, vis);
        if (next == null) next = getNext(array2, vis);
        if (next == null) next = getNext(array1, vis);
        return next;
    }

    private Pair<Integer, Integer> getNext(ArrayList<AttributeTypePos> arr, ArrayList<Boolean> vis) {
        for (int x = 0; x < arr.size(); x++) {
            if (vis.get(arr.get(x).slot)) continue;
            for (int y = x + 1; y < arr.size(); y++) {
                if (vis.get(arr.get(y).slot)) continue;
                if (arr.get(x).level1 == arr.get(y).level1 &&
                        arr.get(x).level2 == arr.get(y).level2) {
                    return new Pair<>(arr.get(x).slot, arr.get(y).slot);
                }
            }
        }
        return null;
    }

    public boolean canFuse() {
        List<String> loreList = getItemLore(ControlUtils.getOpenedInventory().getItemInSlot(22));
        return loreList.size() > 5 && ChatLib.removeFormatting(loreList.get(4)).equals("Click to transfer!");
    }

    public boolean checkFuse() {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return false;
        Pair<Integer, Integer> levels1 = getLevels(inventory.getItemInSlot(29));
        Pair<Integer, Integer> levels2 = getLevels(inventory.getItemInSlot(33));
        return levels1 != null && levels2 != null &&
                levels1.getKey().equals(levels2.getKey()) && levels1.getValue().equals(levels2.getValue());
    }
}

class AttributeTypePos {
    public int level1, level2;
    public int slot;

    public AttributeTypePos(int level1, int level2, int slot) {
        this.level1 = level1;
        this.level2 = level2;
        this.slot = slot;
    }
}