package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class BlockAbility {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        try {
            ItemStack heldItem = ControlUtils.getHeldItemStack();
            if (heldItem == null) return;
            String name = heldItem.getDisplayName();
            if (Configs.BlockGloomlock && name.contains("Gloomlock Grimoire")) {
                event.setCanceled(true);
                ChatLib.chat("&bBlocked Gloomlock Grimoire Right Click!");
            }
            if (Configs.BlockPickobulus && SkyblockUtils.getCurrentMap().equals("Your Island")) {
                if (name.contains("Pickaxe") || name.contains("Drill") || name.contains("Stonk")) {
                    List<String> lore = NBTUtils.getLore(heldItem);
                    for (String s : lore) {
                        if (XiaojiaAddons.isDebug()) ChatLib.chat(s);
                        if (s.contains("Pickobulus")) {
                            event.setCanceled(true);
                            ChatLib.chat("&bBlocked Pickobulus Ability!");
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
