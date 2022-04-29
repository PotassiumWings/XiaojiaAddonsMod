package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketSendEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class XYZ {
    private static Field entityIdField = null;

    static {
        try {
            entityIdField = C02PacketUseEntity.class.getDeclaredField("entityId");
        } catch (NoSuchFieldException e) {
            try {
                entityIdField = C02PacketUseEntity.class.getDeclaredField("field_149567_a");
                entityIdField.setAccessible(true);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
    }

    private final HashSet<Integer> charmed = new HashSet<>();
    private final HashMap<Integer, Integer> hits = new HashMap<>();
    private long lastClick = 0;
//    private AxisAlignedBB entityBox = null;
//    private Vec3 eyePos = null;
//    private Vec3 lookVec = null;
//    private Vec3 hitVec = null;

    private static String getId(Entity entity) {
        if (!(entity instanceof EntityArmorStand)) return null;
        ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
        if (helm == null) return null;
        if (helm.getItem() != Items.skull || helm.getMetadata() != 3) return null;
        return helm.getTagCompound()
                .getCompoundTag("SkullOwner")
                .getString("Id");
    }

    private static boolean isX(Entity entity) {
        String id = getId(entity);
        return id != null && id.equals("25f7c8b4-3cc7-33df-a0c8-33f5e309036f");
    }

    private static boolean isY(Entity entity) {
        String id = getId(entity);
        return id != null && id.equals("954e4eac-fbed-3a5a-8ab7-091cb189cac1");
    }

    private static boolean isZ(Entity entity) {
        String id = getId(entity);
        return id != null && id.equals("df890e75-67c6-3119-be04-9a3175c2455d");
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.XYZHelper) return;
        if (!SkyblockUtils.isInMysticMarsh()) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;

        if (TimeUtils.curTime() - lastClick < 30) return;

        AxisAlignedBB box = getPlayer().getEntityBoundingBox().expand(4.5, 4.5, 4.5);
        List<Entity> entitiesInRange = getWorld().getEntitiesWithinAABBExcludingEntity(getPlayer(), box);
        // priority: click z, click y, charm y, click x, charm x
        ArrayList<EntityArmorStand> xs = new ArrayList<>();
        ArrayList<EntityArmorStand> ys = new ArrayList<>();
        ArrayList<EntityArmorStand> zs = new ArrayList<>();
        for (Entity entity : entitiesInRange) {
            if (charmed.contains(entity.getEntityId()) || !(entity instanceof EntityLivingBase) ||
                    ((EntityLivingBase) entity).getHealth() == 0) continue;

            if (isX(entity)) xs.add((EntityArmorStand) entity);
            else if (isY(entity)) ys.add((EntityArmorStand) entity);
            else if (isZ(entity)) zs.add((EntityArmorStand) entity);
        }
        xs.sort((a, b) -> (int) (MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
        ys.sort((a, b) -> (int) (MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
        zs.sort((a, b) -> (int) (MathUtils.distanceSquareFromPlayer(a) - MathUtils.distanceSquareFromPlayer(b)));
        try {
            Entity entity = null;
            boolean charm = false;
            boolean harvest = false;
            if (zs.size() > 0) {
                entity = zs.get(0);
                harvest = true;
            } else {
                if (ys.size() != 0) {
                    for (EntityArmorStand wai : ys) {
                        if (wai.getHealth() > wai.getMaxHealth() / 2) {
                            entity = wai;
                            harvest = true;
                            break;
                        }
                    }
                    if (entity == null) {
                        for (EntityArmorStand wai : ys) {
                            if (!charmed.contains(wai.getEntityId())) {
                                entity = wai;
                                charm = true;
                                break;
                            }
                        }
                    }
                }
                if (entity == null && xs.size() != 0) {
                    for (EntityArmorStand exe : xs) {
                        if (exe.getHealth() > exe.getMaxHealth() / 2) {
                            entity = exe;
                            harvest = true;
                            break;
                        }
                    }
                    if (entity == null) {
                        for (EntityArmorStand exe : xs) {
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
        hits.clear();
    }

    @SubscribeEvent
    public void onPacketSend(PacketSendEvent event) {
        if (entityIdField != null && event.packet instanceof C02PacketUseEntity) {
            try {
                ItemStack held = ControlUtils.getHeldItemStack();
                if (!NBTUtils.getSkyBlockID(held).equals("ATOMINIZER")) return;
                int entityId = entityIdField.getInt(event.packet);

                Entity entity = getWorld().getEntityByID(entityId);
                if (!(entity instanceof EntityArmorStand)) return;
                boolean x = isX(entity), y = isY(entity), z = isZ(entity);
                if (!x && !y && !z) return;
                float health = ((EntityArmorStand) entity).getHealth();
                float maxHealth = ((EntityArmorStand) entity).getMaxHealth();
                // any health is ok
                if (z || y && Configs.XYZMode >= 1 || x && Configs.XYZMode >= 2) return;
                // should not kill em
                if (2 * health - (hits.getOrDefault(entityId, 0)  + 1) * maxHealth > 0) {
                    hits.put(entityId, hits.getOrDefault(entityId, 0) + 1);
                    return;
                }
                event.setCanceled(true);
                ChatLib.chat("XYZ Helper blocked your wrong click!");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
//        ControlUtils.forceFace();
//        ControlUtils.rightClick();

        Vec3 vec3 = getPlayer().getPositionEyes(1.0F);
        double d0 = XiaojiaAddons.mc.playerController.getBlockReachDistance();
        Vec3 vec31 = getPlayer().getLook(1.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
        float f1 = entity.getCollisionBorderSize();
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f1, f1, f1);
        MovingObjectPosition moving = axisalignedbb.calculateIntercept(vec3, vec32);

        Vec3 vec = new Vec3(
                moving.hitVec.xCoord - entity.posX,
                moving.hitVec.yCoord - entity.posY,
                moving.hitVec.zCoord - entity.posZ
        );

//        hitVec = vec;
//        lookVec = vec31;
//        entityBox = axisalignedbb;
//        eyePos = vec3;

        XiaojiaAddons.mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity(entity, vec));
    }

//    @SubscribeEvent
//    public void onRender(RenderWorldLastEvent event) {
//        if (entityBox == null || lookVec == null || eyePos == null || hitVec == null) return;
//        GuiUtils.drawBoundingBox(entityBox, 2, new Color(240, 220, 230, 80));
//        draw(eyePos, new Color(255, 0, 0, 170));
//        GuiUtils.drawLine(
//                eyePos,
//                eyePos.addVector(lookVec.xCoord * 4.5, lookVec.yCoord * 4.5, lookVec.zCoord * 4.5),
//                new Color(0, 0, 255), 3
//        );
//        draw(hitVec, new Color(0, 255, 0, 170));
//    }

    private void draw(Vec3 vec, Color color) {
        if (vec != null) {
            GuiUtils.drawBoxAtPos(
                    (float) vec.xCoord, (float) vec.yCoord, (float) vec.zCoord,
                    color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(),
                    0.01F, 0.01F, 0
            );
        }
    }
}
