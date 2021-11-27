package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Features.Bestiary.Spider;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.MimicWarn;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Features.QOL.BlockAbility;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostBlock;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.commands.Command;
import com.xiaojia.xiaojiaaddons.commands.TestControl;
import com.xiaojia.xiaojiaaddons.commands.TestGui;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.KeyBindUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XiaojiaAddons.MODID, version = XiaojiaAddons.VERSION)
public class XiaojiaAddons {
    public static final String MODID = "XiaojiaAddons";
    public static final String VERSION = "1.11";
    public static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean debug = false;

    public static void setDebug() {
        debug = !debug;
        ChatLib.chat("debug: " + debug);
    }

    public static boolean isDebug() {
        return debug;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new TestGui());
        ClientCommandHandler.instance.registerCommand(new TestControl());

        MinecraftForge.EVENT_BUS.register(new MathUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());
        MinecraftForge.EVENT_BUS.register(new CommandsUtils());
        MinecraftForge.EVENT_BUS.register(new GuiTest());

        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new BlockAbility());
        MinecraftForge.EVENT_BUS.register(new StonklessStonk());
        MinecraftForge.EVENT_BUS.register(new MimicWarn());

        MinecraftForge.EVENT_BUS.register(new Spider());
        for (KeyBind keyBind : KeyBindUtils.keyBinds.values()) {
            ClientRegistry.registerKeyBinding(keyBind.mcKeyBinding());
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }
}
