package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
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
            Blocks.tallgrass,

            Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
            Blocks.powered_comparator, Blocks.powered_repeater, Blocks.powered_repeater, Blocks.unpowered_comparator,

            Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,

            Blocks.rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail
    ));
    private static final Color[] colors = new Color[]{
            new Color(129, 216, 207, 0xd0),
            new Color(0, 120, 215, 0xd0),
            new Color(0x1c, 0xd4, 0xe4, 0xd0),
            new Color(0, 0, 0, 0xd0)
    };

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled || !Configs.ShowEtherwarp) return;
        EntityPlayer player = getPlayer();
        if (player == null || !player.isSneaking()) return;
        ItemStack held = ControlUtils.getHeldItemStack();
        if (!NBTUtils.getBooleanFromExtraAttributes(held, "ethermerge")) return;

        int dist = 57 + NBTUtils.getIntFromExtraAttributes(held, "tuned_transmission");
        Vec3 eye = player.getPositionEyes(event.partialTicks);
        Vec3 look = player.getLook(event.partialTicks);
        Vec3 farthest = eye.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist);
        BlockPos pos = BlockUtils.getNearestBlock(eye, farthest);
        if (!valid(pos)) return;
        // render
        GuiUtils.enableESP();
        Color color = colors[Configs.EtherwarpPointColor];
        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Configs.EtherwarpPointColorAlpha);
        GuiUtils.drawBoxAtBlock(
                pos, color,
                1, 1, 0.0020000000949949026F
        );
        GuiUtils.disableESP();
    }

    // TODO no optimize currently
    private boolean valid(BlockPos blockPos) {
        if (blockPos == null) return false;
        try {
            Block block = BlockUtils.getBlockAt(blockPos);
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
