package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

enum EnumTerminal {
    NONE, ORDER, MAZE, CORRECT, START, COLOR
}

public class AutoTerminal {
    private final Deque<Integer> clickQueue = new ArrayDeque<>();
    private EnumTerminal enumTerminal = EnumTerminal.NONE;
    private boolean recalculate = false;
    private char startChar = ' ';
    private String color = "";

    private long lastClickTime = 0;
    private int windowID = 0;
    private int windowClicks = 0;

    @SubscribeEvent
    public void onTickCheck(TickEndEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory != null && inventory.getName().equals("container")) {
            if (XiaojiaAddons.isDebug() && !clickQueue.isEmpty()) ChatLib.chat("clearing");
            clickQueue.clear();
            enumTerminal = EnumTerminal.NONE;
            recalculate = false;
            startChar = ' ';
            color = "";
            lastClickTime = windowClicks = windowID = 0;
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Checker.enabled) return;
        if (!SkyblockUtils.isInDungeon()) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;

        String invName = inventory.getName();
        if (invName.startsWith("Click in order!")) enumTerminal = EnumTerminal.ORDER;
        else if (invName.startsWith("Navigate the maze!")) enumTerminal = EnumTerminal.MAZE;
        else if (invName.startsWith("Correct all the panes!")) enumTerminal = EnumTerminal.CORRECT;
        else if (invName.startsWith("What starts with: '")) {
            enumTerminal = EnumTerminal.START;
            startChar = invName.charAt(invName.indexOf("'") + 1);
        } else if (invName.startsWith("Select all the ")) {
            enumTerminal = EnumTerminal.COLOR;
            Pattern pattern = Pattern.compile("Select all the (.*) items!");
            Matcher matcher = pattern.matcher(invName);
            if (matcher.find()) color = matcher.group(1).toUpperCase();
        }
        if (enumTerminal == EnumTerminal.NONE) return;
        if (!Configs.AutoTerminal) return;

