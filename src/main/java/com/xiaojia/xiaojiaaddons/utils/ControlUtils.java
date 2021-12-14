package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getPitch;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ControlUtils {

    private static final KeyBind useKeyBind = new KeyBind(mc.gameSettings.keyBindUseItem);
    private static final KeyBind attackKeyBind = new KeyBind(mc.gameSettings.keyBindAttack);

    private static final KeyBind moveForwardKeyBind = new KeyBind(mc.gameSettings.keyBindForward);
    private static final KeyBind moveBackwardKeyBind = new KeyBind(mc.gameSettings.keyBindBack);
    private static final KeyBind moveLeftKeyBind = new KeyBind(mc.gameSettings.keyBindLeft);
    private static final KeyBind moveRightKeyBind = new KeyBind(mc.gameSettings.keyBindRight);

    private static Inventory openedInventory = null;

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

    public static void face(BlockPos pos) {
        face(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void face(Entity entity) {
        face((float) entity.posX, (float) entity.posY, (float) entity.posZ);
    }

    public synchronized static void faceSlowly(float tx, float ty, float tz, boolean shouldThrow) throws InterruptedException {
        Tuple<Float, Float> res = getFaceYawAndPitch(tx, ty, tz);
        float yaw = res.getFirst(), pitch = res.getSecond();
        float curyaw = MathUtils.getYaw(), curpitch = MathUtils.getPitch();
        if (curyaw < 0) curyaw += 360;
        if (yaw < 0) yaw += 360;
        if (yaw - curyaw > 180) yaw -= 360;
        if (curyaw - yaw > 180) curyaw -= 360;
        int rotate_times = (int) Math.floor(2 + Math.random() * 6);
        for (int j = 1; j <= rotate_times; j++) {
            float toturn_yaw = curyaw + (yaw - curyaw) / rotate_times * j;
            while (toturn_yaw > 180) toturn_yaw -= 360;
            while (toturn_yaw < -180) toturn_yaw += 360;
            float toturn_pitch = curpitch + (pitch - curpitch) / rotate_times * j;
            ControlUtils.changeDirection(toturn_yaw, toturn_pitch);
            Thread.sleep((long) (10 + Math.random() * 20));
            if ((Math.abs(getYaw() - toturn_yaw) > 1e-5 && Math.abs(getYaw() - toturn_yaw) < 360 - 1e-5) ||
                    Math.abs(getPitch() - toturn_pitch) > 1e-5 && shouldThrow) {
                ChatLib.chat("Detected yaw/pitch move, interrupted.");
                throw new InterruptedException();
            }
        }
    }

    public static void faceSlowly(float tx, float ty, float tz) throws InterruptedException {
        faceSlowly(tx, ty, tz, true);
    }

    public static void faceSlowly(BlockPos pos) throws InterruptedException {
        faceSlowly(pos.getX(), pos.getY(), pos.getZ());
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
        return openedInventory;
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

    public static void moveRandomly(long delta) throws InterruptedException {
        int choose = (int) (Math.random() * 4);
        switch (choose) {
            case 0:
            case 1:
                moveForward(delta);
                break;
            case 2:
                moveLeft(delta);
                break;
            case 3:
                moveRight(delta);
                break;
            default:
        }
    }

    public static void moveForward(long delta) throws InterruptedException {
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(delta);
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void moveBackward(long delta) throws InterruptedException {
        KeyBinding.setKeyBindState(moveBackwardKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(delta);
        KeyBinding.setKeyBindState(moveBackwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void moveLeft(long delta) throws InterruptedException {
        KeyBinding.setKeyBindState(moveLeftKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(delta);
        KeyBinding.setKeyBindState(moveLeftKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void moveRight(long delta) throws InterruptedException {
        KeyBinding.setKeyBindState(moveRightKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(delta);
        KeyBinding.setKeyBindState(moveRightKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    @SubscribeEvent
    public void onTickUpdateInventory(TickEndEvent event) {
        if (!Checker.enabled) return;
        EntityPlayerSP player = getPlayer();
        if (player == null || player.openContainer == null) openedInventory = null;
        else openedInventory = new Inventory(player.openContainer);
    }
}
