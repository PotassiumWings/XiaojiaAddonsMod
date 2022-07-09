package com.xiaojia.xiaojiaaddons.Config;

public class Configs {
    // Accentry
    @Property(type = Property.Type.FOLDER, name = "Accentry")
    public static boolean AriaEnabled = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Pick", parent = "Accentry",
            description = "Auto pick up certain items from far away.")
    public static boolean AutoPickEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Pick Enabled", parent = "Auto Pick")
    public static boolean AutoPick = false;

    @Property(type = Property.Type.TEXT, name = "Auto Pick Names", parent = "Auto Pick", description = "xx,yy,z")
    public static String AutoPickNames = "银,金,钻,蜘蛛卵";

    @Property(type = Property.Type.NUMBER, name = "Auto Pick CD", parent = "Auto Pick", description = "CD between 2 picks.",
            min = 100, max = 5000, step = 20)
    public static int AutoPickCD = 1000;

    @Property(type = Property.Type.FOLDER, name = "Auto Left Click", parent = "Accentry")
    public static boolean AutoLeftClickEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Left Click Enable", parent = "Auto Left Click", description = "KeyBind.")
    public static boolean AutoLeftClick = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Left Click CPS", parent = "Auto Left Click", min = 1, max = 20)
    public static int AutoClickCPS = 12;

    @Property(type = Property.Type.FOLDER, name = "Kill All", parent = "Accentry", description = "&c&lVERY DANGEROUS.\n" +
            "&c&lDON'T USE THIS IN ANY SERVER WITH ANTI CHEAT.\n" +
            "Automatically attack nearby entities without aiming or swinging.")
    public static boolean KilAllEnabled = false;

    @Property(type = Property.Type.TEXT, name = "Kill All Contains", parent = "Kill All")
    public static String KillAllName = "Pig";

    @Property(type = Property.Type.NUMBER, name = "Kill All Stop Radius", parent = "Kill All", min = 1, max = 100)
    public static int KillAllStopRadius = 15;

    @Property(type = Property.Type.BOOLEAN, name = "Kill All Stop When Player Neaby", parent = "Kill All")
    public static boolean KillAllStop = false;

    @Property(type = Property.Type.SELECT, name = "Kill All Click Mode", parent = "Kill All", options = {"LEFT", "RIGHT"})
    public static int KillAllLeft = 0;

    @Property(type = Property.Type.FOLDER, name = "Trades", parent = "Accentry")
    public static boolean Trades = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Meat", parent = "Trades",
            description = "Automatically put meat in merchant menu.")
    public static boolean AutoMeat = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Daily Quest", parent = "Trades",
            description = "Automatically Finish Daily Quest.")
    public static boolean AutoRongYao = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Question", parent = "Accentry")
    public static boolean AutoQuestionEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Question Enable", parent = "Auto Question")
    public static boolean AutoQuestion = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Question CD", parent = "Auto Question", suffix = " ms",
            min = 0, max = 2000, step = 20)
    public static int AutoQuestionCD = 1000;

    @Property(type = Property.Type.FOLDER, name = "Combat Related", parent = "Accentry")
    public static boolean CombatRelated = false;

    @Property(type = Property.Type.BOOLEAN, name = "Fast Eat", parent = "Combat Related",
            description = "&c&lVERY DANGEROUS.\n" + "" +
                    "&c&lDON'T USE THIS IN ANY SERVER WITH ANTI CHEAT.\n" +
                    "Right click a food, and you ate it.")
    public static boolean FastUse = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Mi Ni", parent = "Combat Related")
    public static boolean AutoMiNi = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Mi Qi", parent = "Combat Related")
    public static boolean AutoMiQi = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Back", parent = "Combat Related", description = "WIP")
    public static boolean AutoBack = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Eat", parent = "Combat Related",
            description = "Auto eat when low saturation.")
    public static boolean AutoEat = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Eat Hunger", parent = "Combat Related", min = 0, max = 19)
    public static int AutoEatHunger = 14;

    @Property(type = Property.Type.FOLDER, name = "Auto Bottle", parent = "Accentry")
    public static boolean AutoBottleEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Bottle Enable", parent = "Auto Bottle")
    public static boolean AutoBottle = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Bottle Threshold", parent = "Auto Bottle", min = 0, max = 150)
    public static int AutoBottleThreshold = 100;

    // Misc
    @Property(type = Property.Type.FOLDER, name = "Misc")
    public static boolean MiscEnabled = false;

    @Property(type = Property.Type.FOLDER, name = "Easy Trigger", parent = "Misc",
            description = "When you receive something matches 'On Receive',\n" +
                    "do the command in 'Do Command'.\n" +
                    "Use reg-expression to work. Ignores color code.\n" +
                    "&c&lBe aware that in some cases you might spam the command or crash the game " +
                    "&c&lif you don't know how to use this correctly.\n" +
                    "So there's a limit where you can trigger at most 5 times in 1 second.")
    public static boolean EasyTriggerEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Enabled", parent = "Easy Trigger")
    public static boolean EasyTrigger = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Debug Mode", parent = "Easy Trigger")
    public static boolean EasyTriggerDebugMode = true;

    @Property(type = Property.Type.FOLDER, name = "Easy Trigger Pair 1", parent = "Easy Trigger",
            description = "This is an example usage.\n" +
                    "When you receive 'Mimic Dead!' message from party chat, repeat it.")
    public static boolean EasyTriggerPair1 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Pair 1 Enabled", parent = "Easy Trigger Pair 1")
    public static boolean EasyTriggerPair1Enabled = false;

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 1 - On Receive", parent = "Easy Trigger Pair 1")
    public static String EasyTriggerPair1OnReceive = "^Party > (\\[MVP\\+*\\]|\\[VIP\\+*\\]|)[0-9a-zA-Z_]+: Mimic Dead!$";

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 1 - Do Command", parent = "Easy Trigger Pair 1")
    public static String EasyTriggerPair1DoCommand = "/pc Mimic Dead!";

    @Property(type = Property.Type.FOLDER, name = "Easy Trigger Pair 2", parent = "Easy Trigger")
    public static boolean EasyTriggerPair2 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Pair 2 Enabled", parent = "Easy Trigger Pair 2")
    public static boolean EasyTriggerPair2Enabled = false;

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 2 - On Receive", parent = "Easy Trigger Pair 2")
    public static String EasyTriggerPair2OnReceive = "";

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 2 - Do Command", parent = "Easy Trigger Pair 2")
    public static String EasyTriggerPair2DoCommand = "";

    @Property(type = Property.Type.FOLDER, name = "Easy Trigger Pair 3", parent = "Easy Trigger")
    public static boolean EasyTriggerPair3 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Pair 3 Enabled", parent = "Easy Trigger Pair 3")
    public static boolean EasyTriggerPair3Enabled = false;

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 3 - On Receive", parent = "Easy Trigger Pair 3")
    public static String EasyTriggerPair3OnReceive = "";

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 3 - Do Command", parent = "Easy Trigger Pair 3")
    public static String EasyTriggerPair3DoCommand = "";

    @Property(type = Property.Type.FOLDER, name = "Easy Trigger Pair 4", parent = "Easy Trigger")
    public static boolean EasyTriggerPair4 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Pair 4 Enabled", parent = "Easy Trigger Pair 4")
    public static boolean EasyTriggerPair4Enabled = false;

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 4 - On Receive", parent = "Easy Trigger Pair 4")
    public static String EasyTriggerPair4OnReceive = "";

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 4 - Do Command", parent = "Easy Trigger Pair 4")
    public static String EasyTriggerPair4DoCommand = "";

    @Property(type = Property.Type.FOLDER, name = "Easy Trigger Pair 5", parent = "Easy Trigger")
    public static boolean EasyTriggerPair5 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Easy Trigger Pair 5 Enabled", parent = "Easy Trigger Pair 5")
    public static boolean EasyTriggerPair5Enabled = false;

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 5 - On Receive", parent = "Easy Trigger Pair 5")
    public static String EasyTriggerPair5OnReceive = "";

    @Property(type = Property.Type.TEXT, name = "Easy Trigger Pair 5 - Do Command", parent = "Easy Trigger Pair 5")
    public static String EasyTriggerPair5DoCommand = "";

    @Property(type = Property.Type.FOLDER, name = "API", parent = "Misc")
    public static boolean API = false;

    @Property(type = Property.Type.TEXT, name = "API Key", parent = "API",
            description = "Paste your API Key here\n" +
                    "or run '/api new' command")
    public static String APIKey = "";

    @Property(type = Property.Type.FOLDER, name = "Protect Items", parent = "Misc",
            description = "Protect items with high value when dropping, selling items.\n" +
                    "&cRequires lowest bin.")
    public static boolean ProtectItemsEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Protect Items Enable", parent = "Protect Items")
    public static boolean ProtectItems = false;

    @Property(type = Property.Type.NUMBER, name = "Item Value", parent = "Protect Items", suffix = " K",
            min = 0, max = 100000, step = 10,
            description = "Any items with value greater than this will be protected.")
    public static int ProtectItemValue = 1000;

    @Property(type = Property.Type.FOLDER, name = "Dev Mode", parent = "Misc",
            description = "For my testings.")
    public static boolean DevMode = false;

    @Property(type = Property.Type.BOOLEAN, name = "Dev Water", parent = "Dev Mode")
    public static boolean DevWater = false;

    @Property(type = Property.Type.BOOLEAN, name = "Dev Tracing", parent = "Dev Mode")
    public static boolean DevTracing = false;

    @Property(type = Property.Type.BOOLEAN, name = "Particle Spawn Message", parent = "Dev Mode")
    public static boolean ParticleSpawnMessage = false;

    @Property(type = Property.Type.BOOLEAN, name = "Entity Join Message", parent = "Dev Mode")
    public static boolean EntityJoinEvent = false;

    @Property(type = Property.Type.BOOLEAN, name = "Packet Sent Message", parent = "Dev Mode")
    public static boolean PacketSent = false;

    @Property(type = Property.Type.FOLDER, name = "Dangerous Features", parent = "Misc",
            description = "DO NOT USE THESE FEATURES.")
    public static boolean DangerousFeatures = false;

    @Property(type = Property.Type.FOLDER, name = "Remove Hurt Camera", parent = "Misc",
            description = "Only works in SkyBlock.")
    public static boolean RemoveHurtCamEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Remove Hurt Camera Enable", parent = "Remove Hurt Camera")
    public static boolean RemoveHurtCam = false;

    @Property(type = Property.Type.FOLDER, name = "No Rotate", parent = "Dangerous Features")
    public static boolean NoRotateEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Rotate Enabled", parent = "No Rotate")
    public static boolean NoRotate = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Rotate Optimize", parent = "No Rotate",
            description = "IDK if this is bannable.")
    public static boolean NoRotateOptimize = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Rotate Disable When Holding Leaps", parent = "No Rotate")
    public static boolean NoRotateDisableHoldingLeaps = false;

    @Property(type = Property.Type.BOOLEAN, name = "No Rotate Disable When In Teleport Maze", parent = "No Rotate",
            description = "Requires dungeon map enabled!")
    public static boolean NoRotateDisableInMaze = false;

    @Property(type = Property.Type.FOLDER, name = "Velocity", parent = "Dangerous Features",
            description = "Only works in SkyBlock.\n" +
                    "If you are in lava and in dungeon, or used bonzo's staff / jerrychine gun\n" +
                    "within Velocity CD, this feature will be automatically disabled.\n" +
                    "If you are wearing tarantula / spider / spring / spirit boots, \n" +
                    "this feature is also automatically disabled.")
    public static boolean Velocity = false;

    @Property(type = Property.Type.NUMBER, name = "Velocity XZ Scale", parent = "Velocity", min = 0, max = 100, suffix = " %")
    public static int VelocityXZ = 100;

    @Property(type = Property.Type.NUMBER, name = "Velocity Y Scale", parent = "Velocity", min = 0, max = 100, suffix = " %")
    public static int VelocityY = 100;

    @Property(type = Property.Type.NUMBER, name = "Velocity CD", parent = "Velocity", min = 0, max = 2000, step = 10, suffix = " ms")
    public static int VelocityCD = 1000;

    @Property(type = Property.Type.BOOLEAN, name = "Velocity - SkyBlock Only", parent = "Velocity")
    public static boolean DisableOutofSkyBlock = true;

    @Property(type = Property.Type.NUMBER, name = "Velocity Stop Radius", parent = "Velocity", min = 1, max = 100)
    public static int VelocityStopRadius = 15;

    @Property(type = Property.Type.BOOLEAN, name = "Velocity Stop When Player Neaby", parent = "Velocity")
    public static boolean VelocityStop = false;

    @Property(type = Property.Type.FOLDER, name = "Dupe Display", parent = "Misc")
    public static boolean DupeDisplay = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Duped Items", parent = "Dupe Display",
            description = "Show &c&lDup&r when rendering 'duped' items.\n" +
                    "&cNot guaranteed to be 100% accurate cause there's\n" +
                    "&climited resource to know which ones are duped.\n" +
                    "&cOrigin items are marked too.")
    public static boolean ShowDupedItems = false;

    @Property(type = Property.Type.FOLDER, name = "Price Related", parent = "Misc")
    public static boolean PriceRelated = false;

    @Property(type = Property.Type.BOOLEAN, name = "Fetch Lowest Bin", parent = "Price Related")
    public static boolean FetchLowestBin = true;

    @Property(type = Property.Type.BOOLEAN, name = "Display Lowest Bin", parent = "Price Related",
            description = "Display a line in tooltips to show items' lowest bin.")
    public static boolean DisplayLowestBin = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Chest Profit", parent = "Price Related",
            description = "Display dungeon loot chest profit.")
    public static boolean ShowChestProfit = false;

    @Property(type = Property.Type.FOLDER, name = "Chat Copy", parent = "Misc")
    public static boolean ChatCopyEnabled = false;

    @Property(type = Property.Type.FOLDER, name = "Old Animations", parent = "Misc")
    public static boolean OldAnimationsEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Old Block Break", parent = "Old Animations",
            description = "Can use item right click when hitting blocks.")
    public static boolean OldBlockBreak = false;

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

    @Property(type = Property.Type.BOOLEAN, name = "Close Gui When Changing Direction", parent = "General XJA Setting")
    public static boolean CloseInvWhenChangingDirection = true;

    @Property(type = Property.Type.BOOLEAN, name = "Close Gui When Clicking", parent = "General XJA Setting")
    public static boolean CloseInvWhenClicking = true;

    @Property(type = Property.Type.BOOLEAN, name = "General ESP toggle", parent = "General XJA Setting")
    public static boolean GeneralESP = true;

    @Property(type = Property.Type.BOOLEAN, name = "Show XJA Message", parent = "General XJA Setting",
            description = "Show the messages starting with \"XJA >\".")
    public static boolean ShowXJAMessage = true;

    @Property(type = Property.Type.BOOLEAN, name = "Hide XJA mod id", parent = "General XJA Setting",
            description = "Not let the server know that you have XiaojiaAddons installed.\n" +
                    "&cRestart after changing required.")
    public static boolean HideModID = true;

    @Property(type = Property.Type.NUMBER, name = "Box Line Thickness", parent = "General XJA Setting",
            min = 1, max = 20, step = 1,
            description = "Line thickness of every bounding box in this mod.")
    public static int BoxLineThickness = 5;

    @Property(type = Property.Type.SELECT, name = "Change Direction Mode", parent = "General XJA Setting",
            options = {"Fast Mode", "Mid Mode", "Slow Mode", "Super Slow Mode"}
    )
    public static int ChangeDirectionMode = 0;

    @Property(type = Property.Type.FOLDER, name = "Render Name Related", parent = "Misc")
    public static boolean RenderName = false;

    @Property(type = Property.Type.BOOLEAN, name = "Render Self Name", parent = "Render Name Related")
    public static boolean RenderSelfName = true;

    @Property(type = Property.Type.BOOLEAN, name = "Render Rank", parent = "Render Name Related",
            description = "Render rank on top of heads.")
    public static boolean RenderRank = true;

    // HoverCommand
    @Property(type = Property.Type.FOLDER, name = "Chat Hover", parent = "Misc",
            description = "Shows what command clicking it will execute."
    )
    public static boolean ChatHoverCommandEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Chat Hover Enabled", parent = "Chat Hover")
    public static boolean ChatHoverCommand = true;

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

    @Property(type = Property.Type.NUMBER, name = "CD between shootings", parent = "Dragon", suffix = " ms",
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

    // AutoSneakyCreeper
    @Property(type = Property.Type.BOOLEAN, name = "Auto Sneaky Creeper", parent = "Gunpowder Mines",
            description = "Wear a full set of frozen blaze set and get as much mf as you can.\n" +
                    "Auto walking in a route in gunpowder mines and afk kill creepers.\n" +
                    "Disable jump boost, rabbit, speed potions.")
    public static boolean AutoSneakyCreeper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Sneaky Creeper Map Enable", parent = "Gunpowder Mines",
            description = "This is just for your better visualize.")
    public static boolean SneakyCreeperMap = false;

    @Property(type = Property.Type.NUMBER, name = "Sneaky Creeper Optimize Param", parent = "Gunpowder Mines",
            description = "OwO", min = 1, max = 60)
    public static int SNMaxLen = 48;

    @Property(type = Property.Type.NUMBER, name = "Sneaky Creeper Search Radius", parent = "Gunpowder Mines",
            description = "OwO", min = 50, max = 90)
    public static int SneakySearchRadius = 65;

    @Property(type = Property.Type.NUMBER, name = "Sneaky Creeper Split Count", parent = "Gunpowder Mines",
            description = "OwO", min = 1, max = 200)
    public static int SneakySplit = 20;

    @Property(type = Property.Type.NUMBER, name = "(Sneaky) Map Scale", parent = "Gunpowder Mines", min = 1, max = 10)
    public static int SNMapScale = 7;

    @Property(type = Property.Type.NUMBER, name = "(Sneaky) Player Head Scale", parent = "Gunpowder Mines", suffix = " %",
            min = 1, max = 100)
    public static int SNHeadScale = 50;

    @Property(type = Property.Type.NUMBER, name = "(Sneaky) Map X", parent = "Gunpowder Mines",
            min = 0, max = 1000, step = 5)
    public static int SNMapX = 55;

    @Property(type = Property.Type.NUMBER, name = "(Sneaky) Map Y", parent = "Gunpowder Mines",
            min = 0, max = 1000, step = 5)
    public static int SNMapY = 20;

    @Property(type = Property.Type.NUMBER, name = "(Sneaky) Background Alpha", parent = "Gunpowder Mines",
            min = 0, max = 255, step = 5)
    public static int SNBackgroundAlpha = 140;

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


    // Nether
    @Property(type = Property.Type.FOLDER, name = "Nether")
    public static boolean NetherEnabled = false;

    // AshFang
    @Property(type = Property.Type.FOLDER, name = "Ashfang", parent = "Nether")
    public static boolean AshfangEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ashfang Gravity Center ESP", parent = "Ashfang")
    public static boolean AshfangGravityCenterESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ashfang Follower ESP", parent = "Ashfang")
    public static boolean AshfangFollowerESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ashfang Acolyte ESP", parent = "Ashfang")
    public static boolean AshfangAcolyteESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ashfang Underling ESP", parent = "Ashfang")
    public static boolean AshfangUnderlingESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ashfang Blazing Soul ESP", parent = "Ashfang")
    public static boolean BlazingSoulESP = false;

    // XYZ
    @Property(type = Property.Type.FOLDER, name = "XYZ", parent = "Nether")
    public static boolean XYZ = false;

    @Property(type = Property.Type.BOOLEAN, name = "XYZ Helper", parent = "XYZ")
    public static boolean XYZHelper = false;

    @Property(type = Property.Type.SELECT, name = "XYZ Mode", parent = "XYZ", options = {"Z", "Y", "X"})
    public static int XYZMode = 0;

    @Property(type = Property.Type.NUMBER, name = "XYZ Reach", parent = "XYZ", min = 4, max = 100)
    public static int XYZReach = 4;

    // Kuudra
    @Property(type = Property.Type.FOLDER, name = "Kuudra", parent = "Nether")
    public static boolean KuudraEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Convergence Center ESP", parent = "Kuudra")
    public static boolean ConvergenceESP = false;

    @Property(type = Property.Type.FOLDER, name = "Kuudra Display", parent = "Kuudra")
    public static boolean KuudraDisplayEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Kuudra Info Display Test", parent = "Kuudra Display",
            description = "Display lines to show where the display is at.")
    public static boolean KuudraTest = true;

    @Property(type = Property.Type.NUMBER, name = "Kuudra Display X", parent = "Kuudra Display")
    public static int KuudraX = 400;

    @Property(type = Property.Type.NUMBER, name = "Kuudra Display Y", parent = "Kuudra Display")
    public static int KuudraY = 160;

    @Property(type = Property.Type.NUMBER, name = "Kuudra Display Scale", parent = "Kuudra Display",
            min = 5, max = 100)
    public static int KuudraScale = 8;

    @Property(type = Property.Type.BOOLEAN, name = "Kuudra Info Display Enable", parent = "Kuudra Display")
    public static boolean KuudraDisplay = false;

    // Dojo
    @Property(type = Property.Type.FOLDER, name = "Dojo", parent = "Nether")
    public static boolean DojoEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Force Helper", parent = "Dojo",
            description = "Block wrong clicks in force task.")
    public static boolean ForceHelper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Discipline Helper", parent = "Dojo",
            description = "Auto swap click in discipline task.")
    public static boolean DisciplineHelper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Mastery Helper", parent = "Dojo",
            description = "Shows the countdown for blocks to disappear.")
    public static boolean MasteryHelper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Mastery Helper - Auto Turn", parent = "Dojo",
            description = "Auto turn to the exploding wool.")
    public static boolean MasteryAutoTurn = false;

    @Property(type = Property.Type.BOOLEAN, name = "Mastery Helper - Auto Release Right Click", parent = "Dojo")
    public static boolean MasteryAutoRelease = false;

    @Property(type = Property.Type.NUMBER, name = "Mastery Helper - Auto Release Right Click When", parent = "Dojo",
            min = 500, max = 1500, step = 10, description = "Set when to release right click.")
    public static int MasteryAutoReleaseCD = 1000;

    @Property(type = Property.Type.SELECT, name = "Mastery Helper Mode", parent = "Dojo", options = {"Estimate", "Official"})
    public static int MasteryMode = 0;

    // Corrupted
    @Property(type = Property.Type.FOLDER, name = "Nether Mob ESP", parent = "Nether")
    public static boolean NetherMobESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Corrupted Mob ESP", parent = "Nether Mob ESP",
            description = "Bad bad bad.")
    public static boolean CorruptedESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Ghast ESP", parent = "Nether Mob ESP")
    public static boolean GhastESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Golem ESP", parent = "Nether Mob ESP")
    public static boolean GolemESP = false;


    // SpongeESP
    @Property(type = Property.Type.FOLDER, name = "Nether Block ESP", parent = "Nether",
            description = "&cOnly turn this on when you're finding blocks.\n" +
                    "&cThis feature will ruin your performance.")
    public static boolean NetherBlockESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Sponge ESP", parent = "Nether Block ESP")
    public static boolean SpongeESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Prismarine ESP", parent = "Nether Block ESP")
    public static boolean PrismarineESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Titanium ESP", parent = "Nether Block ESP",
            description = "This is not supposed to be in nether block esp " +
                    "but i have 5 hours left with 2k hotm exp so")
    public static boolean TitaniumESP = false;


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

    @Property(type = Property.Type.TEXT, name = "Auto Sell Custom", parent = "Auto Sell",
            description = "Set display name to auto sell.")
    public static String AutoSellConfig = "";

    @Property(type = Property.Type.NUMBER, name = "Auto Sell CD", parent = "Auto Sell", suffix = " ms",
            min = 50, max = 500, step = 5,
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

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Blaze Hat", parent = "Auto Sell Miscellaneous")
    public static boolean AutoSellBlazeHat = false;

    @Property(type = Property.Type.FOLDER, name = "Auto Sell Fishing Stuff", parent = "Auto Sell")
    public static boolean AutoSellFishing = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Ice Rod", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellIceRod = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Music Disc", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellMusicDisc = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Non-recomed Fairy Set", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellFairySet = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Sea Lantern", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellSeaLantern = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Enchanted Feather", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellEnchantedFeather = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Enchanted Golden Apple", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellEnchantedGoldenApple = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Golden Apple", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellGoldenApple = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Bait", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellBait = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Sell Magnet VI Book", parent = "Auto Sell Fishing Stuff")
    public static boolean AutoSellMagnet = false;

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

    @Property(type = Property.Type.BOOLEAN, name = "Display Packet Received Frequency", parent = "Information Display")
    public static boolean DisplayPacketReceived = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Packet Sent Frequency", parent = "Information Display")
    public static boolean DisplayPacketSent = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Bricks", parent = "Information Display")
    public static boolean DisplayBricks = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Iron Ingots", parent = "Information Display")
    public static boolean DisplayIronIngots = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Players", parent = "Information Display")
    public static boolean DisplayPlayers = false;

    @Property(type = Property.Type.BOOLEAN, name = "Display Coords", parent = "Information Display",
            description = "Display current block you're at.")
    public static boolean DisplayCoords = false;

    @Property(type = Property.Type.NUMBER, name = "Display X", parent = "Information Display", max = 999)
    public static int DisplayDayX = 5;

    @Property(type = Property.Type.NUMBER, name = "Display Y", parent = "Information Display", max = 999)
    public static int DisplayDayY = 5;

    @Property(type = Property.Type.NUMBER, name = "Display Scale", parent = "Information Display", min = 5, max = 100)
    public static int DisplayScale = 20;

    // ShowBookName
    @Property(type = Property.Type.FOLDER, name = "Show Book Name", parent = "QOL",
            description = "Shows the book's short name in containers except for AH.")
    public static boolean ShowBookNameEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Book Name Enable", parent = "Show Book Name")
    public static boolean ShowBookName = false;

    // DisableAnimations
    @Property(type = Property.Type.FOLDER, name = "Disable Entity Renders", parent = "QOL")
    public static boolean DisableAnimations = false;

    @Property(type = Property.Type.BOOLEAN, name = "Disable Lightnings", parent = "Disable Entity Renders")
    public static boolean DisableLightnings = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Dying Mobs", parent = "Disable Entity Renders")
    public static boolean DisableDying = false;

    // ShowEtherwarp
    @Property(type = Property.Type.FOLDER, name = "Show Etherwarp Point", parent = "QOL")
    public static boolean ShowEtherwarpEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Etherwarp Point Enable", parent = "Show Etherwarp Point")
    public static boolean ShowEtherwarp = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Nearby Etherwarp Point", parent = "Show Etherwarp Point",
            description = "Show nearby possible etherwarp points.")
    public static boolean ShowNearbyEtherwarp = false;

    @Property(type = Property.Type.NUMBER, name = "Nearby Etherwarp Point Radius", parent = "Show Etherwarp Point",
            description = "Show nearby possible etherwarp points.", min = 0, max = 5)
    public static int NearbyEtherwarpRadius = 2;

    @Property(type = Property.Type.TEXT, name = "Etherwarp Point Color", parent = "Show Etherwarp Point",
            description = "Hex code of color (argb). Example: FFFF1234")
    public static String EtherwarpPointColor = "381cd4e4";

    @Property(type = Property.Type.TEXT, name = "Possible Etherwarp Points Color", parent = "Show Etherwarp Point",
            description = "Hex code of color (argb). Example: FFFF1234")
    public static String PossibleEtherwarpPointColor = "3a22ff22";

    @Property(type = Property.Type.TEXT, name = "Etherwarp Point Bounding Color", parent = "Show Etherwarp Point",
            description = "Color of the bounding of the block.\n" +
                    "Hex code of color (argb). Example: FFFF1234")
    public static String EtherwarpPointBoundingColor = "ff1cd4e4";

    @Property(type = Property.Type.NUMBER, name = "Etherwarp Point Bounding Thickness", parent = "Show Etherwarp Point",
            max = 10)
    public static int EtherwarpPointBoundingThickness = 3;

    // OneTick
    @Property(type = Property.Type.FOLDER, name = "One Tick", parent = "QOL",
            description = "Swap to something, right click it, and swap back.\n" +
                    "One swap is in 1 tick.\n" +
                    "Can be triggered by left click or keybinding press (40 cps).\n" +
                    "&6Both requires keybinding to work!!!\n" +
                    "&cLeft click ability of current item can't be done normally.\n" +
                    "Auto disabled when holding Gyrokinetic Wand or Gloomlock Grimoire.\n" +
                    "Auto disabled when holding Terminator.")
    public static boolean OneTick = false;

    @Property(type = Property.Type.BOOLEAN, name = "Aots With Anything", parent = "One Tick",
            description = "One Tick with item which has a display name contains 'Axe of the \nShredded'.")
    public static boolean AotsWithAnything = false;

    @Property(type = Property.Type.BOOLEAN, name = "Soul Whip With Anything", parent = "One Tick",
            description = "One Tick with item which has a display name contains 'Soul Whip'.")
    public static boolean SoulWhipWithAnything = false;

    @Property(type = Property.Type.BOOLEAN, name = "Terminator With Anything", parent = "One Tick",
            description = "One Tick with item which has a display name contains 'Terminator'.")
    public static boolean TerminatorWithAnything = false;

    // AutoUseItem
    @Property(type = Property.Type.FOLDER, name = "Auto Use Item", parent = "QOL",
            description = "Use (right click) items automatically with cool downs of each item.")
    public static boolean AutoUseItem = false;

    @Property(type = Property.Type.NUMBER, name = "Plasmaflux CD", parent = "Auto Use Item", suffix = " s",
            min = 0, max = 120)
    public static int PlasmaFluxCD = 0;

    @Property(type = Property.Type.NUMBER, name = "Healing Wand CD", parent = "Auto Use Item", suffix = " s",
            min = 0, max = 30)
    public static int HealingWandCD = 0;

    @Property(type = Property.Type.NUMBER, name = "Gyrokinetic Wand CD", parent = "Auto Use Item", suffix = " s",
            min = 0, max = 120)
    public static int GyroCD = 0;

    @Property(type = Property.Type.BOOLEAN, name = "Legit Use Item", parent = "Auto Use Item",
            description = "Delay 3-4 ticks between actions.")
    public static boolean LegitAutoItem = false;

    // AutoAttribute
    @Property(type = Property.Type.FOLDER, name = "Auto Fuse Attribute", parent = "QOL")
    public static boolean FuseAttributeEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Fuse Attribute Enabled", parent = "Auto Fuse Attribute")
    public static boolean FuseAttribute = false;

    @Property(type = Property.Type.TEXT, name = "Auto Fuse - Attribute 1", parent = "Auto Fuse Attribute")
    public static String FuseAttribute1 = "double_hook";

    @Property(type = Property.Type.TEXT, name = "Auto Fuse - Attribute 2", parent = "Auto Fuse Attribute")
    public static String FuseAttribute2 = "fishing_speed";

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

    @Property(type = Property.Type.BOOLEAN, name = "Combine Swarm", parent = "Auto Combine")
    public static boolean CombineSwarm = false;

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

    @Property(type = Property.Type.BOOLEAN, name = "Combine Ferocious Mana", parent = "Auto Combine")
    public static boolean CombineFerociousMana = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Strong Mana", parent = "Auto Combine")
    public static boolean CombineStrongMana = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Hardened Mana", parent = "Auto Combine")
    public static boolean CombineHardenedMana = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Mana Vampire", parent = "Auto Combine")
    public static boolean CombineManaVampire = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Charm", parent = "Auto Combine")
    public static boolean CombineCharm = false;

    @Property(type = Property.Type.BOOLEAN, name = "Combine Corruption", parent = "Auto Combine")
    public static boolean CombineCorruption = false;

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

    @Property(type = Property.Type.BOOLEAN, name = "Hide Golden Fish", parent = "Hide Entity Features")
    public static boolean HideGoldenFish = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Empty Armor Stand", parent = "Hide Entity Features")
    public static boolean HideEmptyArmorStand = false;

    @Property(type = Property.Type.BOOLEAN, name = "Hide Nearby Players", parent = "Hide Entity Features", illegal = true,
            description = "Act like the players dont exist, can click through")
    public static boolean HidePlayers = false;

    @Property(type = Property.Type.NUMBER, name = "Hide Nearby Players radius", parent = "Hide Entity Features", suffix = " blocks",
            min = 0, max = 30)
    public static int HidePlayerRadius = 4;

    // FindFairy
    @Property(type = Property.Type.FOLDER, name = "Find Fairy Grotto", parent = "QOL",
            description = "Bind a key to toggle.\n" +
                    "Automatically scans all loaded blocks.\n" +
                    "The map shows where you are and where is already scanned.\n" +
                    "When it detects a Jasper Gemstone, the map will stop\n" +
                    "rendering and it will highlight the gemstone.\n" +
                    "&cWill drop your fps while scanning.\n" +
                    "So only use when you're finding fairy grottos!")
    public static boolean FindFairyGrottoEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Find Fairy Grotto Map Enable", parent = "Find Fairy Grotto",
            description = "You don't have to turn this on for it to work.\n" +
                    "This is just for your better visualize.")
    public static boolean FindFairyGrottoMap = false;

    @Property(type = Property.Type.NUMBER, name = "(Fairy) Map Scale", parent = "Find Fairy Grotto", min = 1, max = 10)
    public static int CHMapScale = 7;

    @Property(type = Property.Type.NUMBER, name = "(Fairy) Player Head Scale", parent = "Find Fairy Grotto", suffix = " %",
            min = 1, max = 100)
    public static int CHHeadScale = 50;

    @Property(type = Property.Type.NUMBER, name = "(Fairy) Map X", parent = "Find Fairy Grotto",
            min = 0, max = 1000, step = 5)
    public static int CHMapX = 55;

    @Property(type = Property.Type.NUMBER, name = "(Fairy) Map Y", parent = "Find Fairy Grotto",
            min = 0, max = 1000, step = 5)
    public static int CHMapY = 20;

    @Property(type = Property.Type.NUMBER, name = "(Fairy) Background Alpha", parent = "Find Fairy Grotto",
            min = 0, max = 255, step = 5)
    public static int CHBackgroundAlpha = 140;

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

    // Show Attribute
    @Property(type = Property.Type.FOLDER, name = "Show Attribute", parent = "QOL",
            description = "Shows all attribute shards which contains the string you set.")
    public static boolean ShowAttributeEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Attribute Enable", parent = "Show Attribute")
    public static boolean ShowAttribute = false;

    @Property(type = Property.Type.TEXT, name = "Show Attribute Contains", parent = "Show Attribute",
            description = "Use comma to split more than one attributes.\n" +
                    "For example, 'Mana Pool,Life Regen,Fishing Exp'.")
    public static String ShowAttributeName = "Mana Pool";

    @Property(type = Property.Type.FOLDER, name = "Item Attribute Filter", parent = "QOL",
            description = "Display mask of attributes on an item.")
    public static boolean ItemAttributeFilterEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Item Attribute Filter Enable", parent = "Item Attribute Filter")
    public static boolean ItemAttributeFilter = false;

    @Property(type = Property.Type.TEXT, name = "Attribute 1", parent = "Item Attribute Filter")
    public static String Attribute1 = "double_hook";

    @Property(type = Property.Type.TEXT, name = "Attribute 2", parent = "Item Attribute Filter")
    public static String Attribute2 = "fishing_speed";

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

    @Property(type = Property.Type.BOOLEAN, name = "No Slowdown - SkyBlock Only", parent = "No Slowdown")
    public static boolean DisableNoSlowOutofSkyBlock = true;

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
                    "till it thinks you have reached the expected speed.")
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

    //Mythological
    @Property(type = Property.Type.FOLDER, name = "Mythological", parent = "QOL")
    public static boolean Mythological = false;

    @Property(type = Property.Type.BOOLEAN, name = "Burrow Helper", parent = "Mythological",
            description = "Use Ancestral Spade's ability &6Echo&7 twice\n" +
                    "to locate griffin borrows.\n" +
                    "Use the ability once, wait until the particle trail has disappeared,\n" +
                    "move away a bit and use it again.\n" +
                    "Only works when you are looking &cright above&7 or &cright below&7.")
    public static boolean BurrowHelper = false;

    @Property(type = Property.Type.BOOLEAN, name = "Block Ancestral Spade", parent = "Mythological",
            description = "Blocks using ancestral spade if the last trail hasn't disappeared")
    public static boolean BlockAncestralSpade = true;

    @Property(type = Property.Type.BOOLEAN, name = "Block Ancestral Spade - Invalid Clicks", parent = "Mythological",
            description = "Blocks using ancestral spade if you are not looking straight up nor down.")
    public static boolean BlockInvalidClicks = true;

    @Property(type = Property.Type.FOLDER, name = "Other QOLs", parent = "QOL")
    public static boolean OtherQOL = false;

    // FairySoul
    @Property(type = Property.Type.BOOLEAN, name = "Fairy Souls ESP", parent = "Other QOLs")
    public static boolean FairySoulESP = false;

    // RunicESP
    @Property(type = Property.Type.BOOLEAN, name = "Runic Mob ESP", parent = "Other QOLs")
    public static boolean RunicESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Relic ESP", parent = "Other QOLs")
    public static boolean RelicESP = false;

    // NearbyChestESP
    @Property(type = Property.Type.BOOLEAN, name = "Crystal Hollows Nearby Chest ESP", parent = "Other QOLs",
            description = "Radius = 10 * 10 * 10")
    public static boolean ChestESPCrystalHollows = false;

    // MonolithESP
    @Property(type = Property.Type.BOOLEAN, name = "Monolith ESP", parent = "Other QOLs")
    public static boolean MonolithESP = false;

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
            description = "Creates Ghost Block on F7 from Phase 2 to Phase 3.")
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

    @Property(type = Property.Type.BOOLEAN, name = "Quiz Solver", parent = "Solvers",
            description = "Hide wrong answers, and block wrong clicks.\n" +
                    "Fetch newest answer every 10 minutes.")
    public static boolean QuizSolver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Tic Tac Toe Solver", parent = "Solvers",
            description = "Block clicks that let you fail. \n" +
                    "&cWIP, currently useless.")
    public static boolean TicTacToeSolver = false;

    @Property(type = Property.Type.FOLDER, name = "Three Weirdos Solver", parent = "Solvers")
    public static boolean ThreeWeirdosSolverEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Three Weirdos Solver Enable", parent = "Solvers",
            description = "Hide wrong chests.")
    public static boolean ThreeWeirdosSolver = false;

    @Property(type = Property.Type.FOLDER, name = "Water Solver", parent = "Solvers",
            description = "May behave bad at laggy servers.")
    public static boolean WaterSolverEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Water Solver Enable", parent = "Solvers",
            description = "Shows how and when to pull levers to one-flow water puzzle.")
    public static boolean WaterSolver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Water Solver", parent = "Solvers",
            description = "Auto etherwarp and pull levers.")
    public static boolean AutoWater = false;

    @Property(type = Property.Type.FOLDER, name = "Blaze Solver", parent = "Solvers")
    public static boolean BlazeSolver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Blaze", parent = "Blaze Solver",
            description = "Bind a key to use in blaze room!\n" +
                    "Requires dungeon map enabled!")
    public static boolean AutoBlaze = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Blaze Secret", parent = "Blaze Solver",
            description = "Auto Etherwarp to secret after shooting blazes.\n" +
                    "Requires Auto Blaze.")
    public static boolean AutoBlazeSecret = false;

    @Property(type = Property.Type.NUMBER, name = "Etherwarp Delay", parent = "Solvers", suffix = " ms",
            description = "Delay after receiving a packet from etherwarp.\n" +
                    "Not recommend to set this too low!", min = 100, max = 600, step = 5)
    public static int EtherWarpDelayAfter = 400;

    @Property(type = Property.Type.NUMBER, name = "Turn-Shoot Delay", parent = "Blaze Solver", suffix = " ms",
            description = "Delay between a turn and a shot.\n" +
                    "Higher delay = Lower chance to fail.", min = 50, max = 500, step = 5)
    public static int TurnShootDelay = 250;

    @Property(type = Property.Type.NUMBER, name = "Blaze Scale", parent = "Blaze Solver", suffix = " %",
            description = "Scale for blaze in calculation, 100% = 0.6width * 2height.\n" +
                    "Arrows can be inaccurate, this is mc feature (random noise).\n" +
                    "Bigger scale = Lower chance to fail but more tp.", min = 100, max = 500, step = 5)
    public static int BlazeScale = 200;

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

    @Property(type = Property.Type.BOOLEAN, name = "Outline Fix", parent = "Starred Mob ESP",
            description = "Show a Bounding Box around the starred mob\n" +
                    "when corresponding mob is not found,\n" +
                    "in outline mode.")
    public static boolean ShowBoxWhenBoundingNotWork = false;

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

    // M7Dragon
    @Property(type = Property.Type.FOLDER, name = "M7 Phase 5", parent = "Dungeons")
    public static boolean M7P5 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Show Statue Box", parent = "M7 Phase 5",
            description = "Render statues bounding box in M7 P5 with their colors.")
    public static boolean ShowStatueBox = false;

    @Property(type = Property.Type.NUMBER, name = "Dragon Check CD", parent = "M7 Phase 5", suffix = " ms",
            description = "How often to check dragons' colors based on relics.")
    public static int DragonCheckCD = 500;

    @Property(type = Property.Type.SELECT, name = "Replace Dragon Texture", parent = "M7 Phase 5",
            description = "Replace dragons' texture in M7 P5 with their colors.\n" +
                    "&6COLOR&r mode: Show the dragons' corresponding color.\n" +
                    "&4HP&r mode: Show &cRed&r, &6Orange&r, or &aGreen&r based on health.\n" +
                    "  &aGreen&r: >300M hp\n" +
                    "  &6Orange&r: 100~300M hp\n" +
                    "  &cRed&r: <100M hp",
            options = {"NONE", "COLOR", "HP"}
    )
    public static int ReplaceDragonTexture = 1;

    @Property(type = Property.Type.BOOLEAN, name = "Remove Dragon Hurt Render", parent = "M7 Phase 5",
            description = "So you can see its color more easily."
    )
    public static boolean RemoveDragonHurtRender = true;

    @Property(type = Property.Type.FOLDER, name = "Dragon Info Display", parent = "M7 Phase 5",
            description = "Display Dragon HP and their distance to statues.")
    public static boolean dragonInfoDisplayEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Dragon Info Display Test", parent = "Dragon Info Display",
            description = "Display lines to show where the display is at.")
    public static boolean dragonInfoTest = true;

    @Property(type = Property.Type.NUMBER, name = "Dragon Info Display X", parent = "Dragon Info Display")
    public static int dragonInfoX = 400;

    @Property(type = Property.Type.NUMBER, name = "Dragon Info Display Y", parent = "Dragon Info Display")
    public static int dragonInfoY = 160;

    @Property(type = Property.Type.NUMBER, name = "Dragon Info Display Scale", parent = "Dragon Info Display",
            min = 5, max = 100)
    public static int dragonInfoScale = 20;

    @Property(type = Property.Type.BOOLEAN, name = "Dragon Info Display Enable", parent = "Dragon Info Display",
            description = "Display Dragon HP and their distance to statues.")
    public static boolean dragonInfoDisplay = false;

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

    @Property(type = Property.Type.NUMBER, name = "Auto Terminal CD", parent = "Terminals", suffix = " ms",
            min = 80, max = 500, step = 10,
            description = "Cool down between clicks.")
    public static int AutoTerminalCD = 150;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Quit Long Maze", parent = "Terminals",
            description = "Close screen when > 20 clicks needed.")
    public static boolean QuitWhenLongMaze = false;

    // AutoItemFrame
    @Property(type = Property.Type.FOLDER, name = "Auto Devices", parent = "Dungeons")
    public static boolean AutoDevices = false;

    @Property(type = Property.Type.FOLDER, name = "Auto P3-3", parent = "Auto Devices")
    public static boolean AutoDeviceThree = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto P3-3 Arrows", parent = "Auto P3-3",
            description = "When aiming at the itemframe in phase 3 - 3 arrows device,\n" +
                    "automatically rotate the arrows to pass.")
    public static boolean AutoItemFrameArrows = false;

    @Property(type = Property.Type.NUMBER, name = "P3 Arrows CD", parent = "Auto P3-3", suffix = " ms",
            min = 20, max = 500, step = 10,
            description = "Cool down between 2 clicks.")
    public static int ArrowCD = 100;

    @Property(type = Property.Type.NUMBER, name = "P3 Arrows CD Between ItemFrames", parent = "Auto P3-3", suffix = " ms",
            min = 100, max = 500, step = 10,
            description = "Cooldown between calculations. \n" +
                    "Not suggested to be lower than your ping.")
    public static int ArrowCDBetween = 250;

    // SimonSays
    @Property(type = Property.Type.FOLDER, name = "Simon Says Solver", parent = "Auto Devices",
            description = "Show where to click and block wrong clicks.")
    public static boolean SimonSaysSolverEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Simon Says Solver Enable", parent = "Simon Says Solver",
            description = "Show where to click and block wrong clicks.")
    public static boolean SimonSaysSolver = false;

    @Property(type = Property.Type.BOOLEAN, name = "Simon Says Block Wrong Clicks", parent = "Simon Says Solver",
            description = "Show where to click and block wrong clicks.")
    public static boolean SimonSaysBlockWrongClicks = false;

    @Property(type = Property.Type.BOOLEAN, name = "Simon Says Auto Change Direction", parent = "Simon Says Solver",
            description = "Auto Change Direction when you're doing simon says.")
    public static boolean SimonSaysAutoChangeDirection = false;

    // AutoBlood
    @Property(type = Property.Type.FOLDER, name = "Auto Blood", parent = "Dungeons")
    public static boolean AutoBloodEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Blood Enable", parent = "Auto Blood", illegal = true,
            description = "Auto aim and rotate to the blood mob with a Y-Offset.")
    public static boolean AutoBlood = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Blood Yoffset (0.1x block)", parent = "Auto Blood",
            min = 0, max = 20, description = "Higher ping, higher offset!")
    public static int AutoBloodYoffset = 0;

    @Property(type = Property.Type.NUMBER, name = "Auto Blood CD", parent = "Auto Blood", illegal = true, suffix = " ms",
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

    // SecretChecker
    @Property(type = Property.Type.BOOLEAN, name = "Secret Checker", parent = "Other Dungeon Features",
            description = "Fetch secrets found before and after dungeon run\n" +
                    "to calculate secrets found during that run\n" +
                    "for each teammate.\n" +
                    "Requires API Key.")
    public static boolean SecretChecker = false;

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

    // Blaze
    @Property(type = Property.Type.FOLDER, name = "Blaze", parent = "Slayer")
    public static boolean BlazeEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Blaze Slayer Helper", parent = "Blaze",
            description = "Auto swap blades and right click when left-clicking.")
    public static boolean BlazeSlayerHelper = false;

    @Property(type = Property.Type.NUMBER, name = "Blaze Slayer Helper Cache Time", parent = "Blaze",
            description = "Set this above your ping.",
            min = 50, max = 500, step = 5, suffix = " ms")
    public static int BlazeHelperCD = 250;

    @Property(type = Property.Type.FOLDER, name = "Pillar Display", parent = "Blaze")
    public static boolean PillarDisplayEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Pillar Info Display Test", parent = "Pillar Display",
            description = "Display lines to show where the display is at.")
    public static boolean PillarTest = true;

    @Property(type = Property.Type.NUMBER, name = "Pillar Display X", parent = "Pillar Display")
    public static int PillarX = 400;

    @Property(type = Property.Type.NUMBER, name = "PillarDisplay Y", parent = "Pillar Display")
    public static int PillarY = 160;

    @Property(type = Property.Type.NUMBER, name = "Pillar Display Scale", parent = "Pillar Display",
            min = 5, max = 100)
    public static int PillarScale = 20;

    @Property(type = Property.Type.BOOLEAN, name = "Pillar Info Display Enable", parent = "Pillar Display")
    public static boolean PillarDisplay = false;

    // Skills
    @Property(type = Property.Type.FOLDER, name = "Skills")
    public static boolean SkillsEnabled = false;

    // AutoPowder
    @Property(type = Property.Type.FOLDER, name = "Powder Relevant", parent = "Skills")
    public static boolean AutoPowderEnabled = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Powder Enabled", parent = "Powder Relevant", illegal = true,
            description = "AFK mine hardstone and open chests. Low efficiency!\n" +
                    "Toggleable through keybindings.\n" +
                    "&cThis feature is just for testing and won't be updated.\n" +
                    "&c&lDONT FULLY AFK!")
    public static boolean AutoPowder = false;

    // AutoPowderChest
    @Property(type = Property.Type.BOOLEAN, name = "Auto Crystal Hollows Chest", parent = "Powder Relevant", illegal = true,
            description = "Automatically opens powder chests. Toggleable through keybindings.")
    public static boolean AutoPowderChest = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Crystal Hollows Chest - Close", parent = "Powder Relevant",
            description = "Automatically closes powder chests opened by " +
                    "Auto Crystal Hollows Chest.")
    public static boolean AutoPowderChestClose = true;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Close Loot Chests in Crystal Hollows", parent = "Powder Relevant")
    public static boolean AutoCloseCrystalHollowsChest = false;

    // Fishing
    @Property(type = Property.Type.FOLDER, name = "Fishing", parent = "Skills")
    public static boolean FishingFeatures = false;

    @Property(type = Property.Type.BOOLEAN, name = "Unload Useless Entities", parent = "Fishing",
            description = "Unload useless entities that let your fps drop.\n" +
                    "Currently debugging, useless.")
    public static boolean UnloadUnusedNPCEntity = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Pulling Rod", parent = "Fishing", illegal = true,
            description = "Auto Pull Rod When fishing.\n" +
                    "Water / lava fishing are supported.")
    public static boolean AutoPullRod = false;

    @Property(type = Property.Type.SELECT, name = "Fishing Mode", parent = "Fishing",
            description = "Lava Fishing - Pull rod, swap to ice spray, right click, swap to wither blade.\n" +
                    "Regular Fishing - Pull rod, wait 200ms, cast rod.", options = {"Lava", "Regular"})
    public static int FishingMode = 1;

    @Property(type = Property.Type.NUMBER, name = "Fishing Check CD", parent = "Fishing",
            description = "Check if fishing rod is there and in lava, recast if not.",
            min = 1, max = 60, step = 1, suffix = " s")
    public static int FishCheckCD = 3;

    @Property(type = Property.Type.NUMBER, name = "Auto Move Time", parent = "Fishing",
            description = "Set time t, and you'll be randomly move for [t, 2t) every time.",
            min = 0, max = 1000, step = 10, suffix = " ms")
    public static int AutoMoveTime = 100;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Move - Press Sneak", parent = "Fishing")
    public static boolean AutoMoveSneak = true;

    @Property(type = Property.Type.NUMBER, name = "Auto Pulling Rod Blob Time", parent = "Fishing",
            description = "Minimum time between 2 pulls.\n" +
                    "If you're grinding slugfish, set this to 30000.\n" +
                    "Else, set this to 600.",
            min = 100, max = 30000, step = 100, suffix = " ms")
    public static int ReelCD = 600;

    @Property(type = Property.Type.NUMBER, name = "Pull-cast CD", parent = "Fishing",
            description = "Cooldown between pulling and casting.",
            min = 50, max = 6000, step = 10, suffix = " ms")
    public static int PullCastCD = 200;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Move Randomly", parent = "Fishing", illegal = true,
            description = "Move around and rotate to bypass afk test \n" +
                    "and gain fishing exp while afk.\n" +
                    "Requires keybinding to toggle.\n" +
                    "&c&lDONT FULLY AFK!\n")
    public static boolean AutoMove = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Move - Stop When New Discovery", parent = "Fishing")
    public static boolean StopWhenNewDiscovery = false;

    @Property(type = Property.Type.BOOLEAN, name = "Main Lobby Auto Move", parent = "Fishing",
            description = "Requires Auto Move enabled!")
    public static boolean MainLobbyAutoMove = false;

    @Property(type = Property.Type.BOOLEAN, name = "Safer Auto Move", parent = "Fishing",
            description = "Disable Auto Move on world change.")
    public static boolean SafeAutoMove = true;
    @Property(type = Property.Type.BOOLEAN, name = "Auto Move Timer", parent = "Fishing",
            description = "Alarm when you auto-moved for 4min40s.\n" +
                    "Display timer in 'display day and coords'.")
    public static boolean AutoMoveTimer = true;

    @Property(type = Property.Type.BOOLEAN, name = "Jawbus Warn", parent = "Fishing",
            description = "Alarm when someone in your lobby dies to a jawbus.")
    public static boolean JawbusWarn = true;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Move Recast", parent = "Fishing",
            description = "Recast fishing rod when rod is active for too long and auto-move is on.")
    public static boolean AutoMoveRecast = true;

    @Property(type = Property.Type.NUMBER, name = "Auto Move Recast Time", parent = "Fishing",
            min = 15, max = 200, step = 5,
            suffix = " s")
    public static int AutoMoveRecastTime = 60;

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

    // Farming
    @Property(type = Property.Type.FOLDER, name = "Farming", parent = "Skills")
    public static boolean FarmingFeatures = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Build Farm - Step 1", parent = "Farming",
            description = "Stand on a block to start.\n" +
                    "/farmingpoint to set farming point for corp to grow.\n" +
                    "Keep adjusting till the expected blocks are correct.\n" +
                    "Press the keybind and move along the highlighted block direction.\n" +
                    "&6Requires Infinite Dirt Wand.")
    public static boolean AutoBuildFarm1 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Build Farm - Step 2", parent = "Farming",
            description = "Stand on left top of your farm.\n" +
                    "Make sure you can fall between layers.\n" +
                    "Use rancher's boots to set speed to 220\n" +
                    "This step automatically helps you to remove the dirt.")
    public static boolean AutoBuildFarm2 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Build Farm - Step 3", parent = "Farming",
            description = "This makes you move along and till the dirt.")
    public static boolean AutoBuildFarm3 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Build Farm - Step 4", parent = "Farming",
            description = "Stand at the top edge of your farm." +
                    "Make sure your stash is full of materials.\n" +
                    "Put 1 material in your basket of seeds.\n" +
                    "Automatically /pickupstash and use basket of seeds.\n" +
                    "&cDon't move or click, only press keybind if u want to quit.")
    public static boolean AutoBuildFarm4 = false;

    @Property(type = Property.Type.BOOLEAN, name = "Auto Build Farm - Step 5", parent = "Farming",
            description = "Places lighting blocks for you.\n" +
                    "Make sure your inventory is filled with Sea Lantern or Glowstone.\n")
    public static boolean AutoBuildFarm5 = false;

    @Property(type = Property.Type.NUMBER, name = "Island Size", parent = "Farming")
    public static int IslandSize = 170;

    @Property(type = Property.Type.NUMBER, name = "Auto Build Farm - Step 5 CD", parent = "Farming",
            description = "CD between placing blocks.", min = 20, max = 2000, step = 20)
    public static int AutoBuildFarm5CD = 320;

    @Property(type = Property.Type.NUMBER, name = "Auto Build Farm - Step 1 CD", parent = "Farming",
            description = "CD between placing dirt. Must be higher than your ping.", min = 100, max = 2000, step = 20)
    public static int AutoBuildFarm1CD = 500;


    @Property(type = Property.Type.BOOLEAN, name = "Auto Farming", parent = "Farming",
            description = "Auto Farm for this kind of vertical farm.\n")
    public static boolean AutoFarm = false;


    // GemstoneESP
    @Property(type = Property.Type.FOLDER, name = "Gemstone ESP", parent = "Skills",
            description = "Display ESP boxes of gemstones, \n" +
                    "with an alpha related to gem-player distance.")
    public static boolean GemstoneESPEnable = false;

    @Property(type = Property.Type.BOOLEAN, name = "Gemstone ESP Enable", parent = "Gemstone ESP")
    public static boolean GemstoneESP = false;

    @Property(type = Property.Type.BOOLEAN, name = "Include Glass Panes", parent = "Gemstone ESP")
    public static boolean IncludeGlassPanes = false;

    @Property(type = Property.Type.NUMBER, name = "Scan Radius", parent = "Gemstone ESP", suffix = " blocks",
            min = 5, max = 30,
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

    @Property(type = Property.Type.NUMBER, name = "Treasure CD", parent = "Jade Crystal Helper", suffix = " ms",
            description = "To ensure current position corresponds with current distance",
            min = 200, max = 600, step = 20)
    public static int JadeCrystalCD = 400;

    // Experimentation
    @Property(type = Property.Type.FOLDER, name = "Auto Experimentation Table", parent = "Skills",
            description = "Auto Solves Experimentation Table.\n" +
                    "Conflicts with other solvers, and OldAnimation mod.")
    public static boolean ExperimentationTable = false;

    @Property(type = Property.Type.NUMBER, name = "Auto Experimentation Table CD", parent = "Auto Experimentation Table", suffix = " ms",
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

    @Property(type = Property.Type.BOOLEAN, name = "Map Display Enabled", parent = "Map",
            description = "Disable this if you don't want to see this shit.")
    public static boolean MapDisplay = true;


    @Property(type = Property.Type.BOOLEAN, name = "Disable Map In Boss", parent = "Map")
    public static boolean DisableMapInBoss = true;

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

    @Property(type = Property.Type.NUMBER, name = "Player Head Scale", parent = "Map", suffix = " %",
            min = 1, max = 100)
    public static int HeadScale = 50;

    @Property(type = Property.Type.NUMBER, name = "Map X", parent = "Map",
            min = 0, max = 1000, step = 5)
    public static int MapX = 0;

    @Property(type = Property.Type.NUMBER, name = "Map Y", parent = "Map",
            min = 0, max = 1000, step = 5)
    public static int MapY = 0;

    @Property(type = Property.Type.NUMBER, name = "Map Bounding Thickness", parent = "Map",
            min = 0, max = 10)
    public static int MapBoundingThickness = 2;

    @Property(type = Property.Type.SELECT, name = "Map Bounding Color", parent = "Map",
            options = {"§aGreen", "§bAqua", "§cRed", "§dPink", "§eYellow", "§fWhite",
                    "§0Black", "§1Dark Blue", "§2Dark Green", "§3Cyan",
                    "§4Dark Red", "§5Purple", "§6Gold", "§7Gray",
                    "§8Dark Gray", "§9Blue"})
    public static int MapBoundingColor = 0;

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

    @Property(type = Property.Type.NUMBER, name = "Darken Unexplored Factor", parent = "Rooms",
            description = "Determine how dark unexplored rooms are.", suffix = " %", min = 0, max = 100)
    public static int DarkenUnexploredPercent = 34;

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
