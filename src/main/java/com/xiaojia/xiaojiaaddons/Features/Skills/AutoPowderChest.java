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
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
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
    private static boolean enabled = true;
    private final HashSet<BlockPos> solved = new HashSet<>();
    private BlockPos closestChest = null;
    private Vector3f particalPos = null;

    private long lastWarnTime = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
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
        if (!enabled) return;
        closestChest = getClosestChest();
        if (particalPos != null) {
            new Thread(() -> {
                Vector3f temp = (Vector3f) particalPos.clone();
                try {
                    ControlUtils.faceSlowly(particalPos.x, particalPos.y, particalPos.z, false);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (particalPos.equals(temp)) particalPos = null;
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
        if (Configs.AutoPowder) return;
        if (closestChest != null && !Configs.ChestESPCrystalHollows) {
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(
                    closestChest.getX(), closestChest.getY(), closestChest.getZ(),
                    65, 185, 65, 100, 1, 1, 0.01F
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
                MathUtils.distanceSquareFromPlayer(a) > MathUtils.distanceSquareFromPlayer(b) ? 1 : -1
        );
        if (blocks.isEmpty()) return null;
        return blocks.get(0);
    }

    @SubscribeEvent
    public void receivePacket(PacketReceivedEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest || !enabled) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
        if (Configs.AutoPowder) return;
        // TODO: CH CHECK
        if (event.packet instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles) event.packet;
            if (packet.getParticleType() == EnumParticleTypes.CRIT) {
                Vector3f pos = new Vector3f((float) packet.getXCoordinate(), (float) packet.getYCoordinate() - 0.1F, (float) packet.getZCoordinate());
                if (closestChest != null && isChestsParticle(pos, closestChest)) {
                    particalPos = pos;
                }
            }
        }
    }

    private static boolean isChestsParticle(Vector3f pos, BlockPos block) {
        if (pos.x < block.getX())
            return (int) (pos.x * 10) + 1 == block.getX() * 10 && pos.y >= block.getY() && pos.z >= block.getZ();
        if (pos.x > block.getX() + 1)
            return (int) (pos.x * 10) - 1 == block.getX() * 10 + 10 && pos.y >= block.getY() && pos.z >= block.getZ();
        if (pos.z < block.getZ())
            return (int) (pos.z * 10) + 1 == block.getZ() * 10 && pos.y >= block.getY() && pos.x >= block.getX();
        if (pos.z > block.getZ() + 1)
            return (int) (pos.z * 10) - 1 == block.getZ() * 10 + 10 && pos.y >= block.getY() && pos.x >= block.getX();
        return false;
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoPowderChest || !enabled) return;
        if (!SkyblockUtils.isInCrystalHollows()) return;
        if (Configs.AutoPowder) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null || !inventory.getName().contains("Treasure")) return;
        getPlayer().closeScreen();
        if (closestChest != null) solved.add(closestChest);
    }
}
