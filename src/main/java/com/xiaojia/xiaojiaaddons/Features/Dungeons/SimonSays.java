package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;

public class SimonSays {
    private static final ArrayList<BlockPos> clicks = new ArrayList<>();
    private static final BlockPos startButton = new BlockPos(110, 121, 91);
    private static int clickIndex = 0;

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (!Configs.SimonSaysSolver) return;
        if (!SkyblockUtils.getDungeon().contains("7")) return;

        BlockPos pos = event.position;
        Block newBlock = event.newBlock.getBlock();
        Block oldBlock = event.oldBlock.getBlock();
        if (pos.equals(startButton)) {
            if (newBlock == Blocks.air ||
                    newBlock == Blocks.stone_button &&
                            event.newBlock.getValue(BlockButtonStone.POWERED)) {
                clicks.clear();
                clickIndex = 0;
            }
        }
        if (pos.getY() < 120 || pos.getY() > 123 || pos.getZ() < 92 || pos.getZ() > 95) return;
        if (pos.getX() == 111) { // background
            if (newBlock == Blocks.sea_lantern && !clicks.contains(pos.west())) {
                clicks.add(pos.west());
                ChatLib.chat(pos.west() + " added");
            }
        } else if (pos.getX() == 110) { // buttons
            if (newBlock == Blocks.air) {
                clickIndex = 0;
                ChatLib.chat("owo reset");
            } else if (newBlock == Blocks.stone_button && oldBlock == Blocks.stone_button &&
                    event.newBlock.getValue(BlockButtonStone.POWERED) && pos.equals(clicks.get(clickIndex))) {
                clickIndex++;
                ChatLib.chat("now index at " + clickIndex);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SimonSaysSolver) return;
        if (clickIndex >= clicks.size()) return;
        BlockPos blockPos = clicks.get(clickIndex);
        GuiUtils.drawBoxAtBlock(blockPos, new Color(0, 255, 0, 32), 1, 1, 0.0020000000949949026F);
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SimonSaysSolver) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (!SkyblockUtils.getDungeon().contains("7")) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (clickIndex >= clicks.size()) return;
        Block block = BlockUtils.getBlockAt(event.pos);
        if (block != Blocks.stone_button) return;
        if (!event.pos.equals(clicks.get(clickIndex)) && !event.pos.equals(startButton)) {
            ChatLib.chat(event.pos + ", " + clicks.get(clickIndex));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        clickIndex = 0;
        clicks.clear();
    }
}
