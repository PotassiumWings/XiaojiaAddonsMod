package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class JadeCrystalHelper {
    private final HashMap<Vector3d, Double> distanceMap = new HashMap<>();
    private final ArrayList<BlockPos> result = new ArrayList<>();
    private final Vector3d lastPlayerPos = null;
    private long lastPositionTime = 0;
    private Vector3d playerPos = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.JadeCrystal) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
        if (getPlayer() == null || getPlayer().getPosition() == null) return;
        Vector3d pos = new Vector3d(getX(getPlayer()), getY(getPlayer()), getZ(getPlayer()));
        if (playerPos != null && !MathUtils.equal(pos, playerPos)) { // TODO
            lastPositionTime = TimeUtils.curTime();
        }
        playerPos = pos;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.JadeCrystal) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
        if (!result.isEmpty()) {
            for (BlockPos pos : result) {
                GuiUtils.enableESP();
                GuiUtils.drawBoxAtBlock(
                        pos.getX(), pos.getY(), pos.getZ(),
                        65, 65, 185, 100,
                        1, 1, 0
                );
                GuiUtils.disableESP();
            }
        }
    }

    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.JadeCrystal) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
        if (event.type != 2) return;
        if (distanceMap.size() == 3) return;
        String message = ChatLib.removeFormatting(event.message.getUnformattedText().toUpperCase());
        Pattern pattern = Pattern.compile("TREASURE: ([0-9.]*)M");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            double distance = Double.parseDouble(matcher.group(1));
            if (XiaojiaAddons.isDebug()) ChatLib.chat(distance + "");
            if (TimeUtils.curTime() - lastPositionTime > Configs.JadeCrystalCD &&
                    !distanceMap.containsKey(playerPos)) {
                distanceMap.put(playerPos, distance);
                if (XiaojiaAddons.isDebug()) ChatLib.chat("put " + playerPos.toString() + ", dis: " + distance);
                // TODO: GUI
                ChatLib.chat(String.format("Finished (%d / 3) points", distanceMap.size()));
                if (distanceMap.size() == 3) {
                    try {
                        calculate();
                        if (result.get(0).equals(new BlockPos(0, 0, 0))) {
                            distanceMap.remove(playerPos);
                            result.clear();
                            ChatLib.chat("Invalid! Try another position.\n3 Points should not be in the same line.\nIt's better to get Y difference among them.");
                            return;
                        }
                        ChatLib.chat("Chest Found.");
                        if (XiaojiaAddons.isDebug())
                            for (BlockPos pos : result)
                                ChatLib.chat(pos.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        ChatLib.chat("error calculating");
                    }
                }
            }
        }
    }

    private void calculate() {
        ArrayList<Vector3d> positions = new ArrayList<>(distanceMap.keySet());
        Vector3d p1 = positions.get(0), p2 = positions.get(1), p3 = positions.get(2);
        double x1 = p1.x, x2 = p2.x, x3 = p3.x;
        double y1 = p1.y, y2 = p2.y, y3 = p3.y;
        double z1 = p1.z, z2 = p2.z, z3 = p3.z;
        double r1 = distanceMap.get(p1), r2 = distanceMap.get(p2), r3 = distanceMap.get(p3);
        double A1 = r1 * r1 - x1 * x1 - y1 * y1 - z1 * z1;
        double A2 = r2 * r2 - x2 * x2 - y2 * y2 - z2 * z2;
        double A3 = r3 * r3 - x3 * x3 - y3 * y3 - z3 * z3;
        double A21 = -(A2 - A1) / 2;
        double A31 = -(A3 - A1) / 2;
        double X21 = x2 - x1, X31 = x3 - x1;
        double Y21 = y2 - y1, Y31 = y3 - y1;
        double Z21 = z2 - z1, Z31 = z3 - z1;
        double D = X21 * Y31 - Y21 * X31;
        double B0 = (A21 * Y31 - A31 * Y21) / D;
        double B1 = (Y21 * Z31 - Y31 * Z21) / D;
        double C0 = (A31 * X21 - A21 * X31) / D;
        double C1 = (X31 * Z21 - X21 * Z31) / D;
        double E = B1 * B1 + C1 * C1 + 1;
        double F = B1 * (B0 - x1) + C1 * (C0 - y1) - z1;
        double G = (B0 - x1) * (B0 - x1) + (C0 - y1) * (C0 - y1) + z1 * z1 - r1 * r1;
        double delta = Math.sqrt(F * F - E * G);
        double Z1 = (-F + delta) / E;
        double Z2 = (-F - delta) / E;
        double X1 = B0 + B1 * Z1, Y1 = C0 + C1 * Z1;
        double X2 = B0 + B1 * Z2, Y2 = C0 + C1 * Z2;
        result.add(new BlockPos(X1, Y1, Z1));
        result.add(new BlockPos(X2, Y2, Z2));
    }

    @SubscribeEvent
    public void onRefresh(ClientChatReceivedEvent event) {
        if (!SkyblockUtils.isInCrystalHollows()) return;
        String message = event.message.getUnformattedText();
        Pattern pattern = Pattern.compile("You found .* with your Metal Detector!");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            distanceMap.clear();
            lastPositionTime = TimeUtils.curTime();
            result.clear();
            ChatLib.chat("clearing points!");
            // TODO: GUI CLEAR
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        distanceMap.clear();
        lastPositionTime = 0;
        playerPos = null;
        result.clear();
        // TODO: GUI CLEAR
    }
}
