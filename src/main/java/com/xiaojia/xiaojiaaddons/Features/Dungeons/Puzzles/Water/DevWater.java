package com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

public class DevWater {
    public static TreeMap<Integer, EnumOperation> operations = new TreeMap<>();
    private static EnumState[][] board = new EnumState[WaterUtils.height][WaterUtils.width];
    private static int process = 0;
    private static long lastKey = 0;
    private static BufferedImage map = null;
    private final KeyBind devKeyBind = new KeyBind("Dev Water", Keyboard.KEY_NONE);

    public static void setBoard(int index, int flag) {
        Patterns.Pattern pattern = Patterns.patterns.get(index);
        board = pattern.board;
        operations = pattern.operations[flag].operations;

        WaterUtils.getBoardString(board);
        System.err.println(WaterUtils.boardString);
        ChatLib.chat("flag: " + flag);
        for (Map.Entry<Integer, EnumOperation> operation : WaterUtils.operations.entrySet()) {
            if (operation.getValue().equals(EnumOperation.empty) || operation.getValue().equals(EnumOperation.trig))
                continue;
            ChatLib.chat("  " + operation.getKey() * 0.25 + "s: " + WaterSolver.getMessageFromOperation(operation.getValue()));
        }
        process = 0;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (devKeyBind.isKeyDown()) {
            if (TimeUtils.curTime() - lastKey < 250) return;
            lastKey = TimeUtils.curTime();
            if (operations.containsKey(process))
                board = WaterUtils.getStatesFromOperation(board, operations.get(process));
            board = WaterUtils.simulate(board).getKey();
            process++;

            BufferedImage newMap = new BufferedImage(WaterUtils.width, WaterUtils.height, BufferedImage.TYPE_4BYTE_ABGR);
            for (int i = 0; i < WaterUtils.height; i++) {
                for (int j = 0; j < WaterUtils.width; j++) {
                    Color color = null;
                    if (WaterUtils.isWater(board[i][j])) color = new Color(65, 65, 255);
                    else if (board[i][j] == EnumState.cc) color = new Color(0, 0, 0);
                    else if (board[i][j] == EnumState.ccl) color = new Color(180, 65, 65);
                    else if (board[i][j] == EnumState.cd) color = new Color(90, 240, 240);
                    else if (board[i][j] == EnumState.cg) color = new Color(255, 180, 0);
                    else if (board[i][j] == EnumState.ce) color = new Color(0, 180, 0);
                    else if (board[i][j] == EnumState.cq) color = new Color(255, 255, 255);
                    else if (WaterUtils.isBlock(board[i][j])) color = new Color(120, 120, 120);
                    else continue;
                    newMap.setRGB(j, WaterUtils.height - i - 1, color.getRGB());
                }
            }
            map = newMap;
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (!Checker.enabled) return;
        if (!SessionUtils.isDev()) return;
        if (!Configs.DevWater) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        RenderUtils.drawImage(new Image(map), Configs.MapX, Configs.MapY, 25 * Configs.MapScale, 25 * Configs.MapScale);
    }
}
