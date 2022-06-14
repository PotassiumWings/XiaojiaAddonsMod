package com.xiaojia.xiaojiaaddons.Commands;

import com.xiaojia.xiaojiaaddons.Config.ConfigGuiNew;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.AutoSneakyCreeper;
import com.xiaojia.xiaojiaaddons.Features.Bestiary.GolemAlert;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.AutoItemFrame;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.BloodAssist;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Data;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Dungeon;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Room;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.AutoBlaze;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.DevWater;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.Patterns;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Puzzles.Water.WaterSolver;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.BugReport;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ColorName;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.CommandKeybind;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.ItemRename;
import com.xiaojia.xiaojiaaddons.Features.Miscellaneous.MusicRune;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.DojoUtils;
import com.xiaojia.xiaojiaaddons.Features.Nether.Dojo.Mastery;
import com.xiaojia.xiaojiaaddons.Features.QOL.BatchCommands;
import com.xiaojia.xiaojiaaddons.Features.QOL.InCombatQOL;
import com.xiaojia.xiaojiaaddons.Features.Remote.ClientSocket;
import com.xiaojia.xiaojiaaddons.Features.Remote.ShowItem;
import com.xiaojia.xiaojiaaddons.Features.Skills.AutoBuildFarmVertical;
import com.xiaojia.xiaojiaaddons.Features.Slayers.Blaze;
import com.xiaojia.xiaojiaaddons.Features.QOL.ChestFiller;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Tests.CopyWorldInfo;
import com.xiaojia.xiaojiaaddons.Tests.TestM7;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TabUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class Command extends CommandBase {
    @Override
    public String getCommandName() {
        return "xiaojia";
    }

    @Override
    public ArrayList<String> getCommandAliases() {
        ArrayList<String> res = new ArrayList<String>();
        res.add("xj");
        return res;
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
        int x, y, z;
        String arg = strings[0];
        String allArg = strings.length == 1 ? "" : String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));
        if (arg.equals("reconnect")) {
            ClientSocket.reconnect();
            return;
        }
        if (!Checker.enabled) return;
        switch (arg) {
            case "curmap":
                ChatLib.chat(SkyblockUtils.getCurrentMap());
                break;
            case "s":
                XiaojiaAddons.guiToOpen = new ConfigGuiNew(XiaojiaAddons.mc.gameSettings.guiScale);
                XiaojiaAddons.mc.gameSettings.guiScale = 3;
                break;
            case "show":
                String str = strings[1];
                String[] toShow = null;
                if (str.equals("dungarmor")) toShow = InCombatQOL.dungArmor;
                if (str.equals("dungtrash")) toShow = InCombatQOL.dungTrash;
                if (str.equals("runes")) toShow = InCombatQOL.runes;
                if (toShow != null) {
                    ChatLib.chat(Arrays.toString(toShow));
                    break;
                }
            case "report":
                AutoSneakyCreeper.printLog();
                TabUtils.printTab();
                Dungeon.showDungeonInfo();
                AutoItemFrame.printLog();
                BloodAssist.printLog();
                WaterSolver.printLog();
                System.out.println(AutoBlaze.log.toString());
                XiaojiaAddons.guiToOpen = new BugReport();
                break;
            case "commands":
                BatchCommands.execute();
                break;
            case "copy":
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ChatLib.removeColor(allArg)), null);
                break;
            case "rename":
                ItemRename.process(strings);
                break;
            case "showitem":
                ShowItem.show();
                break;
            case "tick":
                TickEndEvent.owo();
                break;

            case "patterns":
                Patterns.printPatterns();
                break;
            case "dev":
                DevWater.setBoard(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
                break;
            case "step":
                AutoBuildFarmVertical.setStep(Integer.parseInt(strings[1]));
                break;
            case "check":
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);
                z = Integer.parseInt(strings[3]);
                int dx = Integer.parseInt(strings[4]);
                int dy = Integer.parseInt(strings[5]);
                int dz = Integer.parseInt(strings[6]);
                new Thread(() -> AutoBuildFarmVertical.check(x, y, z, x + dx, y + dy, z + dz)).start();
                break;
            case "mastery":
                Mastery.printLog();
                break;
            case "musicrune":
                MusicRune.play();
                break;

            // debug commands
            case "dojo":
                ChatLib.chat(DojoUtils.getTask() + "!");
                break;
            case "swap":
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Blaze.doSwap(strings[1]);
                }).start();
                break;
            case "cache":
                ColorName.showCache();
                break;
            case "core":
                x = MathUtils.floor(getX(MinecraftUtils.getPlayer()));
                z = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                ChatLib.chat(String.format("core for current (%d %d) is: %d", x, z,
                        new Room(x, z, new Data(
                                "unknown", "", 0, null
                        )).core));
                break;
            case "debug":
                XiaojiaAddons.setDebug();
                break;
            case "fill":
                ChestFiller.toggle();
                break;
            case "keybind":
                switch (strings.length) {
                    case 1:
                    case 2:
                        ChatLib.chat(CommandKeybind.getUsage());
                        break;
                    default:
                        String command = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));
                        if (strings[1].equalsIgnoreCase("add")) CommandKeybind.add(command);
                        else if (strings[1].equalsIgnoreCase("remove")) CommandKeybind.remove(command);
                        else if (strings[1].equalsIgnoreCase("removeWithKey")) {
                            command = String.join(" ", Arrays.copyOfRange(strings, 3, strings.length));
                            CommandKeybind.remove(command, strings[2]);
                        }
                        else ChatLib.chat(CommandKeybind.getUsage());
                        break;
                }
                break;
            case "keybinds":
                CommandKeybind.list();
                break;
            case "room":
                ChatLib.chat("Current Room: " + Dungeon.currentRoom);
                break;
            case "p3":
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);
                z = Integer.parseInt(strings[3]);
                AutoItemFrame.setPosition(x, y, z);
                break;
            case "golem":
                GolemAlert.golemWarn();
                break;
            case "cpblock":
                x = Integer.parseInt(strings[1]);
                y = Integer.parseInt(strings[2]);
                z = Integer.parseInt(strings[3]);
                int tx = Integer.parseInt(strings[4]);
                int ty = Integer.parseInt(strings[5]);
                int tz = Integer.parseInt(strings[6]);
                XiaojiaAddons.mc.theWorld.setBlockState(new BlockPos(x, y, z),
                        BlockUtils.getBlockStateAt(new BlockPos(tx, ty, tz)));
                break;
            case "block":
                if (strings.length == 1) {
                    x = MathUtils.floor(getX(MinecraftUtils.getPlayer()));
                    y = MathUtils.floor(getY(MinecraftUtils.getPlayer()));
                    z = MathUtils.floor(MathUtils.getZ(MinecraftUtils.getPlayer()));
                } else {
                    x = Integer.parseInt(strings[1]);
                    y = Integer.parseInt(strings[2]);
                    z = Integer.parseInt(strings[3]);
                }
                IBlockState iBlockState = BlockUtils.getBlockStateAt(new BlockPos(x, y, z));
                if (iBlockState == null) return;
                int meta = iBlockState.getBlock().getMetaFromState(iBlockState);
                ChatLib.chat(iBlockState.getBlock() + ", meta: " + meta);
                break;
            case "entities":
                List<Entity> list = getWorld().loadedEntityList;
                for (Entity entity : list) {
                    ChatLib.chat(entity.hasCustomName() + ", " + entity.getName() + ", " + MathUtils.getPosString(entity) +
                            (entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHealth() : ""));
                }
                break;
            case "m7":
                TestM7.m7();
                break;
            case "armorstand":
                TestM7.show();
                break;
            case "sound":
                XiaojiaAddons.mc.thePlayer.playSound(strings[1], Float.parseFloat(strings[2]), Float.parseFloat(strings[3]));
                break;
            case "setDungeon":
                SkyblockUtils.setDungeon(strings[1]);
                break;
            case "setwater":
                Dungeon.currentRoom = "Water Board";
                WaterSolver.setRoom(new Room(MathUtils.floor(getX(MinecraftUtils.getPlayer())),
                        MathUtils.floor(getZ(MinecraftUtils.getPlayer())), Data.blankRoom));
                break;
            case "flag":
                new Thread(() -> {
                    WaterSolver.reset();
                    WaterSolver.calc(Integer.parseInt(strings[1]), null);
                }).start();
            case "sp":
                XiaojiaAddons.mc.getNetHandler().handleParticles(new S2APacketParticles(
                        EnumParticleTypes.SMOKE_LARGE, false,
                        getX(MinecraftUtils.getPlayer()), getY(MinecraftUtils.getPlayer()), getZ(MinecraftUtils.getPlayer()),
                        Float.parseFloat(strings[1]), Float.parseFloat(strings[2]), Float.parseFloat(strings[3]),
                        1, 0
                ));
                break;
            case "cp":
                CopyWorldInfo.copy(strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);
                break;
            case "paste":
                CopyWorldInfo.paste();
                break;
            default:
                if (XiaojiaAddons.isDebug()) SkyblockUtils.setCurrentMap(String.join(" ", strings));
                else ChatLib.chat(getUsage());
                break;
        }
    }

    private String getUsage() {
//        return "/xj curmap for current map information.\n/xj debug to debug.\n/xj s to open gui settings";
        return "" +
//                "&c/xj curmap&b for current map information.\n" +
                "&c/xj s&b to open gui settings.\n" +
//                "&c/xj 300&b to see, and &c/xj 300 message&b to set announce300 message.\n" +
                "&c/xj show dungarmor&b, &c/xj show dungtrash&b, and &c/xj show runes&b to show auto-sell dungeon armors/dungeon trash/runes seperately.\n" +
                "&c/xj rename&b to rename items.\n" +
                "&c/xj showitem&b to show held item.\n" +
                "&c/xj report&b to report a bug.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
