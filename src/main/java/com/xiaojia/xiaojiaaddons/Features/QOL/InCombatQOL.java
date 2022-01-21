package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class InCombatQOL {
    public static final String[] dungArmor = new String[]{
            "Rotten", "Skeleton Master", "Skeleton Grunt", "Skeleton Lord",
            "Zombie Soldier", "Skeleton Soldier", "Zombie Knight", "Zombie Commander",
            "Zombie Lord", "Skeletor", "Super Heavy", "Heavy", "Sniper Helmet",
            "Dreadlord", "Earth Shard", "Zombie Commander Whip", "Machine Gun",
            "Sniper Bow", "Soulstealer Bow", "Cutlass", "Silent Death", "Conjuring"
    };

    public static final String[] dungTrash = new String[]{
            "Training Weight", "Health Potion VIII", "Health Potion 8",
            "Beating Heart", "Premium Flesh", "Mimic Fragment",
            "Defuse Kit", "Tripwire Hook", "Button", "Carpet", "Lever", "Sign",
            "Enchanted Rotten Flesh", "Enchanted Bone", "Enchanted Ice",
            "Optic Lens", "Diamond Atom"
    };

    public static final String[] runes = new String[]{
            "Snow Rune", "Blood Rune", "Zap Rune", "Gem Rune", "Lava Rune",
            "Hot Rune", "White Spiral Rune", "Hearts Rune", "Ice Rune",
            "Redstone Rune", "Sparkling Rune", "Clouds Rune", "Golden Rune"
    };

    private final ArrayList<KeyBind> armorKeyBinds = new ArrayList<KeyBind>() {{
        for (int i = 0; i < 9; i++) {
            add(new KeyBind(String.format("Wardrobe Slot %d", i), Keyboard.KEY_NONE));
        }
    }};
    private final KeyBind tradeKeyBind = new KeyBind("Trade Menu", Keyboard.KEY_NONE);
    private final KeyBind autoSellKeyBind = new KeyBind("Auto Sell", Keyboard.KEY_NONE);

    private boolean openWardrobe = false;
    private boolean openTrade = false;
    private boolean autoSelling = false;
    private int currentStep = 0;
    private int wardrobeSlot = -1;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
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
        if (autoSellKeyBind.isPressed()) {
            ChatLib.chat("Auto Selling");
            CommandsUtils.addCommand("/pets");
            autoSelling = true;
            currentStep = 0;
        }
    }

    @SubscribeEvent
    public void onPostGuiRender(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!Checker.enabled) return;
        if (!openWardrobe && !openTrade && !autoSelling) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || inventory.getName() == null) return;
        String invName = ChatLib.removeFormatting(inventory.getName());
        if (invName.contains("Pets")) {
            if (currentStep == 0) {
                if (openTrade || openWardrobe || autoSelling) {
                    inventory.click(48, false, "MIDDLE");
                    currentStep++;
                }
            }
        } else if (invName.contains("SkyBlock Menu")) {
            if (currentStep == 1) {
                if (openTrade || autoSelling) {
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
                new Thread(() -> {
                    currentStep++;
                    try {
                        ItemStack itemStack = inventory.getItemInSlot(wardrobeSlot + 36);
                        while (itemStack == null || itemStack.getItemDamage() != 9 && itemStack.getItemDamage() != 10) {
                            Thread.sleep(20);
                            itemStack = ControlUtils.getOpenedInventory().getItemInSlot(wardrobeSlot + 36);
                        }
                        if (itemStack.getItemDamage() == 10 && Configs.NakePrevention) {
                            ChatLib.chat("Detected Taking off Armor, stopped.");
                        } else {
                            inventory.click(wardrobeSlot + 36, false, "LEFT");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getPlayer().closeScreen();
                    openWardrobe = false;
                }).start();
            }
        } else if (invName.contains("Trade")) {
            if (autoSelling && currentStep == 2) {
                new Thread(() -> {
                    currentStep++;
                    for (int i = 53; i < 81; i++) {
                        Inventory inventory1 = ControlUtils.getOpenedInventory();
                        if (inventory1 == null || inventory1.getSize() != 90) return;
                        ItemStack itemStack = inventory1.getItemInSlot(i);
                        if (itemStack == null) continue;
                        String name = itemStack.getDisplayName();
                        boolean isRecomed = NBTUtils.isItemRecombobulated(itemStack);
                        boolean isFullQuality = NBTUtils.isItemFullQuality(itemStack);
                        boolean isStarred = NBTUtils.isItemStarred(itemStack);
                        int heldTime = NBTUtils.getIntFromExtraAttributes(itemStack, "trainingWeightsHeldTime");
                        boolean canSell = false;
                        if (Configs.AutoSellDungeonArmor) {
                            if (isRecomed && !Configs.CanAutoSellRecomed) continue;
                            if (isFullQuality && !Configs.CanAutoSellFullQuality) continue;
                            if (isStarred && !Configs.CanAutoSellStarred) continue;
                            if (isRecomed && isFullQuality && !Configs.CanAutoSellFullQualityRecomed) continue;
                            for (String shit : dungArmor) {
                                if (name.toLowerCase().contains(shit.toLowerCase())) {
                                    canSell = true;
                                    break;
                                }
                            }
                        }
                        if (Configs.AutoSellDungeonTrash) {
                            if (isRecomed && !Configs.CanSellRecomedDungeonTrash) continue;
                            if (heldTime > 10000 && !Configs.CanSellTrainingWeightLong) continue;
                            for (String shit : dungTrash) {
                                if (name.toLowerCase().contains(shit.toLowerCase())) {
                                    canSell = true;
                                    break;
                                }
                            }
                        }
                        if (Configs.AutoSellRunes) {
                            for (String shit : runes) {
                                if (name.toLowerCase().contains(shit.toLowerCase())) {
                                    canSell = true;
                                    break;
                                }
                            }
                        }
                        if (canSell) {
                            try {
                                inventory1.click(i, false, "MIDDLE");
                                Thread.sleep(Configs.AutoSellCD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    getPlayer().closeScreen();
                    autoSelling = false;
                }).start();
            }
        }
    }
}
