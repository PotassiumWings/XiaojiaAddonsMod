package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;

public class XiaojiaChatTest extends CommandBase {
    @Override
    public String getCommandName() {
        return "xt";
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
        com.xiaojia.xiaojiaaddons.Features.Remote.XiaojiaChat.chat("@t " + message, 0);
    }

    private String getUsage() {
        return "&c/xt message&b to send tester mode chat";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
