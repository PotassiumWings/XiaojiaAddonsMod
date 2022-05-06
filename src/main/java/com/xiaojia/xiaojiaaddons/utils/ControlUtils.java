package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vector3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getPitch;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.SkyblockUtils.getPing;

public class ControlUtils {

    private static final KeyBind useKeyBind = new KeyBind(mc.gameSettings.keyBindUseItem);
    private static final KeyBind attackKeyBind = new KeyBind(mc.gameSettings.keyBindAttack);

    private static final KeyBind moveForwardKeyBind = new KeyBind(mc.gameSettings.keyBindForward);
    private static final KeyBind moveBackwardKeyBind = new KeyBind(mc.gameSettings.keyBindBack);
    private static final KeyBind moveLeftKeyBind = new KeyBind(mc.gameSettings.keyBindLeft);
    private static final KeyBind moveRightKeyBind = new KeyBind(mc.gameSettings.keyBindRight);

    private static final KeyBind sneakKeyBind = new KeyBind(mc.gameSettings.keyBindSneak);
    private static final KeyBind sprintKeyBind = new KeyBind(mc.gameSettings.keyBindSprint);
    private static final KeyBind jumpKeyBind = new KeyBind(mc.gameSettings.keyBindJump);

    private static Inventory openedInventory = null;

    private static Method synHeldItem = null;
    private static Field pressTime = null;

