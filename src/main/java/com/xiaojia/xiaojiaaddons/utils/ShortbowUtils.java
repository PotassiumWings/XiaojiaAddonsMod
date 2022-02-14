package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Cube;
import com.xiaojia.xiaojiaaddons.Objects.TestCubeGUI;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector2d;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getPitch;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ShortbowUtils {
    public static double getProjectileFunction(double x, double alpha) {
        double v0 = 13.75, g = 1.0, k = 0.049;
        double t = -Math.log(1 - k * x / v0 / Math.cos(alpha)) / k;
        return x / (v0 * k * Math.cos(alpha)) * (v0 * k * Math.sin(alpha) + g) - g * t / k;
    }

    // for testing ender crystals
    public static void testEnderCrystals(double tx, double ty, double tz) {
        Vector2d p = calcYawPitchEnderCrystal(tx, ty, tz);
        double yaw = p.x, pitch = p.y;
        ControlUtils.changeDirection((float) yaw, (float) pitch);
    }

    private static double y_t(double alpha, double t, boolean isMiddle) {
        double v0 = 13.75, g = 1.0, k = 0.049;
        if (!isMiddle) v0 = 13.8;
        return 1 / k / k * (g + k * v0 * Math.sin(alpha)) * (1 - Math.exp(-k * t)) - g / k * t;
    }

    private static double x_t(double alpha, double t, boolean isMiddle) {
        double v0 = 13.75, g = 1.0, k = 0.049;
        if (!isMiddle) v0 = 13.8;
        return v0 / k * Math.cos(alpha) * (1 - Math.exp(-k * t));
    }

    public static void testTerminator() {
        new Thread(() -> {
            try {
                double x = getX(getPlayer());
                double y = getY(getPlayer()) + getPlayer().getEyeHeight();
                double z = getZ(getPlayer());
                double yaw = (getYaw() + 90) * Math.PI / 180;
                double pitch = -getPitch() * Math.PI / 180;
                double dPitch = ((Math.PI / 2) - pitch) * (Math.PI / 180);
                if (pitch < 0) dPitch = (pitch - (-Math.PI / 2)) * (Math.PI / 180);

                double _pitch = pitch + dPitch;
                double dYaw = Math.sqrt(5.1 * 5.1 * Math.pow(Math.PI / 180, 2) + dPitch * dPitch);
                ArrayList<Cube> cubes = new ArrayList<>();
                TestCubeGUI.cubes.clear();
                for (int t = 0; t < 200; t++) {
                    double X = x_t(pitch, t / 10F, true);
                    double Y = y_t(pitch, t / 10F, true);
                    double _X = x_t(_pitch, t / 10F, false);
                    double _Y = y_t(_pitch, t / 10F, false);
                    Cube middleCube = new Cube(
                            X * Math.cos(yaw) + x, y + Y, z + X * Math.sin(yaw),
                            0.1, 0.1
                    );
                    // Terminator
                    Cube leftCube = new Cube(
                            _X * Math.cos(yaw - dYaw) + x,
                            y + _Y,
                            z + _X * Math.sin(yaw - dYaw),
                            0.1, 0.1
                    );
                    Cube rightCube = new Cube(
                            _X * Math.cos(yaw + dYaw) + x,
                            y + _Y,
                            z + _X * Math.sin(yaw + dYaw),
                            0.1, 0.1
                    );
//                    // Runaan's Bow
//                    Cube leftCube1 = new Cube(
//                            X * Math.cos(yaw - 12.5F / 180 * Math.PI) + x, y + Y, z + X * Math.sin(yaw - 12.5F / 180 * Math.PI),
//                            0.1, 0.1
//                    );
//                    Cube rightCube1 = new Cube(
//                            X * Math.cos(yaw + 12.5F / 180 * Math.PI) + x, y + Y, z + X * Math.sin(yaw + 12.5F / 180 * Math.PI),
//                            0.1, 0.1
//                    );
//                    // Hurricane Bow
//                    Cube leftCube2 = new Cube(
//                            X * Math.cos(yaw - 7.5F / 180 * Math.PI) + x, y + Y, z + X * Math.sin(yaw - 7.5F / 180 * Math.PI),
//                            0.1, 0.1
//                    );
//                    Cube rightCube2 = new Cube(
//                            X * Math.cos(yaw + 7.5F / 180 * Math.PI) + x, y + Y, z + X * Math.sin(yaw + 7.5F / 180 * Math.PI),
//                            0.1, 0.1
//                    );
//                    Cube leftCube3 = new Cube(
//                            X * Math.cos(yaw - 15F / 180 * Math.PI) + x, y + Y, z + X * Math.sin(yaw - 15F / 180 * Math.PI),
//                            0.1, 0.1
//                    );
//                    Cube rightCube3 = new Cube(
//                            X * Math.cos(yaw + 15F / 180 * Math.PI) + x, y + Y, z + X * Math.sin(yaw + 15F / 180 * Math.PI),
//                            0.1, 0.1
//                    );
//                    leftCube1.color = rightCube1.color = new Color(255, 0, 0);
//                    leftCube2.color = rightCube2.color = leftCube3.color = rightCube3.color = new Color(0, 0, 255);
                    cubes.add(leftCube);
                    cubes.add(middleCube);
                    cubes.add(rightCube);
//                    cubes.add(leftCube1);
//                    cubes.add(leftCube2);
//                    cubes.add(leftCube3);
//                    cubes.add(rightCube1);
//                    cubes.add(rightCube2);
//                    cubes.add(rightCube3);
                }
                ControlUtils.leftClick();
                TestCubeGUI.cubes.addAll(cubes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Vector2d calcYawPitchEnderCrystal(double tx, double ty, double tz) {
        // consider player EyeHeight and crystal HitBox
        double x = getX(getPlayer()), y = getY(getPlayer()) + getPlayer().getEyeHeight(), z = getZ(getPlayer());
        return getDirection(x, y, z, tx, ty + 1, tz);
    }

    public static Vector2d getDirection(double x, double y, double z, double tx, double ty, double tz) {
        double PI = Math.PI;
        double dx = tx - x, dy = ty - y, dz = tz - z;
        double X = Math.sqrt((x - tx) * (x - tx) + (z - tz) * (z - tz));
        double v0 = 13.75, g = 1.0, k = 0.049;
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
//        for (Entity entity: getWorld().loadedEntityList) {
//            if (entity instanceof EntityArrow) {
//                Cube arrowCube = new Cube(
//                        getX(entity), getY(entity) - 0.1F, getZ(entity),
//                        0.1F, 0.1F
//                );
//                arrowCube.color = new Color(255, 255, 255);
//                TestCubeGUI.cubes.add(arrowCube);
//            }
//        }
    }
}
