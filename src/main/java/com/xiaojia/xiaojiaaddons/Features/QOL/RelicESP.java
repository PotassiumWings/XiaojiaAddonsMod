package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RelicESP {
    private static final HashSet<Integer> got = new HashSet<>();
    private static final ArrayList<BlockPos> locations = new ArrayList<>(Arrays.asList(
            new BlockPos(-236, 51, -239),
            new BlockPos(-254, 57, -279),
            new BlockPos(-272, 48, -291),
            new BlockPos(-217, 58, -304),
            new BlockPos(-206, 63, -301),
            new BlockPos(-274, 100, -178),
            new BlockPos(-284, 49, -234),
            new BlockPos(-275, 64, -272),
            new BlockPos(-225, 70, -316),
            new BlockPos(-300, 50, -218),
            new BlockPos(-300, 51, -254),
            new BlockPos(-296, 37, -270),
            new BlockPos(-303, 71, -318),
            new BlockPos(-313, 58, -250),
            new BlockPos(-311, 69, -251),
            new BlockPos(-317, 69, -273),
            new BlockPos(-328, 50, -238),
            new BlockPos(-348, 65, -202),
            new BlockPos(-342, 89, -221),
            new BlockPos(-342, 122, -253),
            new BlockPos(-355, 86, -213),
            new BlockPos(-354, 73, -285),
            new BlockPos(-384, 89, -225),
            new BlockPos(-372, 89, -242),
            new BlockPos(-183, 51, -252),
            new BlockPos(-178, 136, -297),
            new BlockPos(-188, 80, -346),
            new BlockPos(-147, 83, -335)));
    public static String fileName = "config/XiaojiaAddonsRelicFound.cfg";

    @SubscribeEvent
    public void onRelic(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.RelicESP) return;
        if (!SkyblockUtils.isInSpiderDen()) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (locations.contains(event.pos)) {
            got.add(locations.indexOf(event.pos));
            save();
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.RelicESP) return;
        if (!SkyblockUtils.isInSpiderDen()) return;
        for (int i = 0; i < locations.size(); i++) {
            if (got.contains(i)) continue;
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(locations.get(i), new Color(0, 255, 255),
                    1, 1, 0.0020000000949949026F);
            GuiUtils.disableESP();
        }
    }

    public static void load() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                Reader reader = Files.newBufferedReader(Paths.get(fileName));
                Type type = (new TypeToken<HashSet<Integer>>() {
                }).getType();
                HashSet<Integer> settingsFromConfig = (new Gson()).fromJson(reader, type);
                got.addAll(settingsFromConfig);
            }
        } catch (Exception e) {
            System.out.println("Error while loading item rename config file");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            String json = (new Gson()).toJson(got);
            Files.write(Paths.get(fileName), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error while saving item rename config file");
            e.printStackTrace();
        }
    }
}