        try {
            if (clickQueue.size() == 0 || recalculate) {
                recalculate = calculate(inventory.getItemStacks());
                if (XiaojiaAddons.isDebug()) {
                    ChatLib.chat(clickQueue.size() + "");
                    for (int x : clickQueue) ChatLib.chat(x + "");
                }
            }

            if (!clickQueue.isEmpty()) {
                if (TimeUtils.curTime() - lastClickTime > Configs.AutoTerminalCD) {
                    lastClickTime = TimeUtils.curTime();
                    int slot = clickQueue.getFirst();
                    if (windowClicks == 0 || windowID + windowClicks < inventory.getWindowId()) {
                        windowID = inventory.getWindowId();
                        windowClicks = 0;
                    }
                    if (windowID + windowClicks > inventory.getWindowId() + Configs.TerminalClicksInAdvance) return;
                    mc.playerController.windowClick(
                            windowID + windowClicks, slot, 2, 0, getPlayer()
                    );
                    if (Configs.ZeroPingTerminal) {
                        windowClicks++;
                        clickQueue.pollFirst();
                    }
                    if (XiaojiaAddons.isDebug())
                        ChatLib.chat(String.format("(%d %d), %d", windowID, windowClicks, slot));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean calculate(ArrayList<ItemStack> itemStacks) {
        if (XiaojiaAddons.isDebug()) ChatLib.chat("calculate");
        clickQueue.clear();
        switch (enumTerminal) {
            case MAZE:
                if (itemStacks.size() < 54) return true;
                // 5 -->> 0
                int[] dx = new int[]{0, 0, -1, 1};
                int[] dy = new int[]{1, -1, 0, 0};
                Tuple<Integer, Integer> start = new Tuple<>(-1, -1);
                ArrayList<Boolean> visited = new ArrayList<>();
                for (int i = 0; i < 6; i++)
                    for (int j = 0; j < 9; j++) {
                        visited.add(false);
                        int ind = i * 9 + j;
                        if (itemStacks.get(ind) != null && itemStacks.get(ind).getItemDamage() == 5)
                            start = new Tuple<>(i, j);
                    }
                if (start.getFirst() == -1) return true;
                // bfs
                Deque<Tuple<Integer, Integer>> queue = new ArrayDeque<>();
                queue.addLast(start);
                while (!queue.isEmpty()) {
                    Tuple<Integer, Integer> p = queue.pollFirst();
                    int i = p.getFirst(), j = p.getSecond();
                    if (visited.get(i * 9 + j) || i < 0 || j < 0 || i >= 6 || j >= 9) return true;
                    visited.set(i * 9 + j, true);
                    for (int k = 0; k < 4; k++) {
                        int ti = i + dx[k], tj = j + dy[k];
                        if (ti >= 0 && ti < 6 && tj >= 0 && tj < 9) {
                            int tind = ti * 9 + tj;
                            if (visited.get(tind) || itemStacks.get(tind) == null) continue;
                            int tdmg = itemStacks.get(tind).getItemDamage();
                            if (tdmg == 0 || tdmg == 5) {
                                if (tdmg == 0) clickQueue.add(tind);
                                queue.addLast(new Tuple<>(ti, tj));
                            }
                            if (tdmg == 14) return false; // end
                        }
                    }
                }
                return true;
            case CORRECT:
                if (itemStacks.size() < 45) return true;
                for (int i = 0; i < 45; i++)
                    if (itemStacks.get(i) != null)
                        if (itemStacks.get(i).getItemDamage() == 14)
                            clickQueue.add(i);
                return false;
            case ORDER:
                if (itemStacks.size() < 36) return true;
                int[] order = new int[14];
                for (int i = 0; i < 14; i++) order[i] = -1;
                for (int i = 10; i <= 25; i++) {
                    if (i == 17 || i == 18 || itemStacks.get(i) == null) continue;
                    if (itemStacks.get(i).getItemDamage() != 14) continue;
                    if (itemStacks.get(i).stackSize < 1 || itemStacks.get(i).stackSize > 14) return true;
                    order[itemStacks.get(i).stackSize - 1] = i;
                }
                for (int i = 0; i < 14; i++)
                    if (order[i] != -1)
                        clickQueue.add(order[i]);
                return false;
            case START:
                if (itemStacks.size() < 54) return true;
                for (int i = 0; i < 54; i++) {
                    if (itemStacks.get(i) == null) continue;
                    String itemName = ChatLib.removeFormatting(itemStacks.get(i).getDisplayName());
                    if (itemName == null || itemName.length() < 1) continue;
                    if (XiaojiaAddons.isDebug()) ChatLib.chat(itemName + ", " + startChar);
                    if (itemName.charAt(0) == startChar &&
                            !itemStacks.get(i).isItemEnchanted()) {
                        clickQueue.add(i);
                    }
                }
                return false;
            case COLOR:
                if (itemStacks.size() < 54) return true;
                for (int i = 0; i < 54; i++) {
                    if (itemStacks.get(i) == null) continue;
                    String itemName = ChatLib.removeFormatting(itemStacks.get(i).getDisplayName()).toUpperCase();
                    if (XiaojiaAddons.isDebug()) ChatLib.chat(itemName + ", " + color);
                    if (itemName.contains(color) ||
                            (color.equals("SILVER") && itemName.contains("LIGHT GRAY") ||
                                    (color.equals("WHITE") && itemName.equals("WOOL")) ||
                                    (color.equals("WHITE") && itemName.contains("BONE")) ||
                                    (color.equals("BLACK") && itemName.contains("INK")) ||
                                    (color.equals("BROWN") && itemName.contains("COCOA")) ||
                                    (color.equals("BLUE") && itemName.contains("LAPIS"))
                            )) {
                        clickQueue.add(i);
                    }
                }
                return false;
            default:
                return true;
        }
    }
}

