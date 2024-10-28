package com.xiaojia.xiaojiaaddons;

import com.xiaojia.xiaojiaaddons.Commands.AutoForagingCommand;
import com.xiaojia.xiaojiaaddons.Commands.Command;
import com.xiaojia.xiaojiaaddons.Commands.FarmingPoint;
import com.xiaojia.xiaojiaaddons.Commands.FarmingType;
import com.xiaojia.xiaojiaaddons.Commands.PP;
import com.xiaojia.xiaojiaaddons.Commands.TransferBackCommand;
import com.xiaojia.xiaojiaaddons.Commands.XiaojiaChatTest;
import com.xiaojia.xiaojiaaddons.Config.Config;
import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Config.Setting.Setting;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoBack;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoBottle;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoClick;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoEat;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoMeat;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoQuestion;
import com.xiaojia.xiaojiaaddons.Features.Accentry.AutoRongYao;
import com.xiaojia.xiaojiaaddons.Features.Accentry.FastUse;
import com.xiaojia.xiaojiaaddons.Features.Accentry.HoverCommand;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.AutoScatha;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.AutoSneakyCreeper;
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
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Quiz;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.TeleportMaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.ThreeWeirdos;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.DevWater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.WaterSolver;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.SecretChecker;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.ShadowAssassinESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.ShowHiddenMobs;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.SimonSays;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESP;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StarredMobESPBox;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.TrapChestESP;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.*;
import com.xiaojia.xiaojiaaddons.Features.Nether.AshFangESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.AshFangGravityESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.ConvergenceESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.CorruptedESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Discipline;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.DojoUtils;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Force;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Mastery;
import com.xiaojia.xiaojiaaddons.Features.Nether.GhastESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.GolemESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.Kuudra;
import com.xiaojia.xiaojiaaddons.Features.Nether.PrismarineESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.SpongeESP;
import com.xiaojia.xiaojiaaddons.Features.Nether.XYZ;
import com.xiaojia.xiaojiaaddons.Features.QOL.AttributeFilter;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoAttribute;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoCombine;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoHarp;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoIsland;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoJerryBox;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoLobby;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoSnowball;
import com.xiaojia.xiaojiaaddons.Features.QOL.AutoUseItem;
import com.xiaojia.xiaojiaaddons.Features.QOL.BlockAbility;
import com.xiaojia.xiaojiaaddons.Features.QOL.BurrowHelper;
import com.xiaojia.xiaojiaaddons.Features.QOL.DisableEntityRender;
import com.xiaojia.xiaojiaaddons.Features.QOL.DisplayDayAndCoords;
import com.xiaojia.xiaojiaaddons.Features.QOL.EntityQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.FairySoul;
import com.xiaojia.xiaojiaaddons.Features.QOL.FindFairy;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostBlock;
import com.xiaojia.xiaojiaaddons.Features.QOL.GhostQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.HideCreepers;
import com.xiaojia.xiaojiaaddons.Features.QOL.HoldRightClick;
import com.xiaojia.xiaojiaaddons.Features.QOL.InCombatQOL;
import com.xiaojia.xiaojiaaddons.Features.QOL.MonolithESP;
import com.xiaojia.xiaojiaaddons.Features.QOL.NearbyChestESP;
import com.xiaojia.xiaojiaaddons.Features.QOL.NoSlowdown;
import com.xiaojia.xiaojiaaddons.Features.QOL.OneTick;
import com.xiaojia.xiaojiaaddons.Features.QOL.RelicESP;
import com.xiaojia.xiaojiaaddons.Features.QOL.RemoveBlindness;
import com.xiaojia.xiaojiaaddons.Features.QOL.RunicESP;
import com.xiaojia.xiaojiaaddons.Features.QOL.ShowAttribute;
import com.xiaojia.xiaojiaaddons.Features.QOL.ShowBookName;
import com.xiaojia.xiaojiaaddons.Features.QOL.ShowEtherwarp;
import com.xiaojia.xiaojiaaddons.Features.QOL.SwordSwap;
import com.xiaojia.xiaojiaaddons.Features.QOL.TransferBack;
import com.xiaojia.xiaojiaaddons.Features.Remote.API.ApiKey;
import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import com.xiaojia.xiaojiaaddons.Features.Remote.DungeonLoot;
import com.xiaojia.xiaojiaaddons.Features.Remote.DupedItems;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat;
import com.xiaojia.xiaojiaaddons.Features.Rift.BarrierESP;
import com.xiaojia.xiaojiaaddons.Features.Rift.ShowMirrorPath;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmPumpkin;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmVertical;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoCloseCrystalHollowsChest;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoPowder;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoPowderChest;
import com.xiaojia.xiaojiaaddons.Features.Skills.Experimentation;
import com.xiaojia.xiaojiaaddons.Features.Skills.Farming;
import com.xiaojia.xiaojiaaddons.Features.Skills.Fishing;
import com.xiaojia.xiaojiaaddons.Features.Skills.Foraging;
import com.xiaojia.xiaojiaaddons.Features.Skills.GemstoneESP;
import com.xiaojia.xiaojiaaddons.Features.Skills.JadeCrystalHelper;
import com.xiaojia.xiaojiaaddons.Features.Skills.SuperPairs;
import com.xiaojia.xiaojiaaddons.Features.Skills.TitaniumESP;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Blaze;
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
import com.xiaojia.xiaojiaaddons.Sounds.SoundHandler;
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
    public static final String VERSION = "2.5";
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList<Setting> settings = Config.collect(Configs.class);
    public static GuiScreen guiToOpen = null;
    public static CipherUtils cipherUtils;
    public static AutoSneakyCreeper autoSneakyCreeper;
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
//        ClientSocket.connect();
        DupedItems.load();
        RelicESP.load();

        ClientCommandHandler.instance.registerCommand(new Command());
        ClientCommandHandler.instance.registerCommand(new FarmingPoint());
        ClientCommandHandler.instance.registerCommand(new FarmingType());
        ClientCommandHandler.instance.registerCommand(new PP());
        ClientCommandHandler.instance.registerCommand(new AutoForagingCommand());
        ClientCommandHandler.instance.registerCommand(new TransferBackCommand());
        ClientCommandHandler.instance.registerCommand(new com.xiaojia.xiaojiaaddons.Commands.XiaojiaChat());
        ClientCommandHandler.instance.registerCommand(new XiaojiaChatTest());

        MinecraftForge.EVENT_BUS.register(new XiaojiaChat());

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new DisplayHandler());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());

        // AriaCraft
        MinecraftForge.EVENT_BUS.register(new AutoClick());
