package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.StonklessStonk;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TabUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec4b;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.floor;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Dungeon {
    public static final HashMap<String, Double> floorSecrets = new HashMap<String, Double>() {
        {
            put("F1", 0.3);
            put("F2", 0.4);
            put("F3", 0.5);
            put("F4", 0.6);
            put("F5", 0.7);
            put("F6", 0.85);
            put("F7", 1D);
        }
    };
    private static final String[] entryMessages = new String[]{
            "[BOSS] Bonzo: Gratz for making it this far, but I’m basically unbeatable.",
            "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
            "[BOSS] The Professor: I was burdened with terrible news recently...",
            "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
            "[BOSS] Livid: Welcome, you arrive right on time. I am Livid, the Master of Shadows.",
            "[BOSS] Sadan: So you made it all the way here...and you wish to defy me? Sadan?!",
            "[BOSS] Necron: Finally, I heard so much about you. The Eye likes you very much."
    };
    private static final KeyBind normalRoomNameKeyBind = new KeyBind("Display Normal Room Name Toggle", Keyboard.KEY_NONE);
    public static boolean isFullyScanned = false;
    // states
    public static boolean isInDungeon = false;
    public static long runStarted = 0;
    public static long bloodOpen = 0;
    public static long watcherDone = 0;
    public static long bossEntry = 0;
    public static long runEnded = 0;
    public static int openedWitherDoors = 0;
    public static int startX = 15;
    public static int startZ = 15;
    public static int endX = 190;
    public static int endZ = 190;
    public static int roomSize = 31;
    // dungeons
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<Door> doors = new ArrayList<>();
    public static ArrayList<String> puzzles = new ArrayList<>();
    public static int totalRooms = -1;
    public static int witherDoors = 0;
    public static String trapType = "Unknown";
    public static boolean bloodDone = false;
    public static boolean trapDone = false;
    public static boolean yellowDone = false;
    // calc
    public static String currentRoom = "";
    public static int openedRooms;
    public static int completedRooms;
    public static int crypts;
    public static int deaths;
    public static int skillScore = 0;
    public static int exploreScore = 0;
    public static int bonusScore = 0;
    public static int score = 0;
    public static boolean said300 = false;
    public static int puzzleCount = 0;
    public static int puzzleDone = 0;
    public static int totalSecrets = 0;
    public static double overflowSecrets = 0;
    public static int calculatedTotalSecrets = 0;
    public static double secretsPercent = 0;
    public static double secretsNeeded = 1;
    public static int secretsFound = 0;
    public static double secretsForMax = 0;
    public static boolean isMimicDead = false;
    public static Image greenCheck = new Image("BloomMapGreenCheck.png");
    public static Image whiteCheck = new Image("BloomMapWhiteCheck.png");
    public static Image failedRoom = new Image("BloomMapFailedRoom.png");
    public static Image newGreenCheck = new Image("MapGreenCheck.png");
    public static Image newWhiteCheck = new Image("MapWhiteCheck.png");
    public static Image questionMark = new Image("BloomMapQuestionMark.png");
    public static int floorInt;
    public static String message300 = "8173c4nh29384tcn28734mco8haeuyfgblcaii34icy5jmo8137gbqglgieaw83m7yrho8yahblgwtmp0983q1hc9liuba,wkhznf";
    // scan
    private static boolean isScanning = false;
    private static long lastScan = 0;
    // map
    private static BufferedImage map = null;
    private static Vector2i mapSize = new Vector2i(0, 0);
    // players
    private static ArrayList<Player> players = new ArrayList<>();
    // score str
    private static String scoreString1;
    private static String scoreString2;
    private HashSet<String> scannedPuzzles = new HashSet<>();
    private boolean enableNormalRoomName = true;

    public static void makeMap() {
        BufferedImage newMap = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            Color color = room.getColor();
            if (room.name.equals("Unknown")) color = new Color(255, 176, 31);
            else if (!room.explored && Configs.DarkenUnexplored) color = color.darker().darker().darker();
            setPixels(newMap, MathUtils.floor(room.x / 16) * 2 + 1, MathUtils.floor(room.z / 16) * 2 + 1, 3, 3, color);
        }
        for (int i = 0; i < doors.size(); i++) {
            Door door = doors.get(i);
            Color color = door.getColor();
            if (!door.explored && Configs.DarkenUnexplored) color = color.darker().darker().darker();
            setPixels(newMap, MathUtils.floor(door.x / 16) * 2 + 2, MathUtils.floor(door.z / 16) * 2 + 2, 1, 1, color);
        }
        map = newMap;
    }

    private static void setPixels(BufferedImage map, int x1, int y1, int wid, int height, Color color) {
        for (int x = x1; x < x1 + wid; x++) {
            for (int y = y1; y < y1 + height; y++) {
                map.setRGB(x, y, color.getRGB());
            }
        }
    }

    public static boolean isPlayerMage(String name) {
        for (Player player : players)
            if (player.name.equals(name))
                return player.className.equals("MAGE");
        return false;
    }

    public static void showPlayers() {
        for (Player player : players) {
            ChatLib.chat(player.name + ": " + player.className);
        }
    }

    public static void showMap() {
        byte[] colors = Map.getMapColors();
        if (colors == null) return;
        for (int y = 0; y < 128; y++) {
            StringBuilder res = new StringBuilder();
            for (int x = 0; x < 128; x++) {
                res.append(String.format("%3d", colors[x + y * 128])).append(" ");
            }
            ChatLib.chat(res.toString());
        }

        for (int i = Map.startCorner.x + (Map.roomSize / 2); i < 128; i += Map.roomSize / 2 + 2) {
            for (int j = Map.startCorner.y + (Map.roomSize / 2); j < 128; j += Map.roomSize / 2 + 2) {
                byte color = colors[i + j * 128];
                byte secondColor = colors[(i - 3) + j * 128];
                ChatLib.chat(i + ", " + j + ", " + color + ", " + secondColor);
            }
        }
    }

    public static void showDungeonInfo() {
        if (!isInDungeon && isFullyScanned) return;
        ChatLib.chat("Dungeon floor: " + floorInt);
        ChatLib.chat("Total Rooms: " + totalRooms);
        ChatLib.chat("Start Corner: " + Map.startCorner);
        ChatLib.chat("Room Size: " + Map.roomSize);
        showMap();
    }

    public static void updatePlayers() {
        if (!isInDungeon) return;
        ArrayList<String> tab = TabUtils.getNames();
        if (tab.size() < 18) return;
        int iconNum = 0;
        for (int line : new int[]{5, 9, 13, 17, 1}) {
            String tabLine = ChatLib.removeFormatting(tab.get(line)).trim();
            boolean dead = tabLine.contains("(DEAD)");
            if (!tabLine.contains(" ")) continue;
            String name = tabLine.split(" ")[0];
            if (name.contains("[")) name = tabLine.split(" ")[1];
            if (name.length() == 0) continue;
            String className = "";
            if (tabLine.toUpperCase().contains("(MAGE")) className = "MAGE";
            else if (tabLine.toUpperCase().contains("(ARCHER")) className = "ARCHER";
            else if (tabLine.toUpperCase().contains("(TANK")) className = "TANK";
            else if (tabLine.toUpperCase().contains("(HEALER")) className = "HEALER";
            else if (tabLine.toUpperCase().contains("(BERSERK")) className = "BERSERK";

            boolean found = false;
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (player.name.equals(name)) {
                    found = true;
                    player.isDead = dead;
                    player.className = className;
                    player.icon = dead ? null : "icon-" + iconNum;
                }
            }
            if (!found) {
                Player newPlayer = new Player();
                newPlayer.name = name;
                newPlayer.isDead = dead;
                newPlayer.icon = dead ? null : "icon-" + iconNum;
                newPlayer.className = className;
                players.add(newPlayer);
            }
            if (!dead) iconNum++;
        }
        java.util.Map<String, Vec4b> decor = Map.getMapDecorators();
        if (decor != null) {
            for (java.util.Map.Entry<String, Vec4b> entry : decor.entrySet()) {
                String decorIcon = entry.getKey();
                Vec4b vec = entry.getValue();
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.get(i);
                    if (player.inRender) continue;
                    if (decorIcon.equals(player.icon) &&
                            !getPlayer().getName().equals(player.name)) {
                        player.iconX = (vec.func_176112_b() + 128 - Map.startCorner.x * 2.5) / 10 * Configs.MapScale;
                        player.iconY = (vec.func_176113_c() + 128 - Map.startCorner.y * 2.5) / 10 * Configs.MapScale;
                        player.yaw = (vec.func_176111_d() * 360) / 16F + 180;

                        player.realX = player.iconX * 1.64;
                        player.realZ = player.iconY * 1.64;
                    }
                }
            }
        }
    }

    public static void updateRooms() {
        byte[] colors = Map.getMapColors();
        if (colors == null) return;
        int width = 0, height = 0;

        for (int i = Map.startCorner.x + (Map.roomSize / 2); i < 128; i += Map.roomSize / 2 + 2) {
            for (int j = Map.startCorner.y + (Map.roomSize / 2); j < 128; j += Map.roomSize / 2 + 2) {
                Vector2i coords = Lookup.getRoomCenterCoords(
                        new Vector2i(MathUtils.floor((i - Map.startCorner.x) * (190F / 128)),
                                MathUtils.floor((j - Map.startCorner.y) * (190F / 128))));
                if (coords == null) continue;
                byte color = colors[i + j * 128];
                byte secondColor = colors[(i - 3) + j * 128];
                Room room = getRoomAt(coords);
                if (room == null) continue;

                // Middle of rooms
                if (width % 2 == 0 && height % 2 == 0) {
                    if (color == 30 && secondColor != 30) {
                        checkRoom(room.name, "green");
                    }
                    if (color == 34) {
                        checkRoom(room.name, "white");
                    }
                    if (color == 18 && secondColor != 18) {
                        checkRoom(room.name, "failed");
                    }

                    // Check if trap, blood and yellow are done
                    if (color == 30 || color == 34) {
                        if (secondColor == 62) trapDone = true;
                        if (secondColor == 18) bloodDone = true;
                        if (secondColor == 74) yellowDone = true;
                    }

                    // Set room to explored = true so it isn't darkened on the map
                    // Set room to explored = false so it gets darkened
                    setExplored(room.name, color != 0 && color != 85 && color != 119);
                }
                height++;
            }
            width++;
        }
    }

    private static void checkRoom(String name, String mark) {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.name.equals(name))
                room.checkmark = mark;
        }
    }

    private static void setExplored(String name, boolean explored) {
        if (XiaojiaAddons.isDebug()) {
            ChatLib.chat("set " + name + " to " + explored);
        }
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.name.equals(name))
                room.explored = explored;
        }
    }

    public static Room getRoomAt(Vector2i coords) {
        coords = Lookup.getRoomCenterCoords(coords);
        if (coords == null) return null;
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (room.x == coords.x && room.z == coords.y)
                return room;
        }
        return null;
    }

    public static void updateDoors() {
        ArrayList<Door> toRemove = new ArrayList<>();
        for (int i = 0; i < doors.size(); i++) {
            Door door = doors.get(i);
            if (door == null) continue;
            int id = Block.getIdFromBlock(BlockUtils.getBlockAt(door.x, 70, door.z));
            if (id == 0 || id == 166) door.type = "normal";
            // always light wither
            if (Configs.AlwaysHighlightWitherDoor && door.type.equals("wither")) {
                door.explored = true;
                continue;
            }
            Room room1 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(door.x + 4, door.z + 16)));
            Room room2 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(door.x - 4, door.z - 16)));
            Room room3 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(door.x + 16, door.z + 4)));
            Room room4 = getRoomAt(Lookup.getRoomCenterCoords(new Vector2i(door.x - 16, door.z - 4)));
            if (room1 != null && room2 != null && room1.x == room2.x) {
                if (!room1.explored && !room2.explored) {
                    door.explored = false;
                    continue;
                }
            } else if (room3 != null && room4 != null && room3.z == room4.z) {
                if (!room3.explored && !room4.explored) {
                    door.explored = false;
                    continue;
                }
            }
            // Room has one or no connected sides, not actually a door or in wrong place
            else if (door.type.equals("entrance")) {
                // This is usally the extended part of the entrance room, so delete it.
                toRemove.add(door);
            }
            door.explored = true;
        }
        for (Door door : toRemove) {
            doors.remove(door);
        }
    }

    @SubscribeEvent
    public void onTickKeyBind(TickEndEvent event) {
        if (normalRoomNameKeyBind.isPressed()) {
            enableNormalRoomName = !enableNormalRoomName;
            ChatLib.chat(enableNormalRoomName ? "Normal Room Name &aactivated" : "Normal Room Name &cdeactivated");
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.equals("[NPC] Mort: Here, I found this map when I first entered the dungeon."))
            runStarted = TimeUtils.curTime();
        if (message.startsWith("[BOSS] The Watcher") && bloodOpen == 0) bloodOpen = TimeUtils.curTime();
        if (message.equals("[BOSS] The Watcher: You have proven yourself. You may pass."))
            watcherDone = TimeUtils.curTime();
        if (message.endsWith("opened a WITHER door!")) openedWitherDoors++; // TODO: prevent chat polluting
        if (MapUtils.includes(entryMessages, message)) bossEntry = TimeUtils.curTime();
        if (message.equals("                             > EXTRA STATS <")) runEnded = TimeUtils.curTime();
    }

    @SubscribeEvent
    public void onTickScan(TickEndEvent event) {
        if (isInDungeon && !isScanning && Configs.AutoScan && !isFullyScanned &&
                TimeUtils.curTime() - lastScan >= 500 && Configs.MapEnabled) {
            lastScan = TimeUtils.curTime();
            new Thread(this::scan).start();
        }
        if (isFullyScanned && !Map.calibrated) {
            Map.calibrate();
            if (Map.calibrated && Configs.ChatInfo) {
                ChatLib.chat("&aCurrent Dungeon:\n" +
                        String.format("&aPuzzles &c%d&a: \n &b- &d%s\n", puzzles.size(), String.join("\n &b- &d", scannedPuzzles)) +
                        String.format("&6Trap: &a%s\n", trapType) +
                        String.format("&8Wither Doors: &7%d\n", witherDoors - 1) +
                        String.format("&7Total Secrets: &b%s\n", totalSecrets)
                );
            }
        }
    }

    @SubscribeEvent
    public void onRenderMap(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (!isInDungeon || !Configs.MapEnabled) return;
        RenderUtils.start();
        try {
            drawBackground();
            if (map != null) {
                renderMap();
            }
            renderCheckmarks();
            // render rooms
            HashSet<String> nameRendered = new HashSet<>();
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                if (nameRendered.contains(room.name)) continue;
                nameRendered.add(room.name);

                if (room.type.equals("puzzle") && Configs.ShowPuzzleName ||
                        room.type.equals("trap") && Configs.ShowTrapName)
                    room.renderName();
                else if ((room.type.equals("normal") || room.type.equals("rare")) &&
                        Configs.ShowNormalName && enableNormalRoomName)
                    room.renderName();
                if (Configs.ShowSecrets != 0 &&
                        (room.type.equals("normal") || room.type.equals("rare"))) {
                    room.renderSecrets();
                }
            }
            // render players
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                if (player.isDead) continue;
                player.render();
                ItemStack heldItem = ControlUtils.getHeldItemStack();
                if ((heldItem != null && heldItem.getDisplayName().contains("Spirit Leap") &&
                        Configs.ShowPlayerNames == 1) ||
                        Configs.ShowPlayerNames == 2) {
                    if (!player.name.equals(getPlayer().getName())) {
                        player.renderName();
                    }
                }
            }
            // render score
            if (Configs.ScoreCalculation) drawScoreCalc();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RenderUtils.end();
        }
    }

    @SubscribeEvent
    public void onTickCheckScanIsFull(TickEndEvent event) {
        if (isInDungeon) {
            if (!isFullyScanned) {
                scoreString1 = "&cDungeon has not been";
                scoreString2 = "&cfully scanned.";
            } else {
                try {
                    calcScore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTickUpdatePlayerIcon(TickEndEvent event) {
        if (!isInDungeon || getWorld() == null) return;
        try {
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                EntityPlayer entityPlayer = getWorld().getPlayerEntityByName(player.name);
                if (entityPlayer == null) {
                    player.inRender = false;
                    continue;
                }
                if (MapUtils.isBetween((int) entityPlayer.posX, 0, 190) &&
                        MapUtils.isBetween((int) entityPlayer.posZ, 0, 190)) {
                    player.inRender = true;
                    player.realX = entityPlayer.posX;
                    player.realZ = entityPlayer.posZ;
                    player.iconX = (player.realX * (0.1225 * 5) - 2) * 0.2 * Configs.MapScale + Configs.MapScale / 2f;
                    player.iconY = (player.realZ * (0.1225 * 5) - 2) * 0.2 * Configs.MapScale + Configs.MapScale / 2f;
                    player.yaw = entityPlayer.rotationYaw + 180;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        reset();
    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!isInDungeon) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.startsWith(" ☠ ")) {
//            CommandsUtils.addCommand("/pc " + deathMessage);
        }
        String upperMessage = message.toUpperCase();
        if (upperMessage.contains("MIMIC DEAD!") || upperMessage.contains("MIMIC KILLED!") ||
                upperMessage.contains("SKYTILS-DUNGEON-SCORE-MIMIC")) {
            isMimicDead = true;
        }
    }

    @SubscribeEvent
    public void onTickCheckCurrentRoom(TickEndEvent event) {
        if (!isInDungeon || !isFullyScanned) {
            StonklessStonk.setInPuzzle(false);
            currentRoom = "";
            return;
        }
        if (bossEntry > runStarted) {
            StonklessStonk.setInPuzzle(true);
            return;
        }
        int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));
        if (XiaojiaAddons.isDebug()) ChatLib.chat("x: " + x + ", z: " + z);
        for (Room room : rooms) {
            if (MapUtils.isBetween(x, room.x - 16, room.x + 16) &&
                    MapUtils.isBetween(z, room.z - 16, room.z + 16)) {
                currentRoom = room.name;
                StonklessStonk.setInPuzzle(room.type.equals("puzzle") &&
                        (room.name.equals("Water Board") || room.name.equals("Three Weirdos")) ||
                        room.name.equals("Unknown"));
                return;
            }
        }
        currentRoom = "";
        StonklessStonk.setInPuzzle(false);
    }

    @SubscribeEvent
    public void onTickUpdateDungeon(TickEndEvent event) {
        if (!Configs.MapEnabled || !SkyblockUtils.isInDungeon()) return;
        String dungeon = SkyblockUtils.getDungeon();
        if (!isInDungeon) floorInt = Integer.parseInt(dungeon.substring(1));
        isInDungeon = true;
        if (floorInt == 1 || floorInt == 2 || floorInt == 3) {
            endX = endZ = 158;
        } else if (floorInt == 4) {
            endX = 190;
            endZ = 158;
        } else {
            endX = endZ = 190;
        }
        try {
            ArrayList<String> tab = TabUtils.getNames();
            if (!isInDungeon || tab.isEmpty() || !tab.get(0).contains("Party (")) return;
            puzzles = new ArrayList<>(
                    Arrays.asList(Arrays.stream(new int[]{48, 49, 50, 51, 52})
                            .mapToObj(tab::get).filter(line -> !line.equals(""))
                            .toArray(String[]::new))
            );
            puzzleCount = getInt(tab.get(47), Pattern.compile("Puzzles: \\((\\d+)\\)"));
            puzzleDone = 0;
            for (int i = 1; i <= 5; i++)
                if (tab.get(47 + i).contains("✔"))
                    puzzleDone++;
            secretsFound = getInt(tab.get(31), Pattern.compile("Secrets Found: (\\d+)"));
            secretsPercent = getFloat(tab.get(44), Pattern.compile("Secrets Found: (.+)%"));
            secretsNeeded = floorSecrets.containsKey(dungeon) ? floorSecrets.get(dungeon) : 1;
            // Total secrets in the dungeon based off of the percentage found
            calculatedTotalSecrets = 0;
            if (secretsFound > 0) {
                calculatedTotalSecrets = MathUtils.floor(100F / secretsPercent * secretsFound + 0.5);
            }
            // If a secret has been found then use the percentage on the tablist to calculate exactly how many are in the run. Makes it so that if the scan fails to detect a room
            // it will still be accurate after a secret has been found.
            secretsForMax = (calculatedTotalSecrets > 0 ? calculatedTotalSecrets * secretsNeeded : totalSecrets * secretsNeeded);
            overflowSecrets = secretsFound > secretsForMax ? secretsFound - secretsForMax : 0;
            crypts = getInt(tab.get(32), Pattern.compile("Crypts: (\\d+)"));
            deaths = getInt(tab.get(25), Pattern.compile("Deaths: \\((\\d+)\\)"));
            openedRooms = getInt(tab.get(42), Pattern.compile("Opened Rooms: (\\d+)"));
            completedRooms = getInt(tab.get(43), Pattern.compile("Completed Rooms: (\\d+)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getInt(String s, Pattern pattern) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            int res = Integer.parseInt(matcher.group(1));
            return res;
        }
        System.err.println("error getting int: " + s + ", " + pattern);
        return 0;
    }

    private float getFloat(String s, Pattern pattern) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            float res = Float.parseFloat(matcher.group(1));
            return res;
        }
        System.err.println("error getting float: " + s + ", " + pattern);
        return 0;
    }

    public static void showRooms() {
        for (Room room : rooms) {
            ChatLib.chat(room.name + " is at " + room.x + ", " + room.z);
        }
    }

    private static void addRoom(Room room) {
        if (room.name.equals("Unknown")) return;
        rooms.add(room);
    }

    private void scan() {
        if (isScanning) return;
        isScanning = true;
        ArrayList<Player> curPlayers = players;
        reset();
        players = curPlayers;

        boolean allLoaded = true;
        HashSet<String> names = new HashSet<>();
        scannedPuzzles = new HashSet<>();

        for (int x = startX;
             x <= startX + (roomSize + 1) * (Math.floor((endX / 31F) - 1));
             x += Math.floor((roomSize + 1) / 2F)) {
            for (int z = startZ;
                 z <= startZ + (roomSize + 1) * (Math.floor((endZ / 31F) - 1));
                 z += Math.floor((roomSize + 1) / 2F)) {
                // Center of where a room should be
                if (x % (roomSize + 1) == Math.floor(roomSize / 2F) &&
                        z % (roomSize + 1) == Math.floor(roomSize / 2F)) {
                    if (!MapUtils.chunkLoaded(new Vector3i(x, 100, z))) {
                        allLoaded = false;
                    }
                    if (MapUtils.isColumnAir(x, z)) continue;
                    Room room = Lookup.getRoomFromCoords(new Vector2i(x, z));
                    if (room == null) continue;

                    if (!names.contains(room.name)) {
                        names.add(room.name);
                        totalSecrets += room.secrets;
                    }
                    totalRooms++;
                    addRoom(room);
                    ChatLib.debug(room.name);

                    if (room.type.equals("trap")) trapType = room.name.split(" ")[0];
                    if (room.type.equals("puzzle")) scannedPuzzles.add(room.name);
                }
                // Door or part of a larger room
                else if (((x % (roomSize + 1) == roomSize && z % (roomSize + 1) == Math.floor(roomSize / 2F)) ||
                        (x % (roomSize + 1) == Math.floor(roomSize / 2F) && z % (roomSize + 1) == roomSize)) &&
                        !MapUtils.isColumnAir(x, z)) {
                    // Door
                    if (MapUtils.isDoor(x, z)) {
                        Door door = new Door(x, z);
                        Block doorBlock = BlockUtils.getBlockAt(x, 70, z);
                        if (Block.getIdFromBlock(doorBlock) == 173) {
                            door.type = "wither";
                            witherDoors++;
                        } else if (Block.getIdFromBlock(doorBlock) == 159 &&
                                doorBlock.getMetaFromState(getWorld().getBlockState(new BlockPos(x, 70, z))) == 14) {
                            door.type = "blood";
                        } else if (doorBlock.getRegistryName().equals("minecraft:monster_egg")) {
                            door.type = "entrance";
                        }
                        doors.add(door);
                    }
                    // Part of a larger room
                    else {
                        Room newRoom = new Room(x, z, Data.blankRoom);
                        for (int i = 0; i < rooms.size(); i++) {
                            Room room = rooms.get(i);
                            if (room.x == newRoom.x - 16 && room.z == newRoom.z)
                                newRoom = new Room(x, z, room.getJson());
                            if (room.x == newRoom.x && room.z == newRoom.z - 16)
                                newRoom = new Room(x, z, room.getJson());
                        }
                        if (newRoom.type.equals("entrance")) {
                            Door door = new Door(newRoom.x, newRoom.z);
                            door.type = "entrance";
                            doors.add(door);
                        } else addRoom(newRoom);
                    }
                }
                // Middle of a 2x2 room
                else if (x % (roomSize + 1) == roomSize &&
                        z % (roomSize + 1) == roomSize &&
                        !MapUtils.isColumnAir(x, z)) {
                    Room newRoom = new Room(x, z, Data.blankRoom);
                    for (int i = 0; i < rooms.size(); i++) {
                        Room room = rooms.get(i);
                        if (room.x == newRoom.x - 16 && room.z == newRoom.z - 16)
                            newRoom = new Room(x, z, room.getJson());
                    }
                    addRoom(newRoom);
                }
            }
        }
        makeMap();
        isFullyScanned = allLoaded;
        isScanning = false;
    }

    private void drawBackground() {
        mapSize = Configs.ScoreCalculation ? new Vector2i(25, 27) : new Vector2i(25, 25);
        RenderUtils.drawRect(new Color(0F, 0F, 0F, Configs.BackgroundAlpha / 255F).getRGB(),
                Configs.MapX, Configs.MapY,
                mapSize.x * Configs.MapScale, mapSize.y * Configs.MapScale);
    }

    private void renderMap() {
        RenderUtils.drawImage(new Image(map), Configs.MapX, Configs.MapY, 25 * Configs.MapScale, 25 * Configs.MapScale);
    }

    private void renderCheckmarks() {
        HashSet<String> names = new HashSet<>();
        RenderUtils.retainTransforms(true);
        RenderUtils.translate(Configs.MapX, Configs.MapY);
        RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
        int checkSize = Configs.MapScale * 4;
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if (!names.contains(room.name) && !room.type.equals("entrance")) {

                float x = room.x * 1.25F + Configs.MapScale * 1.25F - checkSize / 2F;
                float y = room.z * 1.25F - checkSize / 4F;

                if (room.checkmark.equals("green")) {
                    if (Configs.DrawCheckMode == 1) RenderUtils.drawImage(greenCheck, x, y, checkSize, checkSize);
                    else if (Configs.DrawCheckMode == 2)
                        RenderUtils.drawImage(newGreenCheck, x, y, checkSize, checkSize);
                    names.add(room.name);
                }
                if (room.checkmark.equals("white")) {
                    if (Configs.DrawCheckMode == 1) RenderUtils.drawImage(whiteCheck, x, y, checkSize, checkSize);
                    else if (Configs.DrawCheckMode == 2)
                        RenderUtils.drawImage(newWhiteCheck, x, y, checkSize, checkSize);
                    names.add(room.name);
                }
                if (room.checkmark.equals("failed")) {
                    if (Configs.DrawCheckMode != 0) RenderUtils.drawImage(failedRoom, x, y, checkSize, checkSize);
                    names.add(room.name);
                }
            }
        }
        RenderUtils.retainTransforms(false);
    }

    private void drawScoreCalc() {
        if (Configs.ScoreCalculation) {
            RenderUtils.translate(Configs.MapX + (25 * Configs.MapScale) / 2F, Configs.MapY + 24.5F * Configs.MapScale);
            RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
            RenderUtils.drawStringWithShadow(scoreString1, -RenderUtils.getStringWidth(ChatLib.removeFormatting(scoreString1)) / 2F, 0);

            RenderUtils.translate(Configs.MapX + (25 * Configs.MapScale) / 2F, Configs.MapY + 25.5F * Configs.MapScale);
            RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
            RenderUtils.drawStringWithShadow(scoreString2, -RenderUtils.getStringWidth(ChatLib.removeFormatting(scoreString2)) / 2F, 0);
        }
    }

    private void calcScore() {
        int deathPenalty = (deaths > 0 && Configs.AssumeSpirit) ? deaths * 2 - 1 : deaths * 2;
        int completedR = bloodDone ? completedRooms : completedRooms + 1;
        completedR += (bossEntry > runStarted) ? 0 : 1;

        skillScore = MathUtils.floor(100 - (14 * puzzleCount) + (14 * puzzleDone) - deathPenalty);
        skillScore = Math.max(skillScore, 20);

        exploreScore = MathUtils.floor((60F * (Math.min(completedR, totalRooms))) / totalRooms) +
                MathUtils.floor(((40F * (secretsFound - overflowSecrets)) / secretsForMax));
        exploreScore = totalRooms == 0 || totalSecrets == 0 ? 0 : exploreScore;

        // Not worth calculating. If you can't get 100 speed score then you shouldn't be playing Dungeons.
        int speedScore = 100;

        bonusScore = (Math.min(crypts, 5)) + (isMimicDead ? 2 : 0) + (Configs.AssumePaul ? 10 : 0);
        score = skillScore + exploreScore + speedScore + bonusScore;
        score = floorInt < 3 || trapDone ? score : score - 5;
        score = yellowDone ? score : score - 5;

        if (XiaojiaAddons.isDebug()) {
            ChatLib.chat("completedR: " + completedR);
            ChatLib.chat("total rooms: " + totalRooms);
            ChatLib.chat("secrets max: " + secretsForMax);
            ChatLib.chat("total secrets: " + totalSecrets);
            ChatLib.chat("found secrets: " + secretsFound);
            ChatLib.chat("calculated secrets: " + calculatedTotalSecrets);
            ChatLib.chat("secrets percent: " + secretsPercent);
            ChatLib.chat("secrets needed: " + secretsNeeded);
            ChatLib.chat("skill: " + skillScore + ", explore: " + exploreScore);
        }

        int secrets = calculatedTotalSecrets > 0 ? calculatedTotalSecrets : totalSecrets;
        // Line 1
        String scSecrets = "&7Secrets: &b" + secretsFound;
        String scSecretsExtra = calculatedTotalSecrets == 0 ? "" : "&8-&e" + (secrets - secretsFound) + "&8-&c" + secrets;
        String scCrypts = crypts == 0 ? "&7Crypts: &c0" : crypts < 5 ? "&7Crypts: &e" + crypts : "&7Crypts: &a" + crypts;
        String scMimic = floorInt < 6 ? "" : isMimicDead ? "&7Mimic: &a✔" : "&7Mimic: &c✘";

        // Line 2
        String scTrap = floorInt <= 2 ? "" : trapDone ? "&7Trap: &a✔" : "&7Trap: &c✘";
        String scDeaths = deaths == 0 ? "&7Deaths: &a0" : "&7Deaths: &c-" + deathPenalty;
        String scScore = score < 270 ? "&7Score: &c" + score : score < 300 ? "&7Score: &e" + score : "&7Score: &a" + score;

        // Assemble the strings
        scoreString1 = (scSecrets + scSecretsExtra + "     " + scCrypts + "     " + scMimic).trim();
        scoreString2 = (scTrap + "     " + scDeaths + "     " + scScore).trim();

        // Announce 300
        if (Configs.Announce300 && !said300 && score >= 300) {
            said300 = true;
            CommandsUtils.addCommand("/pc " + message300);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (Configs.DisplayAnnounce300 && score >= 300 && bossEntry <= runStarted) {
            GuiUtils.showTitle("&c&l300 Score!", "", 0, 5, 0);
        }
    }

    private void reset() {
        currentRoom = "";
        // Stuff from the scoreboard and tablist
        isInDungeon = false;
        floorInt = -1;
        puzzles = new ArrayList<>();
        secretsFound = 0;
        secretsNeeded = 1;
        secretsForMax = 0;
        crypts = 0;
        deaths = 0;
        openedRooms = 0;
        completedRooms = 0;
        // Run Splits and stuff
        openedWitherDoors = 0;
        runStarted = 0;
        bloodOpen = 0;
        watcherDone = 0;
        bossEntry = 0;
        runEnded = 0;

        // Misc stuff
        startX = 15;
        startZ = 15;

        endX = 190;
        endZ = 190;
        roomSize = 31;

        isScanning = false;
        isFullyScanned = false;

        players = new ArrayList<>();

        // Dungeon itself
        rooms = new ArrayList<>();
        doors = new ArrayList<>();
        totalRooms = 0;
        witherDoors = 0;
        trapType = "Unknown";

        map = null;
        mapSize = new Vector2i(0, 0);

        bloodDone = false;
        trapDone = false;
        yellowDone = false;

        // Score Calc Stuff
        scoreString1 = "";
        scoreString2 = "";

        said300 = false;

        skillScore = 0;
        exploreScore = 0;
        bonusScore = 0;
        score = 0;

        puzzleCount = 0;
        puzzleDone = 0;
        totalSecrets = 0;
        overflowSecrets = 0;
        secretsPercent = 0;

        isMimicDead = false;
    }


}
