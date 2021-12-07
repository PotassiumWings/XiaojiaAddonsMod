package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Config.ConfigGui;
import com.xiaojia.xiaojiaaddons.Features.Dragons.AutoShootCrystal;
import com.xiaojia.xiaojiaaddons.Features.Tests.ClickTest;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;

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
            case "shoot":
                AutoShootCrystal.test(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                break;
            case "click":
                ClickTest.setEnabled();
                break;
            default:
                if (XiaojiaAddons.isDebug()) SkyblockUtils.setCurrentMap(String.join(" ", strings));
                else ChatLib.chat(getUsage());
                break;
        }
    }

    private String getUsage() {
//        return "/xj curmap for current map information.\n/xj debug to debug.\n/xj s to open gui settings";
        return "/xj curmap for current map information.\n/xj s to open gui settings";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
