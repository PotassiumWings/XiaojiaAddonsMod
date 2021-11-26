package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Features.Bestiary.Spider;
import com.xiaojia.xiaojiaaddons.Features.QOL.BlockAbility;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostBlock;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.commands.Command;
import com.xiaojia.xiaojiaaddons.commands.TestControl;
import com.xiaojia.xiaojiaaddons.commands.TestGui;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XiaojiaAddons.MODID, version = XiaojiaAddons.VERSION)
public class XiaojiaAddons {
    public static final String MODID = "XiaojiaAddons";
    public static final String VERSION = "1.11";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new TestGui());
        ClientCommandHandler.instance.registerCommand(new TestControl());

        MinecraftForge.EVENT_BUS.register(new MathUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());
        MinecraftForge.EVENT_BUS.register(new GuiTest());

        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new BlockAbility());

        MinecraftForge.EVENT_BUS.register(new Spider());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
    }
}
