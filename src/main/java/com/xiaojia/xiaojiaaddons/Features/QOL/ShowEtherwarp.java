package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class ShowEtherwarp {
    private static final HashSet<Block> invalidBlocks = new HashSet<>(Arrays.asList(
            Blocks.air,
            Blocks.skull,
            Blocks.ladder,
            Blocks.fire,
            Blocks.snow_layer,
            Blocks.portal,
            Blocks.carpet,

            Blocks.web, Blocks.tripwire, Blocks.redstone_wire, Blocks.tripwire_hook,

            Blocks.flower_pot, Blocks.red_flower, Blocks.yellow_flower, Blocks.double_plant,

            Blocks.torch, Blocks.redstone_torch, Blocks.unlit_redstone_torch,

//            Blocks.acacia_door, Blocks.birch_door, Blocks.dark_oak_door, Blocks.iron_door, Blocks.jungle_door,
//            Blocks.oak_door, Blocks.spruce_door, Blocks.iron_trapdoor, Blocks.trapdoor,

            Blocks.vine, Blocks.reeds, Blocks.melon_stem, Blocks.pumpkin_stem,
            Blocks.carrots, Blocks.wheat, Blocks.potatoes, Blocks.nether_wart,
            Blocks.deadbush, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.cocoa,
            Blocks.tallgrass, Blocks.waterlily,

            Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
            Blocks.powered_comparator, Blocks.powered_repeater, Blocks.powered_repeater, Blocks.unpowered_comparator,

            Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,

            Blocks.rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail
    ));

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled || !Configs.ShowEtherwarp) return;
        EntityPlayer player = getPlayer();
        if (player == null || !player.isSneaking()) return;
        ItemStack held = ControlUtils.getHeldItemStack();
        if (!NBTUtils.getBooleanFromExtraAttributes(held, "ethermerge")) return;

        int dist = 57 + NBTUtils.getIntFromExtraAttributes(held, "tuned_transmission");
        Vec3 eye = player.getPositionEyes(event.partialTicks);
        javax.vecmath.Vector3d vec = BlockUtils.getLookingAtVector(dist);
        if (valid(vec)) {
            // render
            BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
            GuiUtils.enableESP();
            Color color = ColorUtils.getColorFromString(Configs.EtherwarpPointColor, new Color(0, 0, 0, 255));
            Color outColor = ColorUtils.getColorFromString(Configs.EtherwarpPointBoundingColor, new Color(0, 0, 0, 255));
            GuiUtils.drawSelectionFilledBoxAtBlock(pos, color);
            if (Configs.EtherwarpPointBoundingThickness != 0)
                GuiUtils.drawSelectionBoundingBoxAtBlock(pos, outColor);
            GuiUtils.disableESP();
        }
        if (vec != null && Configs.ShowNearbyEtherwarp) {
            BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
            int r = Configs.NearbyEtherwarpRadius;
            for (int x = -r; x <= r; x++) {
                for (int y = -r; y <= r; y++) {
                    for (int z = -r; z <= r; z++) {
                        BlockPos blockPos = pos.add(x, y, z);
                        if (blockPos == null) continue;
                        ArrayList<BlockUtils.Face> faces = BlockUtils.getSurfaceMid(eye, blockPos);
                        if (valid(new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5))) {
                            GuiUtils.enableESP();
                            faces.forEach(e -> {
                                if (BlockUtils.getNearestBlock(eye, e.mid) == null) {
                                    Color color = ColorUtils.getColorFromString(Configs.PossibleEtherwarpPointColor,
                                            new Color(0, 0, 0, 255));
                                    GuiUtils.drawFilledFace(e, color);
                                }
                            });
                            GuiUtils.disableESP();
                        }
                    }
                }
            }
        }
    }

    // TODO no optimize currently
    private boolean valid(Vector3d vec) {
        if (vec == null) return false;
        try {
            Vec3 playerEye = getPlayer().getPositionEyes(MathUtils.partialTicks);
            double dis = MathUtils.distanceSquaredFromPoints(vec, new Vector3d(playerEye.xCoord, playerEye.yCoord, playerEye.zCoord));

            BlockPos blockPos = new BlockPos(vec.x, vec.y, vec.z);
            Block block = BlockUtils.getBlockAt(blockPos);

            // right click block
            if (dis < 25 && (block == Blocks.chest || block == Blocks.ender_chest || block == Blocks.trapped_chest ||
                    block == Blocks.hopper || block == Blocks.crafting_table ||
                    block == Blocks.anvil || block == Blocks.enchanting_table ||
                    block == Blocks.furnace || block == Blocks.lit_furnace ||
                    block == Blocks.brewing_stand || block == Blocks.dispenser || block == Blocks.dropper
            ))
                return false;

            // fire -> not collidable
            if (invalidBlocks.contains(block)) return false;
            for (int i = 1; i <= 2; i++) {
                blockPos = blockPos.up();
                block = BlockUtils.getBlockAt(blockPos);
                if (!invalidBlocks.contains(block))
                    return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
