package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ShowItem {
    public static void show() {
        ItemStack itemStack = ControlUtils.getHeldItemStack();
        if (itemStack == null) {
            ChatLib.chat("You're not holding any item!");
            return;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        itemStack.writeToNBT(nbt);
        String nbtString = nbt.toString();
        XiaojiaChat.chat(nbtString, itemStack.getDisplayName(), 3);
    }
}
