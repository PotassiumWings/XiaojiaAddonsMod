package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Commands.Command;
import com.xiaojia.xiaojiaaddons.Commands.TestControl;
import com.xiaojia.xiaojiaaddons.Commands.TestGui;
import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.Spider;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.MimicWarn;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Features.QOL.BlockAbility;
import com.xiaojia.xiaojiaaddons.Features.QOL.EntityQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostBlock;
import com.xiaojia.xiaojiaaddons.Features.QOL.SwordSwap;
import com.xiaojia.xiaojiaaddons.Features.QOL.Terminator;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Sven;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Voidgloom;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.KeyBindUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

@Mod(modid = XiaojiaAddons.MODID, version = XiaojiaAddons.VERSION)
public class XiaojiaAddons {
    public static final String MODID = "XiaojiaAddons";
    public static final String VERSION = "1.11";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList<Setting> settings = Config.collect(Configs.class);
    private static boolean debug = false;

    public static GuiScreen guiToOpen = null;

    public static void setDebug() {
        debug = !debug;
        ChatLib.chat("debug: " + debug);
    }

    public static boolean isDebug() {
        return debug;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load();

        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new TestGui());
        ClientCommandHandler.instance.registerCommand(new TestControl());

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new MathUtils());
        MinecraftForge.EVENT_BUS.register(new HotbarUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());
        MinecraftForge.EVENT_BUS.register(new CommandsUtils());
        MinecraftForge.EVENT_BUS.register(new GuiTest());

        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new BlockAbility());
        MinecraftForge.EVENT_BUS.register(new StonklessStonk());
        MinecraftForge.EVENT_BUS.register(new MimicWarn());
        MinecraftForge.EVENT_BUS.register(new EntityQOL());
        MinecraftForge.EVENT_BUS.register(new Terminator());
        MinecraftForge.EVENT_BUS.register(new SwordSwap());

        MinecraftForge.EVENT_BUS.register(new Spider());

        MinecraftForge.EVENT_BUS.register(new Voidgloom());
        MinecraftForge.EVENT_BUS.register(new Sven());
        for (KeyBind keyBind : KeyBindUtils.keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind.mcKeyBinding());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (guiToOpen != null) {
            mc.displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }
    }
}
