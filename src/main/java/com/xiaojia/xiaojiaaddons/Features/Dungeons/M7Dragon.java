package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
            new BlockPos(56, 14, 125), "&cRed Dragon&r: ",
            new Color(0.5019608f, 0.0f, 0.5019608f), "dragon_soul.png",
            "Corrupted Purple Relic"
    );
    private static final DragonInfo blueDragon = new DragonInfo(
            new BlockPos(84, 14, 94), "&5Purple Dragon&r: ",
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
    private static final HashMap<EntityDragon, DragonInfo> dragonsMap = new HashMap<>();
    private static final HashSet<BlockPos> done = new HashSet<>();
    private final Display display = new Display();

    private static long lastCheck = 0;

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

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (getWorld() == null) return;
        if (TimeUtils.curTime() - lastCheck > Configs.DragonCheckCD) {
            lastCheck = TimeUtils.curTime();
            new Thread(() -> {
                HashMap<String, ArrayList<EntityArmorStand>> relics = new HashMap<>();
                StringBuilder log = new StringBuilder();
                for (Entity entity : getWorld().loadedEntityList) {
                    if (entity instanceof EntityArmorStand) {
                        ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                        if (helm == null) continue;
                        String helmString = helm.getDisplayName();
                        if (helmString.contains("Corrupted ")) {
                            relics.putIfAbsent(helmString, new ArrayList<>());
                            relics.get(helmString).add((EntityArmorStand) entity);
                        }
                    }
                }
                HashMap<EntityDragon, DragonInfo> newInfos = new HashMap<>();
                boolean shouldPrintLog = false;
                for (String name : relics.keySet()) {
                    log.append("relic: ").append(name).append("\n");
                    ArrayList<EntityArmorStand> armorStands =relics.get(name);
                    if (armorStands.size() == 8) {
                        for (EntityDragon entity : dragonsMap.keySet()) {
                            if (entity.getHealth() <= 0 || entity.isDead) continue;
                            int cnt = 0;
                            log.append(String.format("checking entity dragon: %.2f %.2f %.2f", getX(entity), getY(entity), getZ(entity))).append("\n");
                            for (EntityArmorStand relic : armorStands) {
                                double dis = relic.getDistanceSqToEntity(entity);
                                log.append(String.format("relic: %.2f %.2f %.2f, %.2f", getX(relic), getY(relic), getZ(relic), dis)).append("\n");
                                if (dis > 3.65 && dis < 3.85) cnt++;
                            }
                            DragonInfo dragonInfo = getDragonInfoFromHelmName(name);
                            if (cnt == 8) {
                                if (newInfos.containsKey(entity)) {
                                    log.append(String.format("wtf this dragon is counted as %s and %s", newInfos.get(entity).headName, name)).append("\n");
                                    shouldPrintLog = true;
                                }
                                newInfos.put(entity, dragonInfo);
                            }
                        }
                    }
                }
                if (shouldPrintLog) {
                    System.err.println(log);
                    ChatLib.chat("&cAn error occurred in M7 Dragon Color Check. Please &c&l/xj report.");
                }
                dragonsMap.putAll(newInfos);
            }).start();
        }
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
        if (!Checker.enabled) return;
        if (!Configs.dragonInfoDisplay) return;
        if (getWorld() == null) return;
        display.clearLines();
        display.setRenderLoc(Configs.dragonInfoX, Configs.dragonInfoY);
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
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityDragon) {
                if (!dragonsMap.containsKey(entity)) continue;

                String hpPrefix = "&a";
                double hp = ((EntityDragon) entity).getHealth();
                if (hp <= 100000000) hpPrefix = "&c";
                else if (hp <= 300000000) hpPrefix = "&6";
                String hpString = hpPrefix + DisplayUtils.hpToString(hp, true);

                BlockPos blockPos = dragonsMap.get(entity).blockPos;
                double dis = Math.sqrt(entity.getDistanceSq(blockPos));
                String disPrefix = "&a";
                if (hp <= 1 && dis < 15) done.add(blockPos);

                float scale = getScale((float) dis);
                if (dis < 15) disPrefix = "&c";
                else if (dis < 30) disPrefix = "&6";

                String str = dragonsMap.get(entity).prefix + hpString + "&r, " + disPrefix + String.format("%.2f", dis);
                if (done.contains(blockPos)) {
                    str = dragonsMap.get(entity).prefix + "&cDONE";
                    scale = 0.9F;
                }

                DisplayLine line = new DisplayLine(str);
                line.setScale(Configs.dragonInfoScale / 20F * scale);
                display.addLine(line);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        dragonsMap.clear();
        done.clear();
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
}

