package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Commands.AutoForagingCommand;
import com.xiaojia.xiaojiaaddons.Commands.Command;
import com.xiaojia.xiaojiaaddons.Commands.PP;
import com.xiaojia.xiaojiaaddons.Commands.TransferBackCommand;
import com.xiaojia.xiaojiaaddons.Commands.XiaojiaChatTest;
import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.AutoScatha;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.GolemAlert;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.SneakyCreeper;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.Spider;
import com.xiaojia.xiaojiaaddons.Features.Dragons.AutoShootCrystal;
import com.xiaojia.xiaojiaaddons.Features.Dragons.EnderCrystalESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoBlood;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoCloseSecretChest;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoItemFrame;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoLeap;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoSalvage;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoTerminal;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoTerminalNew;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.BatESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.BloodAssist;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.CoordsGB;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.KeyESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.LividESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.M4ESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.M7Dragon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Map;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.MapUpdater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.RoomLoader;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.MimicWarn;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoBlaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.TeleportMaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.ShadowAssassinESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.ShowHiddenMobs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESPBox;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.TrapChestESP;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ChatCopy;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ChestProfit;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ItemRename;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.KeepSprint;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.NoRotate;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.RenderRank;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ShowLowestBin;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.Velocity;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoCombine;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoHarp;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoIsland;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoJerryBox;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoLobby;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoUseItem;
import com.xiaojia.xiaojiaaddons.Features.QOL.BlockAbility;
import com.xiaojia.xiaojiaaddons.Features.QOL.DisableEntityRender;
import com.xiaojia.xiaojiaaddons.Features.QOL.DisplayDayAndCoords;
import com.xiaojia.xiaojiaaddons.Features.QOL.EntityQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.FindFairy;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostBlock;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.HideCreepers;
import com.xiaojia.xiaojiaaddons.Features.QOL.HoldRightClick;
import com.xiaojia.xiaojiaaddons.Features.QOL.InCombatQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.NearbyChestESP;
import com.xiaojia.xiaojiaaddons.Features.QOL.NoSlowdown;
import com.xiaojia.xiaojiaaddons.Features.QOL.OneTick;
import com.xiaojia.xiaojiaaddons.Features.QOL.RemoveBlindness;
import com.xiaojia.xiaojiaaddons.Features.QOL.ShowBookName;
import com.xiaojia.xiaojiaaddons.Features.QOL.ShowEtherwarp;
import com.xiaojia.xiaojiaaddons.Features.QOL.SwordSwap;
import com.xiaojia.xiaojiaaddons.Features.QOL.TransferBack;
import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import com.xiaojia.xiaojiaaddons.Features.Remote.DungeonLoot;
import com.xiaojia.xiaojiaaddons.Features.Remote.DupedItems;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoCloseCrystalHollowsChest;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoPowder;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoPowderChest;
import com.xiaojia.xiaojiaaddons.Features.Skills.Experimentation;
import com.xiaojia.xiaojiaaddons.Features.Skills.Fishing;
import com.xiaojia.xiaojiaaddons.Features.Skills.Foraging;
import com.xiaojia.xiaojiaaddons.Features.Skills.GemstoneESP;
import com.xiaojia.xiaojiaaddons.Features.Skills.JadeCrystalHelper;
import com.xiaojia.xiaojiaaddons.Features.Skills.SuperPairs;
import com.xiaojia.xiaojiaaddons.Features.Slayers.ClickScreenMaddox;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Revenant;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Sven;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Tarantula;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Voidgloom;
import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayHandler;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.ScoreBoard;
import com.xiaojia.xiaojiaaddons.Objects.TestCubeGUI;
import com.xiaojia.xiaojiaaddons.Tests.TestM7;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CipherUtils;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.KeyBindUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
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
    public static final String VERSION = "2.4.3.1";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList<Setting> settings = Config.collect(Configs.class);
    public static GuiScreen guiToOpen = null;
    public static CipherUtils cipherUtils;
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
        try {
            cipherUtils = new CipherUtils();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MinecraftForge.EVENT_BUS.register(new TickEndEvent());
        MinecraftForge.EVENT_BUS.register(new Checker());

        Config.load();
        RoomLoader.load();
        ItemRename.load();
        ClientSocket.connect();
        DupedItems.load();

        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new PP());
        ClientCommandHandler.instance.registerCommand(new AutoForagingCommand());
        ClientCommandHandler.instance.registerCommand(new TransferBackCommand());
        ClientCommandHandler.instance.registerCommand(new com.xiaojia.xiaojiaaddons.Commands.XiaojiaChat());
        ClientCommandHandler.instance.registerCommand(new XiaojiaChatTest());
