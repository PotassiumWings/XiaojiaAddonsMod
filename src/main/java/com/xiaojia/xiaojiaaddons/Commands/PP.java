package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class PP extends CommandBase {
    @Override
    public String getCommandName() {
        return "pp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        CommandsUtils.addCommand("/p PotassiumWings");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
