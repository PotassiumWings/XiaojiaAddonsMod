package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemDrawnEvent extends Event {
    public ItemStack itemStack;
    public int x;
    public int y;
    public String text;
    public FontRenderer renderer;

    public ItemDrawnEvent(FontRenderer fr, ItemStack itemStack, int x, int y, String text) {
        this.renderer = fr;
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
        this.text = text;
    }
}
