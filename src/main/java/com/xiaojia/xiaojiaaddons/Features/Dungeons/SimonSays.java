package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.BlockChangeEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.Color;
import java.util.ArrayList;

public class SimonSays {
    private static final ArrayList<BlockPos> clicks = new ArrayList<>();
    private static final BlockPos startButton = new BlockPos(110, 121, 91);
    private static int clickIndex = 0;

    private static boolean canStartClick = false;
    private Thread thread = null;

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
//                ChatLib.chat(pos.west() + " added");
            }
        } else if (pos.getX() == 110) { // buttons
            if (newBlock == Blocks.air) {
                clickIndex = 0;
                canStartClick = false;
            } else if (newBlock == Blocks.stone_button && oldBlock == Blocks.stone_button &&
                    event.newBlock.getValue(BlockButtonStone.POWERED) && clickIndex < clicks.size() &&
                    pos.equals(clicks.get(clickIndex))) {
                clickIndex++;
                canStartClick = true;
            } else if (newBlock == Blocks.stone_button && oldBlock == Blocks.air) {
                canStartClick = true;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Configs.SimonSaysAutoChangeDirection) return;
        if (MathUtils.distanceSquareFromPlayer(109, 120, 93) > 3 * 3) return;
        if (canStartClick && (thread == null || !thread.isAlive()) && clickIndex < clicks.size()) {
            canStartClick = false;
            thread = new Thread(() -> {
                BlockPos pos = clicks.get(clickIndex).east();
                try {
                    ControlUtils.faceSlowly(pos.getX(), pos.getY() + 0.5, pos.getZ() + 0.5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SimonSaysSolver) return;
        if (clickIndex >= clicks.size()) return;
        BlockPos blockPos = clicks.get(clickIndex);
        GuiUtils.drawBoxAtBlock(blockPos, new Color(0, 255, 0, 80), 1, 1, 0.0020000000949949026F);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.SimonSaysSolver) return;
        if (!Configs.SimonSaysBlockWrongClicks) return;
        if (!SkyblockUtils.isInDungeon()) return;
        if (!SkyblockUtils.getDungeon().contains("7")) return;
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (clickIndex >= clicks.size()) return;
        Block block = BlockUtils.getBlockAt(event.pos);
        if (block != Blocks.stone_button) return;
        if (!event.pos.equals(clicks.get(clickIndex)) && !event.pos.equals(startButton)) {
            ChatLib.chat("Blocked wrong click in simon says!");
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        clickIndex = 0;
        clicks.clear();
        canStartClick = false;
    }
}
