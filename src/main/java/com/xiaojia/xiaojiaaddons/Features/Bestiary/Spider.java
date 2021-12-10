package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Spider extends RenderEntityESP {
    private static final String ARACHNEKEEPER_STRING = "Arachne's Keeper";
    private static final String ARACHNEKEEPER_SHOW = "&c&lArachne's Keeper";
    private static final String BROODMOTHER_STRING = "Brood Mother";
    private static final String BROODMOTHER_SHOW = "&5&lBrood Mother";
    private static final int[][] shadowFuryPointsRange = new int[][]{
            {-237, 68, -320, -243, 63, -326},   // 6 * 5 * 6
            {-225, 60, -319, -232, 62, -324},   // 7 * 2 * 5
            {-212, 59, -314, -215, 63, -312},   // 3 * 3 * 2
            {-218, 59, -319, -221, 64, -315}    // 3 * 5 * 4
    };
    private final HashMap<String, Long> lastDeathWarnTime = new HashMap<>();
    private final HashMap<String, Long> lastWarnTime = new HashMap<>();
    private ArrayList<Tuple3<Integer, Integer, Integer>> bestShadowFuryPoints = new ArrayList<>();
    private boolean shadowFuryWarnedInThisLobby = false;

    @SubscribeEvent
    public void onTickShadowFury(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInSpiderDen() || !Configs.SpiderDenShadowfuryPoint) return;

        List<Entity> shadowFuryEntities = new ArrayList<>();
        List<Entity> list = getWorld().loadedEntityList;
        for (Entity entity : list) {
            if (entity instanceof EntityArmorStand) {
                double ex = getX(entity), ey = getY(entity), ez = getZ(entity);
                if (!(ex < -260 || ex > -204 || ey < 59 || ey > 77 || ez < -350 || ez > -300) &&
                        !entity.getName().contains("Rick") && !entity.getName().contains("CLICK"))
                    shadowFuryEntities.add(entity);
            }
        }

        boolean hasOutsideSpider = false;
        int bestCnt = 100000;
        bestShadowFuryPoints = new ArrayList<>();
        for (int[] ints : shadowFuryPointsRange) {
            int sx = Math.min(ints[0], ints[3]), sy = Math.min(ints[1], ints[4]), sz = Math.min(ints[2], ints[5]);
            int tx = Math.max(ints[0], ints[3]), ty = Math.max(ints[1], ints[4]), tz = Math.max(ints[2], ints[5]);
            for (int x = sx; x <= tx; x++)
                for (int y = sy; y <= ty; y++)
                    for (int z = sz; z <= tz; z++) {
                        int insideCnt = 0;
                        for (Entity entity : shadowFuryEntities) {
                            double ex = getX(entity), ey = getY(entity), ez = getZ(entity);
                            if ((ez > -335 && ez < -320) && (ey > 59 && ey < 77) && (ex > -248 && ex < -216)) {
                                hasOutsideSpider = true;
                            } else if (MathUtils.distanceSquaredFromPoints(x + 0.5, y, z + 0.5, ex, ey - 1.2, ez) < 144) {
                                // shadow fury radius: 12; -0.5 for armorstand/mob offset; + 0.5 for block center
                                insideCnt++;
                            }
                        }
                        if (insideCnt > 0) {
                            if (insideCnt < bestCnt) {
                                bestCnt = insideCnt;
                                bestShadowFuryPoints = new ArrayList<>();
                            }
                            if (insideCnt <= bestCnt)
                                bestShadowFuryPoints.add(new Tuple3<>(x, y, z));
                        }
                    }
        }

        if (hasOutsideSpider && !shadowFuryWarnedInThisLobby) {
            shadowFuryWarnedInThisLobby = true;
            ChatLib.chat("&5Be aware: Kill the outside mobs before shadow fury!");
        }
    }

    @SubscribeEvent
    public void onRenderWorldShadowFury(RenderWorldLastEvent event) {
        if (Configs.SpiderDenShadowfuryPoint) {
            bestShadowFuryPoints.forEach(pos -> {
                int x = pos._1(), y = pos._2(), z = pos._3();
                GuiUtils.drawBoxAtBlock(x, y, z, 0, 255, 0, 100, 1, 1, 0.01F);
            });
        }
    }

    @Override
    public void dealWithEntityInfo(EntityInfo entityInfo) {
        // warn
        long curTime = TimeUtils.curTime();
        String kind = entityInfo.getKind();
        String name = entityInfo.getEntity().getName();
        String curServer = SkyblockUtils.getCurrentServer();
        if (kind.contains("Keeper")) {
            long lastDeathWarn = lastDeathWarnTime.getOrDefault(curServer, 0L);
            long lastWarn = lastWarnTime.getOrDefault(curServer, 0L);
            if (name.contains("§e0§f/§a")) {  // keeper no health, died
                if (curTime > lastDeathWarn + 60 * 1000 && Configs.ArachneKeeperDeathWarn) {
                    lastDeathWarnTime.put(curServer, curTime);
                    ChatLib.chat("&4&lKeeper died!");
                }
            } else if (curTime > lastWarn + 60 * 1000 && Configs.ArachneKeeperWarnMode == 1) {
                lastWarnTime.put(curServer, curTime);
                ChatLib.chat(ARACHNEKEEPER_SHOW + " is in this lobby!");
            }
        }

        // title
        if (Configs.ArachneKeeperWarnMode == 2 && kind.contains(ARACHNEKEEPER_STRING))
            GuiUtils.showTitle(ARACHNEKEEPER_SHOW, "", 0, 5, 0);
        if (Configs.BroodMotherWarn && kind.contains(BROODMOTHER_STRING))
            GuiUtils.showTitle(BROODMOTHER_SHOW, "", 0, 5, 0);
    }

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        if (!Checker.enabled || !SkyblockUtils.isInSpiderDen()) return false;
        String kind = entityInfo.getKind();
        return Configs.ArachneKeeperDisplayBox && kind.contains(ARACHNEKEEPER_STRING) ||
                Configs.BroodMotherDisplayBox && kind.contains(BROODMOTHER_STRING);
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        if (!Checker.enabled || !SkyblockUtils.isInSpiderDen()) return false;
        String kind = entityInfo.getKind();
        return Configs.ArachneKeeperDisplayName && kind.contains(ARACHNEKEEPER_STRING) ||
                Configs.BroodMotherDisplayName && kind.contains(BROODMOTHER_STRING);
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityArmorStand)) return null;
        HashMap<String, Object> hashMap = new HashMap<>();
        String name = ChatLib.removeFormatting(entity.getName());
        hashMap.put("entity", entity);
        hashMap.put("yOffset", 1.0F);
        hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
        hashMap.put("width", 1.0F);
        hashMap.put("fontColor", 0x33ff33);
        hashMap.put("isFilled", true);
        if (name.contains(ARACHNEKEEPER_STRING)) {
            hashMap.put("kind", ARACHNEKEEPER_STRING);
            return new EntityInfo(hashMap);
        } else if (name.contains(BROODMOTHER_STRING)) {
            hashMap.put("kind", BROODMOTHER_STRING);
            return new EntityInfo(hashMap);
        }
        return null;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!Checker.enabled) return;
        shadowFuryWarnedInThisLobby = false;
    }
}
