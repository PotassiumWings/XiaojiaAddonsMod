package com.xiaojia.xiaojiaaddons.utils;

import javax.vecmath.Vector2d;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ShortbowUtils {
    public static double getProjectileFunction(double x, double alpha) {
        double v0 = 13.7, g = 1.0, k = 0.045;
        double t = -Math.log(1 - k * x / v0 / Math.cos(alpha)) / k;
        return x / (v0 * k * Math.cos(alpha)) * (v0 * k * Math.sin(alpha) + g) - g * t / k;
    }

    public static void test(double tx, double ty, double tz) {
        Vector2d p = calc(tx, ty, tz);
        double yaw = p.x, pitch = p.y;
        ControlUtils.changeDirection((float) yaw, (float) pitch);
    }

    public static Vector2d calc(double tx, double ty, double tz) {
        // consider player EyeHeight and crystal HitBox
        double x = getX(getPlayer()), y = getY(getPlayer()) + getPlayer().getEyeHeight(), z = getZ(getPlayer());
        return getDirection(x, y, z, tx, ty + 1, tz);
    }

    public static Vector2d getDirection(double x, double y, double z, double tx, double ty, double tz) {
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
}
