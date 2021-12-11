package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.QOL.TransferBack;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TransferBackCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "ptb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        TransferBack.transferBack();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
