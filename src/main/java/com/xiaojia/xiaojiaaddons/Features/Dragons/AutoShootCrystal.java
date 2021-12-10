package com.xiaojia.xiaojiaaddons.Features.Dragons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.ControlUtils.rightClick;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoShootCrystal {
    private static final KeyBind autoShootCrystalKeybind = new KeyBind("Auto Crystal", Keyboard.KEY_NONE);
    private final ArrayList<Vector3d> shootQueue = new ArrayList<>();
    private boolean enabled = false;
    private int shootQueueP = 0;

    private long lastShootTime = 0;
    private boolean isShooting = false;

    private static double getProjectileFunction(double x, double alpha) {
        double v0 = 13.7, g = 1.0, k = 0.045;
        double t = -Math.log(1 - k * x / v0 / Math.cos(alpha)) / k;
        return x / (v0 * k * Math.cos(alpha)) * (v0 * k * Math.sin(alpha) + g) - g * t / k;
    }

    public static void test(double tx, double ty, double tz) {
        Vector2d p = calc(tx, ty, tz);
        double yaw = p.x, pitch = p.y;
        ControlUtils.changeDirection((float) yaw, (float) pitch);
    }

    private static Vector2d calc(double tx, double ty, double tz) {
        // consider player EyeHeight and crystal HitBox
        double x = getX(getPlayer()), y = getY(getPlayer()) + getPlayer().getEyeHeight(), z = getZ(getPlayer());
        return getDirection(x, y, z, tx, ty + 1, tz);
    }

    private static Vector2d getDirection(double x, double y, double z, double tx, double ty, double tz) {
        double PI = Math.PI;
        double dx = tx - x, dy = ty - y, dz = tz - z;
        double X = Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        double v0 = 13.7, g = 1.0, k = 0.045;
        double MAX_ALPHA = Math.acos(k * X / v0);
        double L = -MAX_ALPHA, R = MAX_ALPHA;
        while (R - L > 0.01) {
            double M1 = L + (R - L) / 3.0;
            double M2 = L + (R - L) / 3.0 * 2;
            double Y1 = getProjectileFunction(X, M1);
            double Y2 = getProjectileFunction(X, M2);
            if (Y1 < Y2) L = M1;
            else R = M2;
        }
        double max_pitch = (L + R) / 2;
        // [-PI/2, max_pitch]↑ [max_pitch, PI/2]↓
        L = max_pitch;
        R = MAX_ALPHA;
        while (R - L > 0.01) {
            double M = (L + R) / 2;
            double Y = getProjectileFunction(X, M);
            if (Y < dy) R = M;
            else L = M;
        }
        double pitch1 = (L + R) / 2;
        L = -MAX_ALPHA;
        R = max_pitch;
        while (R - L > 0.01) {
            double M = (L + R) / 2;
            double Y = getProjectileFunction(X, M);
            if (Y > dy) R = M;
            else L = M;
        }
        double pitch2 = (L + R) / 2;

        double yaw = Math.atan2(dz, dx) + 3 * PI / 2;
        while (yaw < -PI) yaw += 2 * PI;
        while (yaw > PI) yaw -= 2 * PI;
        yaw = 180 / PI * yaw;
        pitch1 = -180 / PI * pitch1;
        pitch2 = -180 / PI * pitch2;
        double pitch = pitch1;
        if (Math.abs(pitch2) < Math.abs(pitch)) pitch = pitch2;
        return new Vector2d(yaw, pitch);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled || !SkyblockUtils.isInDragon()) return;
        if (!Configs.AutoShootCrystal) return;
        if (shootQueue.size() != 0) return;
        List<Entity> list = getWorld().loadedEntityList;
        for (Entity entity : list) {
            if (entity instanceof EntityEnderCrystal) {
                shootQueue.add(new Vector3d(entity.posX, entity.posY, entity.posZ));
            }
        }
    }

    @SubscribeEvent
    public void onTickShoot(TickEndEvent event) {
        if (!Checker.enabled || !SkyblockUtils.isInDragon()) return;
        if (!Configs.AutoShootCrystal) return;
        if (isShooting || !enabled) return;
        if (shootQueueP >= shootQueue.size()) {
            shootQueue.clear();
            shootQueueP = 0;
        } else {
            if (TimeUtils.curTime() - lastShootTime <= Configs.AutoShootCrystalCD) return;
            Vector3d p = shootQueue.get(shootQueueP);
            if (!ControlUtils.checkHotbarItem(HotbarUtils.terminatorSlot, "Terminator")) {
                ChatLib.chat("Auto Crystal requires terminator in hotbar!");
                return;
            }
            double x = p.x, y = p.y, z = p.z;
            Vector2d res = calc(x, y, z);
            double yaw = res.x, pitch = res.y;
            lastShootTime = TimeUtils.curTime();
            new Thread(() -> {
                try {
                    isShooting = true;
                    ControlUtils.changeDirection((float) yaw, (float) pitch);
                    Thread.sleep(40);
                    ControlUtils.changeDirection((float) yaw, (float) pitch);
                    rightClickTerminator();
                    Thread.sleep(20);
                    ControlUtils.changeDirection((float) yaw, (float) pitch);
                    rightClickTerminator();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    isShooting = false;
                    shootQueueP++;
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        lastShootTime = 0;
        shootQueueP = 0;
        shootQueue.clear();
    }

    @SubscribeEvent
    public void onTickKeyBind(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (autoShootCrystalKeybind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "AutoShootCrystal &aactivated" : "AutoShootCrystal &cdeactivated");
        }
    }

    private void rightClickTerminator() {
        ControlUtils.setHeldItemIndex(HotbarUtils.terminatorSlot);
        rightClick();
    }
}
