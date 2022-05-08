package com.xiaojia.xiaojiaaddons.Features.Nether.Dojo;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Mastery {
    private final static ConcurrentHashMap<BlockPos, Long> countDown = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<BlockPos, Integer> officialCountDown = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Integer, BlockPos> link = new ConcurrentHashMap<>();

    public static void clear() {
        officialCountDown.clear();
        countDown.clear();
        link.clear();
    }

    public static void onEnter() {
        if (!Checker.enabled) return;
        if (!Configs.MasteryHelper) return;
        ControlUtils.setHeldItemIndex(0);
        new Thread(() -> {
            try {
                Thread.sleep(4000);
                ControlUtils.holdRightClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onTickCheck(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.MasteryHelper) return;
        if (DojoUtils.getTask() != EnumDojoTask.MASTERY) return;
        List<Entity> entities = new ArrayList<>(getWorld().loadedEntityList);
        HashMap<Integer, BlockPos> tempLink = new HashMap<>();
        for (Entity entity : entities) {
            String name = entity.getName();
            Pattern pattern = Pattern.compile("^\u00a7(\\w)\u00a7l([\\d:]+)$");
            Matcher matcher = pattern.matcher(name);
            if (!matcher.find()) continue;
            String color = matcher.group(1);
            double time = Double.parseDouble(matcher.group(2).replaceAll(":", "."));
            switch (color) {
                case "c":
                    break;
                case "e":
                    time += 0.5;
                    break;
                case "a":
                    time += 3.5;
                    break;
                default:
                    ChatLib.debug(color + "");
                    break;
            }
            int delta = (int) (time * 1000);
            BlockPos pos = countDown.keySet().stream().min(Comparator.comparing(x -> getDis(entity, x))).orElse(null);
            if (pos == null) continue;
            officialCountDown.put(pos, delta);
            tempLink.put(entity.getEntityId(), pos);
        }
        link.clear();
        link.putAll(tempLink);
    }

    public static void printLog() {
        System.err.println("Player at: " + MathUtils.getPosString(getPlayer()));
        System.err.println("Mastery count down set:");
        for (BlockPos pos : countDown.keySet()) {
            System.err.println(pos);
        }
        System.err.println("Mastery entities:");
        List<Entity> entities = new ArrayList<>(getWorld().loadedEntityList);
        for (Entity entity : entities) {
            String name = entity.getName();
            Pattern pattern = Pattern.compile("^\u00a7(\\w)\u00a7l([\\d:]+)$");
            Matcher matcher = pattern.matcher(name);
            if (!matcher.find()) continue;
            String color = matcher.group(1);
            double time = Double.parseDouble(matcher.group(2).replaceAll(":", "."));
            System.err.println("Entity " + color + " " + time + " at " + MathUtils.getPosString(entity));
        }
    }

    public static double getDis(Entity entity, BlockPos pos) {
        double dis = Math.sqrt(MathUtils.distanceSquareFromPlayer(entity));
        double dx = (entity.posX - getX(getPlayer())) / dis;
        double dz = (entity.posZ - getZ(getPlayer())) / dis;
        return entity.getDistanceSq(pos.getX() + 0.5F - dx, pos.getY() + 3, pos.getZ() + 0.5F - dz);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.MasteryHelper) return;
        if (DojoUtils.getTask() != EnumDojoTask.MASTERY) return;
        long cur = TimeUtils.curTime();
        for (BlockPos pos : countDown.keySet()) {
            int delta = (int) (7000 - (cur - countDown.get(pos)));
            Color color = getColorFromDelta(delta);
            GuiUtils.drawString(
                    String.format("%.2f s", delta / 1000F),
                    pos.getX() + 0.5F, pos.getY() + 0.7F, pos.getZ() + 0.5F,
                    color.getRGB(),
                    1.5F, true
            );

            delta = officialCountDown.getOrDefault(pos, -1);
            if (delta == -1) continue;
            color = getColorFromDelta(delta);
            GuiUtils.drawString(
                    String.format("%.2f s", delta / 1000F),
                    pos.getX() + 0.5F, pos.getY() + 1.7F, pos.getZ() + 0.5F,
                    color.getRGB(),
                    1.5F, true
            );
        }
        GuiUtils.enableESP();
        for (Entity entity : getWorld().loadedEntityList) {
            int id = entity.getEntityId();
            if (!link.containsKey(id)) continue;
            BlockPos pos = link.get(id);

            float dis = (float) Math.sqrt(MathUtils.distanceSquareFromPlayer(entity));
            float dx = (float) ((entity.posX - getX(getPlayer())) / dis);
            float dz = (float) ((entity.posZ - getZ(getPlayer())) / dis);
            GuiUtils.drawLine(
                    (float) entity.posX + dx, (float) entity.posY + 3F, (float) entity.posZ + dz,
                    pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                    new Color(255, 0, 0), 2
            );
        }
        GuiUtils.disableESP();
    }

    private static Color getColorFromDelta(int delta) {
        Color color = new Color(0, 0, 255);
        if (delta >= 3500 && delta <= 7000) color = new Color((7000 - delta) * 255 / 3500, 255, 0);
        else if (delta >= 500 && delta < 3500) color = new Color(255, 255 - (3500 - delta) * 255 / 3000, 0);
        else if (delta < 500 && delta > 0) color = new Color(255, 0, 0);
        else ChatLib.debug(delta + "");
        return color;
    }

    private static long lastRelease = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.MasteryAutoTurn) return;
        BlockPos facing = null;
        long min = 0;
        long cur = TimeUtils.curTime();

        if (Configs.MasteryMode == 1) {
            facing = officialCountDown.keySet().stream().min(Comparator.comparing(officialCountDown::get)).orElse(null);
            if (facing != null) min = officialCountDown.get(facing);
        }

        if (facing == null) {
            facing = countDown.keySet().stream().min(Comparator.comparing(countDown::get)).orElse(null);
            if (facing != null) min = (7000 - (cur - countDown.get(facing)));
        }

        if (facing == null) return;
        ControlUtils.face(facing.getX() + 0.5, facing.getY() + 1.4, facing.getZ() + 0.5);
        if (!Configs.MasteryAutoRelease) return;
        if (min < Configs.MasteryAutoReleaseCD && cur - lastRelease > 900) {
            lastRelease = cur;
            new Thread(() -> {
                try {
                    ControlUtils.releaseRightClick();
                    Thread.sleep(40);
                    ControlUtils.holdRightClick();
                } catch (Exception e) {
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (!Checker.enabled) return;
        if (DojoUtils.getTask() != EnumDojoTask.MASTERY) return;
        if (event.position.getX() < getX(getPlayer()) - 20 || event.position.getX() > getX(getPlayer()) + 20) return;
        if (event.position.getZ() < getZ(getPlayer()) - 20 || event.position.getZ() > getZ(getPlayer()) + 20) return;
        if (event.position.getY() < getY(getPlayer()) - 4 || event.position.getY() > getY(getPlayer()) + 10) return;
        if (event.oldBlock.getBlock() != Blocks.wool && event.newBlock.getBlock() == Blocks.wool) {
            countDown.put(event.position, TimeUtils.curTime());
        } else if (event.oldBlock.getBlock() == Blocks.wool && event.newBlock.getBlock() != Blocks.wool) {
            countDown.remove(event.position);
            officialCountDown.remove(event.position);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        clear();
    }
}
