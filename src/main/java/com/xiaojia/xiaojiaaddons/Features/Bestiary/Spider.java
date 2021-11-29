package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Spider {
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
    private ArrayList<EntityInfo> renderEntities = new ArrayList<>();
    private ArrayList<Tuple3<Integer, Integer, Integer>> bestShadowFuryPoints = new ArrayList<>();
    private boolean shadowFuryWarnedInThisLobby = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!SkyblockUtils.isInSpiderDen()) return;
        ArrayList<EntityInfo> newEntities = new ArrayList<>();
        ArrayList<Entity> shadowFuryEntities = new ArrayList<>();
        try {
            List<Entity> list = getWorld().loadedEntityList;
            for (Entity entity : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("entity", entity);
                if (entity instanceof EntityArmorStand) {
                    hashMap.put("yOffset", 1.0F);
                    hashMap.put("drawString", EntityInfo.EnumDraw.DRAW_KIND);
                    hashMap.put("width", 1.0F);
                    hashMap.put("fontColor", 0x33ff33);
                    hashMap.put("isFilled", true);
                    String name = ChatLib.removeFormatting(entity.getName());
                    if (name.contains(ARACHNEKEEPER_STRING)) {
                        hashMap.put("kind", ARACHNEKEEPER_STRING);
                        newEntities.add(new EntityInfo(hashMap));
                        if (true) {
                            GuiUtils.showTitle(ARACHNEKEEPER_SHOW, "", 0, 5, 0);
                        }
                    } else if (name.contains(BROODMOTHER_STRING)) {
                        hashMap.put("kind", BROODMOTHER_STRING);
                        newEntities.add(new EntityInfo(hashMap));
                        if (true) {
                            GuiUtils.showTitle(BROODMOTHER_SHOW, "", 0, 5, 0);
                        }
                    }

                    double ex = getX(entity), ey = getY(entity), ez = getZ(entity);
                    if (!(ex < -260 || ex > -204 || ey < 59 || ey > 77 || ez < -350 || ez > -300) &&
                            !entity.getName().contains("Rick") && !entity.getName().contains("CLICK")) {
                        shadowFuryEntities.add(entity);
                    }
                }
            }

            boolean hasOutsideSpider = false;
            int bestCnt = 100000;
            bestShadowFuryPoints = new ArrayList<>();
            for (int[] ints : shadowFuryPointsRange) {
                int temp;
                int sx = ints[0], sy = ints[1], sz = ints[2];
                int tx = ints[3], ty = ints[4], tz = ints[5];
                if (sx >= tx) {
                    temp = sx;
                    sx = tx;
                    tx = temp;
                }
                if (sy >= ty) {
                    temp = sy;
                    sy = ty;
                    ty = temp;
                }
                if (sz >= tz) {
                    temp = sz;
                    sz = tz;
                    tz = temp;
                }
                for (int x = sx; x <= tx; x++) {
                    for (int y = sy; y <= ty; y++) {
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
                                if (insideCnt <= bestCnt) {
                                    bestShadowFuryPoints.add(new Tuple3<>(x, y, z));
                                }
                            }
                        }
                    }
                }
            }
            if (true && hasOutsideSpider && !shadowFuryWarnedInThisLobby) {
                shadowFuryWarnedInThisLobby = true;
                ChatLib.chat("&5Be aware: Kill the outside mobs before shadow fury!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ChatLib.chat("owo?");
        }
        renderEntities = newEntities;
        if (XiaojiaAddons.isDebug()) ChatLib.chat(renderEntities.size() + ", onTick");
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!SkyblockUtils.isInSpiderDen()) return;
        if (XiaojiaAddons.isDebug()) ChatLib.chat(renderEntities.size() + ", onRenderWorld");
        for (EntityInfo entityInfo : renderEntities) {
            // TODO: abstract
            EntityInfo.EnumDraw draw = entityInfo.getDrawString();
            Entity entity = entityInfo.getEntity();
            String kind = entityInfo.getKind();
            String name = entity.getName();
            String drawString = "";
            boolean filled = entityInfo.isFilled();  // box filled / string

            // string
            if (draw == EntityInfo.EnumDraw.DRAW_KIND) drawString = entityInfo.getKind();
            else if (draw == EntityInfo.EnumDraw.DRAW_ARMORSTAND_HP)
                drawString = DisplayUtils.getHPDisplayFromArmorStandName(name, kind);
            else if (draw == EntityInfo.EnumDraw.DRAW_ENTITY_HP)
                drawString = DisplayUtils.hpToString(((EntityLivingBase) entity).getHealth());
            GuiUtils.drawString(drawString, getX(entity), getY(entity), getZ(entity), entityInfo.getFontColor(), false, entityInfo.getScale(), true);
            if (XiaojiaAddons.isDebug()) ChatLib.chat(filled + ", " + drawString);

            // warn
            long curTime = TimeUtils.curTime();
            String curServer = SkyblockUtils.getCurrentServer();
            if (kind.contains("Keeper")) {
                long lastDeathWarn = lastDeathWarnTime.getOrDefault(curServer, 0L);
                long lastWarn = lastWarnTime.getOrDefault(curServer, 0L);
                if (name.contains("§e0§f/§a")) {  // keeper no health, died
                    if (curTime > lastDeathWarn + 60 * 1000 && true) {
                        lastDeathWarnTime.put(curServer, curTime);
                        ChatLib.chat("&4&lKeeper died!");
                    }
                } else if (curTime > lastWarn + 60 * 1000 && true) {
                    lastWarnTime.put(curServer, curTime);
                    ChatLib.chat(ARACHNEKEEPER_SHOW + " is in this lobby!");
                }
            }

            // esp box
            int r = entityInfo.getR(), g = entityInfo.getG(), b = entityInfo.getB();
            float width = entityInfo.getWidth(), height = entityInfo.getHeight();
            float yOffset = entityInfo.getyOffset();
            if (!filled) GuiUtils.drawBoxAtEntity(entity, r, g, b, 255, width, height, yOffset);
            else GuiUtils.drawFilledBoxAtEntity(entity, r, g, b, 100, width, height, yOffset);
        }
        if (true) {
            bestShadowFuryPoints.forEach(pos -> {
                int x = pos._1(), y = pos._2(), z = pos._3();
                GuiUtils.drawBoxAtBlock(x, y, z, 0, 255, 0, 100, 1, 1, 0.01F);
            });
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        shadowFuryWarnedInThisLobby = false;
    }
}
