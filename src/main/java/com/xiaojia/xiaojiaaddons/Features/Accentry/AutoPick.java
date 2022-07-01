package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoPick {
    public static KeyBind keyBind = new KeyBind("Auto Pick", Keyboard.KEY_NONE);
    private long lastPick = 0;
    private boolean should = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPick) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Auto Pick &aactivated" : "Auto Pick &cdeactivated");
        }
        if (!should) return;
        long cur = TimeUtils.curTime();
        if (cur - lastPick < Configs.AutoPickCD) return;
        EntityPlayer player = getPlayer();
        if (player == null) return;

        String[] names = Configs.AutoPickNames.split(",");
        List<Entity> itemEntities = new ArrayList<>();
        for (Entity entity : EntityUtils.getEntities()) {
            if (!(entity instanceof EntityItem)) continue;
            ItemStack itemStack = ((EntityItem) entity).getEntityItem();
            String name = ChatLib.removeFormatting(itemStack.getDisplayName());
            if (Arrays.stream(names).anyMatch(name::contains)) itemEntities.add(entity);
        }
        itemEntities.removeIf(entity -> vanillaTeleportPositions(entity.posX, entity.posY + 1, entity.posZ, 4) == null);
        if (itemEntities.isEmpty()) return;
        if (player.onGround) {
            player.jump();
        } else {
            lastPick = cur;
            int index = (int) (itemEntities.size() * Math.random());
            Entity entity = itemEntities.get(index);
            List<Vector3f> vectors = vanillaTeleportPositions(entity.posX, entity.posY + 1, entity.posZ, 4);
            if (vectors == null) {
                ChatLib.chat("Error occurred in auto pick.");
                return;
            }
            for (Vector3f vec : vectors)
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x, vec.y, vec.z, false));
        }
    }

    private List<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        List<Vector3f> positions = new ArrayList<>();
        double posX = tpX - mc.thePlayer.posX;
        double posZ = tpZ - mc.thePlayer.posZ;
        float yaw = (float) (Math.atan2(posZ, posX) * 180.0D / Math.PI - 90.0D);
        double tmpY = mc.thePlayer.posY;
        double steps = 1.0D;
        double d;
        for (d = speed; d * d < MathUtils.distanceSquaredFromPoints(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed)
            steps++;
        for (d = speed; d * d < MathUtils.distanceSquaredFromPoints(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed) {
            double tmpX = mc.thePlayer.posX - Math.sin(Math.toRadians(yaw)) * d;
            double tmpZ = mc.thePlayer.posZ + Math.cos(Math.toRadians(yaw)) * d;
            tmpY -= (mc.thePlayer.posY - tpY) / steps;
            if (!BlockUtils.isBlockAir(tmpX, tmpY, tmpZ) || !BlockUtils.isBlockAir(tmpX, tmpY + 1, tmpZ) || !BlockUtils.isBlockAir(tmpX, tmpY + 2, tmpZ))
                return null;
            positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
        }
        positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));
        return positions;
    }
}
