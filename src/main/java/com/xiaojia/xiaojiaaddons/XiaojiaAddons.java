package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Commands.AutoForagingCommand;
import com.xiaojia.xiaojiaaddons.Commands.Command;
import com.xiaojia.xiaojiaaddons.Commands.TransferBackCommand;
import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.SneakyCreeper;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.Spider;
import com.xiaojia.xiaojiaaddons.Features.Dragons.AutoShootCrystal;
import com.xiaojia.xiaojiaaddons.Features.Dragons.EnderCrystalESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoBlood;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoCloseSecretChest;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoTerminal;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Map;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.MapUpdater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.RoomLoader;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.MimicWarn;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoCombine;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoUseItem;
import com.xiaojia.xiaojiaaddons.Features.QOL.BlockAbility;
import com.xiaojia.xiaojiaaddons.Features.QOL.EntityQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostBlock;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.HoldRightClick;
import com.xiaojia.xiaojiaaddons.Features.QOL.InCombatQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.NearbyChestESP;
import com.xiaojia.xiaojiaaddons.Features.QOL.NoSlowdown;
import com.xiaojia.xiaojiaaddons.Features.QOL.SwordSwap;
import com.xiaojia.xiaojiaaddons.Features.QOL.TransferBack;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoCloseCrystalHollowsChest;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoPowder;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoPowderChest;
import com.xiaojia.xiaojiaaddons.Features.Skills.Experimentation;
import com.xiaojia.xiaojiaaddons.Features.Skills.Fishing;
import com.xiaojia.xiaojiaaddons.Features.Skills.Foraging;
import com.xiaojia.xiaojiaaddons.Features.Skills.JadeCrystalHelper;
import com.xiaojia.xiaojiaaddons.Features.Skills.SuperPairs;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Sven;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Voidgloom;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
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

import java.util.ArrayList;

@Mod(modid = XiaojiaAddons.MODID, version = XiaojiaAddons.VERSION)
public class XiaojiaAddons {
    public static final String MODID = "xiaojiaaddons";
    public static final String VERSION = "2.2.2";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList<Setting> settings = Config.collect(Configs.class);
    public static GuiScreen guiToOpen = null;
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
        Config.load();
        RoomLoader.load();

        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new AutoForagingCommand());
        ClientCommandHandler.instance.registerCommand(new TransferBackCommand());
//        ClientCommandHandler.instance.registerCommand(new TestGui());
//        ClientCommandHandler.instance.registerCommand(new TestControl());

        MinecraftForge.EVENT_BUS.register(new Checker());
        MinecraftForge.EVENT_BUS.register(new TickEndEvent());

        MinecraftForge.EVENT_BUS.register(this);

        // Utils
        MinecraftForge.EVENT_BUS.register(new ControlUtils());
        MinecraftForge.EVENT_BUS.register(new MathUtils());
        MinecraftForge.EVENT_BUS.register(new HotbarUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());
        MinecraftForge.EVENT_BUS.register(new CommandsUtils());
        MinecraftForge.EVENT_BUS.register(new GuiTest());

        // Bestiary
        MinecraftForge.EVENT_BUS.register(new Spider());
        MinecraftForge.EVENT_BUS.register(new SneakyCreeper());

        // Dragons
        MinecraftForge.EVENT_BUS.register(new AutoShootCrystal());
        MinecraftForge.EVENT_BUS.register(new EnderCrystalESP());

        // Dungeons
        MinecraftForge.EVENT_BUS.register(new AutoBlood());
        MinecraftForge.EVENT_BUS.register(new AutoCloseSecretChest());
        MinecraftForge.EVENT_BUS.register(new AutoTerminal());
        MinecraftForge.EVENT_BUS.register(new MimicWarn());
        MinecraftForge.EVENT_BUS.register(new StarredMobESP());
        MinecraftForge.EVENT_BUS.register(new StonklessStonk());
        // map
        MinecraftForge.EVENT_BUS.register(new Dungeon());
        MinecraftForge.EVENT_BUS.register(new MapUpdater());
        MinecraftForge.EVENT_BUS.register(new Map());

        // QOL
        MinecraftForge.EVENT_BUS.register(new AutoCombine());
        MinecraftForge.EVENT_BUS.register(new AutoUseItem());
        MinecraftForge.EVENT_BUS.register(new BlockAbility());
        MinecraftForge.EVENT_BUS.register(new EntityQOL());
//        MinecraftForge.EVENT_BUS.register(new FuckHarps());
        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new GhostQOL());
        MinecraftForge.EVENT_BUS.register(new NearbyChestESP());
        MinecraftForge.EVENT_BUS.register(new InCombatQOL());
        MinecraftForge.EVENT_BUS.register(new NoSlowdown());
        MinecraftForge.EVENT_BUS.register(new SwordSwap());
        MinecraftForge.EVENT_BUS.register(new HoldRightClick());
        MinecraftForge.EVENT_BUS.register(new TransferBack());

        // Skills
        MinecraftForge.EVENT_BUS.register(new AutoCloseCrystalHollowsChest());
        MinecraftForge.EVENT_BUS.register(new AutoPowder());
        MinecraftForge.EVENT_BUS.register(new AutoPowderChest());
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Fishing());
        MinecraftForge.EVENT_BUS.register(new JadeCrystalHelper());
        MinecraftForge.EVENT_BUS.register(new Experimentation());
        MinecraftForge.EVENT_BUS.register(new SuperPairs());

        // Slayer
        MinecraftForge.EVENT_BUS.register(new Sven());
        MinecraftForge.EVENT_BUS.register(new Voidgloom());

//        MinecraftForge.EVENT_BUS.register(new ClickTest());

        for (KeyBind keyBind : KeyBindUtils.keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind.mcKeyBinding());
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (guiToOpen != null) {
            mc.displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }
    }
}
