package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoJerryBox {
    private final KeyBind keyBind = new KeyBind("Auto Jerry Box", Keyboard.KEY_NONE);
    private boolean enabled = false;
    private Thread openThread = null;
    private boolean guiOpened = false;
    private boolean received = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            if (!isHoldingJerryBox()) {
                ChatLib.chat("Hold Jerry Box to continue!");
                return;
            }
            if (openThread == null || !openThread.isAlive()) {
                enabled = true;
                ChatLib.chat("Auto Jerry Box &aStarted");
            }
        }
        if (!enabled) return;
        enabled = false;
        openThread = new Thread(() -> {
            try {
                received = guiOpened = false;
                ControlUtils.rightClick();
                while (true) {
                    ControlUtils.stopMoving();
                    // waiting for gui to open
                    int cnt = 0;
                    while (!guiOpened && cnt < 10) {
                        Thread.sleep(100);
                        cnt++;
                    }
                    if (cnt >= 10) break;

                    guiOpened = false;
                    Thread.sleep(100);
                    ControlUtils.getOpenedInventory().click(22);
                    getPlayer().closeScreen();

                    // waiting for loot message
                    cnt = 0;
                    while (!received && cnt < 10) {
                        Thread.sleep(100);
                        cnt++;
                    }
                    if (cnt >= 10) break;
                    received = false;

                    if (!isHoldingJerryBox()) {
                        boolean found = false;
                        Inventory inventory = ControlUtils.getOpenedInventory();
                        for (int i = 0; i < 45; i++) {
                            if (isJerryBox(inventory.getItemInSlot(i))) {
                                found = true;
                                inventory.click(i, false, "SWAP");
                                break;
                            }
                        }
                        if (!found) {
                            ChatLib.chat("Cannot find jerry box!");
                            return;
                        }
                    }
                    Thread.sleep(100);
                    ControlUtils.rightClick();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disable();
            }
        });
        openThread.start();
    }

    @SubscribeEvent
    public void onOpen(GuiOpenEvent event) {
        try {
            if (event.gui instanceof GuiChest && openThread != null && openThread.isAlive() &&
                    ((ContainerChest) ((GuiChest) event.gui).inventorySlots)
                            .getLowerChestInventory()
                            .getName()
                            .equals("Open a Jerry Box")) {
                guiOpened = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onReceiveOpenMessage(ClientChatReceivedEvent event) {
        if (event.type == 0 && ChatLib.removeFormatting(event.message.getUnformattedText())
                .matches(" ☺ ")) {
            received = true;
        }
    }

    private boolean isHoldingJerryBox() {
        ItemStack item = ControlUtils.getHeldItemStack();
        return isJerryBox(item);
    }

    private boolean isJerryBox(ItemStack item) {
        if (item == null) return false;
        return DisplayUtils.getDisplayString(item).endsWith(" Jerry Box");
    }

    private void disable() {
        ChatLib.chat("Auto Jerry Box &cStopped.");
        guiOpened = false;
    }
}
