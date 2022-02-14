package com.xiaojia.xiaojiaaddons.Config;

public class Configs {
    // Misc
    @Property(type = Property.Type.FOLDER, name = "Misc")
    public static boolean MiscEnabled = false;

    @Property(type = Property.Type.FOLDER, name = "Chat Copy", parent = "Misc")
    public static boolean ChatCopyEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Chat Copy Enable", parent = "Chat Copy",
            description = "Append &8[&0C&8C]&r to every message.\n" +
                    "Click &8[&0C&r copies the unformatted message without color code,\n" +
                    "Click &8C]&r copies the whole message with color code.")
    public static boolean ChatCopy = false;

    @Property(type = Property.Type.FOLDER, name = "Keep Sprinting", parent = "Misc")
    public static boolean KeepSprintEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Keep Sprinting Enable", parent = "Keep Sprinting",
            description = "Keep holding sprint key. &cNOT A CHEAT!")
    public static boolean KeepSprint = false;

    @Property(type = Property.Type.FOLDER, name = "XJ Chat Settings", parent = "Misc",
            description = "&c/xc&r to see how to use this channel.")
    public static boolean XJCSetting = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Death Messages", parent = "XJ Chat Settings")
    public static boolean HideDeathMessages = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Puzzle Fail Messages", parent = "XJ Chat Settings")
    public static boolean HidePuzzleFailMessages = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Chat Messages", parent = "XJ Chat Settings")
    public static boolean HideChatMessages = false;

    @Property(type = Property.Type.FOLDER, name = "General XJA Setting", parent = "Misc")
    public static boolean GeneralSetting = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show XJA Message", parent = "General XJA Setting",
            description = "Show the messages starting with \"XJA >\".")
    public static boolean ShowXJAMessage = true;

    @Property(type = Property.Type.BOOLEAN, name = "Hide XJA mod id", parent = "General XJA Setting",
            description = "Not let the server know that you have XiaojiaAddons installed.\n" +
                    "&cRestart after changing required.")
    public static boolean HideModID = true;

    @Property(type = Property.Type.NUMBER, name = "Box Line Width", parent = "General XJA Setting",
            min = 1, max = 20, step = 1,
            description = "Line width of every bounding box in this mod.")
    public static int BoxLineWidth = 5;

    // ColorName
    @Property(type = Property.Type.FOLDER, name = "Color Name", parent = "Misc",
            description = "Replace all ranked name, like &b[MVP+] Xiaojia,\n" +
                    "to ranked colored name &6[RANK] &2Xiaojia&r;\n" +
                    "Replace all name, like Xiaojia,\n" +
                    "to colored name &2Xiaojia&r.\n" +
                    "&cWon't work while nicked!"
    )
    public static boolean ColorNameEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Color Name Chat", parent = "Color Name",
            description = "Color name in chat messages.")
    public static boolean ColorNameChat = true;

    @Property(type = Property.Type.BOOLEAN, name = "Color Name Tab", parent = "Color Name",
            description = "Color name in lobby tab or dungeon tab.\n" +
                    "&cOnly works in SkyBlock!")
    public static boolean ColorNameTab = true;

    @Property(type = Property.Type.BOOLEAN, name = "Color Name Scoreboard", parent = "Color Name",
            description = "Color name in dungeon scoreboard.")
    public static boolean ColorNameScoreboard = true;

    @Property(type = Property.Type.BOOLEAN, name = "Color Name Title", parent = "Color Name",
            description = "Color name in warning title, such as F7 terminals.")
    public static boolean ColorNameTitle = true;

    @Property(type = Property.Type.BOOLEAN, name = "Color Name NameTag", parent = "Color Name",
            description = "Color name in custom name tag of entities, such as pet.")
    public static boolean ColorNameNameTag = true;

    @Property(type = Property.Type.BOOLEAN, name = "Color Name Item", parent = "Color Name",
            description = "Color name in item name and lore, such as cake soul.")
    public static boolean ColorNameItem = true;


    // Bestiary
    @Property(type = Property.Type.FOLDER, name = "Bestiary")
    public static boolean BestiaryEnabled = false;

    // Dragons
    @Property(type = Property.Type.FOLDER, name = "Dragon", parent = "Bestiary")
    public static boolean DragonEnabled = false;

    // EnderCrystalESP
    @Property(type = Property.Type.BOOLEAN, name = "Ender Crystal ESP", parent = "Dragon",
            description = "Display a bounding box at Ender Crystal in Dragon's Nest.")
    public static boolean CrystalESP = false;

    // AutoShootCrystal
    @Property(type = Property.Type.BOOLEAN, name = "Auto Shoot Crystal", parent = "Dragon", illegal = true,
            description = "Auto aim ender crystals round-robin and\n" +
                    "right click Terminator during dragon's fight.\n" +
                    "&cRequires terminator in hotbar.\n" +
                    "&6Bind a key to toggle!\n" +
                    "&c&lPlease use this friendly and respect other players!")
    public static boolean AutoShootCrystal = false;

    @Property(type = Property.Type.NUMBER, name = "CD between shootings (ms)", parent = "Dragon",
            description = "Cool down between shootings.\n" +
                    "Not suggested to set too low.",
            min = 50, max = 500, step = 5)
    public static int AutoShootCrystalCD = 150;

    // SneakyCreeper
    @Property(type = Property.Type.FOLDER, name = "Gunpowder Mines", parent = "Bestiary")
    public static boolean GunpowderMinesEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Sneaky Creeper Name", parent = "Gunpowder Mines",
            description = "Display sneaky creepers' name at their feet.")
    public static boolean SneakyCreeperDisplayName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Sneaky Creeper Box", parent = "Gunpowder Mines",
            description = "Display green esp box at sneaky creepers.")
    public static boolean SneakyCreeperDisplayBox = false;

    // GolemAlert
    @Property(type = Property.Type.FOLDER, name = "Golem", parent = "Bestiary", description = "Endstone Protector features.")
    public static boolean GolemAlertEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Golem Alert", parent = "Golem",
            description = "Display count down when golem is spawning,\n" +
                    "in the format of xx:xx (seconds).")
    public static boolean GolemAlert = false;

    // AutoScatha
    @Property(type = Property.Type.FOLDER, name = "Crystal Hollows", parent = "Bestiary")
    public static boolean CrystalHollowsBestiary = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Scatha", parent = "Crystal Hollows",
            description = "Auto mine 1x2 tunnels in Crystal Hollows.\n" +
                    "Suggest setup: renowned sorrow + daedelus axe +\n" +
                    "lucky clover bal pet in y=31 Magma Fields.\n" +
                    "Stops when: \n" +
                    "1. Digged to the end of the tunnel (Bedrock).\n" +
                    "2. Stopped due to lag or unmineable stairs.\n" +
                    "3. View direction changed.\n" +
                    "4. Y coords changed.\n" +
                    "Plays a sound when worm spawned or it stopped.\n" +
                    "&cSet a keybinding to toggle.")
    public static boolean AutoScatha = false;

    // Spider
    @Property(type = Property.Type.FOLDER, name = "Spider", parent = "Bestiary")
    public static boolean SpiderEnabled = false;

    @Property(type = Property.Type.FOLDER, name = "Arachne's Keeper", parent = "Spider")
    public static boolean KeeperEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Keeper Name", parent = "Arachne's Keeper",
            description = "Display Arachne's Keeper's name near it.")
    public static boolean ArachneKeeperDisplayName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Keeper Box", parent = "Arachne's Keeper",
            description = "Display a bounding box at Arachne's Keepers.")
    public static boolean ArachneKeeperDisplayBox = false;

    @Property(type = Property.Type.BOOLEAN, name = "Keeper Death Warn", parent = "Arachne's Keeper",
            description = "When Arachne's Keepers die, display a chat message.\n" +
                    "&4&lKeeper died!")
    public static boolean ArachneKeeperDeathWarn = false;

    @Property(type = Property.Type.SELECT, name = "Arachne Keeper Warn", parent = "Arachne's Keeper",
            options = {"No warn", "Chat", "Title"},
            description = "Warn if Arachne's Keepers is in the lobby.\n" +
                    "&6Chat&r: Display a chat message, &c&lArachne's Keeper is in this lobby!\n" +
                    "&6Title&r: Display a title in the center of screen, &c&lArachne's Keeper")
    public static int ArachneKeeperWarnMode = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Show Best Shadow Fury Points", parent = "Arachne's Keeper",
            description = "Shows the best shadow fury points when grinding keepers.\n" +
                    "They're in the gravel part of Spider's Den.")
    public static boolean SpiderDenShadowfuryPoint = false;

    @Property(type = Property.Type.FOLDER, name = "Brood Mother", parent = "Spider")
    public static boolean BroodMotherEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "BroodMother Warn", parent = "Brood Mother",
            description = "Display a title in the center of screen, &5&lBrood Mother&r.")
    public static boolean BroodMotherWarn = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show BroodMother Name", parent = "Brood Mother",
            description = "Display Brood Mother's name near it.")
    public static boolean BroodMotherDisplayName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show BroodMother Box", parent = "Brood Mother",
            description = "Display a bounding box at the Brood Mother.")
    public static boolean BroodMotherDisplayBox = false;


    // QOL
    @Property(type = Property.Type.FOLDER, name = "QOL")
    public static boolean QOLEnabled = false;

    @Property(type = Property.Type.FOLDER, name = "In Combat QOL", parent = "QOL",
            description = "Set keybindings to swap armor (slot 0-8),\n" +
                    "Open trade menu, and Auto Selling.\n" +
                    "There has been known crashes with Skytils / DSM.")
    public static boolean InCombatQOLEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Nake Prevention", parent = "In Combat QOL",
            description = "When Auto-Swapping Armor")
    public static boolean NakePrevention = true;

    @Property(type = Property.Type.BOOLEAN, name = "Fast Mode", parent = "In Combat QOL",
            description = "Make Auto-Swapping Armor, Auto Selling or Open Trade faster.")
    public static boolean InCombatFastMode = false;

    // InCombatQOL
    @Property(type = Property.Type.FOLDER, name = "Auto Sell", parent = "QOL",
            description = "Sell items in inventory &cexcludes&r hotbar.\n" +
                    "Set a keybinding to work.\n" +
                    "Can be done while in combat.")
    public static boolean AutoSellEnable = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Sell CD", parent = "Auto Sell", suffix = "ms", min = 50, max = 500, step = 5,
            description = "Cool down between selling two items.")
    public static int AutoSellCD = 200;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Dungeon Armor", parent = "Auto Sell",
            description = "&c/xj show dungarmor&r to see selling list")
    public static boolean AutoSellDungeonArmorEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Trash Dungeon Armor", parent = "Auto Sell Dungeon Armor",
            description = "Auto sell dungeon armor with no stars, not full quality\nand no recomed")
    public static boolean AutoSellDungeonArmor = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Recomed Loots with Non-full Quality", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellRecomed = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Starred Loots", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellStarred = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Full Quality Loots with Non-recomed", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellFullQuality = true;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Full Quality Recomed Loots", parent = "Auto Sell Dungeon Armor")
    public static boolean CanAutoSellFullQualityRecomed = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Dungeon Trash", parent = "Auto Sell",
            description = "&c/xj show dungtrash&r to see list")
    public static boolean AutoSellDungeonTrashEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Normal Dungeon Trash", parent = "Auto Sell Dungeon Trash")
    public static boolean AutoSellDungeonTrash = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Recomed Dungeon Trash", parent = "Auto Sell Dungeon Trash", description = "like Defuse Kit")
    public static boolean CanSellRecomedDungeonTrash = false;

    @Property(type = Property.Type.BOOLEAN, name = "Can Sell Training Weight with >100 minutes held", parent = "Auto Sell Dungeon Trash")
    public static boolean CanSellTrainingWeightLong = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Miscellaneous", parent = "Auto Sell")
    public static boolean AutoSellMisc = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Cheap Runes", parent = "Auto Sell Miscellaneous",
            description = "&c/xj show runes&r to see list")
    public static boolean AutoSellRunes = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Superboom TNT", parent = "Auto Sell Miscellaneous")
    public static boolean AutoSellSuperboom = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Fishing Stuff", parent = "Auto Sell")
    public static boolean AutoSellFishing = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Ice Rod", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellIceRod = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Mining Stuff", parent = "Auto Sell")
    public static boolean AutoSellMining = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Ascension Rope", parent = "Auto Sell Mining Stuff")
    public static boolean AutoSellAscensionRope = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Wishing Compass", parent = "Auto Sell Mining Stuff")
    public static boolean AutoSellWishingCompass = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Fine Gemstone", parent = "Auto Sell Mining Stuff")
    public static boolean AutoSellFineGem = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Enchanted Book", parent = "Auto Sell")
    public static boolean AutoSellBook = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Bank I-V", parent = "Auto Sell Enchanted Book")
    public static boolean AutoSellBank = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell No Pain No Gain I-V", parent = "Auto Sell Enchanted Book")
    public static boolean AutoSellNoPainNoGain = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Ultimate Jerry I-V", parent = "Auto Sell Enchanted Book")
    public static boolean AutoSellUltimateJerry = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Feather Falling VI-VII", parent = "Auto Sell Enchanted Book")
    public static boolean AutoSellFeatherFalling = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Infinite Quiver VI-VII", parent = "Auto Sell Enchanted Book")
    public static boolean AutoSellInfiniteQuiver = false;

    // DisplayDayAndCoords
    @Property(type = Property.Type.FOLDER, name = "Information Display", parent = "QOL")
    public static boolean DisplayDayAndCoords = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Day", parent = "Information Display",
            description = "Display the age of current lobby, like Day 35")
    public static boolean DisplayDay = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Ping", parent = "Information Display",
            description = "Display current ping in ms, &conly works in SkyBlock!")
    public static boolean DisplayPing = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Coords", parent = "Information Display",
            description = "Display current block you're at.")
    public static boolean DisplayCoords = false;

    @Property(type = Property.Type.NUMBER, name = "Display X", parent = "Information Display", max = 999)
    public static int DisplayDayX = 5;

    @Property(type = Property.Type.NUMBER, name = "Display Y", parent = "Information Display", max = 999)
    public static int DisplayDayY = 5;

    @Property(type = Property.Type.NUMBER, name = "Display Scale", parent = "Information Display", min = 5, max = 100)
    public static int DisplayScale = 20;

    // OneTick
    @Property(type = Property.Type.FOLDER, name = "One Tick", parent = "QOL",
            description = "Swap to something, right click it, and swap back.\n" +
                    "One swap is in 1 tick.\n" +
                    "Can be triggered by left click or keybinding press.\n" +
                    "&6Both requires keybinding to work!!!\n" +
                    "&cLeft click ability of current item can't be done normally.\n" +
                    "Auto disabled when holding Gyrokinetic Wand or Gloomlock Grimoire.\n" +
                    "Auto disabled when holding Terminator.")
    public static boolean OneTick = false;

    @Property(type = Property.Type.BOOLEAN, name = "Aots With Anything", parent = "One Tick",
            description = "One Tick with item which has a display name contains 'Axe of the \nShredded'.")
    public static boolean AotsWithAnything = false;

    @Property(type = Property.Type.BOOLEAN, name = "Soul Whip With Anything", parent = "One Tick",
            description = "One Tick with item which has a display name contains'Soul Whip'.")
    public static boolean SoulWhipWithAnything = false;

    // AutoUseItem
    @Property(type = Property.Type.FOLDER, name = "Auto Use Item", parent = "QOL",
            description = "Use (right click) items automatically with cool downs of each item.")
    public static boolean AutoUseItem = false;

    @Property(type = Property.Type.NUMBER, name = "Plasmaflux CD (s)", parent = "Auto Use Item",
            min = 0, max = 120)
    public static int PlasmaFluxCD = 0;

    @Property(type = Property.Type.NUMBER, name = "Healing Wand CD (s)", parent = "Auto Use Item",
            min = 0, max = 30)
    public static int HealingWandCD = 0;

    @Property(type = Property.Type.NUMBER, name = "Gyrokinetic Wand CD (s)", parent = "Auto Use Item",
            min = 0, max = 120)
    public static int GyroCD = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Legit Use Item", parent = "Auto Use Item",
            description = "Delay 3-4 ticks between actions.")
    public static boolean LegitAutoItem = false;

    // AutoCombine
    @Property(type = Property.Type.FOLDER, name = "Auto Combine", parent = "QOL",
            description = "Auto Combine books in anvil menu.")
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
    @Property(type = Property.Type.FOLDER, name = "Hide Entity Features", parent = "QOL")
    public static boolean EntityQOL = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Summons", parent = "Hide Entity Features", illegal = true,
            description = "Act like the summons dont exist, can click through")
    public static boolean HideSummons = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Nearby Players", parent = "Hide Entity Features", illegal = true,
            description = "Act like the players dont exist, can click through")
    public static boolean HidePlayers = false;

    @Property(type = Property.Type.NUMBER, name = "Hide Nearby Players radius", parent = "Hide Entity Features",
            min = 0, max = 30)
    public static int HidePlayerRadius = 4;

    // HideCreepers
    @Property(type = Property.Type.BOOLEAN, name = "Hide Creepers", parent = "Hide Entity Features", illegal = true,
            description = "Act like the creepers dont exist, can mine through / hit through")
    public static boolean HideCreepers = false;

    // GhostBlock
    @Property(type = Property.Type.FOLDER, name = "Ghost Block", parent = "QOL",
            description = "Won't create ghost block of bedrock or interactive blocks.\n" +
                    "&cUse at your own risk!")
    public static boolean GhostBlock = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block with Pickaxe", parent = "Ghost Block",
            description = "Right click a pickaxe to create one ghost block.")
    public static boolean GhostBlockWithPickaxe = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghost Block with KeyBind", parent = "Ghost Block",
            description = "Create a keybinding to use!")
    public static boolean GhostBlockWithKeyBind = false;

    // NoSlowdown
    @Property(type = Property.Type.FOLDER, name = "No Slowdown", parent = "QOL",
            description = "Let using sword not slow down your speed \nnor play blocking animation.\n" +
                    "Same as 1.9+ version.")
    public static boolean NoSlowdownEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for Rogue Sword", parent = "No Slowdown")
    public static boolean NoSlowdownRogue = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for Wither Blades", parent = "No Slowdown")
    public static boolean NoSlowdownWitherBlade = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for Katanas", parent = "No Slowdown")
    public static boolean NoSlowdownKatana = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown for All Swords", parent = "No Slowdown")
    public static boolean NoSlowdownAll = false;

    // HoldRightClick
    @Property(type = Property.Type.FOLDER, name = "Hold Right Click", parent = "QOL")
    public static boolean HoldRightClick = false;

    @Property(type = Property.Type.BOOLEAN, name = "Terminator rightclick Enable", illegal = true, parent = "Hold Right Click",
            description = "Holding Terminator = high CPS right click, making it shoot faster.")
    public static boolean TerminatorAutoRightClick = false;

    @Property(type = Property.Type.NUMBER, name = "Terminator rightclick CPS", parent = "Hold Right Click",
            min = 10, max = 100)
    public static int TerminatorCPS = 40;

    @Property(type = Property.Type.BOOLEAN, name = "Rogue Sword rightclick Enable", illegal = true, parent = "Hold Right Click",
            description = "Holding Rogue Sword = high CPS right click, \n" +
                    "till it thinks you have reached the expected speed.\n" +
                    "Due to the bug of rogue sword, speed after right click\n" +
                    "might not be accurate.")
    public static boolean RogueAutoRightClick = false;

    @Property(type = Property.Type.NUMBER, name = "Rogue Sword rightclick CPS", parent = "Hold Right Click",
            min = 1, max = 250, step = 3)
    public static int RogueCPS = 10;

    @Property(type = Property.Type.NUMBER, name = "Max Speed for Rogue", parent = "Hold Right Click",
            min = 100, max = 500, step = 5)
    public static int MaxSpeed = 400;

    // GhostQOL
    @Property(type = Property.Type.FOLDER, name = "Ghost QOL", parent = "QOL")
    public static boolean GhostQOL = false;

    @Property(type = Property.Type.SELECT, name = "Visible Ghost", parent = "Ghost QOL",
            options = {"Disable", "OutlineBox", "FilledOutlineBox", "Vanilla Creeper"},
            description = "Try it out!")
    public static int VisibleGhost = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Show Runic Ghost", parent = "Ghost QOL",
            description = "Display a green bounding box at runic ghost, and show its HP.\n" +
                    "Works bad at Derpy mayor, will fix it later.")
    public static boolean ShowRunicGhost = false;


    @Property(type = Property.Type.FOLDER, name = "Other QOLs", parent = "QOL")
    public static boolean OtherQOL = false;

    // NearbyChestESP
    @Property(type = Property.Type.BOOLEAN, name = "Crystal Hollows Nearby Chest ESP", parent = "Other QOLs",
            description = "Radius = 10 * 10 * 10")
    public static boolean ChestESPCrystalHollows = false;

    // SwordSwap
    @Property(type = Property.Type.BOOLEAN, name = "Ghost Sword Swap", parent = "Other QOLs", illegal = true,
            description = "Auto right click Soul Whip and\n" +
                    "swap to Emerald Blade or Giant's Sword,\n" +
                    "every 0.5s.\n" +
                    "This method was useful for grinding ghosts.\n" +
                    "Use a keybinding to toggle.")
    public static boolean GhostSwordSwap = false;

    // AutoLobby
    @Property(type = Property.Type.BOOLEAN, name = "Auto Lobby", parent = "Other QOLs",
            description = "/l when falling to the void.\n" +
                    "Only works in SkyBlock, out of dungeons.\n" +
                    "&cTurn this off if you're building at y < 15!")
    public static boolean AutoLobby = false;

    // AutoIsland
    @Property(type = Property.Type.BOOLEAN, name = "Auto Island", parent = "Other QOLs",
            description = "/is when evacuating to hub")
    public static boolean AutoIsland = false;

    // RemoveBlindness
    @Property(type = Property.Type.BOOLEAN, name = "Remove Blindness", parent = "Other QOLs",
            description = "Remove Blindness potion effect. Useful in F5/M5.")
    public static boolean RemoveBlindness = false;

    // AutoHarp
    @Property(type = Property.Type.BOOLEAN, name = "Auto Harp", parent = "Other QOLs",
            description = "Auto click the harp songs.\n" +
                    "Lower packet loss rate and better server \n" +
                    "equals better success rate.")
    public static boolean AutoHarp = false;


    // Dungeons
    @Property(type = Property.Type.FOLDER, name = "Dungeons")
    public static boolean DungeonEnabled = false;

    // CoordsGB
    @Property(type = Property.Type.FOLDER, name = "Coords Ghost Block", parent = "Dungeons")
    public static boolean CoordsGBEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Coords Ghost Block Enable", parent = "Coords Ghost Block",
            description = "Creates Ghost Block on F7 from Phase 2 to Phase 3,\n" +
                    "&cNeed keybinding to toggle to work every launch of game.")
    public static boolean CoordsGB = false;

    // LividESP
    @Property(type = Property.Type.FOLDER, name = "Show Livid", parent = "Dungeons",
            description = "Show correct livid in F5/M5 Boss.")
    public static boolean ShowLivid = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Correct Livid", parent = "Show Livid")
    public static boolean ShowCorrectLivid = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Livid with ESP", parent = "Show Livid",
            description = "To set if the ESP Box can be seen through walls.")
    public static boolean ShowCorrectLividWithESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Livid with Filled Box", parent = "Show Livid",
            description = "To set whether the ESP Box is filled (true) or bounding (false).")
    public static boolean ShowCorrectLividWithFilledBox = false;

    // AutoLeap
    @Property(type = Property.Type.FOLDER, name = "Spirit Leap", parent = "Dungeons")
    public static boolean SpiritLeap = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Player Name in Leaps", parent = "Spirit Leap")
    public static boolean SpiritLeapName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Click Mage When opening Leaps", parent = "Spirit Leap",
            description = "Requires dungeon map enabled!\n" +
                    "Disable sba 'Disable Mort Message' and 'Disable Boss Message'!")
    public static boolean AutoTpToMage = false;

    // TeleportMaze
    @Property(type = Property.Type.FOLDER, name = "Solvers", parent = "Dungeons")
    public static boolean Solver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Teleport Maze Solver", parent = "Solvers",
            description = "Green Pad = next tp is chest;\n" +
                    "Red Pad = don't tp there;\n" +
                    "Blue Pad = has a chance to be final tp.")
    public static boolean TeleportMazeSolver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Blaze", parent = "Solvers",
            description = "Bind a key to use!")
    public static boolean AutoBlaze = false;

    // StarredMobESP
    @Property(type = Property.Type.FOLDER, name = "Starred Mob ESP", parent = "Dungeons")
    public static boolean StarredMobESPEnable = false;

    @Property(type = Property.Type.SELECT, name = "Starred Mob ESP Kind", parent = "Starred Mob ESP",
            options = {"Off", "Outline", "Box"},
            description = "Outline looks better, but can miss some mobs.\n" +
                    "Box looks uglier, but can show all starred mobs.")
    public static int StarredMobESP = 0;

    @Property(type = Property.Type.NUMBER, name = "ESP Outline Length", parent = "Starred Mob ESP",
            min = 1, max = 10, step = 1,
            description = "If you chose 'Outline' kind, this is the line width of the outline.")
    public static int StarredMobESPOutlineLength = 6;

    @Property(type = Property.Type.SELECT, name = "ESP Color", parent = "Starred Mob ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int StarredMobESPColor = 1;

    // TrapChestESP
    @Property(type = Property.Type.FOLDER, name = "Trap Chest ESP", parent = "Dungeons")
    public static boolean TrapChestESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Trap Chest ESP Enable", parent = "Trap Chest ESP",
            description = "Display a red ESP box at trap chests. Can be mimic.")
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

    @Property(type = Property.Type.BOOLEAN, name = "Shadow Assassin ESP Box", parent = "Shadow Assassin ESP",
            description = "Display a bounding box at Shadow Assassins.")
    public static boolean ShadowAssassinESP = false;

    @Property(type = Property.Type.SELECT, name = "Shadow Assassin ESP Color", parent = "Shadow Assassin ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"},
            description = "Color of the bounding box.")
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

    @Property(type = Property.Type.SELECT, name = "Spirit Bear ESP Color", parent = "M4 ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int SpiritBearColor = 11;

    @Property(type = Property.Type.SELECT, name = "Spirit Bow ESP Color", parent = "M4 ESP",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int SpiritBowColor = 11;

    // BatESP
    @Property(type = Property.Type.FOLDER, name = "Bat ESP", parent = "Dungeons")
    public static boolean BatESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Bat ESP In Dungeons", parent = "Bat ESP")
    public static boolean BatESPDungeons = false;

    @Property(type = Property.Type.BOOLEAN, name = "Bat ESP Outside Dungeons", parent = "Bat ESP")
    public static boolean BatESPOutDungeons = false;

    // MimicWarn
    @Property(type = Property.Type.FOLDER, name = "Mimic Warn", parent = "Dungeons")
    public static boolean MimicWarnEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Mimic Warn Enabled", parent = "Mimic Warn",
            description = "/pc Mimic Dead! when \n" +
                    "detected a Little Zombie's death in dungeons.\n" +
                    "Not suggested to use other messages, \n" +
                    "because detecting teammates' mimic death messages\n" +
                    "is used for score calculation.")
    public static boolean MimicWarn = false;

    // AutoTerminal
    @Property(type = Property.Type.FOLDER, name = "Terminals", parent = "Dungeons")
    public static boolean TerminalsEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Terminal", parent = "Terminals",
            description = "Auto Click terminals")
    public static boolean AutoTerminal = false;

    @Property(type = Property.Type.BOOLEAN, name = "0 ping for Auto Terminal", parent = "Terminals", illegal = true,
            description = "Click terminals in advance, before receiving packets.")
    public static boolean ZeroPingTerminal = false;

    @Property(type = Property.Type.NUMBER, name = "Clicks in Advance", parent = "Terminals", min = 1, max = 40,
            description = "Number of clicks in advance. Suggest to be 10.")
    public static int TerminalClicksInAdvance = 10;

    @Property(type = Property.Type.NUMBER, name = "Auto Terminal CD", parent = "Terminals",
            min = 80, max = 500, step = 10,
            description = "Cool down between clicks.")
    public static int AutoTerminalCD = 150;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Quit Long Maze", parent = "Terminals",
            description = "Close screen when > 20 clicks needed.")
    public static boolean QuitWhenLongMaze = false;

    // AutoItemFrame
    @Property(type = Property.Type.FOLDER, name = "Auto Devices", parent = "Dungeons")
    public static boolean AutoDevices = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto P3-3 Arrows", parent = "Auto Devices",
            description = "When aiming at the itemframe in phase 3 - 3 arrows device,\n" +
                    "automatically rotate the arrows to pass.")
    public static boolean AutoItemFrameArrows = false;

    @Property(type = Property.Type.NUMBER, name = "P3 Arrows CD", parent = "Auto Devices",
            min = 20, max = 500, step = 10,
            description = "Cool down between 2 clicks.")
    public static int ArrowCD = 100;

    @Property(type = Property.Type.NUMBER, name = "P3 Arrows CD Between ItemFrames", parent = "Auto Devices",
            min = 100, max = 500, step = 10,
            description = "Cooldown between calculations. \n" +
                    "Not suggested to be lower than your ping.")
    public static int ArrowCDBetween = 250;

    // AutoBlood
    @Property(type = Property.Type.FOLDER, name = "Auto Blood", parent = "Dungeons")
    public static boolean AutoBloodEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Blood Enable", parent = "Auto Blood", illegal = true,
            description = "Auto aim and rotate to the blood mob with a Y-Offset.")
    public static boolean AutoBlood = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Blood Yoffset (0.1x block)", parent = "Auto Blood",
            min = 0, max = 20, description = "Higher ping, higher offset!")
    public static int AutoBloodYoffset = 0;

    @Property(type = Property.Type.NUMBER, name = "Auto Blood CD (ms)", parent = "Auto Blood", illegal = true,
            min = 40, max = 200, step = 10, description = "Cool down between rotating and clicking.")
    public static int AutoBloodCD = 60;

    // StonklessStonk
    @Property(type = Property.Type.FOLDER, name = "Stonkless Stonk", parent = "Dungeons",
            description = "Auto get the secret you're facing, even it's behind walls.\n" +
                    "Default only works with pickaxe, \nand need to right click to get the secret.\n" +
                    "Works with chest, trapped chest, wither essence and redstone key.")
    public static boolean StonklessStonk = false;

    @Property(type = Property.Type.BOOLEAN, name = "Stonkless Stonk Enable", parent = "Stonkless Stonk")
    public static boolean StonklessStonkEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Stonkless Stonk without Pickaxe", parent = "Stonkless Stonk", illegal = true)
    public static boolean StonklessStonkWithoutPickaxe = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Click Secret", parent = "Stonkless Stonk", illegal = true,
            description = "Whether to click manually or auto click secret.")
    public static boolean StonklessStonkAutoClickSecret = false;

    @Property(type = Property.Type.FOLDER, name = "Other Dungeon Features", parent = "Dungeons")
    public static boolean OtherDungeonFeatures = false;

    // AutoCloseSecretChest
    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Secret Chest", parent = "Other Dungeon Features")
    public static boolean AutoCloseSecretChest = false;

    // BloodAssist
    @Property(type = Property.Type.BOOLEAN, name = "Blood Assist", parent = "Other Dungeon Features",
            description = "Predict the blood mobs' spawn place, \n" +
                    "and display if you can hit it.\n" +
                    "Green box = spawn place; Blue box = you can click now.")
    public static boolean BloodAssist = false;

    // AutoSalvage
    @Property(type = Property.Type.BOOLEAN, name = "Auto Click Salvage", parent = "Other Dungeon Features",
            description = "Auto Clicks 'salvage' button when\n" +
                    "non-starred item is in salvage menu.")
    public static boolean AutoClickSalvage = false;


    // Slayer
    @Property(type = Property.Type.FOLDER, name = "Slayer")
    public static boolean SlayerEnabled = false;

    // Other Slayer Features
    @Property(type = Property.Type.FOLDER, name = "Other Slayer Features", parent = "Slayer")
    public static boolean OtherSlayerFeatures = false;

    // ClickScreenMaddox
    @Property(type = Property.Type.BOOLEAN, name = "Click Screen Maddox", parent = "Other Slayer Features",
            description = "Click Screen to pick up maddox phone.")
    public static boolean ClickScreenMaddox = false;

    // Voidgloom
    @Property(type = Property.Type.FOLDER, name = "Voidgloom", parent = "Slayer")
    public static boolean EndermanEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Voidgloom miniboss ESP", parent = "Voidgloom",
            description = "Display a green bounding box at Voidgloom minibosses.")
    public static boolean EndermanMiniESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Voidgloom miniboss HP", parent = "Voidgloom",
            description = "Display HP of Voidgloom minibosses.")
    public static boolean ShowEndermanMiniHP = false;

    // Sven
    @Property(type = Property.Type.FOLDER, name = "Sven", parent = "Slayer")
    public static boolean WolfEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Sven miniboss ESP", parent = "Sven",
            description = "Display a green bounding box at Sven mini/bosses.")
    public static boolean WolfMiniESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Sven miniboss HP", parent = "Sven",
            description = "Display HP of Sven mini/bosses.")
    public static boolean ShowWolfMiniHP = false;

    // Revenant
    @Property(type = Property.Type.FOLDER, name = "Revenant", parent = "Slayer")
    public static boolean RevenantEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Revenant miniboss ESP", parent = "Revenant",
            description = "Display a green bounding box at Revenant mini/bosses.")
    public static boolean RevMiniESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Revenant miniboss HP", parent = "Revenant",
            description = "Display HP of Revenant mini/bosses.")
    public static boolean ShowRevMiniHP = false;

    // Tarantula
    @Property(type = Property.Type.FOLDER, name = "Tarantula", parent = "Slayer")
    public static boolean TaraEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Tarantula miniboss ESP", parent = "Tarantula",
            description = "Display a green bounding box at Tarantula mini/bosses.")
    public static boolean TaraMiniESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Tarantula miniboss HP", parent = "Tarantula",
            description = "Display HP of Tarantula mini/bosses.")
    public static boolean ShowTaraMiniHP = false;


    // Skills
    @Property(type = Property.Type.FOLDER, name = "Skills")
    public static boolean SkillsEnabled = false;

    // AutoPowder
    @Property(type = Property.Type.FOLDER, name = "Powder Relevant", parent = "Skills")
    public static boolean AutoPowderEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Powder Enabled", parent = "Powder Relevant", illegal = true,
            description = "AFK mine hardstone and open chests. Low efficiency!\n" +
                    "Toggleable through keybindings.\n" +
                    "&c&lDONT FULLY AFK!")
    public static boolean AutoPowder = false;

    // AutoPowderChest
    @Property(type = Property.Type.BOOLEAN, name = "Auto Crystal Hollows Chest", parent = "Powder Relevant", illegal = true,
            description = "Automatically opens powder chests. Toggleable through keybindings.")
    public static boolean AutoPowderChest = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Loot Chests in Crystal Hollows", parent = "Powder Relevant")
    public static boolean AutoCloseCrystalHollowsChest = false;

    // Fishing
    @Property(type = Property.Type.FOLDER, name = "Fishing", parent = "Skills")
    public static boolean FishingFeatures = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Pulling Rod", parent = "Fishing", illegal = true,
            description = "Auto Pull Rod When fishing.\n" +
                    "Only support water fishing currently, \n" +
                    "lava fishing will be added later.")
    public static boolean AutoPullRod = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Move Randomly", parent = "Fishing", illegal = true,
            description = "Move around and rotate to bypass afk test \n" +
                    "and gain fishing exp while afk.\n" +
                    "Requires keybinding to toggle.\n" +
                    "&c&lDONT FULLY AFK!\n")
    public static boolean AutoMove = false;

    // Foraging
    @Property(type = Property.Type.FOLDER, name = "Foraging", parent = "Skills")
    public static boolean ForagingFeatures = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Foraging", parent = "Foraging", illegal = true,
            description = "Requires rod, Treecapitator, bone meal / enchanted bone meal\n" +
                    "and sapling in hotbar.\n" +
                    "/foragingpoint north/south etc. to set starting point and direction.\n" +
                    "Autopet rule: on rod, monkey; on jungle collection, ocelot.\n" +
                    "Set a keybinding to toggle.\n" +
                    "&c&lDONT FULLY AFK!")
    public static boolean AutoForaging = false;

    // GemstoneESP
    @Property(type = Property.Type.FOLDER, name = "Gemstone ESP", parent = "Skills",
            description = "Display ESP boxes of gemstones, \n" +
                    "with an alpha related to gem-player distance.")
    public static boolean GemstoneESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Gemstone ESP Enable", parent = "Gemstone ESP")
    public static boolean GemstoneESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Include Glass Panes", parent = "Gemstone ESP")
    public static boolean IncludeGlassPanes = false;

    @Property(type = Property.Type.NUMBER, name = "Scan Radius", parent = "Gemstone ESP", suffix = " blocks", min = 5, max = 30,
            description = "Don't suggest setting radius too large.")
    public static int GemstoneRadius = 15;

    @Property(type = Property.Type.BOOLEAN, name = "Ruby", parent = "Gemstone ESP")
    public static boolean RubyEsp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Amber", parent = "Gemstone ESP")
    public static boolean AmberEsp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Sapphire", parent = "Gemstone ESP")
    public static boolean SapphireEsp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Jade", parent = "Gemstone ESP")
    public static boolean JadeEsp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Amethyst", parent = "Gemstone ESP")
    public static boolean AmethystEsp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Topaz", parent = "Gemstone ESP")
    public static boolean TopazEsp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Jasper", parent = "Gemstone ESP")
    public static boolean JasperEsp = false;

    // Jade Crystal Helper
    @Property(type = Property.Type.FOLDER, name = "Jade Crystal Helper", parent = "Skills",
            description = "Solves the equation when 3 distance\n" +
                    "from 3 points are ensured.")
    public static boolean JadeCrystalHelper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Enable Helper", parent = "Jade Crystal Helper",
            description = "Turn on Nearby Chest ESP in QOL/OtherQOL!")
    public static boolean JadeCrystal = false;

    @Property(type = Property.Type.NUMBER, name = "Treasure CD", parent = "Jade Crystal Helper",
            description = "To ensure current position corresponds with current distance",
            min = 200, max = 600, step = 20)
    public static int JadeCrystalCD = 400;

    // Experimentation
    @Property(type = Property.Type.FOLDER, name = "Auto Experimentation Table", parent = "Skills",
            description = "Auto Solves Experimentation Table.\n" +
                    "Conflicts with other solvers, and OldAnimation mod.")
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

    @Property(type = Property.Type.BOOLEAN, name = "Map Enabled", parent = "Map",
            description = "Need to be enabled to guarantee some dungeon features to work.")
    public static boolean MapEnabled = true;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Scan", parent = "Map",
            description = "Auto Scans the map 0.5s per time when not fully scanned.")
    public static boolean AutoScan = true;

    @Property(type = Property.Type.BOOLEAN, name = "Chat Info", parent = "Map",
            description = "Send map messages in chat when fully scanned:\n" +
                    "Puzzles, Trap type, Wither Doors, Total Secrets.")
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

    @Property(type = Property.Type.NUMBER, name = "Background Alpha", parent = "Map",
            min = 0, max = 255, step = 5)
    public static int BackgroundAlpha = 15;

    @Property(type = Property.Type.SELECT, name = "Checkmark Image", parent = "Map",
            options = {"Off", "Dmap Image", "New Image"})
    public static int DrawCheckMode = 2;

    // Player
    @Property(type = Property.Type.FOLDER, name = "Players", parent = "Illegal Map")
    public static boolean PlayerPart = false;

    @Property(type = Property.Type.SELECT, name = "Show Player Names", parent = "Players",
            options = {"Off", "Holding Leaps", "Always"})
    public static int ShowPlayerNames = 0;

    @Property(type = Property.Type.SELECT, name = "Player Name Color", parent = "Players",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int PlayerNameColor = 5;

    @Property(type = Property.Type.SELECT, name = "Self Icon Color", parent = "Players",
            description = "For Arrow Icon. \n" +
                    "Normal players will show as blue icon,\n" +
                    "and change this to set self icon color.\n" +
                    "Arrow icons are loaded when players' avatars arent loaded successfully.\n" +
                    "&cDon't nick in dungeons!",
            options = {"Blue", "Green", "Red"})
    public static int SelfIconColor = 2;

    @Property(type = Property.Type.SELECT, name = "Self Icon Border Color", parent = "Players",
            description = "Border color of Avatar.\n" +
                    "Restart dungeon to reload changes.",
            options = {"No Border", "Green", "Red", "Blue"})
    public static int SelfIconBorderColor = 2;

    // Rooms
    @Property(type = Property.Type.FOLDER, name = "Rooms", parent = "Illegal Map")
    public static boolean RoomPart = false;

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

    @Property(type = Property.Type.BOOLEAN, name = "Show Trap Room Name", parent = "Rooms")
    public static boolean ShowTrapName = true;

    @Property(type = Property.Type.BOOLEAN, name = "Show Puzzle Room Name", parent = "Rooms")
    public static boolean ShowPuzzleName = true;

    @Property(type = Property.Type.BOOLEAN, name = "Show Normal Room Name", parent = "Rooms",
            description = "Can bind a keybinding to toggle.")
    public static boolean ShowNormalName = false;

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

    @Property(type = Property.Type.BOOLEAN, name = "Announce 270", parent = "Score")
    public static boolean Announce270 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Title for 300 announce", parent = "Score")
    public static boolean DisplayAnnounce300 = false;

    @Property(type = Property.Type.TEXT, name = "Announce 300 Message", parent = "Score")
    public static String Announce300Message = "300 Score Reached!";

    @Property(type = Property.Type.TEXT, name = "Announce 270 Message", parent = "Score")
    public static String Announce270Message = "270 Score Reached!";

    @Property(type = Property.Type.BOOLEAN, name = "Assume Spirit", parent = "Score",
            description = "Assume first death only -1 score.\n" +
                    "Will automatically check in future updates!")
    public static boolean AssumeSpirit = true;  // TODO

    @Property(type = Property.Type.BOOLEAN, name = "Assume Paul", parent = "Score",
            description = "Assume paul's +10 point perk is active.\n" +
                    "Will automatically check in future updates!")
    public static boolean AssumePaul = false;
}