//        MinecraftForge.EVENT_BUS.register(new KillAll());
//        MinecraftForge.EVENT_BUS.register(new AutoPick());
        MinecraftForge.EVENT_BUS.register(new HoverCommand());
        MinecraftForge.EVENT_BUS.register(new AutoMeat());
        MinecraftForge.EVENT_BUS.register(new AutoRongYao());
        MinecraftForge.EVENT_BUS.register(new FastUse());
        MinecraftForge.EVENT_BUS.register(new AutoQuestion());
//        AutoMiNi mini = new AutoMiNi();
//        mini.init();
//        AutoMiQi miqi = new AutoMiQi();
//        miqi.init();
//        MinecraftForge.EVENT_BUS.register(mini);
//        MinecraftForge.EVENT_BUS.register(miqi);
        MinecraftForge.EVENT_BUS.register(new AutoBack());
        MinecraftForge.EVENT_BUS.register(new AutoEat());
        MinecraftForge.EVENT_BUS.register(new AutoBottle());
//        MinecraftForge.EVENT_BUS.register(new AutoRegenBow());

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
        MinecraftForge.EVENT_BUS.register(autoSneakyCreeper = new AutoSneakyCreeper());
        autoSneakyCreeper.init();
        MinecraftForge.EVENT_BUS.register(new GolemAlert());

        // Dragons
        MinecraftForge.EVENT_BUS.register(new AutoShootCrystal());
        MinecraftForge.EVENT_BUS.register(new EnderCrystalESP());

        // Dungeons
        MinecraftForge.EVENT_BUS.register(new AutoBlaze());
        MinecraftForge.EVENT_BUS.register(new AutoBlood());
        MinecraftForge.EVENT_BUS.register(new AutoCloseSecretChest());
        MinecraftForge.EVENT_BUS.register(new AutoItemFrame());
        MinecraftForge.EVENT_BUS.register(new SimonSays());
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
        MinecraftForge.EVENT_BUS.register(new SecretChecker());
        // map
        MinecraftForge.EVENT_BUS.register(new Dungeon());
        MinecraftForge.EVENT_BUS.register(new MapUpdater());
        MinecraftForge.EVENT_BUS.register(new Map());
        // puzzles
        MinecraftForge.EVENT_BUS.register(new TeleportMaze());
        MinecraftForge.EVENT_BUS.register(new Quiz());
        MinecraftForge.EVENT_BUS.register(new WaterSolver());
        MinecraftForge.EVENT_BUS.register(new ThreeWeirdos());

        // Miscellaneous
        MinecraftForge.EVENT_BUS.register(new ChatCopy());
        MinecraftForge.EVENT_BUS.register(new ChestFiller());
        MinecraftForge.EVENT_BUS.register(new ProtectItems());
        MinecraftForge.EVENT_BUS.register(new ColorName());
        MinecraftForge.EVENT_BUS.register(new Velocity());
        MinecraftForge.EVENT_BUS.register(new NoRotate());
        MinecraftForge.EVENT_BUS.register(new RenderRank());
        MinecraftForge.EVENT_BUS.register(new KeepSprint());
        MinecraftForge.EVENT_BUS.register(new ShowLowestBin());
        MinecraftForge.EVENT_BUS.register(new DevMode());
        MinecraftForge.EVENT_BUS.register(new MusicRune());
        MinecraftForge.EVENT_BUS.register(new PacketRelated());
        MinecraftForge.EVENT_BUS.register(new EasyTrigger());

