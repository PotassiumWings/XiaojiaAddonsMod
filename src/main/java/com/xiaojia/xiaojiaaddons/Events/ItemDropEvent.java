package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemDropEvent extends Event {
    public boolean dropAll;
    public ItemStack itemStack;
    public ItemDropEvent(boolean dropAll, ItemStack itemStack) {
        this.dropAll = dropAll;
        this.itemStack = itemStack;
    }
}
