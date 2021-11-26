package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class StonklessStonk {
    private static final KeyBind keyBind = new KeyBind("Stonkless stonk", Keyboard.KEY_NONE);
    private static final int witherEssenceHash = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=".hashCode();
    private static final HashMap<BlockPos, Long> doneSecretsPos = new HashMap<>();
    private static boolean should = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (false) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "SecretAura &aactivated" : "SecretAura &cdeactivated");
        }
        if (!should) return;
        float PI = (float) Math.PI;
        float px = MathUtils.getX(getPlayer()), py = MathUtils.getY(getPlayer()) + getPlayer().getEyeHeight(), pz = MathUtils.getZ(getPlayer());
        float yaw = MathUtils.getYaw() * PI / 180, pitch = MathUtils.getPitch() * PI / 180;
        float R = 5;
        int lastX = -1, lastY = -1, lastZ = -1;
        for (int delta = 1; delta <= 300; delta++) {
            float r = R * delta / 300;
            float x = (float) (px - Math.sin(yaw) * Math.cos(pitch) * r);
            float y = (float) (py - Math.sin(pitch) * r);
            float z = (float) (pz + Math.cos(yaw) * Math.cos(pitch) * r);
            int fx = (int) x, fy = (int) y, fz = (int) z;
            if (fx == lastX && fy == lastY && fz == lastZ) continue;
            lastX = fx;
            lastY = fy;
            lastZ = fz;

            BlockPos pos = new BlockPos(fx, fy, fz);
            if (doneSecretsPos.containsKey(pos) && TimeUtils.curTime() - doneSecretsPos.get(pos) < 300) continue;

            Block block = BlockUtils.getBlockAt(pos);
            if (isSecret(block, pos)) {
                mc.playerController.onPlayerRightClick(
                        getPlayer(),
                        mc.theWorld,
                        getPlayer().inventory.getCurrentItem(),
                        pos,
                        EnumFacing.fromAngle(getPlayer().rotationYaw),
                        new Vec3(x, y, z)
                );
                doneSecretsPos.put(pos, TimeUtils.curTime());
                if (XiaojiaAddons.isDebug()) ChatLib.chat("secret aura: (" + x + ", " + y + ", " + z + ")");
            }
        }
    }

    private boolean isSecret(Block block, BlockPos pos) {
        if (block instanceof BlockChest || block instanceof BlockLever) return true;
        if (block instanceof BlockSkull) {
            return BlockUtils.getTileProperty((BlockSkull) block, pos).hashCode() == witherEssenceHash;
        }
        return false;
    }
}
