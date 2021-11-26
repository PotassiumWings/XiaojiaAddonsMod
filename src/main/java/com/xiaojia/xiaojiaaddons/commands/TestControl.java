package com.xiaojia.xiaojiaaddons.commands;

import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TestControl extends CommandBase {
    private final boolean show = false;

    @Override
    public String getCommandName() {
        return "control";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return getUsage();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) throws CommandException {
        if (strings.length == 0) {
            ChatLib.chat(getUsage());
            return;
        }
        if (strings[0].equals("left")) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(100);
                        ControlUtils.leftClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else if (strings[0].equals("right")) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(100);
                        ControlUtils.rightClick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            new Thread(() -> {
                try {
                    for (int i = -10; i < 10; i++) {
                        for (int j = -10; j < 10; j++) {
                            Thread.sleep(100);
                            ControlUtils.face(1.0F * i, (float) (Math.random() * 50 + 50), 1.0F * j);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private String getUsage() {
        return "/control right to test rightClick\n/control left to test leftClick\n/control face to test face";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