//        MinecraftForge.EVENT_BUS.register(new CustomESP());

        // Nether
        MinecraftForge.EVENT_BUS.register(new CorruptedESP());
        MinecraftForge.EVENT_BUS.register(new GhastESP());
        MinecraftForge.EVENT_BUS.register(new Kuudra());
        MinecraftForge.EVENT_BUS.register(new ConvergenceESP());
        MinecraftForge.EVENT_BUS.register(new AshFangESP());
        MinecraftForge.EVENT_BUS.register(new AshFangGravityESP());
        MinecraftForge.EVENT_BUS.register(new SpongeESP());
        MinecraftForge.EVENT_BUS.register(new PrismarineESP());
        MinecraftForge.EVENT_BUS.register(new TitaniumESP());
        MinecraftForge.EVENT_BUS.register(new XYZ());
        MinecraftForge.EVENT_BUS.register(new GolemESP());
        // Dojo
        MinecraftForge.EVENT_BUS.register(new Force());
        MinecraftForge.EVENT_BUS.register(new Mastery());
        MinecraftForge.EVENT_BUS.register(new Discipline());
        MinecraftForge.EVENT_BUS.register(new DojoUtils());

        // QOL
        MinecraftForge.EVENT_BUS.register(new RunicESP());
        MinecraftForge.EVENT_BUS.register(new FairySoul());
        MinecraftForge.EVENT_BUS.register(new RelicESP());
        MinecraftForge.EVENT_BUS.register(new AutoSnowball());
        MinecraftForge.EVENT_BUS.register(new AutoCombine());
        MinecraftForge.EVENT_BUS.register(new AutoAttribute());
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
        MinecraftForge.EVENT_BUS.register(new MonolithESP());
        MinecraftForge.EVENT_BUS.register(new InCombatQOL());
        MinecraftForge.EVENT_BUS.register(new NoSlowdown());
        MinecraftForge.EVENT_BUS.register(new OneTick());
        MinecraftForge.EVENT_BUS.register(new ShowBookName());
        MinecraftForge.EVENT_BUS.register(new ShowEtherwarp());
        MinecraftForge.EVENT_BUS.register(new SwordSwap());
        MinecraftForge.EVENT_BUS.register(new RemoveBlindness());
        MinecraftForge.EVENT_BUS.register(new HoldRightClick());
        MinecraftForge.EVENT_BUS.register(new TransferBack());
        MinecraftForge.EVENT_BUS.register(new ShowAttribute());
        MinecraftForge.EVENT_BUS.register(new AttributeFilter());
        MinecraftForge.EVENT_BUS.register(new BurrowHelper());

        // Skills
        MinecraftForge.EVENT_BUS.register(new AutoCloseCrystalHollowsChest());
        MinecraftForge.EVENT_BUS.register(new AutoPowder());
        MinecraftForge.EVENT_BUS.register(new AutoPowderChest());
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Fishing());
        MinecraftForge.EVENT_BUS.register(new AutoBuildFarmVertical());
        MinecraftForge.EVENT_BUS.register(new AutoBuildFarmPumpkin());
        MinecraftForge.EVENT_BUS.register(new Farming());
        MinecraftForge.EVENT_BUS.register(new GemstoneESP());
        MinecraftForge.EVENT_BUS.register(new JadeCrystalHelper());
        MinecraftForge.EVENT_BUS.register(new Experimentation());
        MinecraftForge.EVENT_BUS.register(new SuperPairs());

        // Rift
        MinecraftForge.EVENT_BUS.register(new ShowMirrorPath());
        MinecraftForge.EVENT_BUS.register(new BarrierESP());

        // Slayer
        MinecraftForge.EVENT_BUS.register(new Blaze());
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
        MinecraftForge.EVENT_BUS.register(new ApiKey());

        // Tests
//        MinecraftForge.EVENT_BUS.register(new TestM7());
        MinecraftForge.EVENT_BUS.register(new DevWater());

        MinecraftForge.EVENT_BUS.register(new CommandKeybind());
        CommandKeybind.loadKeyBinds();

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
