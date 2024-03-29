package com.xiaojia.xiaojiaaddons.Tests;

import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
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
                String owner = EntityUtils.getHeadOwner(entity);
                ChatLib.chat("owner " + owner + String.format(", %.2f %.2f %.2f", entity.posX, entity.posY, entity.posZ));
            }
            if (entity instanceof EntityDragon) {
                ChatLib.chat(String.format("dragon at %.2f %.2f %.2f, health %.2f", entity.posX, entity.posY, entity.posZ, ((EntityDragon) entity).getHealth()));
            }
        }
        for (String str : ScoreBoard.getLines()) {
            ChatLib.chat(str);
        }
    }

    public static void show() {
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                String name = entity.getDisplayName().getFormattedText();
                ItemStack helm = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                String helmString = "";
                if (helm != null) helmString = helm.getDisplayName();
                ChatLib.chat(String.format("name %s, helm %s, texture %s, %.2f %.2f %.2f",
                        name, helmString, EntityUtils.getHeadTexture(entity), entity.posX, entity.posY, entity.posZ));
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!SessionUtils.isDev()) return;
        if (SessionUtils.isDev()) return;
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
}
