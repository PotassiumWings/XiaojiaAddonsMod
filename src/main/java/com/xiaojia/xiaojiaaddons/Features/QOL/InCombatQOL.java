package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class InCombatQOL {
    private final ArrayList<KeyBind> armorKeyBinds = new ArrayList<KeyBind>() {{
        for (int i = 0; i < 9; i++) {
            add(new KeyBind(String.format("Wardrobe Slot %d", i), Keyboard.KEY_NONE));
        }
    }};
    private final KeyBind tradeKeyBind = new KeyBind("Trade Menu", Keyboard.KEY_NONE);

    private boolean openWardrobe = false;
    private boolean openTrade = false;
    private int currentStep = 0;
    private int wardrobeSlot = -1;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
        for (int i = 0; i < 9; i++) {
            if (armorKeyBinds.get(i).isPressed()) {
                ChatLib.chat(String.format("Swapping to %d", i));
                CommandsUtils.addCommand("/pets");
                openWardrobe = true;
                currentStep = 0;
                wardrobeSlot = i;
                return;
            }
        }
        if (tradeKeyBind.isPressed()) {
            ChatLib.chat("Opening trade");
            CommandsUtils.addCommand("/pets");
            openTrade = true;
            currentStep = 0;
        }
    }

    @SubscribeEvent
    public void onPostGuiRender(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!Checker.enabled) return;
        if (!openWardrobe && !openTrade) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || inventory.getName() == null) return;
        String invName = ChatLib.removeFormatting(inventory.getName());
        if (invName.contains("Pets")) {
            if (currentStep == 0) {
                if (openTrade || openWardrobe) {
                    inventory.click(48, false, "MIDDLE");
                    currentStep++;
                }
            }
        } else if (invName.contains("SkyBlock Menu")) {
            if (currentStep == 1) {
                if (openTrade) {
                    inventory.click(22, false, "MIDDLE");
                    openTrade = false;
                    currentStep++;
                } else if (openWardrobe) {
                    inventory.click(32, false, "MIDDLE");
                    currentStep++;
                }
            }
        } else if (invName.contains("Wardrobe")) {
            if (openWardrobe && currentStep == 2) {
                inventory.click(wardrobeSlot + 36, false, "LEFT");
                getPlayer().closeScreen();
                openWardrobe = false;
            }
        }
    }
}
