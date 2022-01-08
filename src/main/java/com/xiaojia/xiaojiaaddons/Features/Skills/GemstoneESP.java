package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class GemstoneESP {
    private final ConcurrentHashMap<BlockPos, Gemstone> gemstones = new ConcurrentHashMap<>();
    private final HashSet<BlockPos> checked = new HashSet<>();
    private BlockPos lastChecked = null;
    private boolean isScanning = false;

    enum Gemstone {
        RUBY(new Color(188, 3, 29), EnumDyeColor.RED),
        AMETHYST(new Color(137, 0, 201), EnumDyeColor.PURPLE),
        JADE(new Color(157, 249, 32), EnumDyeColor.LIME),
        SAPPHIRE(new Color(60, 121, 224), EnumDyeColor.LIGHT_BLUE),
        AMBER(new Color(237, 139, 35), EnumDyeColor.ORANGE),
        TOPAZ(new Color(249, 215, 36), EnumDyeColor.YELLOW),
        JASPER(new Color(214, 15, 150), EnumDyeColor.MAGENTA);

        public Color color;

        public EnumDyeColor dyeColor;

        Gemstone(Color color, EnumDyeColor dyeColor) {
            this.color = color;
            this.dyeColor = dyeColor;
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (isEnabled() && !this.isScanning &&
                (this.lastChecked == null || !this.lastChecked.equals(getPlayer().getPosition()))) {
            this.isScanning = true;
            (new Thread(() -> {
                BlockPos playerPosition = getPlayer().getPosition();
                this.lastChecked = playerPosition;
                for (int x = playerPosition.getX() - Configs.GemstoneRadius; x < playerPosition.getX() + Configs.GemstoneRadius; x++) {
                    for (int y = playerPosition.getY() - Configs.GemstoneRadius; y < playerPosition.getY() + Configs.GemstoneRadius; y++) {
                        for (int z = playerPosition.getZ() - Configs.GemstoneRadius; z < playerPosition.getZ() + Configs.GemstoneRadius; z++) {
                            BlockPos position = new BlockPos(x, y, z);
                            if (!this.checked.contains(position) && !getWorld().isAirBlock(position)) {
                                Gemstone gemstone = getGemstone(getWorld().getBlockState(position));
                                if (gemstone != null)
                                    this.gemstones.put(position, gemstone);
                            }
                            this.checked.add(position);
                        }
                    }
                }
                this.isScanning = false;
            })).start();
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (event.newBlock.getBlock() == Blocks.air)
            this.gemstones.remove(event.position);
        if (event.oldBlock.getBlock() == Blocks.air) {
            Gemstone gemstone = getGemstone(event.newBlock);
            if (gemstone != null)
                this.gemstones.put(event.position, gemstone);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (isEnabled())
            for (Map.Entry<BlockPos, Gemstone> gemstone : this.gemstones.entrySet()) {
                if (!isGemstoneEnabled(gemstone.getValue()))
                    continue;
                double distance = Math.sqrt(gemstone.getKey().distanceSq(getX(getPlayer()), getY(getPlayer()), getZ(getPlayer())));
                if (distance > (Configs.GemstoneRadius + 2))
                    continue;
                int alpha = (int) Math.abs(100.0D - distance / Configs.GemstoneRadius * 100.0D);
                Color color = gemstone.getValue().color;
                GuiUtils.enableESP();
                BlockPos gemPos = gemstone.getKey();
                GuiUtils.drawBoxAtBlock(
                        gemPos.getX(), gemPos.getY(), gemPos.getZ(),
                        color.getRed(), color.getGreen(), color.getBlue(), alpha,
                        1, 1, 0F
                );
                GuiUtils.disableESP();
            }
    }

    private static boolean isEnabled() {
        return (Checker.enabled && getPlayer() != null && getWorld() != null &&
                Configs.GemstoneESP && SkyblockUtils.isInCrystalHollows());
    }

    private static Gemstone getGemstone(IBlockState block) {
        if (block.getBlock() != Blocks.stained_glass &&
                !(Configs.IncludeGlassPanes && block.getBlock() == Blocks.stained_glass_pane))
            return null;
        EnumDyeColor color = block.getValue(BlockStainedGlass.COLOR);
        if (color == null) color = block.getValue(BlockStainedGlassPane.COLOR);
        if (color == Gemstone.RUBY.dyeColor) return Gemstone.RUBY;
        if (color == Gemstone.AMETHYST.dyeColor) return Gemstone.AMETHYST;
        if (color == Gemstone.JADE.dyeColor) return Gemstone.JADE;
        if (color == Gemstone.SAPPHIRE.dyeColor) return Gemstone.SAPPHIRE;
        if (color == Gemstone.AMBER.dyeColor) return Gemstone.AMBER;
        if (color == Gemstone.TOPAZ.dyeColor) return Gemstone.TOPAZ;
        if (color == Gemstone.JASPER.dyeColor) return Gemstone.JASPER;
        return null;
    }

    private static boolean isGemstoneEnabled(Gemstone gemstone) {
        switch (gemstone) {
            case RUBY:
                return Configs.RubyEsp;
            case AMETHYST:
                return Configs.AmethystEsp;
            case JADE:
                return Configs.JadeEsp;
            case SAPPHIRE:
                return Configs.SapphireEsp;
            case AMBER:
                return Configs.AmberEsp;
            case TOPAZ:
                return Configs.TopazEsp;
            case JASPER:
                return Configs.JasperEsp;
        }
        return false;
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        this.gemstones.clear();
        this.checked.clear();
        this.lastChecked = null;
    }
}