//        ClientCommandHandler.instance.registerCommand(new TestGui());
//        ClientCommandHandler.instance.registerCommand(new TestControl());

        MinecraftForge.EVENT_BUS.register(new XiaojiaChat());

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new DisplayHandler());

        // Utils
        MinecraftForge.EVENT_BUS.register(new ControlUtils());
        MinecraftForge.EVENT_BUS.register(new MathUtils());
        MinecraftForge.EVENT_BUS.register(new HotbarUtils());
        MinecraftForge.EVENT_BUS.register(new ScoreBoard());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());
        MinecraftForge.EVENT_BUS.register(new CommandsUtils());
        MinecraftForge.EVENT_BUS.register(new GuiTest());
        MinecraftForge.EVENT_BUS.register(new ShortbowUtils());
        MinecraftForge.EVENT_BUS.register(new TestCubeGUI());

        // Bestiary
        MinecraftForge.EVENT_BUS.register(new AutoScatha());
        MinecraftForge.EVENT_BUS.register(new Spider());
        MinecraftForge.EVENT_BUS.register(new SneakyCreeper());
        MinecraftForge.EVENT_BUS.register(new GolemAlert());

        // Dragons
        MinecraftForge.EVENT_BUS.register(new AutoShootCrystal());
        MinecraftForge.EVENT_BUS.register(new EnderCrystalESP());

        // Dungeons
        MinecraftForge.EVENT_BUS.register(new AutoBlaze());
        MinecraftForge.EVENT_BUS.register(new AutoBlood());
        MinecraftForge.EVENT_BUS.register(new AutoCloseSecretChest());
        MinecraftForge.EVENT_BUS.register(new AutoItemFrame());
        MinecraftForge.EVENT_BUS.register(new AutoLeap());
        MinecraftForge.EVENT_BUS.register(new AutoSalvage());
        MinecraftForge.EVENT_BUS.register(new AutoTerminal());
        MinecraftForge.EVENT_BUS.register(new AutoTerminalNew());
        MinecraftForge.EVENT_BUS.register(new BatESP());
        MinecraftForge.EVENT_BUS.register(new BloodAssist());
        MinecraftForge.EVENT_BUS.register(new CoordsGB());
        MinecraftForge.EVENT_BUS.register(new KeyESP());
        MinecraftForge.EVENT_BUS.register(new LividESP());
        MinecraftForge.EVENT_BUS.register(new M4ESP());
        MinecraftForge.EVENT_BUS.register(new M7Dragon());
        MinecraftForge.EVENT_BUS.register(new MimicWarn());
        MinecraftForge.EVENT_BUS.register(new ShowHiddenMobs());
        MinecraftForge.EVENT_BUS.register(new ShadowAssassinESP());
        MinecraftForge.EVENT_BUS.register(new StarredMobESP());
        MinecraftForge.EVENT_BUS.register(new StarredMobESPBox());
        MinecraftForge.EVENT_BUS.register(new StonklessStonk());
        MinecraftForge.EVENT_BUS.register(new TrapChestESP());
        // map
        MinecraftForge.EVENT_BUS.register(new Dungeon());
        MinecraftForge.EVENT_BUS.register(new MapUpdater());
        MinecraftForge.EVENT_BUS.register(new Map());
        // puzzles
        MinecraftForge.EVENT_BUS.register(new TeleportMaze());

        // Miscellaneous
        MinecraftForge.EVENT_BUS.register(new ChatCopy());
        MinecraftForge.EVENT_BUS.register(new ColorName());
        MinecraftForge.EVENT_BUS.register(new Velocity());
        MinecraftForge.EVENT_BUS.register(new NoRotate());
        MinecraftForge.EVENT_BUS.register(new RenderRank());
        MinecraftForge.EVENT_BUS.register(new KeepSprint());
        MinecraftForge.EVENT_BUS.register(new ShowLowestBin());

        // QOL
        MinecraftForge.EVENT_BUS.register(new AutoCombine());
        MinecraftForge.EVENT_BUS.register(new AutoHarp());
        MinecraftForge.EVENT_BUS.register(new AutoJerryBox());
        MinecraftForge.EVENT_BUS.register(new AutoLobby());
        MinecraftForge.EVENT_BUS.register(new AutoIsland());
        MinecraftForge.EVENT_BUS.register(new AutoUseItem());
        MinecraftForge.EVENT_BUS.register(new BlockAbility());
        MinecraftForge.EVENT_BUS.register(new DisableEntityRender());
        MinecraftForge.EVENT_BUS.register(new DisplayDayAndCoords());
        MinecraftForge.EVENT_BUS.register(new EntityQOL());
        MinecraftForge.EVENT_BUS.register(new FindFairy());
        MinecraftForge.EVENT_BUS.register(new GhostBlock());
        MinecraftForge.EVENT_BUS.register(new GhostQOL());
        MinecraftForge.EVENT_BUS.register(new HideCreepers());
        MinecraftForge.EVENT_BUS.register(new NearbyChestESP());
        MinecraftForge.EVENT_BUS.register(new InCombatQOL());
        MinecraftForge.EVENT_BUS.register(new NoSlowdown());
        MinecraftForge.EVENT_BUS.register(new OneTick());
        MinecraftForge.EVENT_BUS.register(new ShowBookName());
        MinecraftForge.EVENT_BUS.register(new ShowEtherwarp());
        MinecraftForge.EVENT_BUS.register(new SwordSwap());
        MinecraftForge.EVENT_BUS.register(new RemoveBlindness());
        MinecraftForge.EVENT_BUS.register(new HoldRightClick());
        MinecraftForge.EVENT_BUS.register(new TransferBack());

        // Skills
        MinecraftForge.EVENT_BUS.register(new AutoCloseCrystalHollowsChest());
        MinecraftForge.EVENT_BUS.register(new AutoPowder());
        MinecraftForge.EVENT_BUS.register(new AutoPowderChest());
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Fishing());
        MinecraftForge.EVENT_BUS.register(new GemstoneESP());
        MinecraftForge.EVENT_BUS.register(new JadeCrystalHelper());
        MinecraftForge.EVENT_BUS.register(new Experimentation());
        MinecraftForge.EVENT_BUS.register(new SuperPairs());

        // Slayer
        MinecraftForge.EVENT_BUS.register(new Sven());
        MinecraftForge.EVENT_BUS.register(new Revenant());
        MinecraftForge.EVENT_BUS.register(new Tarantula());
        MinecraftForge.EVENT_BUS.register(new Voidgloom());
        MinecraftForge.EVENT_BUS.register(new ClickScreenMaddox());

        // Remote
        MinecraftForge.EVENT_BUS.register(new DungeonLoot());
        MinecraftForge.EVENT_BUS.register(new LowestBin());
        MinecraftForge.EVENT_BUS.register(new DupedItems());
        MinecraftForge.EVENT_BUS.register(new ChestProfit());

        // Tests
        MinecraftForge.EVENT_BUS.register(new TestM7());

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
