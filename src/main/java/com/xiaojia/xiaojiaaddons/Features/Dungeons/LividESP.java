package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.RenderEntityESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.EntityInfo;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class LividESP extends RenderEntityESP {
    public BlockPos pos = new BlockPos(205, 109, 243);
    private EnumChatFormatting colorChat = null;

    @SubscribeEvent
    public void onTickCheck(TickEndEvent event) {
        colorChat = null;
        if (!Checker.enabled) return;
        if (!Configs.ShowCorrectLivid) return;
        if (Dungeon.bossEntry <= Dungeon.runStarted) return;
        if (Dungeon.floorInt != 5) return;
        IBlockState iBlockState = BlockUtils.getBlockStateAt(pos);
        if (iBlockState == null) return;
        if (iBlockState.getBlock() != Blocks.stained_glass) return;
//        int meta = iBlockState.getBlock().getMetaFromState(iBlockState);
        EnumDyeColor val = iBlockState.getValue(BlockStainedGlass.COLOR);
        if (val == EnumDyeColor.WHITE) colorChat = EnumChatFormatting.WHITE;
        else if (val == EnumDyeColor.MAGENTA) colorChat = EnumChatFormatting.LIGHT_PURPLE;
        else if (val == EnumDyeColor.RED) colorChat = EnumChatFormatting.RED;
        else if (val == EnumDyeColor.SILVER) colorChat = EnumChatFormatting.GRAY;
        else if (val == EnumDyeColor.GRAY) colorChat = EnumChatFormatting.GRAY;
        else if (val == EnumDyeColor.GREEN) colorChat = EnumChatFormatting.DARK_GREEN;
        else if (val == EnumDyeColor.LIME) colorChat = EnumChatFormatting.GREEN;
        else if (val == EnumDyeColor.BLUE) colorChat = EnumChatFormatting.BLUE;
        else if (val == EnumDyeColor.PURPLE) colorChat = EnumChatFormatting.DARK_PURPLE;
        else if (val == EnumDyeColor.YELLOW) colorChat = EnumChatFormatting.YELLOW;
        ChatLib.debug("val: " + val.getName());
        ChatLib.debug("colorChat: " + colorChat.toString());
    }

    @Override
    public boolean shouldRenderESP(EntityInfo entityInfo) {
        return true;
    }

    @Override
    public boolean shouldDrawString(EntityInfo entityInfo) {
        return false;
    }

    @Override
    public EntityInfo getEntityInfo(Entity entity) {
        if (!(entity instanceof EntityArmorStand) || colorChat == null ||
                !entity.getName().contains("Livid") ||
                !entity.getName().contains(colorChat.toString() + "\u00a7lLivid")) return null;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("entity", entity);
        hashMap.put("yOffset", 1.0F);
        hashMap.put("drawString", EntityInfo.EnumDraw.DONT_DRAW_STRING);
        hashMap.put("width", 0.5F);
        hashMap.put("height", 2F);
        hashMap.put("fontColor", 0x33ff33);
        hashMap.put("isFilled", Configs.ShowCorrectLividWithFilledBox);
        hashMap.put("isESP", Configs.ShowCorrectLividWithESP);
        hashMap.put("kind", "Livid");
        return new EntityInfo(hashMap);
    }
}