    static {
        try {
            synHeldItem = PlayerControllerMP.class.getDeclaredMethod("syncCurrentPlayItem");
        } catch (NoSuchMethodException e) {
            try {
                synHeldItem = PlayerControllerMP.class.getDeclaredMethod("func_78750_j");
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (synHeldItem != null)
                synHeldItem.setAccessible(true);
        }


        try {
            pressTime = KeyBinding.class.getDeclaredField("pressTime");
        } catch (NoSuchFieldException e) {
            try {
                pressTime = KeyBinding.class.getDeclaredField("field_151474_i");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (pressTime != null)
                pressTime.setAccessible(true);
        }
    }

    public static boolean reportedFacing(double yaw, double pitch) {
        try {
            Field lastReportedYawField;
            try {
                lastReportedYawField = EntityPlayerSP.class.getDeclaredField("lastReportedYaw");
            } catch (NoSuchFieldException e) {
                lastReportedYawField = EntityPlayerSP.class.getDeclaredField("field_175164_bL");
            }
            Field lastReportedPitchField;
            try {
                lastReportedPitchField = EntityPlayerSP.class.getDeclaredField("lastReportedPitch");
            } catch (NoSuchFieldException e) {
                lastReportedPitchField = EntityPlayerSP.class.getDeclaredField("field_175165_bM");
            }
            lastReportedPitchField.setAccessible(true);
            lastReportedYawField.setAccessible(true);
            double lastReportedYaw = lastReportedYawField.getFloat(getPlayer());
            double lastReportedPitch = lastReportedPitchField.getFloat(getPlayer());
            return MathUtils.sameYaw(yaw, lastReportedYaw) && MathUtils.samePitch(pitch, lastReportedPitch);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
        if (Configs.CloseInvWhenChangingDirection)
            getPlayer().closeScreen();
        getPlayer().rotationYaw += MathHelper.wrapAngleTo180_float(yaw - getPlayer().rotationYaw);
        getPlayer().rotationPitch += MathHelper.wrapAngleTo180_float(pitch - getPlayer().rotationPitch);
        mc.entityRenderer.getMouseOver(MathUtils.partialTicks);
    }

    public static void randomChangeDirection(double delta) {
        float yaw = (float) MathUtils.validYaw(getPlayer().rotationYaw + delta - 2 * delta * Math.random());
        float pitch = (float) MathUtils.validPitch(getPlayer().rotationPitch + delta - 2 * delta * Math.random());
        changeDirection(yaw, pitch);
    }

    public static void face(float tx, float ty, float tz) {
        getPlayer().closeScreen();
        Tuple<Float, Float> res = getFaceYawAndPitch(tx, ty, tz);
        float yaw = res.getFirst(), pitch = res.getSecond();
        changeDirection(yaw, pitch);
    }

    public static void forceFace() {
        getPlayer().prevRotationPitch = getPlayer().rotationPitch;
        getPlayer().prevRotationYaw = getPlayer().rotationYaw;
    }

    public static void face(double tx, double ty, double tz) {
        face((float) tx, (float) ty, (float) tz);
    }

    public static void face(BlockPos pos) {
        face(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void face(Entity entity) {
        face((float) entity.posX, (float) entity.posY, (float) entity.posZ);
    }

    public static void faceSlowly(float yaw, float pitch, boolean shouldThrow) throws InterruptedException {
        float curyaw = MathUtils.getYaw(), curpitch = MathUtils.getPitch();
        if (curyaw < 0) curyaw += 360;  // curyaw \in [0, 360]
        if (yaw < 0) yaw += 360;  // yaw \in [0, 360]
        if (yaw - curyaw > 180) yaw -= 360;  // yaw = 359, curyaw = 1 -> yaw = -1, curyaw = 1
        if (curyaw - yaw > 180) curyaw -= 360;  // yaw = 1, curyaw = 359 -> yaw = 1, curyaw = -1

        int[] factors = new int[]{50, 20, 7, 3};
        int factor = factors[Configs.ChangeDirectionMode];
        int rotate_times = MathUtils.floor(
                Math.sqrt(Math.pow(curyaw - yaw, 2) + Math.pow(curpitch - pitch, 2)) / factor
        ) + 1;
        System.err.printf("curyaw %.2f, yaw %.2f%n", curyaw, yaw);
        for (int j = 1; j <= rotate_times; j++) {
            float toturn_yaw = curyaw + (yaw - curyaw) / rotate_times * j;
            while (toturn_yaw > 180) toturn_yaw -= 360;
            while (toturn_yaw < -180) toturn_yaw += 360;
            float toturn_pitch = curpitch + (pitch - curpitch) / rotate_times * j;
            ControlUtils.changeDirection(toturn_yaw, toturn_pitch);
            Thread.sleep((long) (10 + Math.random() * 20));
            checkDirection(toturn_yaw, toturn_pitch, shouldThrow);
        }
    }

    public static void etherWarp(javax.vecmath.Vector3d v) throws Exception {
        etherWarp(v.x, v.y, v.z);
    }

    public static void etherWarp(double x, double y, double z) throws Exception {
        // shift click
        ControlUtils.sneak();
        ControlUtils.faceSlowly(x, y, z);
        Thread.sleep(getPing() + 50);
        ControlUtils.rightClick();
        Thread.sleep(getPing() + 100);
        ControlUtils.unSneak();
    }

    public static void checkDirection(float yaw, float pitch, boolean shouldThrow) throws InterruptedException {
        if (differentDirection(yaw, pitch) && shouldThrow) {
            ChatLib.chat("Detected yaw/pitch move, interrupted.");
            throw new InterruptedException();
        }
    }

    public static boolean differentDirection(float yaw, float pitch) {
        return (Math.abs(getYaw() - yaw) > 1e-2 && Math.abs(getYaw() - yaw) < 360 - 1e-2) ||
                Math.abs(getPitch() - pitch) > 1e-2;
    }

    public synchronized static void faceSlowly(float tx, float ty, float tz, boolean shouldThrow) throws InterruptedException {
        Tuple<Float, Float> res = getFaceYawAndPitch(tx, ty, tz);
        System.err.printf("facing slowly to %.2f %.2f %.2f%n", tx, ty, tz);
        float yaw = res.getFirst(), pitch = res.getSecond();
        faceSlowly(yaw, pitch, shouldThrow);
    }

    public static void faceSlowly(float tx, float ty, float tz) throws InterruptedException {
        faceSlowly(tx, ty, tz, true);
    }

    public static void faceSlowly(double yaw, double pitch) throws InterruptedException {
        faceSlowly((float) yaw, (float) pitch, true);
    }

    public static void faceSlowly(double tx, double ty, double tz) throws InterruptedException {
        faceSlowly((float) tx, (float) ty, (float) tz);
    }

    public static void faceSlowly(javax.vecmath.Vector3d v) throws InterruptedException {
        faceSlowly((float) v.x, (float) v.y, (float) v.z);
    }

    public static void faceSlowly(BlockPos pos) throws InterruptedException {
        faceSlowly(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Tuple<Float, Float> getFaceYawAndPitch(float tx, float ty, float tz) {
        System.err.printf("facing %.2f %.2f %.2f%n", tx, ty, tz);
        float PI = (float) Math.PI;
        float x = getX(getPlayer()), y = getY(getPlayer()) + getPlayer().getEyeHeight(), z = getZ(getPlayer());
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
        setHeldItemIndex(index, true);
    }

    public static void setHeldItemIndex(int index, boolean sync) {
        if (index < 0 || index > 8) {
            System.err.println("WTF? NO");
            return;
        }
        if (getPlayer() == null) return;
        InventoryPlayer inventoryPlayer = getPlayer().inventory;
        if (inventoryPlayer == null) return;
        inventoryPlayer.currentItem = index;
        if (synHeldItem != null && sync) {
            try {
                synHeldItem.invoke(mc.playerController);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkHoldingItem(List<String> names) {
        // requires opened inventory, not open GUI
        if (getOpenedInventory() == null || getOpenedInventory().getSize() != 45) return false;
        return checkHotbarItem(getHeldItemIndex(), names);
    }

    public static ItemStack getBoots() {
        EntityPlayerSP player = getPlayer();
        return player.getEquipmentInSlot(1);
    }

    public static boolean checkHoldingItem(String name) {
        ArrayList<String> names = new ArrayList<>();
        names.add(name);
        return checkHoldingItem(names);
    }

    public static Inventory getOpenedInventory() {
        return openedInventory;
    }

    public static String getOpenedInventoryName() {
        Inventory inventory = getOpenedInventory();
        if (inventory != null) return inventory.getName();
        return "";
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

    public static void holdSprint() {
        KeyBinding.setKeyBindState(sprintKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void releaseLeftClick() {
        KeyBinding.setKeyBindState(attackKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void sneak() {
        KeyBinding.setKeyBindState(sneakKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void unSneak() {
        KeyBinding.setKeyBindState(sneakKeyBind.mcKeyBinding().getKeyCode(), false);
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

    public static void holdForward() {
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void releaseForward() {
        KeyBinding.setKeyBindState(moveForwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void releaseRightClick() {
        KeyBinding.setKeyBindState(useKeyBind.mcKeyBinding().getKeyCode(), false);
        if (getPlayer().isUsingItem())
            mc.playerController.onStoppedUsingItem(getPlayer());
    }

    public static int getRightClickPressTime() throws IllegalAccessException {
        return (int) pressTime.get(useKeyBind.mcKeyBinding());
    }

    public static void holdRightClick() {
        KeyBinding.setKeyBindState(useKeyBind.mcKeyBinding().getKeyCode(), true);
    }


    public static void releaseBackward() {
        KeyBinding.setKeyBindState(moveBackwardKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void releaseLeft() {
        KeyBinding.setKeyBindState(moveLeftKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void releaseRight() {
        KeyBinding.setKeyBindState(moveRightKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void releaseJump() {
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), false);
    }

    public static void jump() throws InterruptedException {
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), true);
        Thread.sleep(100);
        KeyBinding.setKeyBindState(jumpKeyBind.mcKeyBinding().getKeyCode(), false);
        Thread.sleep(100);
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
        holdLeft();
        Thread.sleep(delta);
        releaseLeft();
    }

    public static void moveRight(long delta) throws InterruptedException {
        holdRight();
        Thread.sleep(delta);
        releaseRight();
    }

    public static void holdRight() {
        KeyBinding.setKeyBindState(moveRightKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void holdLeft() {
        KeyBinding.setKeyBindState(moveLeftKeyBind.mcKeyBinding().getKeyCode(), true);
    }

    public static void stopMoving() {
        releaseForward();
        releaseBackward();
        releaseLeft();
        releaseRight();
        releaseJump();
        unSneak();
    }

    @SubscribeEvent
    public void onTickUpdateInventory(TickEndEvent event) {
        if (!Checker.enabled) return;
        EntityPlayerSP player = getPlayer();
        if (player == null || player.openContainer == null) openedInventory = null;
        else openedInventory = new Inventory(player.openContainer);
    }
}
