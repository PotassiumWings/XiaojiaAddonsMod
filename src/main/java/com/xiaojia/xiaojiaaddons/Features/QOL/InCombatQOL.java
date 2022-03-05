package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static boolean canSell(ItemStack itemStack) {
        String name = DisplayUtils.getDisplayString(itemStack).toLowerCase();
        boolean isRecomed = NBTUtils.isItemRecombobulated(itemStack);
        boolean isFullQuality = NBTUtils.isItemFullQuality(itemStack);
        boolean isStarred = NBTUtils.isItemStarred(itemStack);
        int heldTime = NBTUtils.getIntFromExtraAttributes(itemStack, "trainingWeightsHeldTime");
        if (Configs.AutoSellDungeonArmor) {
            for (String shit : dungArmor)
                if (name.contains(shit.toLowerCase())) {
                    if (isRecomed && !Configs.CanAutoSellRecomed) return false;
                    if (isFullQuality && !Configs.CanAutoSellFullQuality) return false;
                    if (isStarred && !Configs.CanAutoSellStarred) return false;
                    return !isRecomed || !isFullQuality || Configs.CanAutoSellFullQualityRecomed;
                }
        }
        if (Configs.AutoSellDungeonTrash) {
            if (heldTime > 10000 && !Configs.CanSellTrainingWeightLong) return false;
            for (String shit : dungTrash) {
                if (name.contains(shit.toLowerCase())) {
                    return !isRecomed || Configs.CanSellRecomedDungeonTrash;
                }
            }
        }
        // misc
        if (Configs.AutoSellSuperboom && name.contains("superboom tnt")) return true;
        if (Configs.AutoSellRunes) {
            for (String shit : runes)
                if (name.contains(shit.toLowerCase()))
                    return true;
        }
        if (Configs.AutoSellBlazeHat && name.equals("blaze hat")) return true;
        // fishing stuff
        if (Configs.AutoSellIceRod && name.contains("ice rod")) return true;
        if (Configs.AutoSellMusicDisc && name.contains("music disc")) return true;
        if (Configs.AutoSellFairySet && !isRecomed) {
            if (name.equals("fairy's fedora") || name.equals("fairy's polo") ||
                    name.equals("fairy's trousers") || name.equals("fairy's galoshes"))
                return true;
        }
        if (Configs.AutoSellEnchantedFeather && name.equals("enchanted feather")) return true;
        if (Configs.AutoSellEnchantedGoldenApple && name.equals("enchanted golden apple")) return true;
        if (Configs.AutoSellGoldenApple && name.equals("golden apple")) return true;
        if (Configs.AutoSellSeaLantern && name.equals("sea lantern")) return true;
        if (Configs.AutoSellBait && name.endsWith(" bait")) return true;
        // mining stuff
        if (Configs.AutoSellAscensionRope && name.contains("ascension rope")) return true;
        if (Configs.AutoSellWishingCompass && name.contains("wishing compass")) return true;
        if (Configs.AutoSellFineGem) {
            Pattern pattern = Pattern.compile("fine \\w+ gemstone");
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) return true;
        }
        // books
        if (name.startsWith("enchanted book")) {
            ArrayList<String> nameAndLevel = NBTUtils.getBookNameAndLevel(itemStack);
            // WARN: return from this!
            if (nameAndLevel.size() != 2) return false;
            // "Feather Falling"
            String bookName = nameAndLevel.get(0);
            // "6"
            String levelString = nameAndLevel.get(1);
            if (bookName.equals("Feather Falling") && Configs.AutoSellFeatherFalling &&
                    (levelString.equals("6") || levelString.equals("7"))) return true;
            if (bookName.equals("Infinite Quiver") && Configs.AutoSellInfiniteQuiver &&
                    (levelString.equals("6") || levelString.equals("7"))) return true;
            if (bookName.equals("Bank") && Configs.AutoSellBank) return true;
            if (bookName.equals("No Pain No Gain") && Configs.AutoSellNoPainNoGain) return true;
            if (bookName.equals("Magnet") && Configs.AutoSellMagnet &&
                    (levelString.equals("6"))) return true;
            return bookName.equals("Ultimate Jerry") && Configs.AutoSellUltimateJerry;
        }
        return false;
    }

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
                    if (!Configs.InCombatFastMode) return;
                    if (openTrade || autoSelling) {
                        inventory.click(22, false, "MIDDLE", 1);
                        if (openTrade) {
                            openTrade = false;
                        }
                        currentStep++;
                    } else if (openWardrobe) {
                        inventory.click(32, false, "MIDDLE", 1);
                        currentStep++;

                        if (!Configs.NakePrevention) {
                            inventory.click(wardrobeSlot + 36, false, "LEFT", 2);
                            getPlayer().closeScreen();
                            openWardrobe = false;
                        }
                    }
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
                        try {
                            if (canSell(itemStack)) {
                                inventory1.click(i, false, "MIDDLE");
                                Thread.sleep(Configs.AutoSellCD);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    getPlayer().closeScreen();
                    autoSelling = false;
                }).start();
            }
        }
    }
}
