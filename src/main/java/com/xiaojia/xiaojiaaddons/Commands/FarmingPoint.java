package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmPumpkin;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmVertical;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class FarmingPoint extends CommandBase {
    private static EnumFarmingType type = EnumFarmingType.VERTICAL;

    public static void setType(EnumFarmingType farmingType) {
        type = farmingType;
    }

    @Override
    public String getCommandName() {
        return "farmingpoint";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "&c/farmingpoint start end&b to set farming range.\n" +
                "For vertical farm, &c/farmingpoint startY endY&b;\n" +
                "For pumpkin farm, &c/farmingpoint startZ endZ&b.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        int mode = 0;
//        if (args.length > 0) {
//            if (args[0].toLowerCase().equals("south")) mode = 1;
//            else if (args[0].toLowerCase().equals("west")) mode = 2;
//            else if (args[0].toLowerCase().equals("east")) mode = 3;
//        }
        int sy = Integer.parseInt(args[0]);
        int ty = Integer.parseInt(args[1]);
        ChatLib.chat("Current farming type: " + type);
        if (type == EnumFarmingType.VERTICAL) AutoBuildFarmVertical.setFarmingPoint(sy, ty);
        else if (type == EnumFarmingType.PUMPKIN) AutoBuildFarmPumpkin.setFarmingPoint(sy, ty);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}


enum EnumFarmingType {
    VERTICAL, PUMPKIN
}
