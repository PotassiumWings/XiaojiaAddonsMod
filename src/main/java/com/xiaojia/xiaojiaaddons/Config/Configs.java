package com.xiaojia.xiaojiaaddons.Config;

public class Configs {
    // Dragons
    @Property(type = Property.Type.FOLDER, name = "Dragon")
    public static boolean DragonEnabled = false;

    // EnderCrystalESP
    @Property(type = Property.Type.BOOLEAN, name = "Ender Crystal ESP", parent = "Dragon")
    public static boolean CrystalESP = false;

    // AutoShootCrystal
    @Property(type = Property.Type.BOOLEAN, name = "Auto Shoot Crystal", parent = "Dragon", illegal = true)
    public static boolean AutoShootCrystal = false;


    // QOL
    @Property(type = Property.Type.FOLDER, name = "QOL")
    public static boolean QOLEnabled = false;

    // BlockAbility
    @Property(type = Property.Type.FOLDER, name = "Block Abilities", parent = "QOL")
    public static boolean BlockAbility = false;

    @Property(type = Property.Type.BOOLEAN, name = "Block Gloomlock Grimoire RightClick", parent = "Block Abilities")
    public static boolean BlockGloomlock = false;

    @Property(type = Property.Type.BOOLEAN, name = "Block Pickobulus in Private Island", parent = "Block Abilities")
    public static boolean BlockPickobulus = false;

    // EntityQOL
    @Property(type = Property.Type.FOLDER, name = "Summon/Player Features", parent = "QOL")
    public static boolean EntityQOL = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Summons", parent = "Summon/Player Features")
    public static boolean HideSummons = false;

    @Property(type = Property.Type.BOOLEAN, name = "Click Through Summons", parent = "Summon/Player Features", illegal = true)
    public static boolean ClickThroughSummons = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Nearby Players", parent = "Summon/Player Features")
    public static boolean HidePlayers = false;

    @Property(type = Property.Type.NUMBER, name = "Hide Nearby Players radius", parent = "Summon/Player Features")
    public static int HidePlayerRadius = 4;

    @Property(type = Property.Type.BOOLEAN, name = "Click Through Players", parent = "Summon/Player Features", illegal = true)
    public static boolean ClickThroughPlayers = false;

    // GhostBlock
    @Property(type = Property.Type.FOLDER, name = "Ghost Block", parent = "QOL")
    public static boolean GhostBlock = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block with Pickaxe", parent = "Ghost Block")
    public static boolean GhostBlockWithPickaxe = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block with KeyBind", parent = "Ghost Block")
    public static boolean GhostBlockWithKeyBind = false;

    // NoSlowdown
    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown", parent = "QOL")
    public static boolean NoSlowdown = false;

    // SwordSwap
    @Property(type = Property.Type.BOOLEAN, name = "Sword Swap", parent = "QOL")
    public static boolean GhostSwordSwap = false;

    // Terminator
    @Property(type = Property.Type.BOOLEAN, name = "Terminator rightclick=100cps", illegal = true, parent = "QOL")
    public static boolean TerminatorAutoRightClick = false;

    // GhostQOL
    @Property(type = Property.Type.FOLDER, name = "Ghost QOL", parent = "QOL")
    public static boolean GhostQOL = false;

    @Property(type = Property.Type.SELECT, name = "Visible Ghost", parent = "Ghost QOL", options = {"Disable", "OutlineBox", "FilledOutlineBox", "Vanilla Creeper"})
    public static int VisibleGhost = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Show Runic Ghost", parent = "Ghost QOL")
    public static boolean ShowRunicGhost = false;


    // Dungeons
    @Property(type = Property.Type.FOLDER, name = "Dungeons")
    public static boolean DungeonEnabled = false;

    // MimicWarn
    @Property(type = Property.Type.BOOLEAN, name = "Mimic Warn", parent = "Dungeons")
    public static boolean MimicWarn = false;

    // StonklessStonk
    @Property(type = Property.Type.FOLDER, name = "Stonkless Stonk", parent = "Dungeons")
    public static boolean StonklessStonk = false;

    @Property(type = Property.Type.BOOLEAN, name = "Stonkless Stonk Enable", parent = "Stonkless Stonk")
    public static boolean StonklessStonkEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Stonkless Stonk without Pickaxe", parent = "Stonkless Stonk", illegal = true)
    public static boolean StonklessStonkWithoutPickaxe = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Click Secret", parent = "Stonkless Stonk", illegal = true)
    public static boolean StonklessStonkAutoClickSecret = false;

    // AutoCloseSecretChest
    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Secret Chest", parent = "Dungeons")
    public static boolean AutoCloseSecretChest = false;


    // Bestiary
    @Property(type = Property.Type.FOLDER, name = "Bestiary")
    public static boolean BestiaryEnabled = false;

    // Spider
    @Property(type = Property.Type.FOLDER, name = "Spider", parent = "Bestiary")
    public static boolean SpiderEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Keeper Name", parent = "Spider")
    public static boolean ArachneKeeperDisplayName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Keeper Box", parent = "Spider")
    public static boolean ArachneKeeperDisplayBox = false;

    @Property(type = Property.Type.BOOLEAN, name = "Keeper Death Warn", parent = "Spider")
    public static boolean ArachneKeeperDeathWarn = false;

    @Property(type = Property.Type.SELECT, name = "Arachne Keeper Warn", parent = "Spider", options = {"No warn", "Chat", "Title"})
    public static int ArachneKeeperWarnMode = 0;

    @Property(type = Property.Type.BOOLEAN, name = "BroodMother Warn", parent = "Spider")
    public static boolean BroodMotherWarn = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show BroodMother Name", parent = "Spider")
    public static boolean BroodMotherDisplayName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show BroodMother Box", parent = "Spider")
    public static boolean BroodMotherDisplayBox = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Best Shadow Fury Point", parent = "Spider")
    public static boolean SpiderDenShadowfuryPoint = false;
}
