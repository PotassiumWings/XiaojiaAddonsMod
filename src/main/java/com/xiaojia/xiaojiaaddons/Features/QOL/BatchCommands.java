package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.utils.CommandsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class BatchCommands {
    private static final String fileName = "config/XiaojiaAddonsCommands.cfg";

    public static void execute() {
        ArrayList<String> commands = new ArrayList<>();
        try {
            File file = new File(fileName);
            BufferedReader reader = null;
            if (file.exists()) {
                reader = new BufferedReader(new FileReader(fileName));
                String s;
                while ((s = reader.readLine()) != null) {
                    if (s.length() == 0 || s.charAt(0) != '/') continue;
                    commands.add(s);
                }
                reader.close();
            }
            for (String str : commands) {
                CommandsUtils.addCommand(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
