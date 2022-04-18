package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmVertical;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class FarmingType extends CommandBase {

    @Override
    public String getCommandName() {
        return "farmingtype";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return getUsage();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args[0].toUpperCase().equals("VERTICAL")) FarmingPoint.setType(EnumFarmingType.VERTICAL);
        else if (args[0].toUpperCase().equals("PUMPKIN")) FarmingPoint.setType(EnumFarmingType.PUMPKIN);
        else ChatLib.chat(getUsage());
    }

    public String getUsage() {
        return "&c/farmingtype vertical&b for vertical farm.\n" +
                "&c/farmingtype pumpkin&b for pumpkin farm.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}

