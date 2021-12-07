package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class FuckHarps {
    public HashMap<String, ArrayList<Tuple<Integer, Integer>>> notesOf = new HashMap<String, ArrayList<Tuple<Integer, Integer>>>(){{
        put("Hymn to the Joy", getHymn());
        put("Through the Campfire", getCamp());
    }};

    public HashMap<String, Integer> speedOf = new HashMap<String, Integer>() {{
        put("Hymn to the Joy", 350);
        put("Through the Campfire", 250);
    }};

    private boolean isThreadRunning = false;
    private boolean stop = false;
    private String currentSong = "";

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
//        if (!Configs.FuckHarps) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;
        if (isThreadRunning) return;
        if (inventory.getName().startsWith("Harp - ")) {
            String songName = inventory.getName().substring(7);
            ArrayList<Tuple<Integer, Integer>> arr = notesOf.get(songName);
            if (arr == null) return;
            isThreadRunning = true;
            currentSong = songName;
            ChatLib.chat("Starting " + songName);
            long cur = TimeUtils.curTime();
            new Thread(() -> {
                try {
                    int stepTime = speedOf.get(currentSong);
                    Thread.sleep(3000);
                    ChatLib.chat("Starting notes! " + (TimeUtils.curTime() - cur));
                    for (Tuple<Integer, Integer> currentNote : arr) {
                        int sleepTime = currentNote.getFirst();
                        int slot = currentNote.getSecond() + 37;
                        Thread.sleep(stepTime * sleepTime);
                        if (stop) return;
                        ChatLib.chat("clicking! " + (TimeUtils.curTime() - cur));
                        inventory.click(slot, false, "MIDDLE");
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getPlayer().closeScreen();
                    isThreadRunning = false;
                    stop = false;
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onTickCheckStop(TickEvent.ClientTickEvent event) {
        if (!isThreadRunning) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().contains(currentSong)) {
            stop = true;
        }
    }

    private ArrayList<Tuple<Integer, Integer>> getHymn() {
        ArrayList<Tuple<Integer, Integer>> arr = new ArrayList<>();
        arr.add(new Tuple<>(0, 2));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 3));
        arr.add(new Tuple<>(2, 4));
        arr.add(new Tuple<>(2, 4));
        arr.add(new Tuple<>(2, 3));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(3, 1));
        arr.add(new Tuple<>(1, 1));

        arr.add(new Tuple<>(3, 2));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 3));
        arr.add(new Tuple<>(2, 4));
        arr.add(new Tuple<>(2, 4));
        arr.add(new Tuple<>(2, 3));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(3, 0));
        arr.add(new Tuple<>(1, 0));
        return arr;
    }

    private ArrayList<Tuple<Integer, Integer>> getCamp() {
        ArrayList<Tuple<Integer, Integer>> arr = new ArrayList<>();
        arr.add(new Tuple<>(0, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 6));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(2, 0));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 6));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(2, 2));
        arr.add(new Tuple<>(1, 5));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 4));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 3));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 2));
        arr.add(new Tuple<>(1, 1));
        arr.add(new Tuple<>(1, 0));
        arr.add(new Tuple<>(2, 1));
        arr.add(new Tuple<>(1, 6));
        return arr;
    }
}
