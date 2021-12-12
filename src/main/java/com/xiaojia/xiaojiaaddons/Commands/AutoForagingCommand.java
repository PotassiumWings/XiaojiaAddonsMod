package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.Skills.Foraging;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class AutoForagingCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "foragingpoint";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int mode = 0;
        if (args.length > 0) {
            if (args[0].toLowerCase().equals("south")) mode = 1;
            else if (args[0].toLowerCase().equals("west")) mode = 2;
            else if (args[0].toLowerCase().equals("east")) mode = 3;
        }
        Foraging.setForagingPoint(mode);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
