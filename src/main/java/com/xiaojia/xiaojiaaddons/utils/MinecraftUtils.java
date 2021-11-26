package com.xiaojia.xiaojiaaddons.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class MinecraftUtils {
    public static EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    public static World getWorld() {
        return mc.theWorld;
    }
}
