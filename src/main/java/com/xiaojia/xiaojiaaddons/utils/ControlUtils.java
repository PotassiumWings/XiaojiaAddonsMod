package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ControlUtils {

    public static void rightClick() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        getPlayer().closeScreen();
        Method rightClickMethod = mc.getClass().getDeclaredMethod("rightClickMouse");
        rightClickMethod.setAccessible(true);
        rightClickMethod.invoke(mc);
    }

    public static void leftClick() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        getPlayer().closeScreen();
        Method leftClickMethod = mc.getClass().getDeclaredMethod("clickMouse");
        leftClickMethod.setAccessible(true);
        leftClickMethod.invoke(mc);
    }

    public static void changeDirection(float yaw, float pitch) {
        if (pitch > 90 || pitch < -90 || yaw < -180 || yaw > 180) {
            System.out.println("wrong dir! " + yaw + ", " + pitch);
            return;
        }
        System.out.println("changed dir! " + yaw + ", " + pitch);
        getPlayer().rotationYaw += MathHelper.wrapAngleTo180_float(yaw - getPlayer().rotationYaw);
        getPlayer().rotationPitch += MathHelper.wrapAngleTo180_float(pitch - getPlayer().rotationPitch);
    }

    public static void face(float tx, float ty, float tz) {
        getPlayer().closeScreen();
        Tuple<Float, Float> res = getFaceYawAndPitch(tx, ty, tz);
        float yaw = res.getFirst(), pitch = res.getSecond();
        changeDirection(yaw, pitch);
    }

    private static Tuple<Float, Float> getFaceYawAndPitch(float tx, float ty, float tz) {
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

    public static Inventory getOpenedInventory() {
        EntityPlayerSP player = getPlayer();
        if (player == null || player.openContainer == null) return null;
        return new Inventory(player.openContainer);
    }
}
