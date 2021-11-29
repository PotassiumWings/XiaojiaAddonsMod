package com.xiaojia.xiaojiaaddons.Config;

public class Configs {
    // QOL

    // BlockAbility
    @Property(type = Property.Type.FOLDER, name = "Block Abilities")
    public static boolean BlockAbility = false;

    @Property(type = Property.Type.BOOLEAN, name = "Block Gloomlock Grimoire RightClick", parent = "Block Abilities")
    public static boolean BlockGloomlock = false;

    @Property(type = Property.Type.BOOLEAN, name = "Block Pickobulus in Private Island", parent = "Block Abilities")
    public static boolean BlockPickobulus = false;

    // EntityQOL
    @Property(type = Property.Type.FOLDER, name = "Summon/Player Features")
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
    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block")
    public static boolean GhostBlock = false;

    // SwordSwap
    @Property(type = Property.Type.BOOLEAN, name = "Sword Swap")
    public static boolean GhostSwordSwap = false;

    // Terminator
    @Property(type = Property.Type.BOOLEAN, name = "Terminator rightclick=100cps", illegal = true)
    public static boolean TerminatorAutoRightClick = false;


    // Dungeons

    // MimicWarn
    @Property(type = Property.Type.BOOLEAN, name = "Mimic Warn")
    public static boolean MimicWarn = false;

    // StonklessStonk
    @Property(type = Property.Type.BOOLEAN, name = "Stonkless Stonk")
    public static boolean StonklessStonk = false;


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
