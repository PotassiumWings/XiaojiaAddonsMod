package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Config.ConfigGui;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoItemFrame;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Map;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TabUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public class Command extends CommandBase {
    @Override
    public String getCommandName() {
        return "xiaojia";
    }

    @Override
    public ArrayList<String> getCommandAliases() {
        ArrayList<String> res = new ArrayList<String>();
        res.add("xj");
        return res;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return getUsage();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (!Checker.enabled) return;
        if (strings == null) return;
        if (strings.length == 0) {
            ChatLib.chat(getUsage());
            return;
        }
        String arg = strings[0];
        switch (arg) {
            case "curmap":
                ChatLib.chat(SkyblockUtils.getCurrentMap());
                break;
            case "debug":
                XiaojiaAddons.setDebug();
                break;
            case "s":
                XiaojiaAddons.guiToOpen = new ConfigGui();
                break;
            case "300":
                if (strings.length == 1) ChatLib.chat("announce300: " + Dungeon.message300);
                else {
                    Dungeon.message300 = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));
                    ChatLib.chat("Successfully set announce300 message to: " + Dungeon.message300);
                }
                break;
            case "tab":
                TabUtils.printTab();
                break;
            case "room":
                ChatLib.chat("Current Room: " + Dungeon.currentRoom);
                break;
            case "rooms":
                Dungeon.showRooms();
                break;
            case "map":
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
                break;
            case "p3":
                int x = Integer.parseInt(strings[1]);
                int y = Integer.parseInt(strings[2]);
                int z = Integer.parseInt(strings[3]);
                AutoItemFrame.setPosition(x, y, z);
                break;
//            case "shoot":
//                AutoShootCrystal.test(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
//                break;
//            case "click":
//                ClickTest.setEnabled();
//                break;
//            case "face":
//                ControlUtils.face(0, 65, 0);
//                break;
            default:
                if (XiaojiaAddons.isDebug()) SkyblockUtils.setCurrentMap(String.join(" ", strings));
                else ChatLib.chat(getUsage());
                break;
        }
    }

    private String getUsage() {
//        return "/xj curmap for current map information.\n/xj debug to debug.\n/xj s to open gui settings";
        return "/xj curmap for current map information.\n/xj s to open gui settings.\n/xj 300 to see, and /xj 300 message to set announce300 message.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
