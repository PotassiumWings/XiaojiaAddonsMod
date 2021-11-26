package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class BlockAbility {
    private static boolean debug = false;
    public static void setDebug() {
        debug = !debug;
        ChatLib.chat("debug: " + debug);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            ItemStack heldItem = ControlUtils.getHeldItemStack();
            String name = heldItem.getDisplayName();
            if (name.contains("Gloomlock Grimoire")) {
                event.setCanceled(true);
                ChatLib.chat("&bBlocked Gloomlock Grimoire Right Click!");
            }
            if (SkyblockUtils.getCurrentMap().equals("Your Island")) {
                if (name.contains("Pickaxe") || name.contains("Drill") || name.contains("Stonk")) {
                    List<String> lore = NBTUtils.getLore(heldItem);
                    for (int i = 0; i < lore.size(); i++) {
                        if (debug) ChatLib.chat(lore.get(i));
                        if (lore.get(i).contains("Pickobulus")) {
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
