package com.xiaojia.xiaojiaaddons.Config;

public class Configs {
    @Property(type = Property.Type.BOOLEAN, name = "Show XJA Message")
    public static boolean ShowXJAMessage = true;

    @Property(type = Property.Type.BOOLEAN, name = "Hide XJA mod id")
    public static boolean HideModID = true;

    // Miscellaneous
    @Property(type = Property.Type.FOLDER, name = "Miscellaneous")
    public static boolean MiscellaneousEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Chat Copy", parent = "Miscellaneous")
    public static boolean ChatCopy = false;

    // Dragons
    @Property(type = Property.Type.FOLDER, name = "Dragon")
    public static boolean DragonEnabled = false;

    // EnderCrystalESP
    @Property(type = Property.Type.BOOLEAN, name = "Ender Crystal ESP", parent = "Dragon")
    public static boolean CrystalESP = false;

    // AutoShootCrystal
    @Property(type = Property.Type.BOOLEAN, name = "Auto Shoot Crystal", parent = "Dragon", illegal = true)
    public static boolean AutoShootCrystal = false;

    @Property(type = Property.Type.NUMBER, name = "CD between shootings (ms)", parent = "Dragon",
            min = 100, max = 500, step = 50)
    public static int AutoShootCrystalCD = 150;


    // QOL
    @Property(type = Property.Type.FOLDER, name = "QOL")
    public static boolean QOLEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Nake Prevention", parent = "QOL",
            description = "When Auto-Swapping Armor")
    public static boolean NakePrevention = true;

    // DisplayDayAndCoords
    @Property(type = Property.Type.FOLDER, name = "Day and Coords Display", parent = "QOL")
    public static boolean DisplayDayAndCoords = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Day", parent = "Day and Coords Display")
    public static boolean DisplayDay = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Coords", parent = "Day and Coords Display")
    public static boolean DisplayCoords = false;

    @Property(type = Property.Type.NUMBER, name = "Display X", parent = "Day and Coords Display", step = 10)
    public static int DisplayDayX = 5;

    @Property(type = Property.Type.NUMBER, name = "Display Y", parent = "Day and Coords Display", step = 10)
    public static int DisplayDayY = 5;

    @Property(type = Property.Type.NUMBER, name = "Display Scale", parent = "Day and Coords Display", min = 5, max = 100, step = 5)
    public static int DisplayScale = 20;

    // OneTick
    @Property(type = Property.Type.FOLDER, name = "One Tick", parent = "QOL")
    public static boolean OneTick = false;

    @Property(type = Property.Type.BOOLEAN, name = "Aots With Anything (left click/keybind)", parent = "One Tick")
    public static boolean AotsWithAnything = false;

    @Property(type = Property.Type.BOOLEAN, name = "Soul Whip With Anything (left click/keybind)", parent = "One Tick")
    public static boolean SoulWhipWithAnything = false;

    // AutoIsland
    @Property(type = Property.Type.BOOLEAN, name = "Auto Island", parent = "QOL", description = "/is when evacuating to hub")
    public static boolean AutoIsland = false;

    // RemoveBlindness
    @Property(type = Property.Type.BOOLEAN, name = "Remove Blindness", parent = "QOL")
    public static boolean RemoveBlindness = false;

    // AutoHarp
    @Property(type = Property.Type.BOOLEAN, name = "Auto Harp", parent = "QOL")
    public static boolean AutoHarp = false;

    // AutoUseItem
    @Property(type = Property.Type.FOLDER, name = "Auto Use Item", description = "CD=0 <=> disable", parent = "QOL")
    public static boolean AutoUseItem = false;

    @Property(type = Property.Type.NUMBER, name = "Plasmaflux CD (s)", parent = "Auto Use Item",
            min = 0, max = 120, step = 10)
    public static int PlasmaFluxCD = 0;

    @Property(type = Property.Type.NUMBER, name = "Healing Wand CD (s)", parent = "Auto Use Item",
            min = 0, max = 30, step = 1)
    public static int HealingWandCD = 0;

    @Property(type = Property.Type.NUMBER, name = "Gyrokinetic Wand CD (s)", parent = "Auto Use Item",
            min = 0, max = 120, step = 1)
    public static int GyroCD = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Legit Use Item", parent = "Auto Use Item")
    public static boolean LegitAutoItem = false;

    // AutoCombine
    @Property(type = Property.Type.FOLDER, name = "Auto Combine", parent = "QOL")
    public static boolean AutoCombine = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Feather Falling", parent = "Auto Combine")
    public static boolean CombineFeatherFalling = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Infinite Quiver", parent = "Auto Combine")
    public static boolean CombineInfiniteQuiver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Rejuvenate", parent = "Auto Combine")
    public static boolean CombineRejuvenate = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Ultimate Wise", parent = "Auto Combine")
    public static boolean CombineUltimateWise = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Wisdom", parent = "Auto Combine")
    public static boolean CombineWisdom = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Last Stand", parent = "Auto Combine")
    public static boolean CombineLastStand = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Combo", parent = "Auto Combine")
    public static boolean CombineCombo = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Legion", parent = "Auto Combine")
    public static boolean CombineLegion = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Overload", parent = "Auto Combine")
    public static boolean CombineOverload = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Soul Eater", parent = "Auto Combine")
    public static boolean CombineSoulEater = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Ultimate Jerry", parent = "Auto Combine")
    public static boolean CombineUltimateJerry = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Bank", parent = "Auto Combine")
    public static boolean CombineBank = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine No Pain No Gain", parent = "Auto Combine")
    public static boolean CombineNoPainNoGain = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Mana Steal", parent = "Auto Combine")
    public static boolean CombineManaSteal = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Smarty Pants", parent = "Auto Combine")
    public static boolean CombineSmartyPants = false;


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

    @Property(type = Property.Type.BOOLEAN, name = "Hide Summons", parent = "Summon/Player Features", illegal = true,
            description = "Act like the summons dont exist")
    public static boolean HideSummons = false;

