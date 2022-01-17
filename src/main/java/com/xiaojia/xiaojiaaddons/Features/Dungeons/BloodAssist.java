package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector2i;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class BloodAssist {
    private static final HashMap<String, BloodMobInfo> infos = new HashMap<>();
    private static final HashSet<EntityArmorStand> skulls = new HashSet<>();
    private static final ArrayDeque<EntityArmorStand> newArmorStands = new ArrayDeque<>();
    private static Vector2i bloodTrunk = new Vector2i(-1, -1);

    private int spawnId = 0;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BloodAssist) return;
        if (!SkyblockUtils.isInDungeon()) return;
        for (EntityArmorStand skull : skulls) {
            String uuid = skull.getUniqueID().toString();
            BloodMobInfo info = infos.get(uuid);
//            GuiUtils.enableESP();
//            GuiUtils.drawBoxAtEntity(skull, 0, 0, 255, 255, 0.5F, 2F, 0);
//            GuiUtils.disableESP();

            if (info == null || info.endX < 0) continue;
            GuiUtils.enableESP();
            GuiUtils.drawBoundingBoxAtPos(
                    info.endX, info.endY + 1, info.endZ,
                    new Color(0, 255, 0), 0.5F, 1F
            );
            GuiUtils.disableESP();

            GuiUtils.enableESP();
            GuiUtils.drawLine(
                    (float) skull.posX, (float) skull.posY + 2, (float) skull.posZ,
                    info.endX, info.endY + 2, info.endZ,
                    new Color(255, 0, 0), 2
            );
            GuiUtils.disableESP();
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BloodAssist) return;
        if (event.entity instanceof EntityArmorStand) {
            newArmorStands.offerLast((EntityArmorStand) event.entity);
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.BloodAssist) return;
        if (!SkyblockUtils.isInDungeon()) return;

        ArrayList<String> toRemove = new ArrayList<>();

        for (EntityArmorStand skull : skulls) {
            float xSpeed = (float) (skull.posX - skull.lastTickPosX);
            float ySpeed = (float) (skull.posY - skull.lastTickPosY);
            float zSpeed = (float) (skull.posZ - skull.lastTickPosZ);
            String uuid = skull.getUniqueID().toString();
            BloodMobInfo info = infos.get(uuid);
            if (info != null && info.startTime < TimeUtils.curTime() - 5000) {
                setDead(uuid, info, 1);
                toRemove.add(uuid);
                continue;
            }
            // moving
            if (xSpeed != 0 || ySpeed != 0 || zSpeed != 0) {
                ChatLib.debug(String.format("%s speed: (%.2f %.2f %.2f)", uuid, xSpeed, ySpeed, zSpeed));
                if (info == null) {
                    info = new BloodMobInfo(TimeUtils.curTime(), skull.posX, skull.posY, skull.posZ);
                    infos.put(uuid, info);
                    ChatLib.debug("new info: " + uuid + ", " + info);
                }

                if (info.lastX == skull.posX && info.lastY == skull.posY && info.lastZ == skull.posZ) {
                    ItemStack itemStack = skull.getEquipmentInSlot(4);
                    // dead!
                    if (!info.isDead && (skull.isDead || itemStack == null || !itemStack.getDisplayName().endsWith("Head"))) {
                        setDead(uuid, info, 2);
                        toRemove.add(uuid);
                    }
                } else {
                    // moving!
                    long delta = TimeUtils.curTime() - info.startTime;
                    xSpeed = (float) ((skull.posX - info.startX) / delta);
                    ySpeed = (float) ((skull.posY - info.startY) / delta);
                    zSpeed = (float) ((skull.posZ - info.startZ) / delta);

                    long remainTime = (spawnId >= 4 ? 2950 : 4875) - delta;
                    info.endX = (float) skull.posX + remainTime * xSpeed;
                    info.endY = (float) (skull.posY + remainTime * ySpeed);
                    info.endZ = (float) (skull.posZ + remainTime * zSpeed);
                }
                info.lastX = skull.posX;
                info.lastY = skull.posY;
                info.lastZ = skull.posZ;
            }
        }
        skulls.removeIf(e -> e.isDead || (
                infos.get(e.getUniqueID().toString()) != null &&
                infos.get(e.getUniqueID().toString()).isDead
        ));
        toRemove.forEach(infos::remove);

        while (!newArmorStands.isEmpty()) {
            EntityArmorStand entityArmorStand = newArmorStands.pollFirst();
            ItemStack helm = entityArmorStand.getEquipmentInSlot(4);
            if (helm == null) continue;
            String name = ChatLib.removeFormatting(helm.getDisplayName()).trim();
            if (name.endsWith("Head")) addSkull(entityArmorStand);
        }
    }

    private void setDead(String uuid, BloodMobInfo info, int from) {
        info.isDead = true;
        spawnId++;
        ChatLib.debug("set dead: " + uuid + ", " + info + ", from: " + from);
    }

    public static void showBloodMobs() {
        ChatLib.chat("-------------------------------------------------------");
        for (EntityArmorStand entity: skulls) {
            ChatLib.chat("skulls: " + entity.getUniqueID().toString());
            if (entity.getEquipmentInSlot(4) != null) ChatLib.chat(entity.getEquipmentInSlot(4).getDisplayName());
        }
        for (Entity entity: getWorld().loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                ChatLib.chat("entity: " + entity.getUniqueID().toString());
                if (((EntityArmorStand) entity).getEquipmentInSlot(4) != null)
                    ChatLib.chat(((EntityArmorStand) entity).getEquipmentInSlot(4).getDisplayName());
                ChatLib.chat(String.format("pos: (%.2f %.2f %.2f)", entity.posX, entity.posY, entity.posZ));
            }
        }
        ChatLib.chat("-------------------------------------------------------");
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        newArmorStands.clear();
        bloodTrunk = new Vector2i(-1, -1);
        if (getWorld() == null) return;
        spawnId = 0;
        infos.clear();
        skulls.clear();
        try {
            for (Entity entity : getWorld().loadedEntityList) {
                if (!(entity instanceof EntityArmorStand)) continue;
                newArmorStands.offerLast((EntityArmorStand) entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSkull(EntityArmorStand entityArmorStand) {
        ItemStack helm = entityArmorStand.getEquipmentInSlot(4);
        if (helm == null) return;
        String name = ChatLib.removeFormatting(helm.getDisplayName()).trim();

        if (!bloodTrunk.equals(new Vector2i(-1, -1))) {
            if (new Vector2i(entityArmorStand.chunkCoordX / 2, entityArmorStand.chunkCoordZ / 2).equals(bloodTrunk)) {
                skulls.add(entityArmorStand);
                ChatLib.debug("added " + entityArmorStand.getUniqueID().toString());
            }
        } else {
            if (name.endsWith(getPlayer().getName() + "'s Head") || name.endsWith(getPlayer().getName() + "' Head")) {
                bloodTrunk = new Vector2i(entityArmorStand.chunkCoordX / 2, entityArmorStand.chunkCoordZ / 2);
                ChatLib.debug("Head: " + name + ", trunk: " + bloodTrunk);
                for (Entity entity : getWorld().loadedEntityList) {
                    if (!(entity instanceof EntityArmorStand)) continue;
                    helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                    if (helm == null) continue;
                    name = ChatLib.removeFormatting(helm.getDisplayName()).trim();
                    if (name.endsWith("Head")) addSkull((EntityArmorStand) entity);
                }
            }
        }
    }

    public static class BloodMobInfo {
        public long startTime = -1;
        public double startX = -1;
        public double startY = -1;
        public double startZ = -1;

        public double lastX = -1;
        public double lastY = -1;
        public double lastZ = -1;
        public boolean isDead = false;

        public float endX = -1;
        public float endY = -1;
        public float endZ = -1;

        public BloodMobInfo(long startTime, double startX, double startY, double startZ) {
            this.startTime = startTime;
            this.startX = startX;
            this.startY = startY;
            this.startZ = startZ;
            this.isDead = false;
        }

        public String toString() {
            return String.format("%d (%.2f %.2f %.2f) (%.2f %.2f %.2f) ",
                    startTime, startX, startY, startZ,
                    lastX, lastY, lastZ
            ) + isDead;
        }
    }
}
