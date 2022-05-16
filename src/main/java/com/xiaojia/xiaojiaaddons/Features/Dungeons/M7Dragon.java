package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class M7Dragon {
    private static final DragonInfo redDragon = new DragonInfo(
            new BlockPos(27, 14, 59), "&cRed Dragon&r: ",
            new Color(1F, 0F, 0F), "dragon_power.png",
            "Corrupted Red Relic"
    );
    private static final DragonInfo greenDragon = new DragonInfo(
            new BlockPos(27, 14, 94), "&aGreen Dragon&r: ",
            new Color(0F, 1F, 0F), "dragon_apex.png",
            "Corrupted Green Relic"
    );
    private static final DragonInfo purpleDragon = new DragonInfo(
            new BlockPos(56, 14, 125), "&5Purple Dragon&r: ",
            new Color(0.5019608f, 0.0f, 0.5019608f), "dragon_soul.png",
            "Corrupted Purple Relic"
    );
    private static final DragonInfo blueDragon = new DragonInfo(
            new BlockPos(84, 14, 94), "&bBlue Dragon&r: ",
            new Color(0.0f, 1.0f, 1.0f), "dragon_ice.png",
            "Corrupted Blue Relic"
    );
    private static final DragonInfo orangeDragon = new DragonInfo(
            new BlockPos(85, 14, 56), "&6Orange Dragon&r: ",
            new Color(1.0f, 0.64705884f, 0.0f), "dragon_flame.png",
            "Corrupted Orange Relic"
    );
    private static final ArrayList<DragonInfo> dragonInfos = new ArrayList<DragonInfo>() {{
        add(redDragon);
        add(greenDragon);
        add(purpleDragon);
        add(blueDragon);
        add(orangeDragon);
    }};
    private static final ConcurrentHashMap<EntityDragon, DragonInfo> dragonsMap = new ConcurrentHashMap<>();
    private static final HashSet<BlockPos> done = new HashSet<>();
    private static final Deque<S2APacketParticles> particles = new ArrayDeque<>();
    private static final HashMap<DragonInfo, Long> lastWarn = new HashMap<>();
    private static long lastCheck = 0;
    private final Display display = new Display();

    public M7Dragon() {
        display.setShouldRender(true);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("left");
    }

    private static DragonInfo getDragonInfoFromHP(float health) {
        if (health > 300000000) return greenDragon;
        if (health > 100000000) return orangeDragon;
        return redDragon;
    }

    private static DragonInfo getDragonInfoFromHelmName(String name) {
        for (DragonInfo info : dragonInfos) if (info.headName.equals(name)) return info;
        return null;
    }

    public static void onSpawnDragon(EntityDragon entity) {
        if (!SkyblockUtils.isInDungeon()) return;
        double distance = 1e9;
        DragonInfo dragonInfo = null;
        for (DragonInfo info : dragonInfos) {
            BlockPos pos = info.blockPos;
            double dis = entity.getDistanceSq(pos);
            if (dis < distance) {
                distance = dis;
                dragonInfo = info;
            }
        }
        if (dragonInfo == null) return;
        ChatLib.debug("spawning drag: " + dragonInfo.prefix + ", lastwarn diff " +
                (TimeUtils.curTime() - lastWarn.getOrDefault(dragonInfo, 0L))
        );
        dragonsMap.put(entity, dragonInfo);
    }

    public static void getEntityTexture(EntityDragon entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (!Checker.enabled) return;
        if (Configs.ReplaceDragonTexture == 0) return;
        ResourceLocation ret;
        String location;
        if (Configs.ReplaceDragonTexture == 1) {
            if (!dragonsMap.containsKey(entity)) return;
            location = dragonsMap.get(entity).textureName;
        } else {
            location = getDragonInfoFromHP(entity.getHealth()).textureName;
        }
        ret = new ResourceLocation(XiaojiaAddons.MODID + ":" + location);
        cir.setReturnValue(ret);
    }

    private static float getScale(float distance) {
        if (distance > 30) return 1;
        if (distance > 15) return 1 + (30 - distance) / 15 * 0.1F;
        return 1.3F - distance / 15 * 0.2F;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (getWorld() == null) return;
        if (TimeUtils.curTime() - lastCheck > Configs.DragonCheckCD) {
            lastCheck = TimeUtils.curTime();
            new Thread(() -> {
                HashMap<String, ArrayList<EntityArmorStand>> relics = new HashMap<>();
                ArrayList<EntityDragon> dragons = new ArrayList<>();
                StringBuilder log = new StringBuilder();
                for (Entity entity : EntityUtils.getEntities()) {
                    if (entity instanceof EntityArmorStand) {
                        ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                        if (helm == null) continue;
                        String helmString = ChatLib.removeFormatting(helm.getDisplayName());
                        if (helmString.contains("Corrupted ")) {
                            relics.putIfAbsent(helmString, new ArrayList<>());
                            relics.get(helmString).add((EntityArmorStand) entity);
                        }
                    }
                    if (entity instanceof EntityDragon) {
                        dragons.add((EntityDragon) entity);
                    }
                }
                HashMap<EntityDragon, DragonInfo> newInfos = new HashMap<>();
                boolean shouldPrintLog = false;
                for (String name : relics.keySet()) {
                    log.append("relic: ").append(name).append("\n");
                    ArrayList<EntityArmorStand> armorStands = relics.get(name);
                    for (EntityDragon entity : dragons) {
                        int cnt = 0;
                        log.append(String.format("checking entity dragon: %.2f %.2f %.2f", getX(entity), getY(entity), getZ(entity))).append("\n");
                        for (EntityArmorStand relic : armorStands) {
                            double dis = Math.sqrt(relic.getDistanceSqToEntity(entity));
                            log.append(String.format("relic: %.2f %.2f %.2f, %.2f", getX(relic), getY(relic), getZ(relic), dis)).append("\n");
                            if (dis > 3 && dis < 4) cnt++;
                        }
                        DragonInfo dragonInfo = getDragonInfoFromHelmName(name);
                        if (dragonInfo == null) shouldPrintLog = true;
                        else if (cnt >= 8) {
                            if (newInfos.containsKey(entity)) {
                                newInfos.remove(entity);
                                log.append(String.format("wtf this dragon is counted as %s and %s", newInfos.get(entity).headName, name)).append("\n");
                                shouldPrintLog = true;
                            } else {
                                newInfos.put(entity, dragonInfo);
                            }
                        }
                    }
                }
//                if (SessionUtils.isDev())
//                    System.err.println(log + "\n");
                dragonsMap.putAll(newInfos);
            }).start();
        }
    }

    @SubscribeEvent
    public void onReceive(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (!SkyblockUtils.getDungeon().contains("7")) return;
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() != EnumParticleTypes.SMOKE_LARGE) return;
            if (packet.getYCoordinate() < 10 || packet.getYCoordinate() > 28) return;
            DragonInfo res = null;
            for (DragonInfo info : dragonInfos) {
                if (info.blockPos.distanceSq(packet.getXCoordinate(),
                        info.blockPos.getY(), packet.getZCoordinate()) < 1) {
                    res = info;
                }
            }
            if (res == null) return;

            if (TimeUtils.curTime() - lastWarn.getOrDefault(res, 0L) > 6000) {
                lastWarn.put(res, TimeUtils.curTime());
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ShowStatueBox) return;
        if (!SkyblockUtils.isInDungeon() || !SkyblockUtils.getDungeon().equals("M7")) return;
        for (DragonInfo info : dragonInfos) {
            BlockPos blockPos = info.blockPos;
            AxisAlignedBB box = new AxisAlignedBB(
                    blockPos.add(-12, -2, -12),
                    blockPos.add(12, 15, 12)
            );
            GuiUtils.drawBoundingBox(box, 5, info.color);
        }
    }

    @SubscribeEvent
    public void renderLivingPost(TickEndEvent event) {
        display.clearLines();
        display.setRenderLoc(Configs.dragonInfoX, Configs.dragonInfoY);
        if (!Checker.enabled) return;
        if (!Configs.dragonInfoDisplay) return;
        if (getWorld() == null) return;
        if (Configs.dragonInfoTest) {
            display.addLine("dragon Info is here");
            DisplayLine line1 = new DisplayLine("&aGreen Dragon&r: &a400M, &c7");
            line1.setScale(Configs.dragonInfoScale / 20F * getScale(7));
            display.addLine(line1);

            DisplayLine line2 = new DisplayLine("&cRed Dragon&r: &c512K, &a40");
            line2.setScale(Configs.dragonInfoScale / 20F * getScale(45));
            display.addLine(line2);

            DisplayLine line3 = new DisplayLine("&6Orange Dragon&r: &cDONE");
            line3.setScale(Configs.dragonInfoScale / 20F * 0.9F);
            display.addLine(line3);

            DisplayLine line4 = new DisplayLine("&5Purple Dragon&r: &6150M, &620");
            line4.setScale(Configs.dragonInfoScale / 20F * getScale(20));
            display.addLine(line4);

            DisplayLine line5 = new DisplayLine("&bCyan Dragon&r: &6200M, &c13");
            line5.setScale(Configs.dragonInfoScale / 20F * getScale(13));
            display.addLine(line5);
        }
        for (Entity entity : EntityUtils.getEntities()) {
            if (entity instanceof EntityDragon) {
                if (!dragonsMap.containsKey(entity)) continue;
                DragonInfo info = dragonsMap.get(entity);

                String hpPrefix = "&a";
                double hp = ((EntityDragon) entity).getHealth();
                if (hp <= 100000000) hpPrefix = "&c";
                else if (hp <= 300000000) hpPrefix = "&6";
                String hpString = hpPrefix + DisplayUtils.hpToString(hp, true);

                BlockPos blockPos = info.blockPos;
                double dis = Math.sqrt(entity.getDistanceSq(blockPos));
                String disPrefix = "&a";
                if (hp <= 1 && dis < 15) done.add(blockPos);

                float scale = getScale((float) dis);
                if (dis < 15) disPrefix = "&c";
                else if (dis < 30) disPrefix = "&6";

                String str = info.prefix + hpString + "&r, " + disPrefix + String.format("%.2f", dis);
                if (done.contains(blockPos)) {
                    str = info.prefix + "&cDONE";
                    scale = 0.9F;
                }

                DisplayLine line = new DisplayLine(str);
                line.setScale(Configs.dragonInfoScale / 20F * scale);
                display.addLine(line);
            }
        }
        for (DragonInfo info : lastWarn.keySet()) {
            long last = lastWarn.getOrDefault(info, 0L);
            long diff = TimeUtils.curTime() - last;
            diff = 5000 - diff;
            if (diff > 0 && diff < 5000) {
                String color = diff < 1500 ? "&c" : diff < 3000 ? "&6" : "&a";
                String str = info.prefix + color + String.format("%.2fs", diff / 1000F);
                DisplayLine line = new DisplayLine(str);
                line.setScale(Configs.dragonInfoScale / 20F * 1.5F);
                display.addLine(line);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        dragonsMap.clear();
        done.clear();
        synchronized (particles) {
            particles.clear();
        }
    }
}

class DragonInfo {
    BlockPos blockPos;
    String prefix;
    Color color;
    String textureName;
    String headName;

    public DragonInfo(BlockPos blockPos, String prefix, Color color, String textureName, String headName) {
        this.blockPos = blockPos;
        this.prefix = prefix;
        this.color = color;
        this.textureName = textureName;
        this.headName = headName;
    }

    public int hashCode() {
        return prefix.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof  DragonInfo) {
            return o.hashCode() == hashCode();
        }
        return false;
    }
}

