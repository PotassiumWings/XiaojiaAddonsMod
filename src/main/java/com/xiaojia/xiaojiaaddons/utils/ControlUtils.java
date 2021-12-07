package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;

import java.lang.reflect.Method;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ControlUtils {

    private static final KeyBind useKeyBind = new KeyBind(mc.gameSettings.keyBindUseItem);
    private static final KeyBind attackKeyBind = new KeyBind(mc.gameSettings.keyBindAttack);

    public static void rightClick() {
        try {
            getPlayer().closeScreen();
            Method rightClickMethod;
            try {
                rightClickMethod = mc.getClass().getDeclaredMethod("rightClickMouse");
            } catch (NoSuchMethodException e) {
                rightClickMethod = mc.getClass().getDeclaredMethod("func_147121_ag");
            }
            rightClickMethod.setAccessible(true);
            rightClickMethod.invoke(mc);
        } catch (Exception ignored) {
        }
    }

    public static void leftClick() {
        try {
            getPlayer().closeScreen();
            Method leftClickMethod;
            try {
                leftClickMethod = mc.getClass().getDeclaredMethod("clickMouse");
            } catch (NoSuchMethodException e) {
                leftClickMethod = mc.getClass().getDeclaredMethod("func_147116_af");
            }
            leftClickMethod.setAccessible(true);
            leftClickMethod.invoke(mc);
        } catch (Exception ignored) {
        }
    }

    public static void changeDirection(float yaw, float pitch) {
        if (pitch > 90 || pitch < -90 || yaw < -180 || yaw > 180) {
            System.err.println("wrong dir! " + yaw + ", " + pitch);
            return;
        }
        System.err.println("changed dir! " + yaw + ", " + pitch);
        getPlayer().rotationYaw += MathHelper.wrapAngleTo180_float(yaw - getPlayer().rotationYaw);
        getPlayer().rotationPitch += MathHelper.wrapAngleTo180_float(pitch - getPlayer().rotationPitch);
    }

    public static void face(float tx, float ty, float tz) {
        getPlayer().closeScreen();
        Tuple<Float, Float> res = getFaceYawAndPitch(tx, ty, tz);
        float yaw = res.getFirst(), pitch = res.getSecond();
        changeDirection(yaw, pitch);
    }

    public static Tuple<Float, Float> getFaceYawAndPitch(float tx, float ty, float tz) {
        System.err.printf("facing %.2f %.2f %.2f%n", tx, ty, tz);
        float PI = (float) Math.PI;
        float x = getX(getPlayer()), y = getY(getPlayer()) + 1.5F, z = getZ(getPlayer());
        float dx = tx - x, dy = ty - y, dz = tz - z;
        float X = (float) Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        float alpha = (float) Math.atan2(dy, X);

        float pitch = -180 / PI * alpha;
        float yaw = (float) (Math.atan2(dz, dx) + 3F * PI / 2F);
        while (yaw < -PI) yaw += 2 * PI;
        while (yaw > PI) yaw -= 2 * PI;
        yaw = 180 / PI * yaw;
        return new Tuple<>(yaw, pitch);
    }

    public static ItemStack getHeldItemStack() {
        if (getPlayer() == null) return null;
        InventoryPlayer inventoryPlayer = getPlayer().inventory;
        if (inventoryPlayer == null) return null;
        return inventoryPlayer.getCurrentItem();
    }

    public static int getHeldItemIndex() {
        // 0 - 8
        if (getPlayer() == null) return -1;
        InventoryPlayer inventoryPlayer = getPlayer().inventory;
        if (inventoryPlayer == null) return -1;
        return inventoryPlayer.currentItem;
    }

    public static void setHeldItemIndex(int index) {
        if (index < 0 || index > 8) {
            System.err.println("WTF? NO");
            return;
        }
        if (getPlayer() == null) return;
        InventoryPlayer inventoryPlayer = getPlayer().inventory;
        if (inventoryPlayer == null) return;
        inventoryPlayer.currentItem = index;
    }

    public static boolean checkHoldingItem(List<String> names) {
        // requires opened inventory, not open GUI
        if (getOpenedInventory() == null || getOpenedInventory().getSize() != 45) return false;
        return checkHotbarItem(getHeldItemIndex(), names);
    }

    public static Inventory getOpenedInventory() {
        EntityPlayerSP player = getPlayer();
        if (player == null || player.openContainer == null) return null;
        return new Inventory(player.openContainer);
    }

    public static String getInventoryName() {
        Inventory inventory = getOpenedInventory();
        if (inventory == null || inventory.getName() == null) return "";
        return ChatLib.removeFormatting(inventory.getName());
    }

    public static boolean checkHotbarItem(int slot, List<String> names) {  // [0, 9)
        Inventory inventory = getOpenedInventory();
        if (slot == -1 || inventory == null) return false;
        for (String name : names) {
            ItemStack item = getItemStackInSlot(slot + 36, true);
            if (item != null && item.getDisplayName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkHotbarItem(int slot, String name) {  // [0, 9)
        Inventory inventory = getOpenedInventory();
        if (slot == -1 || inventory == null) return false;
        ItemStack item = getItemStackInSlot(slot + 36, true);
        if (item == null) return false;
        String itemName = item.hasDisplayName() ? item.getDisplayName() : item.getItem().getRegistryName();
        return itemName.contains(name);
    }

    public static ItemStack getItemStackInSlot(int slot, boolean requiresNoGui) {
        Inventory inventory = getOpenedInventory();
        if (inventory == null || (requiresNoGui && inventory.getSize() != 45)) return null;
        return inventory.getItemInSlot(slot);
    }

    public static boolean checkHotbarItemRegistryName(int slot, String name) {
        Inventory inventory = getOpenedInventory();
        if (slot == -1 || inventory == null) return false;
        ItemStack item = getItemStackInSlot(slot + 36, true);
        if (item == null) return false;
        return item.getItem().getRegistryName().toLowerCase().contains(name);
    }

    public static void holdLeftClick() {
        KeyBinding.setKeyBindState(attackKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void releaseLeftClick() {
        KeyBinding.setKeyBindState(attackKeyBind.mcKeyBinding().getKeyCode(), false);
    }
}
