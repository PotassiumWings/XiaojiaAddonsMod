package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.SessionUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public class XiaojiaChat extends CommandBase {
    @Override
    public String getCommandName() {
        return "xc";
    }

    @Override
    public ArrayList<String> getCommandAliases() {
        return new ArrayList<String>();
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
        String message = String.join(" ", strings);
        if (message.equals("toggle")) {
            ChatLib.toggle();
            return;
        }
        if (message.equals("online")) {
            com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.queryOnline();
            return;
        }
        if (strings[0].equals("addRank") && SessionUtils.getUUID().equals("1c6d48a96cb3465681382590ec82fa68")) {
            String color = strings[strings.length - 1];
            String name = strings[1];
            String rank = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length - 1));
            com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.addRank(name, rank, color);
            return;
        }
        if (strings[0].equals("updateRanks")) {
            com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.addRank("", "", "");
            return;
        }
        com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.chat(message, 0);
    }

    private String getUsage() {
        return "&c/xc message&b to send chat\n" +
                "&c/xc toggle&b to toggle\n" +
                "&c/xc online&b to see online members";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
