package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Features.Tests.GuiTest;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TestGui extends CommandBase {

    private final boolean show = false;

    @Override
    public String getCommandName() {
        return "show";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        GuiTest.changeShow();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
