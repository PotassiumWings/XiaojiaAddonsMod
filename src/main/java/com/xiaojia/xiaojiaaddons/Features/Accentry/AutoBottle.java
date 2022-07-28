package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.PacketRelated;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AutoBottle {
    private static boolean should = false;
    private final KeyBind keyBind = new KeyBind("Auto Bottle", Keyboard.KEY_NONE);

    public static void stop(String reason) {
        ChatLib.chat(reason);
        ChatLib.chat("Auto Bottle &cdeactivated");
        should = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoBottle) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Auto Bottle &aactivated" : "Auto Bottle &cdeactivated");
        }
        if (!should) return;
        if (PacketRelated.getReceivedQueueLength() == 0) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;
        ArrayList<Integer> slots = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> hotbarSlots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize() - 9; i++) {
            ItemStack itemStack = inventory.getItemInSlot(i);
            if (itemStack != null && itemStack.getItem() == Items.experience_bottle) slots.add(i);
        }
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inventory.getItemInSlot(i + inventory.getSize() - 9);
            if (itemStack != null && itemStack.getItem() == Items.experience_bottle)
                hotbarSlots.add(new Pair<>(i, itemStack.stackSize));
        }
        if (hotbarSlots.size() == 0) {
            if (slots.size() == 0) stop("No enough bottles.");
            else for (int i : slots) inventory.click(i, true, "LEFT");
        } else {
            for (Pair<Integer, Integer> pair : hotbarSlots) {
                ControlUtils.setHeldItemIndex(pair.getKey());
                for (int i = 0; i < pair.getValue(); i++) {
                    if (PacketRelated.getSentQueueLength() > Configs.PacketThreshold) return;
                    ControlUtils.rightClick();
                }
            }
        }
    }
}