//    @Property(type = Property.Type.BOOLEAN, name = "Click Through Summons", parent = "Summon/Player Features", illegal = true)
//    public static boolean ClickThroughSummons = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Nearby Players", parent = "Summon/Player Features", illegal = true,
            description = "Act like the players dont exist")
    public static boolean HidePlayers = false;

    @Property(type = Property.Type.NUMBER, name = "Hide Nearby Players radius", parent = "Summon/Player Features",
            min = 0, max = 30, step = 1)
    public static int HidePlayerRadius = 4;

    // HideCreepers
    @Property(type = Property.Type.BOOLEAN, name = "Hide Creepers", parent = "QOL", illegal = true,
            description = "Act like the creepers dont exist, mine through / hit through")
    public static boolean HideCreepers = false;

//    @Property(type = Property.Type.BOOLEAN, name = "Click Through Players", parent = "Summon/Player Features", illegal = true)
//    public static boolean ClickThroughPlayers = false;

//    // FuckHarps
//    @Property(type = Property.Type.BOOLEAN, name = "Bald Harps", parent = "QOL")
//    public static boolean FuckHarps = false;


    // GhostBlock
    @Property(type = Property.Type.FOLDER, name = "Ghost Block", parent = "QOL")
    public static boolean GhostBlock = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block with Pickaxe", parent = "Ghost Block")
    public static boolean GhostBlockWithPickaxe = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block with KeyBind", parent = "Ghost Block")
    public static boolean GhostBlockWithKeyBind = false;

    // InCombatQOL
    @Property(type = Property.Type.FOLDER, name = "Auto Sell", parent = "QOL", description = "KeyBind")
    public static boolean AutoSellEnable = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Sell CD", parent = "Auto Sell", suffix = "ms", min = 50, max = 500, step = 5)
    public static int AutoSellCD = 200;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Dungeon Armor", parent = "Auto Sell", description = "/xj show dungarmor to see list")
    public static boolean AutoSellDungeonArmorEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Trash Dungeon Armor", parent = "Auto Sell Dungeon Armor")
    public static boolean AutoSellDungeonArmor = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Recomed Loots with Non-full Quality", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellRecomed = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Starred Loots", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellStarred = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Full Quality Loots with Non-recomed", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellFullQuality = true;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Full Quality Recomed Loots", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellFullQualityRecomed = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Dungeon Trash", parent = "Auto Sell", description = "/xj show dungtrash to see list")
    public static boolean AutoSellDungeonTrashEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Normal Dungeon Trash", parent = "Auto Sell Dungeon Trash")
    public static boolean AutoSellDungeonTrash = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Recomed Dungeon Trash", parent = "Auto Sell Dungeon Trash", description = "like Defuse Kit")
    public static boolean CanSellRecomedDungeonTrash = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Training Weight with >100 minutes held", parent = "Auto Sell Dungeon Trash")
    public static boolean CanSellTrainingWeightLong = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Cheap Runes", parent = "Auto Sell", description = "/xj show runes to see list")
    public static boolean AutoSellRunes = false;

    // NearbyChestESP
    @Property(type = Property.Type.FOLDER, name = "Nearby Chest ESP", parent = "QOL", description = "Radius = 10 * 10 * 10")
    public static boolean ChestESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Enable in Crystal Hollows", parent = "Nearby Chest ESP")
    public static boolean ChestESPCrystalHollows = false;

    // NoSlowdown
    @Property(type = Property.Type.FOLDER, name = "No Slowdown", parent = "QOL")
    public static boolean NoSlowdownEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for Rogue Sword", parent = "No Slowdown")
    public static boolean NoSlowdownRogue = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for Wither Blades", parent = "No Slowdown")
    public static boolean NoSlowdownWitherBlade = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for Katanas", parent = "No Slowdown")
    public static boolean NoSlowdownKatana = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for All Swords", parent = "No Slowdown")
    public static boolean NoSlowdownAll = false;

    // SwordSwap
    @Property(type = Property.Type.BOOLEAN, name = "Sword Swap", parent = "QOL", illegal = true)
    public static boolean GhostSwordSwap = false;

    // HoldRightClick
    @Property(type = Property.Type.FOLDER, name = "Hold Right Click", parent = "QOL")
    public static boolean HoldRightClick = false;

    @Property(type = Property.Type.BOOLEAN, name = "Terminator rightclick enable", illegal = true, parent = "Hold Right Click")
    public static boolean TerminatorAutoRightClick = false;

    @Property(type = Property.Type.NUMBER, name = "Terminator rightclick CPS", parent = "Hold Right Click",
            min = 10, max = 100, step = 5)
    public static int TerminatorCPS = 40;

    @Property(type = Property.Type.BOOLEAN, name = "Rogue Sword rightclick enable", illegal = true, parent = "Hold Right Click")
    public static boolean RogueAutoRightClick = false;

    @Property(type = Property.Type.NUMBER, name = "Rogue Sword rightclick CPS", parent = "Hold Right Click",
            min = 1, max = 250, step = 3)
    public static int RogueCPS = 10;

    @Property(type = Property.Type.NUMBER, name = "Max Speed for Rogue", parent = "Hold Right Click",
            min = 200, max = 500, step = 10)
    public static int MaxSpeed = 400;

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

    // AutoSalvage
    @Property(type = Property.Type.BOOLEAN, name = "Auto Click Salvage", parent = "Dungeons", description = "Won't salvage starred items.")
    public static boolean AutoClickSalvage = false;

    // CoordsGB
    @Property(type = Property.Type.BOOLEAN, name = "Coords Ghost Block", parent = "Dungeons")
    public static boolean CoordsGB = false;

    // LividESP
    @Property(type = Property.Type.FOLDER, name = "Show Livid", parent = "Dungeons")
    public static boolean ShowLivid = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Correct Livid", parent = "Show Livid")
    public static boolean ShowCorrectLivid = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Livid with ESP", parent = "Show Livid")
    public static boolean ShowCorrectLividWithESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Livid with Filled Box", parent = "Show Livid")
    public static boolean ShowCorrectLividWithFilledBox = false;

    // AutoLeap
    @Property(type = Property.Type.FOLDER, name = "Spirit Leap", parent = "Dungeons")
    public static boolean SpiritLeap = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Player Name in Leaps", parent = "Spirit Leap")
    public static boolean SpiritLeapName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Click Mage When opening Leaps", parent = "Spirit Leap", description = "Requires dungeon map enabled!")
    public static boolean AutoTpToMage = false;

    // TeleportMaze
    @Property(type = Property.Type.FOLDER, name = "Solvers", parent = "Dungeons")
    public static boolean Solver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Teleport Maze Solver", parent = "Solvers")
    public static boolean TeleportMazeSolver = false;

    // StarredMobESP
    @Property(type = Property.Type.FOLDER, name = "Starred Mob ESP", parent = "Dungeons")
    public static boolean StarredMobESPEnable = false;

    @Property(type = Property.Type.SELECT, name = "Starred Mob ESP Kind", parent = "Starred Mob ESP",
            options = {"Off", "Outline", "Box"})
    public static int StarredMobESP = 0;

    @Property(type = Property.Type.NUMBER, name = "ESP Outline Length", parent = "Starred Mob ESP",
            min = 1, max = 10, step = 1)
    public static int StarredMobESPOutlineLength = 6;

    @Property(type = Property.Type.SELECT, name = "ESP Color", parent = "Starred Mob ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int StarredMobESPColor = 1;

    // TrapChestESP
    @Property(type = Property.Type.BOOLEAN, name = "Trap Chest ESP", parent = "Dungeons")
    public static boolean TrapChestESP = false;

    // ShowHiddenMobs
    @Property(type = Property.Type.FOLDER, name = "Show Hidden Mobs", parent = "Dungeons")
    public static boolean ShowHiddenMobsEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Shadow Assassin", parent = "Show Hidden Mobs")
    public static boolean ShowShadowAssassin = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Stealthy Mobs", parent = "Show Hidden Mobs")
    public static boolean ShowStealthy = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Fels", parent = "Show Hidden Mobs")
    public static boolean ShowFels = false;

    // ShadowAssassinESP
    @Property(type = Property.Type.FOLDER, name = "Shadow Assassin ESP", parent = "Dungeons")
    public static boolean ShadowAssassinESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Shadow Assassin ESP Box", parent = "Shadow Assassin ESP")
    public static boolean ShadowAssassinESP = false;

    @Property(type = Property.Type.SELECT, name = "Shadow Assassin ESP Color", parent = "Shadow Assassin ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int ShadowAssassinESPColor = 0;

    // KeyESP
    @Property(type = Property.Type.FOLDER, name = "Wither/Blood Key ESP", parent = "Dungeons")
    public static boolean KeyESPEnable = false;

    @Property(type = Property.Type.SELECT, name = "Key ESP Type", parent = "Wither/Blood Key ESP",
            options = {"Off", "Bounding Box", "Filled Box"})
    public static int KeyESPType = 0;

    @Property(type = Property.Type.SELECT, name = "Wither Key ESP Color", parent = "Wither/Blood Key ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int WitherKeyColor = 12;

    @Property(type = Property.Type.SELECT, name = "Blood Key ESP Color", parent = "Wither/Blood Key ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int BloodKeyColor = 2;

    // M4ESP
    @Property(type = Property.Type.FOLDER, name = "M4 ESP", parent = "Dungeons")
    public static boolean M4ESPEnable = false;

    @Property(type = Property.Type.SELECT, name = "Spirit Bear ESP Type", parent = "M4 ESP",
            options = {"Off", "Bounding Box", "Filled Box"})
    public static int SpiritBearESPType = 0;

    @Property(type = Property.Type.SELECT, name = "Spirit Bow ESP Type", parent = "M4 ESP",
            options = {"Off", "Bounding Box", "Filled Box"})
    public static int SpiritBowESPType = 0;

    @Property(type = Property.Type.SELECT, name = "Wither Key ESP Color", parent = "M4 ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int SpiritBearColor = 11;

    @Property(type = Property.Type.SELECT, name = "Blood Key ESP Color", parent = "M4 ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int SpiritBowColor = 11;

    // BatESP
    @Property(type = Property.Type.FOLDER, name = "Bat ESP", parent = "Dungeons")
    public static boolean BatESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Bat ESP In Dungeons", parent = "Bat ESP")
    public static boolean BatESPDungeons = true;

    @Property(type = Property.Type.BOOLEAN, name = "Bat ESP Outside Dungeons", parent = "Bat ESP")
    public static boolean BatESPOutDungeons = false;

    // MimicWarn
    @Property(type = Property.Type.BOOLEAN, name = "Mimic Warn", parent = "Dungeons")
    public static boolean MimicWarn = false;

    // AutoTerminal
    @Property(type = Property.Type.FOLDER, name = "Terminals", parent = "Dungeons")
    public static boolean TerminalsEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Terminal", parent = "Terminals",
            description = "Auto Click, not detectable")
    public static boolean AutoTerminal = false;

    @Property(type = Property.Type.BOOLEAN, name = "0 ping for Auto Terminal", parent = "Terminals", illegal = true,
            description = "Click in Advance, detectable")
    public static boolean ZeroPingTerminal = false;

    @Property(type = Property.Type.NUMBER, name = "Clicks in Advance", parent = "Terminals", min = 1, max = 40)
    public static int TerminalClicksInAdvance = 10;

    @Property(type = Property.Type.NUMBER, name = "Auto Terminal CD", parent = "Terminals",
            min = 80, max = 500, step = 10)
    public static int AutoTerminalCD = 150;

    // AutoItemFrame
    @Property(type = Property.Type.FOLDER, name = "Auto Devices", parent = "Dungeons", description = "Not tested dont use!")
    public static boolean AutoDevices = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto P3-3 Arrows", parent = "Auto Devices")
    public static boolean AutoItemFrameArrows = false;

    @Property(type = Property.Type.NUMBER, name = "P3 Arrows CD", parent = "Auto Devices", min = 20, max = 500, step = 10)
    public static int ArrowCD = 100;

    @Property(type = Property.Type.NUMBER, name = "P3 Arrows CD Between ItemFrames", parent = "Auto Devices", min = 100, max = 500, step = 10)
    public static int ArrowCDBetween = 250;

    // AutoBlood
    @Property(type = Property.Type.FOLDER, name = "Auto Blood", parent = "Dungeons")
    public static boolean AutoBloodEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Blood Enable", parent = "Auto Blood", illegal = true)
    public static boolean AutoBlood = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Blood Yoffset (0.1x block)", parent = "Auto Blood",
            min = 0, max = 20, description = "Higher ping, higher offset!")
    public static int AutoBloodYoffset = 0;

    @Property(type = Property.Type.NUMBER, name = "Auto Blood CD (ms)", parent = "Auto Blood", illegal = true,
            min = 40, max = 200, step = 10)
    public static int AutoBloodCD = 60;


    // BloodAssist
    @Property(type = Property.Type.BOOLEAN, name = "Blood Assist", parent = "Dungeons")
    public static boolean BloodAssist = false;


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

    // SneakyCreeper
    @Property(type = Property.Type.FOLDER, name = "Gunpowder Mines", parent = "Bestiary")
    public static boolean GunpowderMinesEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Sneaky Creeper Name", parent = "Gunpowder Mines")
    public static boolean SneakyCreeperDisplayName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Sneaky Creeper Box", parent = "Gunpowder Mines")
    public static boolean SneakyCreeperDisplayBox = false;

    // GolemAlert
    @Property(type = Property.Type.BOOLEAN, name = "Golem Alert", parent = "Bestiary")
    public static boolean GolemAlert = false;

    // AutoScatha
    @Property(type = Property.Type.BOOLEAN, name = "Auto Scatha", parent = "Bestiary", description = "Keybind to start.")
    public static boolean AutoScatha = false;

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


    // Slayer
    @Property(type = Property.Type.FOLDER, name = "Slayer")
    public static boolean SlayerEnabled = false;

    // ClickScreenMaddox
    @Property(type = Property.Type.BOOLEAN, name = "Click Screen Maddox", parent = "Slayer")
    public static boolean ClickScreenMaddox = false;

    // Voidgloom
    @Property(type = Property.Type.FOLDER, name = "Voidgloom", parent = "Slayer")
    public static boolean EndermanEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Voidgloom miniboss ESP", parent = "Voidgloom")
    public static boolean EndermanMiniESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Voidgloom miniboss HP", parent = "Voidgloom")
    public static boolean ShowEndermanMiniHP = false;

    // Sven
    @Property(type = Property.Type.FOLDER, name = "Sven", parent = "Slayer")
    public static boolean WolfEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Sven miniboss ESP", parent = "Sven")
    public static boolean WolfMiniESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Sven miniboss HP", parent = "Sven")
    public static boolean ShowWolfMiniHP = false;


    // Skills
    @Property(type = Property.Type.FOLDER, name = "Skills")
    public static boolean SkillsEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Loot Chests in Crystal Hollows", parent = "Skills")
    public static boolean AutoCloseCrystalHollowsChest = false;

    // AutoPowder
    @Property(type = Property.Type.FOLDER, name = "Auto Powder", parent = "Skills")
    public static boolean AutoPowderEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Powder Enabled", parent = "Auto Powder", illegal = true,
            description = "AFK but low efficiency!")
    public static boolean AutoPowder = false;

    // AutoPowderChest
    @Property(type = Property.Type.BOOLEAN, name = "Auto Crystal Hollows Chest", parent = "Auto Powder", illegal = true,
            description = "Chests Only!")
    public static boolean AutoPowderChest = false;

    // Fishing
    @Property(type = Property.Type.BOOLEAN, name = "Auto Pulling Rod", parent = "Skills", illegal = true)
    public static boolean AutoPullRod = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Move Randomly", parent = "Skills", illegal = true)
    public static boolean AutoMove = false;

    // Foraging
    @Property(type = Property.Type.BOOLEAN, name = "Auto Foraging", parent = "Skills", illegal = true)
    public static boolean AutoForaging = false;

    // GemstoneESP
    @Property(type = Property.Type.FOLDER, name = "Gemstone ESP", parent = "Skills")
    public static boolean GemstoneESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Gemstone ESP Enable", parent = "Gemstone ESP")
    public static boolean GemstoneESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Include Glass Panes", parent = "Gemstone ESP")
    public static boolean IncludeGlassPanes = false;

    @Property(type = Property.Type.NUMBER, name = "Scan Radius", parent = "Gemstone ESP", suffix = " blocks", min = 5, max = 30)
    public static int GemstoneRadius = 15;

    @Property(type = Property.Type.CHECKBOX, name = "Ruby", parent = "Gemstone ESP")
    public static boolean RubyEsp = false;

    @Property(type = Property.Type.CHECKBOX, name = "Amber", parent = "Gemstone ESP")
    public static boolean AmberEsp = false;

    @Property(type = Property.Type.CHECKBOX, name = "Sapphire", parent = "Gemstone ESP")
    public static boolean SapphireEsp = false;

    @Property(type = Property.Type.CHECKBOX, name = "Jade", parent = "Gemstone ESP")
    public static boolean JadeEsp = false;

    @Property(type = Property.Type.CHECKBOX, name = "Amethyst", parent = "Gemstone ESP")
    public static boolean AmethystEsp = false;

    @Property(type = Property.Type.CHECKBOX, name = "Topaz", parent = "Gemstone ESP")
    public static boolean TopazEsp = false;

    @Property(type = Property.Type.CHECKBOX, name = "Jasper", parent = "Gemstone ESP")
    public static boolean JasperEsp = false;

    // Jade Crystal Helper
    @Property(type = Property.Type.FOLDER, name = "Jade Crystal Helper", parent = "Skills",
            description = "Explore yourself and have fun!")
    public static boolean JadeCrystalHelper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Enable Helper", parent = "Jade Crystal Helper",
            description = "Turn on Nearby Chest ESP!")
    public static boolean JadeCrystal = false;

    @Property(type = Property.Type.NUMBER, name = "Treasure CD", parent = "Jade Crystal Helper",
            description = "To ensure position corresponds with distance",
            min = 200, max = 600, step = 20)
    public static int JadeCrystalCD = 400;

    // Experimentation
    @Property(type = Property.Type.FOLDER, name = "Auto Experimentation Table", parent = "Skills")
    public static boolean ExperimentationTable = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Experimentation Table CD", parent = "Auto Experimentation Table",
            min = 50, max = 800, step = 50)
    public static int ExperimentClickCoolDown = 300;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Chronomatron", parent = "Auto Experimentation Table", illegal = true)
    public static boolean AutoChronomatron = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Ultrasequencer", parent = "Auto Experimentation Table", illegal = true)
    public static boolean AutoUltrasequencer = false;

    @Property(type = Property.Type.BOOLEAN, name = "Superpairs Solver", parent = "Auto Experimentation Table")
    public static boolean SuperpairsSolver = false;


    // Maps
    @Property(type = Property.Type.FOLDER, name = "Illegal Map", parent = "Dungeons",
            description = "Based on UnclaimedBloom6's dmap", illegal = true)
    public static boolean DmapEnabled = false;

    // Map
    @Property(type = Property.Type.FOLDER, name = "Map", parent = "Illegal Map")
    public static boolean MapPart = false;

    @Property(type = Property.Type.BOOLEAN, name = "Map Enabled", parent = "Map")
    public static boolean MapEnabled = true;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Scan", parent = "Map")
    public static boolean AutoScan = true;

    @Property(type = Property.Type.BOOLEAN, name = "Chat Info", parent = "Map",
            description = "Send chat when fully scanned")
    public static boolean ChatInfo = true;

    @Property(type = Property.Type.NUMBER, name = "Map Scale", parent = "Map", min = 1, max = 10)
    public static int MapScale = 5;

    @Property(type = Property.Type.NUMBER, name = "Player Head Scale (%)", parent = "Map",
            min = 1, max = 100)
    public static int HeadScale = 50;

    @Property(type = Property.Type.NUMBER, name = "Map X", parent = "Map",
            min = 0, max = 1000, step = 5)
    public static int MapX = 0;

    @Property(type = Property.Type.NUMBER, name = "Map Y", parent = "Map",
            min = 0, max = 1000, step = 5)
    public static int MapY = 0;

    @Property(type = Property.Type.SELECT, name = "Show Player Names", parent = "Map",
            options = {"Off", "Holding Leaps", "Always"})
    public static int ShowPlayerNames = 0;

    @Property(type = Property.Type.SELECT, name = "Player Name Color", parent = "Map",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int PlayerNameColor = 5;

    @Property(type = Property.Type.SELECT, name = "Self Icon Color", parent = "Map",
            options = {"Blue", "Green", "Red"})
    public static int SelfIconColor = 2;

    @Property(type = Property.Type.NUMBER, name = "Background Alpha", parent = "Map",
            min = 0, max = 255, step = 5)
    public static int BackgroundAlpha = 15;

    @Property(type = Property.Type.SELECT, name = "Checkmark Image", parent = "Map",
            options = {"Off", "Dmap Image", "New Image"})
    public static int DrawCheckMode = 2;

    // Rooms
    @Property(type = Property.Type.FOLDER, name = "Rooms", parent = "Illegal Map")
    public static boolean RoomPart = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Mimic Room", parent = "Rooms", description = "Currently useless")
    public static boolean ShowMimicRoom = true;

    @Property(type = Property.Type.SELECT, name = "Room Name Color", parent = "Rooms",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int RoomNameColor = 5;

    @Property(type = Property.Type.SELECT, name = "Wither Door Color", parent = "Rooms",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int WitherDoorColor = 1;

    @Property(type = Property.Type.BOOLEAN, name = "Darken Unexplored", parent = "Rooms")
    public static boolean DarkenUnexplored = true;

    @Property(type = Property.Type.FOLDER, name = "Show Room Name", parent = "Rooms")
    public static boolean RoomNamesEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Trap Room Name", parent = "Show Room Name")
    public static boolean ShowTrapName = true;

    @Property(type = Property.Type.BOOLEAN, name = "Puzzle Room Name", parent = "Show Room Name")
    public static boolean ShowPuzzleName = true;

    @Property(type = Property.Type.BOOLEAN, name = "Normal Room Name", parent = "Show Room Name")
    public static boolean ShowNormalName = false;

    //    @Property(type = Property.Type.SELECT, name = "Show Secrets", parent = "Rooms",
//            options = {"Off", "Small", "Large", "Replace Checkmarks"})
//    public static int ShowSecrets = 0;
    @Property(type = Property.Type.SELECT, name = "Show Secrets", parent = "Rooms",
            options = {"Off", "Small"})
    public static int ShowSecrets = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Always Highlight Wither doors", parent = "Rooms")
    public static boolean AlwaysHighlightWitherDoor = true;

    // TODO: Secrets replacing checkmarks

    // Score Calculation
    @Property(type = Property.Type.FOLDER, name = "Score", parent = "Illegal Map")
    public static boolean ScorePart = false;

    @Property(type = Property.Type.BOOLEAN, name = "Score Calculation", parent = "Score")
    public static boolean ScoreCalculation = true;

    @Property(type = Property.Type.BOOLEAN, name = "Announce 300", parent = "Score")
    public static boolean Announce300 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Title for 300 announce", parent = "Score")
    public static boolean DisplayAnnounce300 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Assume Spirit", parent = "Score")
    public static boolean AssumeSpirit = true;

    @Property(type = Property.Type.BOOLEAN, name = "Assume Paul", parent = "Score")
    public static boolean AssumePaul = false;
}
