package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class StonklessStonk {
    private static final KeyBind keyBind = new KeyBind("Stonkless Stonk", Keyboard.KEY_NONE);
    private static final int witherEssenceHash = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=".hashCode();
    private static final int redstoneKeyHash = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I4NTJiYTE1ODRkYTllNTcxNDg1OTk5NTQ1MWU0Yjk0NzQ4YzRkZDYzYWU0NTQzYzE1ZjlmOGFlYzY1YzgifX19".hashCode();
    private static final HashMap<BlockPos, Long> doneSecretsPos = new HashMap<>();
    private static boolean should = true;
    private static long lastClickTime = 0;
    private final HashMap<BlockPos, Block> blockHashMap = new HashMap<>();
    private BlockPos facingPos;
    private BlockPos lastPlayerPos;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        facingPos = null;
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Stonkless Stonk &aactivated" : "Stonkless Stonk &cdeactivated");
        }
        if (!SkyblockUtils.isInDungeon()) return;
        if (!Configs.StonklessStonkEnable) return;
        if (!should) return;

        float PI = (float) Math.PI;
        float px = MathUtils.getX(getPlayer()), py = MathUtils.getY(getPlayer()) + getPlayer().getEyeHeight(), pz = MathUtils.getZ(getPlayer());
        float yaw = MathUtils.getYaw() * PI / 180, pitch = MathUtils.getPitch() * PI / 180;
        float R = 5;
        int lastX = -1, lastY = -1, lastZ = -1;
        for (int delta = 1; delta <= 300; delta++) {
            float r = R * delta / 300;
            float x = (float) (px - Math.sin(yaw) * Math.cos(pitch) * r);
            float y = (float) (py - Math.sin(pitch) * r);
            float z = (float) (pz + Math.cos(yaw) * Math.cos(pitch) * r);
            int fx = MathUtils.floor(x), fy = MathUtils.floor(y), fz = MathUtils.floor(z);
            if (fx == lastX && fy == lastY && fz == lastZ) continue;
            lastX = fx;
            lastY = fy;
            lastZ = fz;

            BlockPos pos = new BlockPos(fx, fy, fz);
            if (doneSecretsPos.containsKey(pos) && TimeUtils.curTime() - doneSecretsPos.get(pos) < 1000) continue;

            Block block = BlockUtils.getBlockAt(pos);
            if (isSecret(block, pos)) {
                if (Configs.StonklessStonkAutoClickSecret) {
                    boolean canClick = TimeUtils.curTime() - lastClickTime > 200;
                    // opened inv
                    Inventory inventory = ControlUtils.getOpenedInventory();
                    if (inventory == null || inventory.getSize() != 45) canClick = false;

                    // held pickaxe!
                    ItemStack heldItemStack = ControlUtils.getHeldItemStack();
                    String heldItemName = "";
                    if (heldItemStack != null && heldItemStack.hasDisplayName())
                        heldItemName = ControlUtils.getHeldItemStack().getDisplayName();
                    if (!Configs.StonklessStonkWithoutPickaxe && !heldItemName.contains("Stonk") && !heldItemName.contains("Pickaxe"))
                        canClick = false;

                    if (canClick) {
                        lastClickTime = TimeUtils.curTime();
                        if (XiaojiaAddons.isDebug()) ChatLib.chat("click");
                        mc.playerController.onPlayerRightClick(
                                getPlayer(),
                                mc.theWorld,
                                getPlayer().inventory.getCurrentItem(),
                                pos,
                                EnumFacing.fromAngle(getPlayer().rotationYaw),
                                new Vec3(Math.random(), Math.random(), Math.random())
                        );
                        doneSecretsPos.put(pos, TimeUtils.curTime());
                        if (XiaojiaAddons.isDebug()) ChatLib.chat("stonkless stonk: (" + x + ", " + y + ", " + z + ")");
                    }
                }
                facingPos = pos;
                return;
            }
        }
    }

    @SubscribeEvent
    public void getSecretsOnTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.StonklessStonkEnable) return;
        if (!SkyblockUtils.isInDungeon()) return;
        int px = MathUtils.getBlockX(getPlayer()), py = MathUtils.getBlockY(getPlayer()), pz = MathUtils.getBlockZ(getPlayer());
        BlockPos playerPos = new BlockPos(MathUtils.floor(px), MathUtils.floor(py), MathUtils.floor(pz));
        if (playerPos.equals(lastPlayerPos)) return;
        lastPlayerPos = playerPos;
        blockHashMap.clear();
        for (int x = px - 6; x <= px + 6; x++) {
            for (int y = py - 6; y <= py + 6; y++) {
                for (int z = pz - 6; z <= pz + 6; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = BlockUtils.getBlockAt(pos);
                    if (isSecret(block, pos)) {
                        blockHashMap.put(pos, block);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.StonklessStonkEnable) return;
        if (!SkyblockUtils.isInDungeon()) return;
        for (Map.Entry<BlockPos, Block> entry : blockHashMap.entrySet()) {
            BlockPos pos = entry.getKey();
            if (doneSecretsPos.containsKey(pos)) continue;
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            GuiUtils.enableESP();
            if (pos.equals(facingPos)) GuiUtils.drawBoxAtBlock(x, y, z, 0, 255, 0, 100, 1, 1, 0);
            else GuiUtils.drawBoxAtBlock(x, y, z, 0xd3, 0xd3, 0xd3, 100, 1, 1, 0);
            GuiUtils.disableESP();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!Checker.enabled) return;
        blockHashMap.clear();
        doneSecretsPos.clear();
        facingPos = null;
        lastPlayerPos = null;
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (!Checker.enabled) return;
        if (facingPos == null) return;
        if (!(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK))
            return;
        if (!facingPos.equals(mc.objectMouseOver.getBlockPos())) {
            mc.playerController.onPlayerRightClick(
                    getPlayer(),
                    mc.theWorld,
                    getPlayer().inventory.getCurrentItem(),
                    facingPos,
                    EnumFacing.fromAngle(getPlayer().rotationYaw),
                    new Vec3(Math.random(), Math.random(), Math.random())
            );
        }
        doneSecretsPos.put(facingPos, TimeUtils.curTime());
    }

    private boolean isSecret(Block block, BlockPos pos) {
        if (block instanceof BlockChest || block instanceof BlockLever) return true;
        if (block instanceof BlockSkull) {
            String str = BlockUtils.getTileProperty((BlockSkull) block, pos);
            return str.hashCode() == witherEssenceHash || str.hashCode() == redstoneKeyHash;
        }
        return false;
    }
}
