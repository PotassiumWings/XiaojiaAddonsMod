package com.xiaojia.xiaojiaaddons.Features.Nether;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kuudra {
    private static final ArrayList<KuudraInfo> primaryInfos = new ArrayList<>();
    private static final ArrayList<KuudraInfo> secondaryInfos = new ArrayList<>();
    private static final ArrayList<KuudraInfo> personalInfos = new ArrayList<>();
    private static final ArrayList<KuudraInfo> infos = new ArrayList<>();
    private static final HashMap<String, Integer> tiers = new HashMap<>();
    public static Display display1 = new Display();
    public static Display display2 = new Display();
    public static Display display3 = new Display();

    static {
        primaryInfos.add(new KuudraInfo("Rapid Fire", new HashMap<Integer, Integer>() {{
            put(1, 40);
            put(2, 80);
            put(3, 120);
            put(4, 160);
            put(5, 200);
        }}));
        primaryInfos.add(new KuudraInfo("Bonus Damage", new HashMap<Integer, Integer>() {{
            put(1, 50);
            put(2, 80);
            put(3, 110);
            put(4, 140);
            put(5, 170);
            put(6, 200);
        }}));
        primaryInfos.add(new KuudraInfo("Accelerated", new HashMap<Integer, Integer>() {{
            put(1, 10);
            put(2, 20);
            put(3, 30);
            put(4, 40);
            put(5, 50);
            put(6, 60);
        }}));
        primaryInfos.add(new KuudraInfo("Blast Radius", new HashMap<Integer, Integer>() {{
            put(1, 20);
            put(2, 40);
            put(3, 60);
            put(4, 80);
            put(5, 100);
            put(6, 120);
        }}));
        primaryInfos.add(new KuudraInfo("Multi Shot", new HashMap<Integer, Integer>() {{
            put(1, 50);
            put(2, 80);
            put(3, 110);
            put(4, 140);
            put(5, 170);
        }}));

        secondaryInfos.add(new KuudraInfo("Confusion", new HashMap<Integer, Integer>() {{
            put(1, 100);
            put(2, 200);
            put(3, 300);
            put(4, 400);
            put(5, 500);
        }}));
        secondaryInfos.add(new KuudraInfo("Wounded", new HashMap<Integer, Integer>() {{
            put(1, 100);
            put(2, 200);
            put(3, 300);
            put(4, 400);
            put(5, 500);
        }}));
        secondaryInfos.add(new KuudraInfo("Sweet Spot", new HashMap<Integer, Integer>() {{
            put(1, 100);
            put(2, 200);
            put(3, 300);
            put(4, 400);
            put(5, 500);
        }}));
        secondaryInfos.add(new KuudraInfo("Hinder", new HashMap<Integer, Integer>() {{
            put(1, 100);
            put(2, 200);
            put(3, 300);
            put(4, 400);
            put(5, 500);
        }}));

        personalInfos.add(new KuudraInfo("Detonate", new HashMap<Integer, Integer>() {{
            put(1, 50);
            put(2, 80);
            put(3, 110);
            put(4, 140);
        }}));
        personalInfos.add(new KuudraInfo("Shell", new HashMap<Integer, Integer>() {{
            put(1, 50);
            put(2, 80);
            put(3, 110);
            put(4, 140);
        }}));
        personalInfos.add(new KuudraInfo("Magical Arrows", new HashMap<Integer, Integer>() {{
            put(1, 50);
            put(2, 80);
            put(3, 110);
            put(4, 140);
        }}));

        infos.addAll(primaryInfos);
        infos.addAll(secondaryInfos);
        infos.addAll(personalInfos);
    }

    private static String getStringFromInfo(KuudraInfo info) {
        // Rapid Fire:
        String name = info.name;
        int tier = tiers.getOrDefault(name, 1);
        int cost = info.cost.getOrDefault(tier, -1);
        String colorString = getColorStringFromTier(tier, info.cost.size() + 1);
        String tierString = "T" + tier;
        String costString = cost == -1 ? "MAXED" : cost + "";
        return "&e" + name + ": " + colorString + tierString + " " + costString;
    }

    private static String getColorStringFromTier(int tier, int max) {
        if (tier == max) return "&b&l";
        if (tier == 1) return "&9&l";
        if (tier == 2) return "&6&l";
        if (tier < 5) return "&e&l";
        return "&c&l";
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        display1.clearLines();
        display2.clearLines();
        display3.clearLines();
        display1.setRenderLoc(Configs.KuudraX, Configs.KuudraY);
        display2.setRenderLoc(Configs.KuudraX + 9 * Configs.KuudraScale, Configs.KuudraY);
        display3.setRenderLoc(Configs.KuudraX + 18 * Configs.KuudraScale, Configs.KuudraY);
        display1.setShouldRender(true);
        display2.setShouldRender(true);
        display3.setShouldRender(true);
        if (!Configs.KuudraTest && !SkyblockUtils.isInKuudra()) return;
        if (Configs.KuudraTest) {
            tiers.put("Rapid Fire", 1);
            tiers.put("Bonus Damage", 2);
            tiers.put("Accelerated", 3);
            tiers.put("Blast Radius", 4);
            tiers.put("Multi Shot", 5);
            tiers.put("Confusion", 6);
            tiers.put("Hinder", 6);
            tiers.put("Shell", 4);
        }
        DisplayLine title = new DisplayLine("&c&lPrimary");
        title.setScale(1.51F * Configs.KuudraScale / 15);
        display1.addLine(title);
        title = new DisplayLine("&d&lSecondary");
        title.setScale(1.51F * Configs.KuudraScale / 15);
        display2.addLine(title);
        title = new DisplayLine("&4&lPersonal");
        title.setScale(1.51F * Configs.KuudraScale / 15);
        display3.addLine(title);
        for (KuudraInfo info : primaryInfos) {
            DisplayLine line = new DisplayLine(getStringFromInfo(info));
            line.setScale(1.51F * Configs.KuudraScale / 20);
            display1.addLine(line);
        }
        for (KuudraInfo info : secondaryInfos) {
            DisplayLine line = new DisplayLine(getStringFromInfo(info));
            line.setScale(1.51F * Configs.KuudraScale / 20);
            display2.addLine(line);
        }
        for (KuudraInfo info : personalInfos) {
            DisplayLine line = new DisplayLine(getStringFromInfo(info));
            line.setScale(1.51F * Configs.KuudraScale / 20);
            display3.addLine(line);
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (event.type != 0) return;
        if (!Configs.KuudraDisplay) return;
        String text = ChatLib.removeFormatting(event.message.getUnformattedText());
        Pattern pattern = Pattern.compile("^([a-zA-Z ]+)( was upgraded to tier: | has been upgraded to tier: )(\\d+)$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String name = matcher.group(1);
            int tier = Integer.parseInt(matcher.group(3));
            tiers.put(name, tier);
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        tiers.clear();
    }

    static class KuudraInfo {
        String name;
        HashMap<Integer, Integer> cost;

        public KuudraInfo(String name, HashMap<Integer, Integer> cost) {
            this.name = name;
            this.cost = cost;
        }
    }
}
