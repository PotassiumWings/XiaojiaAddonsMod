package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;

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
        com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.chat(message, 0);
    }

    private String getUsage() {
        return "Usage: /xc message";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
