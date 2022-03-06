package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class GhostBlock {
    private static final KeyBind ghostBlockKeyBind = new KeyBind("Ghost Block", Keyboard.KEY_NONE);
    private long lastSwing = 0;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.GhostBlockWithPickaxe) return;
        try {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (!BlockUtils.canGhostBlock(mc.theWorld.getBlockState(event.pos).getBlock())) return;
                ItemStack heldItemStack = ControlUtils.getHeldItemStack();
                if (heldItemStack == null || !heldItemStack.hasDisplayName()) return;
                String heldItemName = heldItemStack.getDisplayName();
                if (heldItemName.contains("Stonk") || heldItemName.contains("Pickaxe")) {
                    mc.theWorld.setBlockToAir(event.pos);
                    if (lastSwing + 250 < TimeUtils.curTime()) {
                        getPlayer().swingItem();
                        lastSwing = TimeUtils.curTime();
                    }
                    event.setCanceled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (Configs.GhostBlockWithKeyBind && ghostBlockKeyBind.isKeyDown()) {
            BlockPos lookingAtPos = getPlayer().rayTrace(mc.playerController.getBlockReachDistance(), 1.0F).getBlockPos();
            if (lookingAtPos != null) {
                Block lookingAtBlock = getWorld().getBlockState(lookingAtPos).getBlock();
                if (BlockUtils.canGhostBlock(lookingAtBlock)) {
                    if (lastSwing + 250 < TimeUtils.curTime()) {
                        getPlayer().swingItem();
                        lastSwing = TimeUtils.curTime();
                    }
                    mc.theWorld.setBlockToAir(lookingAtPos);
                }
            }
        }
    }
}
