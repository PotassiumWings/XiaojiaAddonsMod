package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class XYZ {
    private HashSet<Integer> charmed = new HashSet<>();
    private long lastClick = 0;

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.XYZHelper) return;
        if (!SkyblockUtils.isInMysticMarsh()) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;

        if (TimeUtils.curTime() - lastClick < 30) return;

        AxisAlignedBB box = getPlayer().getEntityBoundingBox().expand(4, 4, 4);
        List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(getPlayer(), box);
        // priority: click z, click y, charm y, click x, charm x
        ArrayList<Entity> xs = new ArrayList<>();
        ArrayList<Entity> ys = new ArrayList<>();
        ArrayList<Entity> zs = new ArrayList<>();
        for (Entity entity : entitiesInRange) {
            if (!(entity instanceof EntityArmorStand)) continue;
            String name = ChatLib.removeFormatting(entity.getName());
            if (name.contains(" 0/100")) continue;
            if (name.contains("Exe")) xs.add(entity);
            else if (name.contains("Wai")) ys.add(entity);
            else if (name.contains("Zee")) zs.add(entity);
        }
        xs.sort((a, b) -> (int)(MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
        ys.sort((a, b) -> (int)(MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
        zs.sort((a, b) -> (int)(MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
        try {
            Entity entity = null;
            boolean charm = false;
            boolean harvest = false;
            if (zs.size() > 0) {
                entity = zs.get(0);
                harvest = true;
            } else {
                if (ys.size() != 0) {
                    for (Entity wai : ys) {
                        if (!ChatLib.removeFormatting(wai.getName()).contains("50/100")) {
                            entity = wai;
                            harvest = true;
                            break;
                        }
                    }
                    if (entity == null) {
                        for (Entity wai : ys) {
                            if (!charmed.contains(wai.getEntityId())) {
                                entity = wai;
                                charm = true;
                                break;
                            }
                        }
                    }
                }
                if (entity == null && xs.size() != 0) {
                    for (Entity exe : xs) {
                        if (!ChatLib.removeFormatting(exe.getName()).contains("50/100")) {
                            entity = exe;
                            harvest = true;
                            break;
                        }
                    }
                    if (entity == null) {
                        for (Entity exe : xs) {
                            if (!charmed.contains(exe.getEntityId())) {
                                entity = exe;
                                charm = true;
                                break;
                            }
                        }
                    }
                }
            }
            // harvest or charm
            if (entity != null) {
                event.setCanceled(true);
                lastClick = TimeUtils.curTime();
                if (charm) {
                    swapCharm();
                    charmed.add(entity.getEntityId());
                }
                if (harvest) swapHarvest();
                tryClickEntity(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        charmed.clear();
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (!SessionUtils.isDev()) return;
        if (TimeUtils.curTime() - lastClick < 400) {
            ChatLib.chat(event.packet.getClass().getSimpleName() +
                    (event.packet instanceof C02PacketUseEntity ? ", " + (((C02PacketUseEntity) event.packet)).getAction() : ""));
        }
    }

    private void swapCharm() throws Exception {
        if (HotbarUtils.charmSlot != -1) {
            ControlUtils.setHeldItemIndex(HotbarUtils.charmSlot);
        } else {
            ChatLib.chat("Charminizer in hotbar is required.");
            throw new Exception("Charminizer Not Found");
        }
    }

    private void swapHarvest() throws Exception {
        if (HotbarUtils.harvestSlot != -1) {
            ControlUtils.setHeldItemIndex(HotbarUtils.harvestSlot);
        } else {
            ChatLib.chat("Atominizer in hotbar is required.");
            throw new Exception("Atominizer Not Found");
        }
    }

    private void tryClickEntity(Entity entity) {
        ControlUtils.face(getX(entity), getY(entity) + entity.height / 2, getZ(entity));
        ControlUtils.forceFace();
//        ControlUtils.rightClick();

        Vec3 vec3 = entity.getPositionEyes(MathUtils.partialTicks);
        double d0 = (double)XiaojiaAddons.mc.playerController.getBlockReachDistance();
        Vec3 vec31 = entity.getLook(MathUtils.partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
        float f1 = entity.getCollisionBorderSize();
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
        MovingObjectPosition moving = axisalignedbb.calculateIntercept(vec3, vec32);

        Vec3 vec = new Vec3(
                moving.hitVec.xCoord - entity.posX,
                moving.hitVec.yCoord - entity.posY,
                moving.hitVec.zCoord - entity.posZ
        );

        XiaojiaAddons.mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, vec));
    }
}
