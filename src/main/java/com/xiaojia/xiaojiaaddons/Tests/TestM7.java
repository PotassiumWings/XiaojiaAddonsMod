package com.xiaojia.xiaojiaaddons.Tests;

import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class TestM7 {
    public static void m7() {
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                if (helm == null) continue;
                String name = ChatLib.removeFormatting(helm.getDisplayName()).trim();
                ChatLib.chat(name + String.format(", %.2f %.2f %.2f", entity.posX, entity.posY, entity.posZ));
            }
            if (entity instanceof EntityDragon) {
                ChatLib.chat(String.format("dragon at %.2f %.2f %.2f, health %.2f", entity.posX, entity.posY, entity.posZ, ((EntityDragon) entity).getHealth()));
            }
        }
        for (String str : ScoreBoard.getLines()) {
            ChatLib.chat(str);
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!SessionUtils.getUUID().equals("1c6d48a96cb3465681382590ec82fa68")) return;
        Entity entity = event.entity;
        if (entity instanceof EntityDragon) {
            ChatLib.chat(String.format("spawning dragon at %.2f %.2f %.2f", entity.posX, entity.posY, entity.posZ));
            for (String str : ScoreBoard.getLines()) {
                ChatLib.chat(str);
            }
        }
        if (entity instanceof EntityArmorStand) {
            ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
            if (helm == null) return;
            String name = ChatLib.removeFormatting(helm.getDisplayName()).trim();
            ChatLib.chat(String.format("spawning armorstand wearing helm %s at %.2f %.2f %.2f", name, entity.posX, entity.posY, entity.posZ));
        }
    }

    public static void show() {
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                String name = entity.getDisplayName().getFormattedText();
                ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                String helmString = "";
                if (helm != null) helmString = helm.getDisplayName();
                ChatLib.chat(String.format("name %s, helm %s, %.2f %.2f %.2f", name, helmString, entity.posX, entity.posY, entity.posZ));
            }
        }
    }
}
