package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class M7Dragon {
    private static final HashMap<EntityDragon, BlockPos> nearestBlockPosMap = new HashMap<>();
    private static final HashMap<BlockPos, Color> dragonLocations = new HashMap<BlockPos, Color>() {{
        put(new BlockPos(27, 14, 59), new Color(1F, 0F, 0F));
        put(new BlockPos(27, 14, 94), new Color(0F, 1F, 0F));
        put(new BlockPos(56, 14, 125), new Color(0.5019608f, 0.0f, 0.5019608f));
        put(new BlockPos(85, 14, 94), new Color(0.0f, 1.0f, 1.0f));
        put(new BlockPos(85, 14, 56), new Color(1.0f, 0.64705884f, 0.0f));
    }};
    private static final HashMap<BlockPos, String> prefixMap = new HashMap<BlockPos, String>() {{
        put(new BlockPos(27, 14, 59), "&cRed Dragon&r: ");
        put(new BlockPos(27, 14, 94), "&aGreen Dragon&r: ");
        put(new BlockPos(56, 14, 125), "&5Purple Dragon&r: ");
        put(new BlockPos(85, 14, 94), "&bCyan Dragon&r: ");
        put(new BlockPos(85, 14, 56), "&6Orange Dragon&r: ");
    }};

    private final Display display = new Display();

    public M7Dragon() {
        display.setShouldRender(true);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("left");
    }

    public static void onSpawnDragon(EntityDragon entity) {
        BlockPos blockPos = null;
        if (!SkyblockUtils.isInDungeon()) return;
        double distance = 1e9;
        for (Map.Entry<BlockPos, Color> entry : dragonLocations.entrySet()) {
            BlockPos pos = entry.getKey();
            double dis = entity.getDistanceSq(pos);
            if (dis < distance) {
                distance = dis;
                blockPos = pos;
            }
        }
//        ChatLib.chat("dragon spawn " + entity + " at " + blockPos);
        nearestBlockPosMap.put(entity, blockPos);
    }

    public static void onRenderMainModel(EntityDragon entity) {
        if (!Checker.enabled) return;
        if (!Configs.ShowM7DragonColor) return;
        Color color = dragonLocations.get(nearestBlockPosMap.get(entity));
        if (color != null)
            GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static float replaceHurtOpacity(EntityDragon entity, float value) {
        if (Configs.ShowM7DragonColor && nearestBlockPosMap.containsKey(entity))
            return 0.03F;
        return value;
    }

    @SubscribeEvent
    public void renderLivingPost(RenderGameOverlayEvent.Pre event) {
        if (!Checker.enabled) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getWorld() == null) return;
        display.clearLines();
        display.setRenderLoc(Configs.dragonInfoX, Configs.dragonInfoY);
        if (Configs.dragonInfoTest)
            display.addLine("dragon Info is here");
        for (Entity entity: getWorld().loadedEntityList) {
            if (entity instanceof EntityDragon ) {
                String hpPrefix = "&a";
                double hp = ((EntityDragon) entity).getHealth();
                if (hp <= 100000000) hpPrefix = "&c";
                else if (hp <= 300000000) hpPrefix = "&6";
                String hpString = hpPrefix + DisplayUtils.hpToString(hp);

                BlockPos blockPos = nearestBlockPosMap.get(entity);
                double dis = Math.sqrt(entity.getDistanceSq(blockPos));
                String disPrefix = "&a";
                if (dis < 20) disPrefix = "&c";
                else if (dis < 40) disPrefix = "&6";
                String str = prefixMap.get(blockPos) + hpString + "&r HP, " + disPrefix + String.format("%.2f&r Blocks", dis);

                DisplayLine line = new DisplayLine(str);
                line.setScale(Configs.dragonInfoScale / 20F);
                display.addLine(line);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        nearestBlockPosMap.clear();
    }
}

