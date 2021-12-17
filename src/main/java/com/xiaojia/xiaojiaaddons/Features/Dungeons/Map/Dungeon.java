package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Dungeon {
    private final String[] entryMessages = new String[]{
            "[BOSS] Bonzo: Gratz for making it this far, but I’m basically unbeatable.",
            "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
            "[BOSS] The Professor: I was burdened with terrible news recently...",
            "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
            "[BOSS] Livid: Welcome, you arrive right on time. I am Livid, the Master of Shadows.",
            "[BOSS] Sadan: So you made it all the way here...and you wish to defy me? Sadan?!",
            "[BOSS] Necron: Finally, I heard so much about you. The Eye likes you very much."
    };
    // scan
    private final boolean isScanning = false;
    private final boolean isFullyScanned = false;
    private long lastScan = 0;
    // map
    private final BufferedImage map = null;
    private final Coords mapSize = new Coords(0, 0);
    // players
    private final ArrayList<Player> players = new ArrayList<>();
    // states
    public boolean isInDungeon = false;
    public long runStarted = 0;
    public long bloodOpen = 0;
    public long watcherDone = 0;
    public long bossEntry = 0;
    public long runEnded = 0;
    public int openedWitherDoors = 0;
    public int startX = 15;
    public int startZ = 15;
    public int endX = 190;
    public int endZ = 190;
    public int roomSize = 31;
    // dungeons
    public ArrayList<Room> rooms = new ArrayList<>();
    public ArrayList<Door> doors = new ArrayList<>();
    public int totalRooms = -1;
    public int witherDoors = 0;
    public String trapType = "Unknown";
    public boolean bloodDone = false;
    public boolean trapDone = false;
    public boolean yellowDone = false;
    // calc
    public int skillScore = 0;
    public int exploreScore = 0;
    public int bonusScore = 0;
    public int score = 0;
    public boolean said300 = false;
    public int puzzleCount = 0;
    public int puzzleDone = 0;
    public int totalSecrets = 0;
    public int overflowSecrets = 0;

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = ChatLib.removeFormatting(event.message.getUnformattedText());
        if (message.equals("[NPC] Mort: Here, I found this map when I first entered the dungeon."))
            this.runStarted = TimeUtils.curTime();
        if (message.startsWith("[BOSS] The Watcher") && this.bloodOpen == 0) this.bloodOpen = TimeUtils.curTime();
        if (message.equals("[BOSS] The Watcher: You have proven yourself. You may pass."))
            this.watcherDone = TimeUtils.curTime();
        if (message.endsWith("opened a WITHER door!")) this.openedWitherDoors++; // TODO: prevent chat polluting
        if (MapUtils.includes(entryMessages, message)) this.bossEntry = TimeUtils.curTime();
        if (message.equals("                             > EXTRA STATS <")) this.runEnded = TimeUtils.curTime();
    }

    @SubscribeEvent
    public void onTickScan(TickEndEvent event) {
        if (isInDungeon && !isScanning && Configs.AutoScan && !isFullyScanned &&
                TimeUtils.curTime() - lastScan >= 500 && Configs.MapEnabled) {
            lastScan = TimeUtils.curTime();
            new Thread(this::scan).start();
        }
    }

    @SubscribeEvent
    public void onRenderMap(RenderGameOverlayEvent.Pre event) {
        if (!isInDungeon || !Configs.MapEnabled) return;
        drawBackground();
        if (map != null) renderMap();
        renderCheckmarks();
        // render rooms
        for (Room room : rooms) {
            if (Configs.ShowRoomNames) {
                room.renderName();
            }
            if (Configs.ShowSecrets != 0 &&
                    (room.type.equals("normal") || room.type.equals("rare"))) {
                room.renderSecrets();
            }
        }
        // render players
        for (Player player : players) {
            // TODO
        }
        // render score
        if (Configs.ScoreCalculation) drawScoreCalc();
    }

    private void scan() {

    }

    private void drawBackground() {
    }

    private void renderMap() {
    }

    private void renderCheckmarks() {
    }

    private void drawScoreCalc() {

    }
}
