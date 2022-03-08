package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class AutoSnowball {
    private final KeyBind keyBind = new KeyBind("Auto Snowball", Keyboard.KEY_NONE);
    private Thread snowballThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || inventory.getSize() != 45) return;
        if (keyBind.isKeyDown() && (snowballThread == null || !snowballThread.isAlive())) {
            snowballThread = new Thread(() -> {
                int index = ControlUtils.getHeldItemIndex();
                List<ItemStack> items = inventory.getItemStacks().subList(36, 45);
                for (int x = 0; x < 9; x++) {
                    ItemStack itemStack = items.get(x);
                    if (itemStack == null) continue;
                    Item item = itemStack.getItem();
                    if (!(item instanceof ItemSnowball)) continue;
                    ControlUtils.setHeldItemIndex(x);
                    for (int i = 0; i < itemStack.stackSize * 2; i++)
                        ControlUtils.rightClick();
                }
                ControlUtils.setHeldItemIndex(index);
                CommandsUtils.addCommand("/pickupstash", 3);
            });
            snowballThread.start();
        }
    }
}
