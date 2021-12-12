package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.PacketReceivedEvent;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoPowderChest {
    private static final KeyBind keyBind = new KeyBind("Auto Powder Chest", Keyboard.KEY_NONE);
    private static boolean enabled = false;
    private final HashSet<BlockPos> solved = new HashSet<>();
    private BlockPos closestChest = null;
    private Vector3f particalPos = null;

    private long lastWarnTime = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest) return;
        if (Configs.AutoPowder) {
            if (TimeUtils.curTime() - lastWarnTime > 10000) {
                ChatLib.chat("Don't turn on Auto Powder / Auto Crystal Hollows Chest on at the same time!");
                ChatLib.chat("Disabled Auto Crystal Hollows Chest.");
                lastWarnTime = TimeUtils.curTime();
            }
            return;
        }
        if (keyBind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "Auto Crystal Hollows Chest &aactivated" : "Auto Crystal Hollows Chest &cdeactivated");
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest) return;
        if (Configs.AutoPowder) return;
        closestChest = getClosestChest();
        if (closestChest != null) {
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(
                    closestChest.getX(), closestChest.getY(), closestChest.getZ(),
                    185, 65, 65, 180, 1, 1, 0.01F
            );
            GuiUtils.disableESP();
        }
    }


    private BlockPos getClosestChest() {
        BlockPos pos = getPlayer().getPosition();
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (int x = pos.getX() - 4; x <= pos.getX() + 4; x++)
            for (int y = pos.getY() - 4; y <= pos.getY() + 4; y++)
                for (int z = pos.getZ() - 4; z <= pos.getZ() + 4; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    if (getWorld().getBlockState(blockPos).getBlock() == Blocks.chest && !solved.contains(blockPos))
                        blocks.add(blockPos);
                }
        blocks.sort((BlockPos a, BlockPos b) ->
                MathUtils.yawPitchSquareFromPlayer(a.getX() + 0.5F, a.getY() + 0.5F, a.getZ() + 0.5F) >
                        MathUtils.yawPitchSquareFromPlayer(b.getX() + 0.5F, b.getY() + 0.5F, b.getZ() + 0.5F) ? 1 : -1
        );
        if (blocks.isEmpty()) return null;
        return blocks.get(0);
    }


    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest || !enabled) return;
        if (Configs.AutoPowder) return;
        // TODO: CH CHECK
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() == EnumParticleTypes.CRIT) {
                Vector3f pos = new Vector3f((float) packet.getXCoordinate(), (float) packet.getYCoordinate() - 0.1F, (float) packet.getZCoordinate());
                ChatLib.chat(pos.toString() + ", " + closestChest.toString());
                if (closestChest != null && MathUtils.distanceSquaredFromPoints(
                        pos.x, pos.y, pos.z,
                        closestChest.getX() + 0.5, closestChest.getY() + 0.5, closestChest.getZ() + 0.5
                ) < 1) {
                    particalPos = pos;
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest || !enabled) return;
        if (Configs.AutoPowder) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().contains("Treasure")) return;
        getPlayer().closeScreen();
        if (closestChest != null) solved.add(closestChest);
    }
}
